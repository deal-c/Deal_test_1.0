package com.first.yuliang.deal_community.frament.Community_Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.first.yuliang.deal_community.R;
import com.first.yuliang.deal_community.frament.pojo.Dynamic;
import com.first.yuliang.deal_community.frament.testpic.PublishedActivity;
import com.first.yuliang.deal_community.frament.utiles.HttpUtile;
import com.first.yuliang.deal_community.frament.utiles.HttpUtils;
import com.first.yuliang.deal_community.model.ComMainActivity;
import com.first.yuliang.deal_community.pojo.Community;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.image.ImageOptions;
import org.xutils.x;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class Community_model extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener, ContentAdapter.Callback {

    private ImageView backToSearch;
    private TextView communityName;
    private int count = 3;
    private ListView tiezilisst;
    private Community community;
    private Intent intent;
    private ContentAdapter myadapter;
    private ImageView comImg;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            count += 3;
            Log.i("handleMessage", count + "");
            myadapter.notifyDataSetChanged();
        }
    };
    private int sendCommunityid = 0;
    ArrayList<Dynamic> dynamicList = new ArrayList<>();
    private ImageView logo;
    private TextView namecom;
    private TextView comdesc;
    private Button care;

    private     String  sendynamiclist;

    @Override
     protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community_model);
        //设置信息栏颜色
        setColor();
        initview();


        backToSearch.setOnClickListener(this);
        View headview = View.inflate(this, R.layout.comlayout_item, null);
        final View footview = View.inflate(this, R.layout.comfootview_item, null);
        footview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handler.sendEmptyMessageAtTime(1, 1000);
            }
        });
        //添加头部和尾部的view
        tiezilisst.addHeaderView(headview);
        tiezilisst.addFooterView(footview);

        intent = getIntent();
        community = intent.getParcelableExtra("bundle");
        //判断是否关注了
        ifCare();
        sendCommunityid=community.getCommunityId();
        communityName.setText(community.getCommunityName() + "");

        //headview 的控件、
        logo = ((ImageView) headview.findViewById(R.id.logo));
        namecom = ((TextView) headview.findViewById(R.id.namecom));
        comdesc = ((TextView) headview.findViewById(R.id.comdesc));
        comImg = ((ImageView) headview.findViewById(R.id.communityimg));
        care = ((Button) headview.findViewById(R.id.care));
        care.setOnClickListener(this);

        //拆分图片路径
        if (!community.getComImg().equals("0")) {
            String[] imgs = community.getComImg().split(";");

            ImageOptions imageOptions = new ImageOptions.Builder()
                    .setImageScaleType(ImageView.ScaleType.CENTER_CROP)
                    .build();
            //设置加载图片

            x.image().bind(comImg, HttpUtile.yu + imgs[0], imageOptions);

            x.image().bind(logo, HttpUtile.yu + imgs[1], imageOptions);
        }
// 赋值
        communityName.setText(community.getCommunityName());
        namecom.setText(community.getCommunityName());
        comdesc.setText(community.getCommunityInfo());

        myadapter = new ContentAdapter(this,dynamicList,this);
        geAlldynamic(sendCommunityid);

        tiezilisst.setAdapter(myadapter);
        tiezilisst.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(Community_model.this, ComMainActivity.class);



                intent.putExtra("dynamicList", sendynamiclist);
                Community_model.this.startActivity(intent);




            }
        });

    }

    private void geAlldynamic(int sendCommunityid) {
        RequestParams params = new RequestParams
                (HttpUtils.hostLuoqingshanSchool+"/usys/Dservlt?sendCommunityid=" + sendCommunityid);//网络请求
        x.http().get(params, new org.xutils.common.Callback.CommonCallback<String>() {//使用xutils开启网络线程
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                Type type = new TypeToken<List<Dynamic>>() {
                }.getType();


                List<Dynamic>   communityList1=new ArrayList<>();
                communityList1  = gson.fromJson(result, type);
                Log.e("看看数据====", dynamicList.toString());
                Log.e("看看数据====", gson.toString());
                if (result!=null){
                    dynamicList.addAll(communityList1);
                    sendynamiclist=result;
                }

                System.out.print(result);




                myadapter.notifyDataSetChanged();
                System.out.print(dynamicList);

            }

            @Override
            public void onError(Throwable ex, boolean
                    isOnCallback) {
                //Toast.makeText(Frag_community_guanzhu.this, ex.toString(), Toast.LENGTH_LONG).show();
                System.out.println(ex.toString());

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });


    }


    List<Community> comlist_care = new ArrayList<Community>();

    private void ifCare() {


        int id = this.getSharedPreferences("shared_loginn_info", Context.MODE_PRIVATE).getInt("id", 0);
        RequestParams params = new RequestParams(HttpUtile.yu + "/community/manegecarecom");

        params.addQueryStringParameter("flag", "getall");
        params.addQueryStringParameter("userId", id + "");

        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e("onSuccess", result);
                Gson gson = new Gson();
                Type type = new TypeToken<List<Community>>() {
                }.getType();
                comlist_care = gson.fromJson(result, type);
                for (Community com : comlist_care) {
                    if (com.getCommunityId() == community.getCommunityId()) {
                        care.setText("已关注");
                    }

                }

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Toast.makeText(Community_model.this, "加载出错", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });

    }

    private void initview() {
        backToSearch = ((ImageView) findViewById(R.id.backToSearch));
        communityName = ((TextView) findViewById(R.id.tv_communityname1));
        tiezilisst=((ListView) findViewById(R.id.lv_tiezi));

    }

    //设置信息栏颜色
    private void setColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window win = getWindow();
            WindowManager.LayoutParams winParams = win.getAttributes();
            final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
            winParams.flags |= bits;
            win.setAttributes(winParams);
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintEnabled(true);
            tintManager.setStatusBarTintResource(R.color.xinxilan);

        }
    }


    @Override
     public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backToSearch:
                finish();
                break;
            case R.id.care:
                careCommunity();
        }
    }

        private void careCommunity() {

        if (care.getText().toString().trim().equals("关注")) {


            int id = this.getSharedPreferences("shared_loginn_info", Context.MODE_PRIVATE).getInt("id", 0);


            if (id != 0) {
                care.setText("已关注");

                RequestParams params = new RequestParams(HttpUtile.yu + "/community/manegecarecom");
                params.addQueryStringParameter("flag", "add");
                params.addQueryStringParameter("userId", id + "");
                params.addQueryStringParameter("communityId", community.getCommunityId() + "");
                x.http().get(params, new Callback.CommonCallback<String>() {
                    @Override
                    public void onSuccess(String result) {
                        Toast.makeText(Community_model.this, result, Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onError(Throwable ex, boolean isOnCallback) {
                        Toast.makeText(Community_model.this, "关注失败，可能是网络较差", Toast.LENGTH_LONG).show();
                        care.setText("关注");
                    }

                    @Override
                    public void onCancelled(CancelledException cex) {

                    }

                    @Override
                    public void onFinished() {

                    }
                });
            } else {
                Toast.makeText(Community_model.this, "还未登陆", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(Community_model.this, "你已关注过了", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }
    public void inputDynamicClick(View view) {

        Intent intent=new Intent(Community_model.this,PublishedActivity.class);
        intent.putExtra("sendCommunityid",sendCommunityid);
        startActivity(intent);
    }
    @Override
    public void click(View v) {

    }
}
