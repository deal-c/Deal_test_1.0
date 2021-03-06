package com.first.yuliang.deal_community.MyCenter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.first.yuliang.deal_community.BuySuccessActivity;
import com.first.yuliang.deal_community.MaijiaInfoActivity;
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


public class MyBuyActivity extends AppCompatActivity {

    private int id;
    private ListView lv_mybuy;
    private BaseAdapter adapter;
    private CommodityBean.Commodity commodity;
    List<OrderBean> orderList=new ArrayList<OrderBean>();
    List<CommodityInfoUser> commodityList = new ArrayList<CommodityInfoUser>();
    List<User> userList = new ArrayList<User>();
    List<Integer> list=new ArrayList<>();
    private View view;
    private Button btn_state;
    private String tips;
    private ImageButton ib_return_mine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_buy);

        id = getSharedPreferences("shared_loginn_info", Context.MODE_PRIVATE).getInt("id",0);

        getOrder(id);

        ib_return_mine = ((ImageButton) findViewById(R.id.ib_return_mine));
        ib_return_mine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyBuyActivity.this.finish();
            }
        });
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

                view = View.inflate(MyBuyActivity.this,R.layout.item_mybuy,null);

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
                tv_state.setText(StateUtil.getBuyState(order.getState()));
                tv_cg_l.setText(commodityInfo.getCommodityTitle());
                tv_price_l.setText(commodityInfo.getPrice()+"");
                tv_local_l.setText(commodityInfo.getLocation());
                tv_price_order.setText(order.getPrice()+"");
                getBtn(order.getState(),order);
                return view;
            }
        };
        lv_mybuy.setAdapter(adapter);
    }

    private void getOrder(int userid){
        RequestParams params = null;
        params = new RequestParams(HttpUtile.szj+"csys/getorderbyid?userId="+userid);
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

                for(int m=0;m<commodityList.size();m++)
                {
                    list.add(commodityList.get(m).getStatement());

                }

                for(int n=0;n<list.size();n++)
                {
                    if(list.get(n)==2||list.get(n)==4)
                    {
                       selectMessage(id);
                    }

                }

                for (int j = 0; j < commodityList.size(); j++)
                {
                    userList.add(commodityList.get(j).getUser_r());
                    Log.e("kkkkkkkkkk",userList.get(0).getUserName());
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

    private void selectMessage(int id) {

        RequestParams params=new RequestParams(HttpUtile.zy+"/servlet/SelectMessage");
        params.addBodyParameter("id",id+"");
        x.http().post(params, new Callback.CacheCallback<String>() {


            @Override
            public void onSuccess(String result) {

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

            @Override
            public boolean onCache(String result) {
                return false;
            }
        });
    }

    private void getBtn(final int state, final OrderBean order) {
        if (state==0){
            btn_state.setText("删除订单");
            btn_state.setBackgroundResource(R.drawable.button_stroke);
            btn_state.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    updateCommodityState(order.getCommodityInfoUser().getCommodityId(),state);

                    //
                    sendMessage(state,order.getCommodityInfoUser().getUser_r().getUserId());

                    deleteOrder(order.getOrderId());
                    getOrder(order.getUserId());
                    getOrder(id);
                }
            });
        }
        if (state==1){
            btn_state.setText("立即付款");
            btn_state.setTextColor(getResources().getColor(R.color.checkbox));
            btn_state.setBackgroundResource(R.drawable.button_main);
            btn_state.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getCommodityById(order.getCommodityInfoUser().getCommodityId(),order.getTips());




                }
            });
        }
        if (state==2){
            btn_state.setText("取消订单");
            btn_state.setBackgroundResource(R.drawable.button_stroke);
            btn_state.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

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
            btn_state.setText("确认收货");
            btn_state.setTextColor(getResources().getColor(R.color.checkbox));
            btn_state.setBackgroundResource(R.drawable.button_main);
            btn_state.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    updateCommodityState(order.getCommodityInfoUser().getCommodityId(),5);
                    getOrder(id);
                    //
                    sendMessage(state,order.getCommodityInfoUser().getUser_r().getUserId());
                }
            });
        }
        if (state==5){
            btn_state.setText("评价");
            btn_state.setTextColor(getResources().getColor(R.color.checkbox));
            btn_state.setBackgroundResource(R.drawable.button_main);
            btn_state.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    updateCommodityState(order.getCommodityInfoUser().getCommodityId(),6);
                    getOrder(id);
                    //

                    tiaozhuan(order.getCommodityInfoUser().getCommodityId(),order.getCommodityInfoUser().getUser_r());

                    sendMessage(state,order.getCommodityInfoUser().getUser_r().getUserId());
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

    private void tiaozhuan(int commodityId,User user) {



        SharedPreferences preference=getSharedPreferences("shared_loginn_info", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit=preference.edit();
        edit.putInt("commentToMaijia",1);
        edit.commit();
        Intent intent=new Intent(MyBuyActivity.this, MaijiaInfoActivity.class);
        intent.putExtra("commodityId",commodityId+"");
        intent.putExtra("buyerId",id+"");
        intent.putExtra("user",user);
        startActivity(intent);
    }

    private void sendMessage(int state,int id) {

        if(state==0)
        {
            RequestParams params=new RequestParams(HttpUtile.zy+"servlet/SendMessageToMaiDeleteServlet");
            params.addBodyParameter("userId",id+"");
            x.http().get(params, new Callback.CacheCallback<String>() {


                @Override
                public void onSuccess(String result) {

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

                @Override
                public boolean onCache(String result) {
                    return false;
                }
            });

        }


        if(state==4)
        {
            RequestParams params=new RequestParams(HttpUtile.zy+"servlet/SendMessageToMaiConfirmServlet");
            params.addBodyParameter("userId",id+"");

            x.http().get(params, new Callback.CacheCallback<String>() {


                @Override
                public void onSuccess(String result) {


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

                @Override
                public boolean onCache(String result) {
                    return false;
                }
            });
        }


        if(state==5)
        {
            RequestParams params=new RequestParams(HttpUtile.zy+"servlet/SendMessageToMaiRemarkServlet");
            params.addBodyParameter("userId",id+"");
            x.http().get(params, new Callback.CacheCallback<String>() {


                @Override
                public void onSuccess(String result) {

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

                @Override
                public boolean onCache(String result) {
                    return false;
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
        String url = HttpUtile.szj +"/csys/modifycommoditystate?commodityId=" + commodityId +"&buyUserId="+id + "&state="+state;
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
    public void getCommodityById(int commodityId,String t) {
        tips = t;
        RequestParams params = null;
        params = new RequestParams(HttpUtile.szj+"/csys/getcommoditybyid?commodityId="+ commodityId);
        x.http().get(params,new Callback.CommonCallback<String>(){

            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                commodity = gson.fromJson(result, CommodityBean.Commodity.class);
                if(commodity.statement!=1&&commodity.statement!=0){
                    Toast.makeText(MyBuyActivity.this,"该商品已被购买",Toast.LENGTH_SHORT).show();
                }else {
                    Intent intent = new Intent(MyBuyActivity.this, BuySuccessActivity.class);
                    intent.putExtra("search", tips);
                    intent.putExtra("bundle", commodity);
                    startActivity(intent);
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Toast.makeText(MyBuyActivity.this,"是不是这里无法连接服务器",Toast.LENGTH_LONG).show();
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
