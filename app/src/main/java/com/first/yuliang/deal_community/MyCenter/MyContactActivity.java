package com.first.yuliang.deal_community.MyCenter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.first.yuliang.deal_community.R;


public class MyContactActivity extends AppCompatActivity {

    int id=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_contact);


        Intent intent=getIntent();

        id=Integer.parseInt(intent.getStringExtra("userId").toString().trim());

        //getUserData(id);




    }

//    private void getToken(int id) {
//
//        RequestParams params=new RequestParams(HttpUtile.zy+"servlet/getTokenServlet");
//        params.addBodyParameter("id",id+"");
//        x.http().post(params, new Callback.CacheCallback<String>() {
//
//
//            @Override
//            public void onSuccess(String result) {
//
//                if(result!=null)
//                {
//                    connect(result);
//                }
//
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
//
//            @Override
//            public boolean onCache(String result) {
//                return false;
//            }
//        });
//    }

//    private void connect(String token) {
//        RongIM.connect(token, new RongIMClient.ConnectCallback() {
//            @Override
//            public void onTokenIncorrect() {
//                Log.e("MyContactActivity", "--onTokenIncorrect");
//            }
//
//            @Override
//            public void onSuccess(String userid) {
//
//                Log.e("MyContactActivity", "--onSuccess" + userid);
//                //RongIM.getInstance().refreshUserInfoCache(new UserInfo(getIntent().getStringExtra("userId"),getIntent().getStringExtra("name"), Uri.parse(getIntent().getStringExtra("img"))));
//               // RongIM.getInstance().refreshUserInfoCache(new UserInfo(userid,));
//
//
//            }
//
//            @Override
//            public void onError(RongIMClient.ErrorCode errorCode) {
//
//                Log.e("MyContactActivity", "--onError" + errorCode);
//            }
//        });
//    }


//    private void  getUserData(int id)
//    {
//
//        Log.e("hhhh","+++++" );
//
//
//        RequestParams params=new RequestParams(HttpUtile.zy+"/servlet/SelectUserServlet?id="+id);
//
//        x.http().get(params, new Callback.CommonCallback<String>() {
//
//            @Override
//            public void onSuccess(String result) {
//
//                Gson gson=new Gson();
//                User user=gson.fromJson(result,User.class);
//
//
//                connect(user.getToken());
//
//
//
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
}
