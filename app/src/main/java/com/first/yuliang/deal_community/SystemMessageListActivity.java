package com.first.yuliang.deal_community;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.first.yuliang.deal_community.frament.utiles.HttpUtile;
import com.first.yuliang.deal_community.pojo.Message;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class SystemMessageListActivity extends AppCompatActivity {

    private ListView lv_system_list;
    BaseAdapter adapter;
    List<Message> messageList=new ArrayList<>();

    int id=0;
    private ImageButton ib_return_common;
    private TextView tv_activity_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_system_message_list);

        id=this.getSharedPreferences("shared_loginn_info", Context.MODE_PRIVATE).getInt("id",0);
        lv_system_list = ((ListView) findViewById(R.id.lv_system_list));
        tv_activity_title = ((TextView) findViewById(R.id.tv_activity_title));
        tv_activity_title.setText("系统消息");
        ib_return_common = ((ImageButton) findViewById(R.id.ib_return_common));
        ib_return_common.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SystemMessageListActivity.this.finish();
            }
        });
        adapter=new BaseAdapter() {
            @Override
            public int getCount() {
                return messageList.size();
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
                View view=View.inflate(SystemMessageListActivity.this,R.layout.activity_system_message_list_item,null);
                TextView tv_message_item=((TextView) view.findViewById(R.id.tv_message_item));

                tv_message_item.setText(messageList.get(position).getMessage());

                return view;
            }
        };

        getAllSystemMessage();
        lv_system_list.setAdapter(adapter);

    }

    private void getAllSystemMessage() {
        RequestParams params=new RequestParams(HttpUtile.zy+"servlet/SelectAllMessageServlet");
        params.addBodyParameter("userId",id+"");
        x.http().post(params, new Callback.CacheCallback<String>() {


            @Override
            public void onSuccess(String result) {

                Gson gson=new Gson();
                Type type=new TypeToken<List<Message>>(){}.getType();
                List<Message> newmessageList=new ArrayList<Message>();
                newmessageList=gson.fromJson(result,type);
                messageList.clear();
                messageList.addAll(newmessageList);

                adapter.notifyDataSetChanged();

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

            @Override
            public boolean onCache(String result) {
                return false;
            }
        });

    }

    @Override
    public void onBackPressed() {
        SystemMessageListActivity.this.finish();
    }
}
