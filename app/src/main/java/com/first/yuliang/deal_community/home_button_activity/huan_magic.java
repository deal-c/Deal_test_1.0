package com.first.yuliang.deal_community.home_button_activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Movie;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Layout;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.first.yuliang.deal_community.CommodityActivity;
import com.first.yuliang.deal_community.MyCenter.MyPublishActivity;
import com.first.yuliang.deal_community.R;
import com.first.yuliang.deal_community.SearchResultActivity;
import com.first.yuliang.deal_community.ToolsClass;
import com.first.yuliang.deal_community.Util.DateUtils;
import com.first.yuliang.deal_community.Util.GifView;
import com.first.yuliang.deal_community.frament.utiles.HttpUtile;
import com.first.yuliang.deal_community.pojo.CommodityBean;
import com.first.yuliang.deal_community.pojo.Product;
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
import java.util.Date;
import java.util.List;

import io.rong.imkit.RongIM;

public class huan_magic extends AppCompatActivity {

    private GifView pic;
    private int screenWidth;
    private int screenHeight;
    private int startLeft;
    private int startTop;
    private int startRight;
    private int startBottom;
    private int endLeft;
    private int endTop;
    private int endRight;
    private int endBottom;
    int lastX, lastY;
    int left;
    int top;
    int right;
    int bottom;
    private Context mContext = null;
    private ImageView iv_product;
    private BaseAdapter adapter;
    List<CommodityBean.Commodity> commodityList = new ArrayList<CommodityBean.Commodity>();
    private CommodityBean.Commodity commodity_r;
    private Toolbar tb;
    private ImageView iv_black;
    private ImageView faguang;
    private ImageView huam;
    private LinearLayout ll_jh;
    private Button btn_cancel;
    private Button btn_ok;
    private User user=null;
    private int id;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0:
                    pic.setPaused(true);
                    huam.setVisibility(View.VISIBLE);
                    ll_jh.setVisibility(View.VISIBLE);
                    Animation anim = AnimationUtils.loadAnimation(mContext, R.anim.larger);
                    huam.startAnimation(anim);

