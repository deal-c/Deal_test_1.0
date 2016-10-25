package com.first.yuliang.deal_community.frament.Community_Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.first.yuliang.deal_community.R;
import com.first.yuliang.deal_community.frament.pojo.Dynamic;
import com.first.yuliang.deal_community.frament.utiles.HttpUtile;
import com.first.yuliang.deal_community.frament.utiles.HttpUtils;
import com.first.yuliang.deal_community.model.ComMainActivity;
import com.first.yuliang.deal_community.pojo.Community;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import org.xutils.http.RequestParams;
import org.xutils.image.ImageOptions;
import org.xutils.x;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class Community_model extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener, ContentAdapter.Callback {
private     String  sendynamiclist;
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
    private ImageView logo;
    private TextView namecom;
    private int sendCommunityid = 1;
    private TextView comdesc;
    ArrayList<Dynamic> dynamicList = new ArrayList<>();
    Dynamic dynamic = null;
    private ListView viewById;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community_model);
        //设置信息栏颜色
        setColor();
        initview();
        final Bundle bundle = this.getIntent().getExtras();
        //接收namint sendCommunityid = bundle.getInt("dynamicid");


        tiezilisst.setAdapter(myadapter);
        backToSearch.setOnClickListener(this);
        intent = getIntent();
        community = intent.getParcelableExtra("bundle");
        sendCommunityid = community.getCommunityId();
        viewById = ((ListView) findViewById(R.id.lv_tiezi));

        communityName.setText(community.getCommunityName() + "");
        View headview = View.inflate(this, R.layout.comlayout_item, null);
        final View footview = View.inflate(this, R.layout.comfootview_item, null);
        footview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handler.sendEmptyMessageAtTime(1, 1000);
            }
        });
        //headview 的控件、
        logo = ((ImageView) headview.findViewById(R.id.logo));
        namecom = ((TextView) headview.findViewById(R.id.namecom));
        comdesc = ((TextView) headview.findViewById(R.id.comdesc));
        comImg = ((ImageView) headview.findViewById(R.id.communityimg));
        //添加头部和尾部的view
        tiezilisst.addHeaderView(headview);
        tiezilisst.addFooterView(footview);
        //拆分图片路径
        String[] imgs = community.getComImg().split(";");


        ImageOptions imageOptions = new ImageOptions.Builder().setImageScaleType(ImageView.ScaleType.CENTER_CROP).build();
        //设置加载图片
        x.image().bind(comImg, HttpUtile.host + imgs[0], imageOptions);
        x.image().bind(logo, HttpUtile.host + imgs[1], imageOptions);
        communityName.setText(community.getCommunityName());
        namecom.setText(community.getCommunityName());
        comdesc.setText(community.getCommunityInfo());
        myadapter = new ContentAdapter(this, dynamicList, this);
        geAlldynamic(sendCommunityid);

        viewById.setAdapter(myadapter);
        viewById.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(Community_model.this, ComMainActivity.class);



               intent.putExtra("dynamicList", sendynamiclist);
                Community_model.this.startActivity(intent);




            }
        });

    }

    private void initview() {
        backToSearch = ((ImageView) findViewById(R.id.backToSearch));
        communityName = ((TextView) findViewById(R.id.tv_communityname1));
        tiezilisst = ((ListView) findViewById(R.id.lv_tiezi));
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backToSearch:
                finish();
                break;
        }
    }

    @Override
    public void click(View v) {


    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


    }
}
