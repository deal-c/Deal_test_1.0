package com.first.yuliang.deal_community.MyCenter;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.first.yuliang.deal_community.R;
import com.first.yuliang.deal_community.Util.StateUtil;
import com.first.yuliang.deal_community.frament.utiles.HttpUtile;
import com.first.yuliang.deal_community.pojo.CommodityBean;
import com.first.yuliang.deal_community.pojo.CommodityInfoUser;
import com.first.yuliang.deal_community.pojo.OrderBean;
import com.first.yuliang.deal_community.pojo.User;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;


public class MyMaiActivity extends AppCompatActivity {

    private int id;
    private ListView lv_publish;
    private BaseAdapter adapter;
    private CommodityBean.Commodity commodity;
    List<OrderBean> orderList=new ArrayList<OrderBean>();
    List<CommodityInfoUser> commodityList = new ArrayList<CommodityInfoUser>();
    List<User> userList = new ArrayList<User>();
    private View view;
    private Button btn_state;
    private String tips;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_mai);

        id = getSharedPreferences("shared_loginn_info", Context.MODE_PRIVATE).getInt("id",0);

        getOrder(id);

        lv_publish = ((ListView) findViewById(R.id.lv_publish));
        adapter = new BaseAdapter() {
            private TextView tv_name;
            private TextView tv_state;
            private TextView tv_cg_l;
            private TextView tv_price_l;
            private TextView tv_local_l;
            private TextView tv_price_order;
            private ImageView iv_head;
            private ImageView iv_cg_l;

            @Override
            public int getCount() {
                return orderList.size();
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

                view = View.inflate(MyMaiActivity.this,R.layout.item_mybuy,null);

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
                CommodityInfoUser commodityInfo = commodityList.get(position);
                OrderBean order = orderList.get(position);
                x.image().bind(iv_head, HttpUtile.zy1 + user.getUserImg());
                x.image().bind(iv_cg_l, HttpUtile.szj+ (commodityInfo.getCommodityImg().split(","))[0]);

                tv_name.setText(user.getUserName());
                tv_state.setText(StateUtil.getSellState(order.getState()));
                tv_cg_l.setText(commodityInfo.getCommodityTitle());
                tv_price_l.setText(commodityInfo.getPrice()+"");
                tv_local_l.setText(commodityInfo.getLocation());
                tv_price_order.setText(order.getPrice()+"");
                getBtn(order.getState(),order);
                return view;
            }
        };
        lv_publish.setAdapter(adapter);
    }

    private void getOrder(int userid){
        RequestParams params = null;
        params = new RequestParams(HttpUtile.szj+"csys/getorderbyr_userid?userId="+userid);
        x.http().get(params,new Callback.CommonCallback<String>(){

            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                Type type = new TypeToken<List<OrderBean>>() {
                }.getType();

                Log.e("kkkkkkkkkk",result);
                try {
                    orderList = gson.fromJson(URLDecoder.decode(result,"utf-8"), type);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                for (int i = 0; i < orderList.size(); i++)
                {
                    commodityList.add(orderList.get(i).getCommodityInfoUser());
                }
                for (int j = 0; j < commodityList.size(); j++)
                {
                    userList.add(commodityList.get(j).getUser_b());
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Toast.makeText(MyMaiActivity.this,"无法连接服务器",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    private void getBtn(final int state, final OrderBean order) {
        if (state==0){
            btn_state.setText("待售出");
            btn_state.setBackgroundResource(R.drawable.button_stroke);
            btn_state.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }
        if (state==1){
            btn_state.setText("待付款");
            btn_state.setBackgroundResource(R.drawable.button_stroke);
            btn_state.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }
        if (state==2){
            btn_state.setText("发货");
            btn_state.setTextColor(getResources().getColor(R.color.checkbox));
            btn_state.setBackgroundResource(R.drawable.button_main);
            btn_state.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    updateCommodityState(order.getCommodityInfoUser().getCommodityId(),3);
                    getOrder(id);
                }
            });
        }
        if (state==3){
            btn_state.setText("查看物流");
            btn_state.setBackgroundResource(R.drawable.button_stroke);
            btn_state.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }
        if (state==4){
            btn_state.setText("待确认");
            btn_state.setBackgroundResource(R.drawable.button_stroke);
            btn_state.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                }
            });
        }
        if (state==5){
            btn_state.setText("待评价");
            btn_state.setBackgroundResource(R.drawable.button_stroke);
            btn_state.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                }
            });
        }
        if (state==6){
            btn_state.setText("交易成功");
            btn_state.setBackgroundResource(R.drawable.button_stroke);
            btn_state.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }
    }
    private void deleteOrder(int orderId){
        RequestParams params = null;
        params = new RequestParams(HttpUtile.szj+"/csys/deletorder?orderId="+orderId);
        x.http().get(params,new Callback.CommonCallback<String>(){

            @Override
            public void onSuccess(String result) {

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Toast.makeText(MyMaiActivity.this,"无法连接服务器",Toast.LENGTH_LONG).show();
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
        String url = HttpUtile.szj +"/csys/modifycommoditystate?commodityId=" + commodityId +"&buyUserId="+id + "&state="+state;
        params = new RequestParams(url);
        x.http().get(params, new Callback.CommonCallback<String>() {

            @Override
            public void onSuccess(String result) {

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Toast.makeText(MyMaiActivity.this, "是不是我的无法连接服务器", Toast.LENGTH_LONG).show();
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
