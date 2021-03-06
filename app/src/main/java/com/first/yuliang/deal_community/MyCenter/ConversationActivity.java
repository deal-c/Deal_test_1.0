package com.first.yuliang.deal_community.MyCenter;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.first.yuliang.deal_community.R;

import io.rong.imkit.RongIM;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.UserInfo;

public class ConversationActivity extends AppCompatActivity {

    private TextView tv_conversation_contactor;

    int id=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation);


        tv_conversation_contactor = ((TextView) findViewById(R.id.tv_conversation_contactor));

        tv_conversation_contactor.setText(getIntent().getData().getQueryParameter("title"));
        //id=Integer.parseInt(getIntent().getData().getQueryParameter("targetId").toString());


        RongIM.setConversationBehaviorListener(new RongIM.ConversationBehaviorListener() {


            @Override
            public boolean onUserPortraitClick(Context context, Conversation.ConversationType conversationType, UserInfo userInfo) {

                //Toast.makeText(context, "点击了头像", Toast.LENGTH_SHORT).show();

                return false;
            }

            @Override
            public boolean onUserPortraitLongClick(Context context, Conversation.ConversationType conversationType, UserInfo userInfo) {
                return false;
            }

            @Override
            public boolean onMessageClick(Context context, View view, Message message) {
                return false;
            }

            @Override
            public boolean onMessageLinkClick(Context context, String s) {
                return false;
            }

            @Override
            public boolean onMessageLongClick(Context context, View view, Message message) {
                return false;
            }
        });


    }
}
