package com.first.yuliang.deal_community;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.first.yuliang.deal_community.Util.CustomDialog;
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
        x.image().bind(iv_cg, "http://192.168.191.1:8080"+(commodity.commodityImg.split(","))[0]);
        cg_title = ((TextView) findViewById(R.id.tv_cg_l));
        cg_title.setText(commodity.commodityTitle);
        cg_price = ((TextView) findViewById(R.id.tv_price_l));
        cg_price.setText(commodity.price+"");
        cg_local = ((TextView) findViewById(R.id.tv_local_l));
        cg_local.setText(commodity.location);
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
                user_name.setText(addressList.get(0).getUserName());
                address.setText(addressList.get(0).getCity()+addressList.get(0).getAddressDetail());
                phone_num.setText(addressList.get(0).getContactPhoneNum());
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
        params = new RequestParams("http://10.40.5.52:8080/FourProject/servlet/SelectUserServlet?id=" + releaseUserId);
        x.http().get(params, new Callback.CommonCallback<String>() {

            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                user = gson.fromJson(result, User.class);
                x.image().bind(seller_head, "http://10.40.5.52:8080" + user.getUserImg());
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
}
