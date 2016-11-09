package com.first.yuliang.deal_community;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.first.yuliang.deal_community.Util.DateUtils;
import com.first.yuliang.deal_community.frament.Community_Activity.tieziactivity;
import com.first.yuliang.deal_community.frament.pojo.Post;
import com.first.yuliang.deal_community.frament.pojo.Reply_son;
import com.first.yuliang.deal_community.frament.utiles.HttpUtile;
import com.first.yuliang.deal_community.frament.utiles.ToastUtil;
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
import java.util.Date;
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
    private RelativeLayout mst_rl;
    private EditText msg_reply_con;
    private Button btn_msg;
    private InputMethodManager imm;
    private int flag=-1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community_reply);

        id = getSharedPreferences("shared_loginn_info", Context.MODE_PRIVATE).getInt("id",0);
        Log.e("qqqqqqqqqqqqqqqqqqq",id+"");
        pb = ((ProgressBar) findViewById(R.id.pb_comunityreply));
        mst_rl = ((RelativeLayout) findViewById(R.id.msg_huifu));
        msg_reply_con = ((EditText) findViewById(R.id.msg_reply_con));
        btn_msg = ((Button) findViewById(R.id.btn_msg_huifu));

        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        msg_reply_con.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    imm.showSoftInput(v, InputMethodManager.SHOW_FORCED);

                } else {
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

                }
            }
        });


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
            private Button btn_reply;
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
            public View getView(final int position, View convertView, ViewGroup parent) {
                View view = View.inflate(CommunityReply.this,R.layout.c_reply_item,null);

                iv_userhead = ((ImageView) view.findViewById(R.id.iv_userhead));
                tv_username = ((TextView) view.findViewById(R.id.tv_username));
                tv_replytime = ((TextView) view.findViewById(R.id.tv_replytime));
                tv_replycontent = ((TextView) view.findViewById(R.id.tv_replycontent));
                tv_post = ((TextView) view.findViewById(R.id.tv_post));
                tv_communityname = ((TextView) view.findViewById(R.id.tv_communityname));
                btn_sixin = ((Button) view.findViewById(R.id.btn_sixin));
                btn_reply = ((Button) view.findViewById(R.id.btn_reply));

                Responsess responsess = responsessList.get(position);
                final User user = responsess.getUser();
                Post post = responsess.getPost();
                Community community = responsess.getCommunity();

                btn_reply.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mst_rl.setVisibility(View.VISIBLE);
                        msg_reply_con.requestFocus();
                        flag=position;
                    }
                });


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

        lv_reply.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                mst_rl.setVisibility(View.GONE);
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });
        lv_reply.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent=new Intent(CommunityReply.this, tieziactivity.class);

                intent.putExtra("post",postList.get(position));

                startActivity(intent);
            }
        });

        btn_msg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (msg_reply_con.getText().toString().equals("")) {
                    ToastUtil.show(CommunityReply.this, "输入不能为空");
                } else {
                   if(flag!=-1){
                       Responsess responsess = responsessList.get(flag);

                    Reply_son son = new Reply_son(null,52,responsess.getUser() ,new User(responsess.getUserId()),msg_reply_con.getText().toString(), DateUtils.dateToString(new Date(), "yyyy-MM-dd hh:mm:ss"));

                    msg_reply_con.clearFocus();
                    sendSonReply(son);

                   }
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    msg_reply_con.setVisibility(View.GONE);
                }
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


    private void sendSonReply(Reply_son son) {



        //  Reply_son son = new Reply_son(null, reply.getReplyid(), userfrom, touser, s, DateUtils.dateToString(new Date(), "yyyy-MM-dd hh:mm:ss"));
        RequestParams params=new RequestParams(HttpUtile.yu+"/community/toaddreplyson");
        params.addQueryStringParameter("replyid",son.getReplyid()+"");
        params.addQueryStringParameter("fromuserid",son.getRs_from_user().getUserId()+"");
        params.addQueryStringParameter("touserid",son.getRs_to_user().getUserId()+"");
        params.addQueryStringParameter("content",son.getRs_content());
        params.addQueryStringParameter("time",son.getTime());

        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                ToastUtil.show(CommunityReply.this,"回复成功");
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {


                ToastUtil.show(CommunityReply.this,"回复失败");
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
