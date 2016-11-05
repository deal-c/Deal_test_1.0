package com.first.yuliang.deal_community;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.first.yuliang.deal_community.Util.DateUtil;
import com.first.yuliang.deal_community.frament.utiles.HttpUtile;
import com.first.yuliang.deal_community.pojo.Address;
import com.first.yuliang.deal_community.pojo.CommodityBean;
import com.first.yuliang.deal_community.pojo.User;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Order extends AppCompatActivity {

    private Intent intent;
    private String search;
    private CommodityBean.Commodity commodity;
    private ImageView seller_head;
    private TextView seller_name;
    private ImageView iv_cg;
    private TextView cg_title;
    private TextView cg_price;
    private TextView cg_local;
    private User user = null;
    private TextView user_name;
    private TextView address;
    private TextView phone_num;
    private int id;
    List<Address> addressList=new ArrayList<>();
    private Address ad;
    private Button btn_buy;
    private TextView tv_price_order;
    private int i=0;
    private EditText et_post_way;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        intent = getIntent();
        search = intent.getStringExtra("search");
        commodity = intent.getParcelableExtra("bundle");

        id = getSharedPreferences("shared_loginn_info", Context.MODE_PRIVATE).getInt("id",0);

        getAddress(id);
        user_name = ((TextView) findViewById(R.id.tv_user_name));
        address = ((TextView) findViewById(R.id.tv_address_2));
        phone_num = ((TextView) findViewById(R.id.tv_phone2));

        getSeller(commodity.releaseUserId);
        seller_head = ((ImageView) findViewById(R.id.iv_seller_head));
        seller_name = ((TextView) findViewById(R.id.iv_seller_name));


        iv_cg = ((ImageView) findViewById(R.id.iv_cg_l));
        x.image().bind(iv_cg, HttpUtile.szj +(commodity.commodityImg.split(","))[0]);
        cg_title = ((TextView) findViewById(R.id.tv_cg_l));
        cg_title.setText(commodity.commodityTitle);
        cg_price = ((TextView) findViewById(R.id.tv_price_l));
        cg_price.setText(commodity.price+"");
        cg_local = ((TextView) findViewById(R.id.tv_local_l));
        cg_local.setText(commodity.location);
        tv_price_order = ((TextView) findViewById(R.id.tv_price_order));
        tv_price_order.setText(commodity.price+"");
        et_post_way = ((EditText) findViewById(R.id.et_post_way));

        btn_buy = ((Button) findViewById(R.id.btn_buy));
        btn_buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Date date = new Date();
                insertOrder(date);
                updateCommodityState(commodity.commodityId,id);
                Intent intent = new Intent(Order.this, BuySuccessActivity.class);
                intent.putExtra("tips",et_post_way.getText().toString());
                intent.putExtra("bundle",commodity);
                startActivity(intent);
                Order.this.finish();
            }
        });
    }

    private void getAddress(int id) {

        RequestParams params=new RequestParams(HttpUtile.zy+"/servlet/getAllAddressServlet");
        params.addBodyParameter("userId",id+"");
        x.http().post(params, new Callback.CommonCallback<String>() {


            @Override
            public void onSuccess(String result) {

                Gson gson=new Gson();
                Type type=new TypeToken<List<Address>>(){}.getType();
                List<Address> newAddressList=new ArrayList<Address>();
                newAddressList=gson.fromJson(result,type);
                addressList.addAll(newAddressList);
                for (;i<addressList.size();i++){
                    if(addressList.get(i).isdefault()){
                        break;
                    }
                }
                user_name.setText(addressList.get(i).getUserName());
                address.setText(addressList.get(i).getCity()+addressList.get(i).getAddressDetail());
                phone_num.setText(addressList.get(i).getContactPhoneNum());
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    private void getSeller(Integer releaseUserId) {

        RequestParams params = null;
        params = new RequestParams(HttpUtile.zy+"/servlet/SelectUserServlet?id=" + releaseUserId);
        x.http().get(params, new Callback.CommonCallback<String>() {

            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                user = gson.fromJson(result, User.class);
                x.image().bind(seller_head, HttpUtile.zy1 + user.getUserImg());
                seller_name.setText(user.getUserName());
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Toast.makeText(Order.this, "是不是我的无法连接服务器", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }
    private void updateCommodityState(int commodityId,int id){
        Log.e("看看userid",id+"");
        RequestParams params = null;
        String url = HttpUtile.szj + "/csys/modifycommoditystate?commodityId="+commodityId+"&buyUserId="+id+"&state=1";
        params = new RequestParams(url);
        x.http().get(params, new Callback.CommonCallback<String>() {

            @Override
            public void onSuccess(String result) {

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Toast.makeText(Order.this, "是不是我的无法连接服务器", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }
    private void insertOrder(Date date){
        RequestParams params = null;
        String url = HttpUtile.szj +"/csys/insertorder?userId="+id+"&"+"commodityId="+commodity.commodityId+
                "&"+"changeDate="+ DateUtil.dateToStringDate(date) +"&"+"changeTime="+ DateUtil.dateToStringTime(date) +"&"+
                "addressName="+ addressList.get(i).getUserName() +"&"+"addressCity="+ addressList.get(i).getCity()+"&"+"addressDetail="+addressList.get(i).getAddressDetail() +"&"+ "addressPhoneNum="+addressList.get(i).getContactPhoneNum() +"&"+
                "tips=" + et_post_way.getText().toString() +"&"+"price="+ commodity.price;
        Log.e("url=============",url);
        params = new RequestParams(url);
        x.http().get(params, new Callback.CommonCallback<String>() {

            @Override
            public void onSuccess(String result) {

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Toast.makeText(Order.this, "是不是我的无法连接服务器", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }
    private void updateCommodityState(int commodityId){
        RequestParams params = null;
        String url = "http://192.168.191.1:8080/csys/modifycommoditystate?commodityId="+commodityId+"&state=1";
        params = new RequestParams(url);
        x.http().get(params, new Callback.CommonCallback<String>() {

            @Override
            public void onSuccess(String result) {

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Toast.makeText(Order.this, "是不是我的无法连接服务器", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }
}
