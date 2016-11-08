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


                Intent intent=new Intent(this,SystemMessageListActivity.class);
                startActivity(intent);
                break;
            case R.id.rl_community_message:

               Intent intentSqu=new Intent(this,ShequActivity.class);
                startActivity(intentSqu);
                break;
            case R.id.rl_wuliu_message:


                start();
                break;
        }
    }

    private void start() {


        if(RongIM.getInstance()!=null)
        {
            RongIM.getInstance().startConversationList(this);
        }



    }




    @Override
    public void onBackPressed() {
        MessageActivity.this.finish();
    }
}
