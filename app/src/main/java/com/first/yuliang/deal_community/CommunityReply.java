package com.first.yuliang.deal_community;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.first.yuliang.deal_community.frament.Community_Activity.tieziactivity;
import com.first.yuliang.deal_community.frament.pojo.Post;
import com.first.yuliang.deal_community.frament.utiles.HttpUtile;
import com.first.yuliang.deal_community.pojo.Community;
import com.first.yuliang.deal_community.pojo.Responsess;
import com.first.yuliang.deal_community.pojo.User;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import io.rong.imkit.RongIM;

public class CommunityReply extends AppCompatActivity {

    private int id;
    List<Responsess> responsessList = new ArrayList<>();
    List<User> userList = new ArrayList<>();
    List<Community> communityList = new ArrayList<>();
    List<Post> postList = new ArrayList<>();
    private ListView lv_reply;
    private BaseAdapter adapter;
    private ImageButton ib_return_common;
    private TextView tv_activity_title;
    private ProgressBar pb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community_reply);

        id = getSharedPreferences("shared_loginn_info", Context.MODE_PRIVATE).getInt("id",0);
        Log.e("qqqqqqqqqqqqqqqqqqq",id+"");
        pb = ((ProgressBar) findViewById(R.id.pb_comunityreply));
        getReply();

        tv_activity_title = ((TextView) findViewById(R.id.tv_activity_title));
        tv_activity_title.setText("社区回复");
        ib_return_common = ((ImageButton) findViewById(R.id.ib_return_common));
        ib_return_common.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommunityReply.this.finish();
            }
        });

        lv_reply = ((ListView) findViewById(R.id.lv_reply));
        adapter = new BaseAdapter() {
            private Button btn_sixin;
            private ImageView iv_userhead;
            private TextView tv_username;
            private TextView tv_replytime;
            private TextView tv_replycontent;
            private TextView tv_post;
            private TextView tv_communityname;

            @Override
            public int getCount() {
                return responsessList.size();
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
                View view = View.inflate(CommunityReply.this,R.layout.c_reply_item,null);

                iv_userhead = ((ImageView) view.findViewById(R.id.iv_userhead));
                tv_username = ((TextView) view.findViewById(R.id.tv_username));
                tv_replytime = ((TextView) view.findViewById(R.id.tv_replytime));
                tv_replycontent = ((TextView) view.findViewById(R.id.tv_replycontent));
                tv_post = ((TextView) view.findViewById(R.id.tv_post));
                tv_communityname = ((TextView) view.findViewById(R.id.tv_communityname));
                btn_sixin = ((Button) view.findViewById(R.id.btn_sixin));

                Responsess responsess = responsessList.get(position);
                final User user = responsess.getUser();
                Post post = responsess.getPost();
                Community community = responsess.getCommunity();

                btn_sixin.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(RongIM.getInstance()!=null)
                        {
                            RongIM.getInstance().startPrivateChat(CommunityReply.this,user.getUserId()+"",user.getUserName());
                        }
                    }
                });
                x.image().bind(iv_userhead, HttpUtile.zy1 + user.getUserImg());
                iv_userhead.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(CommunityReply.this,MaijiaInfoActivity.class);
                        intent.putExtra("bundle", user);
                        startActivity(intent);
                    }
                });
                tv_username.setText(user.getUserName());
                tv_replytime.setText(responsess.getTime());
                tv_replycontent.setText(responsess.getContent());
                tv_post.setText("原帖："+post.getPostTitle());
                tv_communityname.setText(community.getCommunityName());

                return view;
            }
        };
        lv_reply.setAdapter(adapter);
        lv_reply.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent=new Intent(CommunityReply.this, tieziactivity.class);

                intent.putExtra("post",postList.get(position));

                startActivity(intent);
            }
        });
    }
    private void getReply(){
        pb.setVisibility(View.VISIBLE);
        RequestParams params = null;
        params = new RequestParams(HttpUtile.zy1+"/FourProject/servlet/QueryAllServlet?userId="+id);
        x.http().get(params,new Callback.CommonCallback<String>(){

            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                Type type = new TypeToken<List<Responsess>>() {
                }.getType();

                responsessList = gson.fromJson(result, type);

                for (int i = 0; i < responsessList.size(); i++)
                {
                    userList.add(responsessList.get(i).getUser());
                    postList.add(responsessList.get(i).getPost());
                }

                for (int j = 0; j < responsessList.size(); j++)
                {
                    communityList.add(responsessList.get(j).getCommunity());
                }
                pb.setVisibility(View.GONE);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Toast.makeText(CommunityReply.this,"无法连接服务器",Toast.LENGTH_LONG).show();
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
