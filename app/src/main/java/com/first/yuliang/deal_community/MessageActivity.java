package com.first.yuliang.deal_community;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.first.yuliang.deal_community.pojo.User;

import io.rong.imkit.RongIM;

public class MessageActivity extends AppCompatActivity implements View.OnClickListener{

    private ImageView iv_message_back;
    private RelativeLayout rl_wuliu_message;
    private RelativeLayout rl_sixin_message;
    private RelativeLayout rl_system_message;
    private RelativeLayout rl_community_message;
    private User user=null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        initView();
        initEvent();
        initData();

        Intent intent=getIntent();
        int id=Integer.parseInt(intent.getStringExtra("id").toString().trim());
       //getContactorData(id);

    }

    private void initView() {
        iv_message_back = ((ImageView) findViewById(R.id.iv_message_back));
        rl_wuliu_message = ((RelativeLayout) findViewById(R.id.rl_wuliu_message));
        rl_sixin_message = ((RelativeLayout) findViewById(R.id.rl_sixin_message));
        rl_system_message = ((RelativeLayout) findViewById(R.id.rl_system_message));
        rl_community_message = ((RelativeLayout) findViewById(R.id.rl_community_message));

    }
    private void initEvent() {
        iv_message_back.setOnClickListener(this);
        rl_wuliu_message.setOnClickListener(this);
        rl_sixin_message.setOnClickListener(this);
        rl_system_message.setOnClickListener(this);
        rl_community_message.setOnClickListener(this);
    }
    private void initData() {

    }

    @Override
    public void onClick(View v) {
        switch(v.getId())
        {
            case R.id.iv_message_back:
                MessageActivity.this.finish();
                break;
            case R.id.rl_sixin_message:

               start();
                break;
            case R.id.rl_system_message:


                break;
            case R.id.rl_community_message:

                break;
        }
    }

    private void start() {

        if(RongIM.getInstance()!=null)
        {

            RongIM.getInstance().startConversationList(this);


        }
    }

//    private void getContactorData(int id) {
//
//
//        RequestParams params=new RequestParams(HttpUtile.zy+"/servlet/SelectUserServlet?id="+id);
//
//        x.http().get(params, new Callback.CommonCallback<String>() {
//
//            @Override
//            public void onSuccess(String result) {
//                Gson gson=new Gson();
//                user=gson.fromJson(result,User.class);
//                String token=user.getToken();
//                connect(token);
//
//               // RongIM.getInstance().refreshUserInfoCache(new UserInfo(user.getUserId()+"",user.getUserName(), Uri.parse(HttpUtile.zy1+user.getUserImg())));
//            }
//
//            @Override
//            public void onError(Throwable ex, boolean isOnCallback) {
//
//            }
//
//            @Override
//            public void onCancelled(CancelledException cex) {
//
//            }
//
//            @Override
//            public void onFinished() {
//
//            }
//        });
//
//    }
//
//    private void connect(String token) {
//
//        RongIM.connect(token, new RongIMClient.ConnectCallback() {
//            @Override
//            public void onTokenIncorrect() {
//                Log.e("Activitycc", "--onTokenIncorrect");
//            }
//
//            @Override
//            public void onSuccess(String userid) {
//
//                Log.e("Activitycc", "--onSuccess" + userid);
//
//
//
//                RongIM.getInstance().refreshUserInfoCache(new UserInfo(userid,user.getUserName(), Uri.parse(HttpUtile.zy1+user.getUserImg())));
//
//
//            }
//
//            @Override
//            public void onError(RongIMClient.ErrorCode errorCode) {
//
//                Log.e("Activitycc", "--onError" + errorCode);
//            }
//        });
//    }


    @Override
    public void onBackPressed() {
        MessageActivity.this.finish();
    }
}
