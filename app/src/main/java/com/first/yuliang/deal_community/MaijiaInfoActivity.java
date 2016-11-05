package com.first.yuliang.deal_community;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.first.yuliang.deal_community.frament.utiles.HttpUtile;
import com.first.yuliang.deal_community.pojo.User;

import org.xutils.x;

import io.rong.imkit.RongIM;
import io.rong.imlib.model.UserInfo;

public class MaijiaInfoActivity extends AppCompatActivity implements View.OnClickListener{

    private ImageView iv_sichat;
    private User user;

    int id=0;
    private TextView tv_shangjia_name;
    private ImageView iv_shangjia_tx;
    private TextView tv_shangjia_often;
    private ImageView iv_guanzhu;
    private ListView lv_shangjia_Info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maijia_info);

        Intent intent=getIntent();
        user = intent.getParcelableExtra("bundle");

        RongIM.getInstance().refreshUserInfoCache(new UserInfo(user.getUserId()+"",user.getUserName(), Uri.parse(HttpUtile.zy1+user.getUserImg())));
        //id=user.getUserId();
       // getUserDetail();


        iv_sichat = ((ImageView) findViewById(R.id.iv_sichat));
        iv_guanzhu = ((ImageView) findViewById(R.id.iv_guanzhu));
        iv_shangjia_tx = ((ImageView) findViewById(R.id.iv_shangjia_tx));
        tv_shangjia_name = ((TextView) findViewById(R.id.tv_shangjia_name));
        tv_shangjia_often = ((TextView) findViewById(R.id.tv_shangjia_often));
        lv_shangjia_Info = ((ListView) findViewById(R.id.lv_shangjia_Info));
        iv_sichat.setOnClickListener(this);

        x.image().bind(iv_shangjia_tx,HttpUtile.zy1+user.getUserImg());
        tv_shangjia_name.setText(user.getUserName());
        Log.e("jiushini","+++++"+user.getUserAddress_s());
        tv_shangjia_often.setText(user.getUserAddress_s());


    }

//    private void getUserDetail() {
//
//        RequestParams params=new RequestParams(HttpUtile.zy+"/servlet/SelectUserServlet?id="+id);
//
//        x.http().get(params, new Callback.CommonCallback<String>() {
//
//            @Override
//            public void onSuccess(String result) {
//
//                Gson gson=new Gson();
//                User user= null;
//                try {
//                    user = gson.fromJson(URLDecoder.decode(result,"utf-8"),User.class);
//                } catch (UnsupportedEncodingException e) {
//                    e.printStackTrace();
//                }
//                RongIM.getInstance().refreshUserInfoCache(new UserInfo(user.getUserId()+"",user.getUserName(), Uri.parse(HttpUtile.zy1+user.getUserImg())));
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
//    }

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
            RongIM.getInstance().startPrivateChat(MaijiaInfoActivity.this,id+"",user.getUserName());

        }
    }


}
