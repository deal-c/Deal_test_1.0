package com.first.yuliang.deal_community;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.first.yuliang.deal_community.frament.utiles.HttpUtile;
import com.first.yuliang.deal_community.pojo.User;
import com.google.gson.Gson;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import io.rong.imkit.RongIM;
import io.rong.imlib.model.UserInfo;

public class MaijiaInfoActivity extends AppCompatActivity implements View.OnClickListener{

    private ImageView iv_sichat;

    int id=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maijia_info);
        Intent intent=getIntent();
        id=Integer.parseInt(intent.getStringExtra("id").toString().trim());
        getUserDetail();


        iv_sichat = ((ImageView) findViewById(R.id.iv_sichat));
        iv_sichat.setOnClickListener(this);
    }

    private void getUserDetail() {

        RequestParams params=new RequestParams(HttpUtile.zy+"/servlet/SelectUserServlet?id="+id);

        x.http().get(params, new Callback.CommonCallback<String>() {

            @Override
            public void onSuccess(String result) {

                Gson gson=new Gson();
                User user=gson.fromJson(result,User.class);
                RongIM.getInstance().refreshUserInfoCache(new UserInfo(user.getUserId()+"",user.getUserName(), Uri.parse(HttpUtile.zy1+user.getUserImg())));


            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

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
        switch(v.getId())
        {
            case R.id.iv_sichat:
                start();
                break;

        }
    }

    private void start() {

        if(RongIM.getInstance()!=null)
        {
            RongIM.getInstance().startPrivateChat(MaijiaInfoActivity.this,id+"","");

        }
    }


}
