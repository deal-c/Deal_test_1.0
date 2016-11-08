package com.first.yuliang.deal_community;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.first.yuliang.deal_community.Util.DateUtil;
import com.first.yuliang.deal_community.frament.utiles.HttpUtile;
import com.first.yuliang.deal_community.pojo.CommodityBean;
import com.first.yuliang.deal_community.pojo.CommodityCollection;
import com.first.yuliang.deal_community.pojo.User;
import com.google.gson.Gson;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.rong.imkit.RongIM;

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
    private TextView tv_rmb_c;
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
    private List<CommodityCollection.CommodityCollect> collection = new ArrayList<CommodityCollection.CommodityCollect>();
    private Button btn_buy;
    private int id;
    private Button btn_sc;
    private boolean flag_sc = false;
    private Drawable sc_1;
    private Drawable sc;
    private TextView tv_browse;
    private String browses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commodity);

        intent = getIntent();
        search = intent.getStringExtra("search");
        commodity = intent.getParcelableExtra("bundle");
        getCommodityList(search);
        getBrowse(commodity.commodityId);
        getUser(commodity.releaseUserId);

        share = 0;

        id = getSharedPreferences("shared_loginn_info", Context.MODE_PRIVATE).getInt("id",0);
        if (id!=0){
            Date date = new Date();
            insertCBH(id,commodity.commodityId,date);
            queryCollection(id,commodity.commodityId);
        }else {
            Date date = new Date();
            int i = date.getDate()*1000+date.getMonth()*10000+date.getYear()*100000+date.getHours()*100+date.getMinutes()*10+date.getSeconds();
            Log.e("看看时间",i+"");
            insertCBH(i,commodity.commodityId,date);
        }

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
        iv_user_head.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(CommodityActivity.this,MaijiaInfoActivity.class);
                intent.putExtra("bundle", user);
                startActivity(intent);
            }
        });
        tv_user_name = ((TextView) view.findViewById(R.id.tv_user_name));
        btn_local = ((Button) view.findViewById(R.id.btn_local));
        btn_local.setText(commodity.location);
        tv_price_c = ((TextView) view.findViewById(R.id.tv_price_c));
        tv_rmb_c = ((TextView) view.findViewById(R.id.tv_rmb_c));
        if(commodity.price==0){
            tv_price_c.setVisibility(View.GONE);
            tv_rmb_c.setVisibility(View.GONE);
        }else {
            tv_price_c.setText(commodity.price + "");
        }
        tv_cd = ((TextView) view.findViewById(R.id.tv_cd));
        tv_cd.setText(commodity.commodityDescribe);
        tv_time = ((TextView) view.findViewById(R.id.tv_time));
        tv_time.setText(commodity.releaseTime);
        tv_browse = ((TextView) view.findViewById(R.id.tv_browse));

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

                x.image().bind(iv_cg, HttpUtile.yu+(commodity.commodityImg.split(","))[0]);
                tv_cg.setText(commodity.commodityTitle);
                tv_price.setText(commodity.price+"");
                tv_local.setText(commodity.location);

                return view;
            }
        };
        gv_commodity.setAdapter(adapter_g);
        gv_commodity.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(CommodityActivity.this, CommodityActivity.class);
                CommodityBean.Commodity commodity = commodityList.get(position);
                intent.putExtra("search",commodity.commodityTitle);
                intent.putExtra("bundle", commodity);
                startActivity(intent);
            }
        });

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

                x.image().bind(iv_cg_c, HttpUtile.yu + img);

                return view;
            }
        };
        lv_commodity.setAdapter(adapter_l);
        btn_buy = ((Button) findViewById(R.id.btn_buy));
        btn_buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(id == 0){
                    Intent intent = new Intent(CommodityActivity.this, RegActivity.class);
                    intent.putExtra("flag","1");
                    startActivity(intent);
                }else if(id==commodity.releaseUserId){
                    Toast.makeText(CommodityActivity.this,"您不能购买自己的商品",Toast.LENGTH_SHORT).show();
                }else {
                    getCommodityById(commodity.commodityId);
                }
            }
        });
        btn_sc = ((Button) findViewById(R.id.ib_sc));
        sc = getResources().getDrawable(R.drawable.sc);
        sc_1 = getResources().getDrawable(R.drawable.sc_1);
        btn_sc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (flag_sc){
                    btn_sc.setCompoundDrawablesWithIntrinsicBounds(null,sc,null,null);
                    btn_sc.setTextColor(getResources().getColor(R.color.main));
                    btn_sc.setText("收藏");
                    Date date = new Date();
                    insertSC(id,commodity.commodityId,date,flag_sc);
                    flag_sc = false;
                }else {
                    if (id==0){
                        Intent intent = new Intent(CommodityActivity.this, RegActivity.class);
                        intent.putExtra("flag","1");
                        startActivity(intent);
                    }else {
                        btn_sc.setCompoundDrawablesWithIntrinsicBounds(null,sc_1,null,null);
                        btn_sc.setTextColor(getResources().getColor(R.color.sc));
                        btn_sc.setText("已收藏");
                        Date date = new Date();
                        insertSC(id,commodity.commodityId,date,flag_sc);
                        flag_sc = true;
                    }
                }
            }
        });
    }

    private void getBrowse(Integer commodityId) {
        RequestParams params = null;
        String url = HttpUtile.szj+"/csys/qureycb?commodityId="+ commodityId;
        Log.e("看看浏览量",url);
        params = new RequestParams(url);
        x.http().get(params,new Callback.CommonCallback<String>(){

            @Override
            public void onSuccess(String result) {
                browses = result;
                tv_browse.setText(browses);
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

    private void getUser(Integer releaseUserId) {

        RequestParams params = null;
        params = new RequestParams(HttpUtile.zy1+"/FourProject/servlet/SelectUserServlet?id="+ releaseUserId);
        x.http().get(params,new Callback.CommonCallback<String>(){

            @Override
            public void onSuccess(String result) {
                Gson gson=new Gson();
                try {
                    user=gson.fromJson(URLDecoder.decode(result,"utf-8"),User.class);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                x.image().bind(iv_user_head, HttpUtile.zy1 + user.getUserImg());
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
                Toast.makeText(CommodityActivity.this,"是不是刚刚我的无法连接服务器",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }
    public void getCommodityById(final int commodityId) {
        RequestParams params = null;
        params = new RequestParams(HttpUtile.szj+"/csys/getcommoditybyid?commodityId="+ commodityId);
        x.http().get(params,new Callback.CommonCallback<String>(){

            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                commodity = gson.fromJson(result, CommodityBean.Commodity.class);
                if(commodity.statement!=1&&commodity.statement!=0){
                    Toast.makeText(CommodityActivity.this,"该商品已被购买",Toast.LENGTH_SHORT).show();
                }else if (commodity.buyWay==2){
                    if(RongIM.getInstance()!=null)
                    {
                        RongIM.getInstance().startPrivateChat(CommodityActivity.this,user.getUserId()+"",user.getUserName());
                    }
                }else {
                    Intent intent = new Intent(CommodityActivity.this, Order.class);
                    intent.putExtra("search", search);
                    intent.putExtra("bundle", commodity);
                    startActivity(intent);
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Toast.makeText(CommodityActivity.this,"是不是这里无法连接服务器",Toast.LENGTH_LONG).show();
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
    private void insertCBH(int id,int commodity,Date date){

        String url = HttpUtile.szj+"/csys/commoditybh?commodityId="+commodity+"&"+"userId="+id+"&"+"date="+DateUtil.dateToStringDate(date)+"&"+"time="+DateUtil.dateToStringTime(date);
        Log.e("看看历史====",url);
        RequestParams params = new RequestParams(url);
        x.http().get(params,new Callback.CommonCallback<String>(){

            @Override
            public void onSuccess(String result) {

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Toast.makeText(CommodityActivity.this,"是不是无法插入历史",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }
    private void insertSC(int id,int commodity,Date date,boolean flag_sc){
        String url = HttpUtile.szj+"/csys/insertcommoditycollect?commodityId="+commodity+"&"+"userId="+id+"&"+"date="+DateUtil.dateToStringDate(date)+"&"+"time="+DateUtil.dateToStringTime(date)+"&"+"flag="+flag_sc;
        Log.e("看看收藏====",url);
        RequestParams params = new RequestParams(url);
        x.http().get(params,new Callback.CommonCallback<String>(){

            @Override
            public void onSuccess(String result) {

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Toast.makeText(CommodityActivity.this,"是不是无法插入收藏",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }
    private void queryCollection(int id, final int commodity){
        String url = HttpUtile.szj+"/csys/qureycommoditycollection?userId="+id;
        Log.e("看看收藏====",url);
        RequestParams params = new RequestParams(url);
        x.http().get(params,new Callback.CommonCallback<String>(){

            @Override
            public void onSuccess(String result) {
                Gson gson=new Gson();
                CommodityCollection collect=gson.fromJson(result,CommodityCollection.class);
                collection.addAll(collect.cc);
                for (CommodityCollection.CommodityCollect c : collection) {
                    if(c.commodityId == commodity){
                        btn_sc.setCompoundDrawablesWithIntrinsicBounds(null,sc_1,null,null);
                        btn_sc.setTextColor(getResources().getColor(R.color.sc));
                        btn_sc.setText("已收藏");
                        flag_sc = true;
                    }
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Toast.makeText(CommodityActivity.this,"是不是无法插入收藏",Toast.LENGTH_LONG).show();
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
