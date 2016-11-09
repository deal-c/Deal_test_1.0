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
import com.first.yuliang.deal_community.frament.pojo.Post;
import com.first.yuliang.deal_community.frament.utiles.HttpUtile;
import com.first.yuliang.deal_community.frament.utiles.ToastUtil;
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

import static android.widget.ImageView.ScaleType.CENTER_CROP;

public class Community_model extends AppCompatActivity implements View.OnClickListener{

    private ImageView backToSearch;
    private TextView communityName;
    private int count = 3;
    private ListView tiezilisst;
    private Community community;
    private Intent intent;
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
    private List <Post>pstlist=new ArrayList<>();
    private ImageView logo;
    private TextView namecom;
    private TextView comdesc;
    private Button care;
    private BaseAdapter myadapter;
    private Button Button;

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

        getpostlist( community.getCommunityId());

        communityName.setText(community.getCommunityName() + "");

        //headview 的控件、
        logo = ((ImageView) headview.findViewById(R.id.logo));
        namecom = ((TextView) headview.findViewById(R.id.namecom));
        comdesc = ((TextView) headview.findViewById(R.id.comdesc));
        comImg = ((ImageView) headview.findViewById(R.id.communityimg));
        care = ((Button) headview.findViewById(R.id.care));
        care.setOnClickListener(this);
        Button.setOnClickListener(this);
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

        myadapter = new BaseAdapter() {
            private TextView ping_num;
            private TextView dongtai_time;
            private TextView dongtai_title;
            private TextView textView;
            private TextView tv_dongtai_content;

            private TextView tv_dongtai_usename;
            private ImageView iv_dongtai_userphoto;
            private ImageView iv_dongtai_dongtaiphoto;

            @Override
            public int getCount() {

                return pstlist.size();
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
                View view = View.inflate(Community_model.this, R.layout.item_dongtai, null);

                iv_dongtai_userphoto = ((ImageView) view.findViewById(R.id.iv_dongtai_userphoto));
                tv_dongtai_usename = ((TextView) view.findViewById(R.id.tv_dongtai_usename));
                iv_dongtai_dongtaiphoto = ((ImageView) view.findViewById(R.id.iv_dongtai_dongtaiphoto));
                tv_dongtai_content = ((TextView) view.findViewById(R.id.tv_dongtai_content));
                dongtai_title = ((TextView) view.findViewById(R.id.tv_dongtai_title));
                dongtai_time = ((TextView) view.findViewById(R.id.dongtai_time));

                Post post=pstlist.get(position);
                tv_dongtai_usename.setText(post.getUser().getUserName());
                tv_dongtai_content.setText(post.getPostInfo());
                String time=  com.first.yuliang.deal_community.Util.DateUtils.dateToString(com.first.yuliang.deal_community.Util.DateUtils.stringToDate(post.getPostTime(),"yyyy-MM-dd hh:mm:ss"),"MM月dd日 hh:mm");

                dongtai_time.setText(time);
                dongtai_title.setText(post.getPostTitle());
                ImageOptions options=new ImageOptions.Builder()
                        .setImageScaleType(CENTER_CROP)
                        .build();
                ImageOptions options1=new ImageOptions.Builder()
                        .setImageScaleType(CENTER_CROP)
                        .setFailureDrawableId(R.drawable.head_)
                        .setLoadingDrawableId(R.drawable.head_)
                        .setCircular(true)
                        .build();

                x.image().bind(iv_dongtai_dongtaiphoto,HttpUtile.yu+post.getImgs(),options);
                x.image().bind(iv_dongtai_userphoto,HttpUtile.zy1+post.getUser().getUserImg(),options1);

                return view;


            }
        };


        tiezilisst.setAdapter(myadapter);
        tiezilisst.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(Community_model.this, tieziactivity.class);

                intent.putExtra("post",pstlist.get(position-1));
                  startActivity(intent);


            }
        });

    }

    private void getpostlist(int communityId) {
        RequestParams params=new RequestParams(HttpUtile.yu+"/community/togetpostbycomid?comid="+communityId);

        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Gson gson=new Gson();
                Type type=new TypeToken<List<Post>>(){}.getType();
                pstlist=gson.fromJson(result,type);
                myadapter.notifyDataSetChanged();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                ToastUtil.show(Community_model.this,"获取数据失败");
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
        Button = ((Button) findViewById(R.id.fatie));

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
                break;
            case  R.id.fatie:
                Intent intent=new Intent(Community_model.this,pubpostActivity.class);
                 intent.putExtra("comid",community.getCommunityId()+"");

                startActivity(intent);
                break;
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



}
