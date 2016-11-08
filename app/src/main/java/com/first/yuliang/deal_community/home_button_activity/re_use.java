package com.first.yuliang.deal_community.home_button_activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.first.yuliang.deal_community.R;
import com.first.yuliang.deal_community.frament.Community_Activity.tieziactivity;
import com.first.yuliang.deal_community.frament.pojo.Post;
import com.first.yuliang.deal_community.frament.utiles.HttpUtile;
import com.first.yuliang.deal_community.frament.utiles.ToastUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class re_use extends AppCompatActivity {

    private ListView zailiyong_listview;
    BaseAdapter madapter;
    private  List<Post>postList=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_re_use);
        zailiyong_listview = ((ListView) findViewById(R.id.zailiyong_listview));

        madapter=new BaseAdapter() {
            private ImageView img;
            private TextView username;
            private TextView title;

            @Override
            public int getCount() {
                return postList.size();
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
                View  v=View.inflate(re_use.this,R.layout.zailiyong_item,null);
                title = ((TextView) v.findViewById(R.id.reuse_title));
                username = ((TextView) v.findViewById(R.id.reuse_username));
                img = ((ImageView) v.findViewById(R.id.reuse_img));

                if (postList.size()!=0){

                Post post=postList.get(position);
                title.setText(post.getPostTitle());
                username.setText(post.getPostTime());
                x.image().bind(img,HttpUtile.yu+post.getImgs());

                }
                return v;
            }
        };
        View v=View.inflate(re_use.this,R.layout.zailiyong_best_item,null);
        zailiyong_listview.addHeaderView(v);
        zailiyong_listview.setAdapter(madapter);

        zailiyong_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               Post post= postList.get(position-1);
                Intent intent=new Intent(re_use.this, tieziactivity.class);

                intent.putExtra("post",post);

                startActivity(intent);

            }
        });
        getreuserlist();
    }

    private void getreuserlist() {
        RequestParams params=new RequestParams(HttpUtile.yu+"/community/togetreuse");

        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Gson gson=new Gson();
                Type type=new TypeToken<List<Post>>(){}.getType();
                postList=gson.fromJson(result,type);
                madapter.notifyDataSetChanged();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                ToastUtil.show(re_use.this,"获取数据失败");
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
