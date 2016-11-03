package com.first.yuliang.deal_community.MyCenter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.first.yuliang.deal_community.BuySuccessActivity;
import com.first.yuliang.deal_community.CommodityActivity;
import com.first.yuliang.deal_community.Order;
import com.first.yuliang.deal_community.R;
import com.first.yuliang.deal_community.Util.StateUtil;
import com.first.yuliang.deal_community.frament.utiles.HttpUtile;
import com.first.yuliang.deal_community.pojo.CommodityBean;
import com.first.yuliang.deal_community.pojo.CommodityInfo;
import com.first.yuliang.deal_community.pojo.OrderBean;
import com.first.yuliang.deal_community.pojo.User;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;


public class MyBuyActivity extends AppCompatActivity {

    private int id;
    private ListView lv_mybuy;
    private BaseAdapter adapter;
    private CommodityBean.Commodity commodity;
    List<OrderBean> orderList;
    List<CommodityInfo> commodityList=new ArrayList<CommodityInfo>();
    List<User> userList = new ArrayList<User>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_buy);

        id = getSharedPreferences("shared_loginn_info", Context.MODE_PRIVATE).getInt("id",0);

        getOrder(id);

        lv_mybuy = ((ListView) findViewById(R.id.lv_mybuy));
        adapter = new BaseAdapter() {
            private TextView tv_name;
            private TextView tv_state;
            private TextView tv_cg_l;
            private TextView tv_price_l;
            private TextView tv_local_l;
            private TextView tv_price_order;
            private ImageView iv_head;
            private ImageView iv_cg_l;
            private Button btn_state;

            @Override
            public int getCount() {
                return 0;
            }

            @Override
            public Object getItem(int position) {
                return null;
            }

            @Override
            public long getItemId(int position) {
                return 0;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = View.inflate(MyBuyActivity.this,R.layout.item_mybuy,null);

                iv_head = ((ImageView) view.findViewById(R.id.iv_head_buy));
                iv_cg_l = ((ImageView) view.findViewById(R.id.iv_cg_l));
                tv_name = ((TextView) view.findViewById(R.id.tv_name_buy));
                tv_state = ((TextView) view.findViewById(R.id.tv_state));
                tv_cg_l = ((TextView) view.findViewById(R.id.tv_cg_l));
                tv_price_l = ((TextView) view.findViewById(R.id.tv_price_l));
                tv_local_l = ((TextView) view.findViewById(R.id.tv_local_l));
                tv_price_order = ((TextView) view.findViewById(R.id.tv_price_order));
                btn_state = ((Button) view.findViewById(R.id.btn_buy));

                User user = userList.get(position);
                CommodityInfo commodityInfo = commodityList.get(position);
                OrderBean order = orderList.get(position);
                x.image().bind(iv_head, "http://10.40.5.52:8080" + user.getUserImg());
                x.image().bind(iv_cg_l, "http://192.168.191.1:8080" + commodityInfo.getCommodityImg());

                tv_name.setText(user.getUserName());
                tv_state.setText(StateUtil.getBuyState(order.getState()));
                tv_cg_l.setText(commodityInfo.getCommodityTitle());
                tv_price_l.setText(commodityInfo.getPrice()+"");
                tv_local_l.setText(commodityInfo.getLocation());
                tv_price_order.setText(order.getPrice()+"");
                getBtn(btn_state,order.getState(),order);

                return view;
            }
        };
    }

    private void getOrder(int userid){
        RequestParams params = null;
        params = new RequestParams(HttpUtile.yu+"/csys/getorderbyid?userId="+userid);
        x.http().get(params,new Callback.CommonCallback<String>(){

            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                Type type = new TypeToken<List<Order>>() {
                }.getType();
                orderList = gson.fromJson(result, type);
                for (int i = 0; i < orderList.size(); i++)
                {
                    commodityList.add(orderList.get(i).getCommodityInfo());
                }
                for (int j = 0; j < commodityList.size(); j++)
                {
                    userList.add(commodityList.get(j).getUser());
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Toast.makeText(MyBuyActivity.this,"无法连接服务器",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    private void getBtn(Button btn_state, final int state, final OrderBean order) {
        if (state==0){
            btn_state.setText("删除订单");
            btn_state.setBackgroundResource(R.color.checkbox);
            btn_state.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    updateCommodityState(order.getCommodityInfo().getCommodityId(),state);
                    deleteOrder(order.getOrderId());
                    getOrder(order.getUserId());
                }
            });
        }
        if (state==1){
            btn_state.setText("立即付款");
            btn_state.setTextColor(getResources().getColor(R.color.checkbox));
            btn_state.setBackgroundResource(R.color.main);
            btn_state.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MyBuyActivity.this, BuySuccessActivity.class);
                    intent.putExtra("tips",order.getTips());
                    intent.putExtra("bundle",commodity);
                    startActivity(intent);
                }
            });
        }
        if (state==2){
            btn_state.setText("取消订单");
            btn_state.setBackgroundResource(R.color.checkbox);
            btn_state.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                }
            });
        }
        if (state==3){
            btn_state.setText("查看物流");
            btn_state.setBackgroundResource(R.color.checkbox);
            btn_state.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }
        if (state==4){
            btn_state.setText("确认收货");
            btn_state.setTextColor(getResources().getColor(R.color.checkbox));
            btn_state.setBackgroundResource(R.color.main);
            btn_state.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    updateCommodityState(order.getCommodityInfo().getCommodityId(),5);
                }
            });
        }
        if (state==5){
            btn_state.setText("评价");
            btn_state.setTextColor(getResources().getColor(R.color.checkbox));
            btn_state.setBackgroundResource(R.color.main);
            btn_state.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    updateCommodityState(order.getCommodityInfo().getCommodityId(),6);
                }
            });
        }
        if (state==6){
            btn_state.setText("交易成功");
            btn_state.setBackgroundResource(R.color.checkbox);
            btn_state.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }
    }
    private void deleteOrder(int orderId){
        RequestParams params = null;
        params = new RequestParams(HttpUtile.yu+"/csys/deletorder?orderId="+orderId);
        x.http().get(params,new Callback.CommonCallback<String>(){

            @Override
            public void onSuccess(String result) {

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Toast.makeText(MyBuyActivity.this,"无法连接服务器",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }
    private void updateCommodityState(int commodityId,int state) {
        RequestParams params = null;
        String url = "http://192.168.191.1:8080/csys/modifycommoditystate?commodityId=" + commodityId + "&state="+state;
        params = new RequestParams(url);
        x.http().get(params, new Callback.CommonCallback<String>() {

            @Override
            public void onSuccess(String result) {

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Toast.makeText(MyBuyActivity.this, "是不是我的无法连接服务器", Toast.LENGTH_LONG).show();
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
