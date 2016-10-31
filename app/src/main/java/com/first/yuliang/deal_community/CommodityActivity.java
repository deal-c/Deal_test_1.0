package com.first.yuliang.deal_community;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.first.yuliang.deal_community.frament.utiles.HttpUtile;
import com.first.yuliang.deal_community.pojo.CommodityBean;
import com.first.yuliang.deal_community.pojo.User;
import com.google.gson.Gson;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class CommodityActivity extends AppCompatActivity {

    private ListView lv_commodity;
    private Intent intent;
    private String search;
    private CommodityBean.Commodity commodity;
    private BaseAdapter adapter_l;
    private BaseAdapter adapter_g;
    final List<CommodityBean.Commodity> commodityList = new ArrayList<CommodityBean.Commodity>();
    private GridView gv_commodity;
    private String[] imgs;
    private ImageView iv_user_head;
    private TextView tv_user_name;
    private Button btn_local;
    private TextView tv_price_c;
    private TextView tv_cd;
    private TextView tv_time;
    private ImageButton ib_re_se_re;
    private ImageButton ib_share;
    private View view_share;
    private LinearLayout ll_share;
    private int share;
    private View view_bar;
    private ImageView iv_black;
    private ImageButton s_1;
    private ObjectAnimator oa1;
    private ObjectAnimator oa2;
    private User user=null;
    private Button btn_buy;
    private int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commodity);

        intent = getIntent();
        search = intent.getStringExtra("search");
        commodity = intent.getParcelableExtra("bundle");
        getCommodityList(search);
        getUser(commodity.releaseUserId);

        share = 0;

        view_bar = View.inflate(CommodityActivity.this,R.layout.commodity_bar,null);
        ib_re_se_re = ((ImageButton) findViewById(R.id.ib_return_search_result));
        ib_re_se_re.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommodityActivity.this.finish();
            }
        });
        ib_share = ((ImageButton) findViewById(R.id.ib_share));
        iv_black = ((ImageView) findViewById(R.id.iv_black));

        ll_share = ((LinearLayout) findViewById(R.id.ll_share));
        ib_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (share==0) {
                    share = 1;
                    iv_black.setVisibility(View.VISIBLE);
                    Animation animation = new AlphaAnimation(0,1);
                    animation.setDuration(500);
                    iv_black.startAnimation(animation);
                    getAnimationInto().start();
                }
            }
        });

        s_1 = ((ImageButton)findViewById(R.id.s_1));
        s_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(CommodityActivity.this,"点击了分享",Toast.LENGTH_SHORT).show();
            }
        });

        View view = View.inflate(CommodityActivity.this,R.layout.commodity_head,null);
        iv_user_head = ((ImageView) view.findViewById(R.id.iv_user_head));
        tv_user_name = ((TextView) view.findViewById(R.id.tv_user_name));
        btn_local = ((Button) view.findViewById(R.id.btn_local));
        btn_local.setText(commodity.location);
        tv_price_c = ((TextView) view.findViewById(R.id.tv_price_c));
        tv_price_c.setText(commodity.price+"");
        tv_cd = ((TextView) view.findViewById(R.id.tv_cd));
        tv_cd.setText(commodity.commodityDescribe);
        tv_time = ((TextView) view.findViewById(R.id.tv_time));
        tv_time.setText(commodity.releaseTime);

        View view_g = View.inflate(CommodityActivity.this,R.layout.commodity_footer,null);
        View view_like = View.inflate(CommodityActivity.this,R.layout.commodity_like,null);

        gv_commodity = ((GridView) view_g.findViewById(R.id.gv_commodity));

        adapter_g = new BaseAdapter() {
            private TextView tv_local;
            private TextView tv_price;
            private TextView tv_cg;
            private ImageView iv_cg;

            @Override
            public int getCount() {
                return  commodityList.size();
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
                View view = View.inflate(CommodityActivity.this,R.layout.item_commodity_list_g,null);

                iv_cg = ((ImageView) view.findViewById(R.id.iv_cg));
                tv_cg = ((TextView) view.findViewById(R.id.tv_cg));
                tv_price = ((TextView) view.findViewById(R.id.tv_price));
                tv_local = ((TextView) view.findViewById(R.id.tv_local));
                CommodityBean.Commodity commodity = commodityList.get(position);

                x.image().bind(iv_cg, "http://192.168.191.1:8080"+(commodity.commodityImg.split(","))[0]);
                tv_cg.setText(commodity.commodityTitle);
                tv_price.setText(commodity.price+"");
                tv_local.setText(commodity.location);

                return view;
            }
        };
        gv_commodity.setAdapter(adapter_g);

        lv_commodity = ((ListView) findViewById(R.id.lv_commodity));
        lv_commodity.addHeaderView(view);
        lv_commodity.addFooterView(view_like);
        lv_commodity.addFooterView(view_g);

        imgs = commodity.commodityImg.split(",");

        adapter_l = new BaseAdapter() {

            private ImageView iv_cg_c;

            @Override
            public int getCount() {
                return (imgs == null) ? 0 : imgs.length;
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
                View view = View.inflate(CommodityActivity.this, R.layout.commodity_img, null);

                iv_cg_c = ((ImageView) view.findViewById(R.id.iv_cg_c));

                String img = imgs[position];

                x.image().bind(iv_cg_c, HttpUtile.szj + img);

                return view;
            }
        };
        lv_commodity.setAdapter(adapter_l);
        btn_buy = ((Button) findViewById(R.id.btn_buy));
        btn_buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                id = getSharedPreferences("shared_loginn_info", Context.MODE_PRIVATE).getInt("id",0);
                if(id == 0){
                    Intent intent = new Intent(CommodityActivity.this, RegActivity.class);
                    intent.putExtra("flag",1);
                    startActivity(intent);
                }else{
                    Intent intent = new Intent(CommodityActivity.this, Order.class);
                    intent.putExtra("search",search);
                    intent.putExtra("bundle",commodity);
                    startActivity(intent);
                }
            }
        });
    }

    private void getUser(Integer releaseUserId) {

        RequestParams params = null;
        params = new RequestParams("http://10.40.5.52:8080/FourProject/servlet/SelectUserServlet?id="+ releaseUserId);
        x.http().get(params,new Callback.CommonCallback<String>(){

            @Override
            public void onSuccess(String result) {
                Gson gson=new Gson();
                user=gson.fromJson(result,User.class);
                x.image().bind(iv_user_head, "http://10.40.5.52:8080" + user.getUserImg());
                tv_user_name.setText(user.getUserName());
                adapter_l.notifyDataSetChanged();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Toast.makeText(CommodityActivity.this,"是不是我的无法连接服务器",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    public void getCommodityList(String search) {
        search = search.replace(" ","%");
        RequestParams params = null;
        try {
            params = new RequestParams(HttpUtile.szj+"/csys/getcommodity?search="+ URLEncoder.encode(search,"utf-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        x.http().get(params,new Callback.CommonCallback<String>(){

            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                CommodityBean bean = gson.fromJson(result, CommodityBean.class);
                commodityList.addAll(bean.commodities);
//                imgs = commodityList.get(0).commodityImg.split(",");
//                Log.e("看图片",imgs.length+"");
                adapter_l.notifyDataSetChanged();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Toast.makeText(CommodityActivity.this,"是不是我的无法连接服务器",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }
    public ObjectAnimator getAnimationInto(){
        oa1 = ObjectAnimator.ofFloat(ll_share,"translationY",0,ll_share.getHeight());
        oa1.setDuration(500);
        return oa1;
    }
    public ObjectAnimator getAnimationOut(){
        oa2 = ObjectAnimator.ofFloat(ll_share,"translationY",ll_share.getHeight(),0);
        oa2.setDuration(500);
        return oa2;
    }
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            if (isHideInput(ll_share, ev)) {
                if(share==1){
                    share = 0;
                    iv_black.setVisibility(View.GONE);
                    Animation animation = new AlphaAnimation(1,0);
                    animation.setDuration(500);
                    iv_black.startAnimation(animation);
                    getAnimationOut().start();
                    return true;
                }
//            }else {
//                if (share == 1){
//                    return true;
//                }
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    // 根据EditText所在坐标和用户点击的坐标相对比，来判断是否隐藏键盘，因为当用户点击EditText时没必要隐藏
    private boolean isHideInput(View v, MotionEvent ev) {
        int left = 0, top = v.getBottom(), bottom = top + v.getHeight()+v.getBottom(), right = left
                + v.getWidth();

        if (ev.getX() > left && ev.getX() < right && ev.getY() > top
                && ev.getY() < bottom) {
            // 点击EditText的事件，忽略它。
            return false;
        } else {
            return true;
        }
    }
}