                    huam.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(huan_magic.this, CommodityActivity.class);
                            CommodityBean.Commodity temp = commodity_r;
                            intent.putExtra("search",commodity_r.commodityTitle);
                            intent.putExtra("bundle", temp);
                            startActivity(intent);
                        }
                    });

                    iv_black.setVisibility(View.VISIBLE);
                    Animation animation = new AlphaAnimation(0,1);
                    animation.setDuration(500);
                    iv_black.startAnimation(animation);


                    faguang.setVisibility(View.VISIBLE);
                    Animation operatingAnim = AnimationUtils.loadAnimation(mContext, R.anim.xuanzhuan);
                    faguang.startAnimation(operatingAnim);

                    Log.e("kkkkkkkkkk",lastX/2+","+lastY/2+","+((lastX/2)+iv_product.getWidth())+","+((lastY/2)+iv_product.getHeight()));
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_huan_magic);

        id = getSharedPreferences("shared_loginn_info", Context.MODE_PRIVATE).getInt("id",0);
        mContext = this;

        tb = ((Toolbar) findViewById(R.id.qijihuan));
        iv_black = ((ImageView) findViewById(R.id.iv_blackqj));
        faguang = ((ImageView) findViewById(R.id.iv_faguang));
        huam = ((ImageView) findViewById(R.id.huam));
        ll_jh = ((LinearLayout) findViewById(R.id.ll_jh));
        btn_cancel = ((Button) findViewById(R.id.btn_cancel));
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                huan_magic.this.finish();
            }
        });
        btn_ok = ((Button) findViewById(R.id.btn_ok));
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getUser(commodity_r.releaseUserId);
            }
        });

        pic = ((GifView) findViewById(R.id.circlepic));
        pic.setMovieResource(R.drawable.change);
        pic.setPaused(true);

        iv_product = ((ImageView) findViewById(R.id.product_photo));

        iv_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (id==0){
                    Toast.makeText(huan_magic.this,"请先登录",Toast.LENGTH_SHORT).show();
                }else {

                    startLeft = iv_product.getLeft();
                    startTop = iv_product.getTop();
                    startRight = iv_product.getRight();
                    startBottom = iv_product.getBottom();
                    Log.e("kkkkkkkk", startLeft + "," + startTop + "," + startRight + "," + startBottom);

                    endLeft = pic.getLeft();
                    endTop = pic.getTop();
                    endRight = pic.getRight();
                    endBottom = pic.getBottom();

                    MyPublishPopupWindow(v);
                }
            }
        });

        DisplayMetrics dm = getResources().getDisplayMetrics();
        screenWidth = dm.widthPixels;
        screenHeight = dm.heightPixels - 50;
    }

    private View.OnTouchListener movingEventListener = new View.OnTouchListener() {

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    if (left==startLeft&&top==startTop){
                        MyPublishPopupWindow(v);
                    }
                    lastX = (int) event.getRawX();
                    lastY = (int) event.getRawY();
                    break;
                case MotionEvent.ACTION_MOVE:
                    int dx = (int) event.getRawX() - lastX;
                    int dy = (int) event.getRawY() - lastY;

                    left = v.getLeft() + dx;
                    top = v.getTop() + dy ;
                    right = v.getRight() + dx;
                    bottom = v.getBottom() + dy;
                    // 设置不能出界

                    if (left < 0) {
                        left = 0;
                        right = left + v.getWidth();
                    }

                    if (right > screenWidth) {
                        right = screenWidth;
                        left = right - v.getWidth();
                    }

                    if (top < 150) {
                        top = 150;
                        bottom = top + v.getHeight();
                    }

                    if (bottom > screenHeight) {
                        bottom = screenHeight;
                        top = bottom - v.getHeight();
                    }

                    v.layout(left, top, right, bottom);

                    lastX = (int) event.getRawX();
                    lastY = (int) event.getRawY();

                    break;
                case MotionEvent.ACTION_UP:

                    if (left>endLeft&&top>1300&&right<endRight&bottom<endBottom){
                        iv_product.setVisibility(View.GONE);
                        getrAndomCommodity();
                        pic.setPaused(false);
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Thread.sleep(3500);
                                    Message msg = new Message();
                                    msg.what = 0;
                                    handler.sendMessage(msg);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        }).start();
                    }else {
                        v.layout(startLeft, startTop, startRight, startBottom);
                    }
                    break;
            }
            return true;
        }
    };
    private void MyPublishPopupWindow(View v) {

        commodityList.clear();

        getAllProductInfo();

        View view = LayoutInflater.from(mContext).inflate(R.layout.qijijiaohuan, null);
        final PopupWindow popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        iv_black.setVisibility(View.VISIBLE);
        Animation animation = new AlphaAnimation(0,1);
        animation.setDuration(500);
        iv_black.startAnimation(animation);
        popupWindow.setAnimationStyle(R.style.popupwindow);

        ListView lv_mypublish = (ListView) view.findViewById(R.id.lv_mypublish);

        adapter = new BaseAdapter() {
            @Override
            public int getCount() {
                return commodityList.size();
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
                View view = View.inflate(getApplicationContext(), R.layout.item_qiji_list, null);

                TextView tv_publish_product_name = ((TextView) view.findViewById(R.id.tv_cg_l));
                TextView tv_publish_product_price = ((TextView) view.findViewById(R.id.tv_price_l));
                TextView tv_publish_product_time = ((TextView) view.findViewById(R.id.tv_local_l));
                ImageView iv_publish_product = ((ImageView) view.findViewById(R.id.iv_cg_l));
                CommodityBean.Commodity commodity = commodityList.get(position);

                tv_publish_product_name.setText(commodity.commodityTitle);
                tv_publish_product_price.setText(commodity.price + "");
                tv_publish_product_time.setText(commodity.location);
                x.image().bind(iv_publish_product, HttpUtile.yu + commodity.commodityImg);
                return view;
            }
        };
        lv_mypublish.setAdapter(adapter);

        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                iv_black.setVisibility(View.GONE);
                Animation animation = new AlphaAnimation(1,0);
                animation.setDuration(500);
                iv_black.startAnimation(animation);
            }
        });

        //显示在v的下面
        popupWindow.showAtLocation(tb,Gravity.CENTER,0,0);

        lv_mypublish.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CommodityBean.Commodity commodity = commodityList.get(position);
                x.image().bind(iv_product, HttpUtile.yu + commodity.commodityImg);
                popupWindow.dismiss();
                iv_product.setOnTouchListener(movingEventListener);
            }
        });
    }
    private void getAllProductInfo() {

//        progressDialog = ToolsClass.createLoadingDialog(huan_magic.this, "加载中...", true,
//                0);
//        progressDialog.show();

        RequestParams params = null;
        String url = HttpUtile.szj + "/csys/getcommoditybyr_userid?userId="+id;
        Log.e("看url===========",url);
        params = new RequestParams(url);
        x.http().get(params,new Callback.CommonCallback<String>(){

            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                CommodityBean bean = gson.fromJson(result, CommodityBean.class);
                commodityList.addAll(bean.commodities);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Toast.makeText(huan_magic.this,"是不是我的无法连接服务器",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }
    private void getrAndomCommodity(){
        RequestParams params = null;
        params = new RequestParams(HttpUtile.szj+"/csys/radomcommodity?userId="+id);
        x.http().get(params,new Callback.CommonCallback<String>(){

            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                commodity_r = gson.fromJson(result, CommodityBean.Commodity.class);
                x.image().bind(huam, HttpUtile.yu + commodity_r.commodityImg);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Toast.makeText(huan_magic.this,"是不是我的无法连接服务器",Toast.LENGTH_LONG).show();
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
                if(RongIM.getInstance()!=null)
                {
                    RongIM.getInstance().startPrivateChat(huan_magic.this,user.getUserId()+"",user.getUserName());
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Toast.makeText(huan_magic.this,"是不是我的无法连接服务器",Toast.LENGTH_LONG).show();
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
