package com.first.yuliang.deal_community.model;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.first.yuliang.deal_community.R;
import com.first.yuliang.deal_community.frament.pojo.Dynamic;
import com.first.yuliang.deal_community.frament.utiles.HttpUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.readystatesoftware.systembartint.SystemBarTintManager;


import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ComMainActivity extends Activity {

    public static User sUser = new User(1, "走向远方"); // 当前登录用户
    public static  ArrayList<Dynamic>  dynamicArrayList;
    public ListView mListView;
    public MomentAdapter mAdapter;
    public Comment moment;
    ArrayList<Comment> moments = new ArrayList<>();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.main);
        setColor();
        getAllDynamic();
        Intent it = getIntent();
     String sendynamiclist=it.getStringExtra("dynamicList");
        Gson    gson=new Gson();
        Type type = new TypeToken<List<Dynamic>>() {
        }.getType();



        dynamicArrayList= gson.fromJson(sendynamiclist, type);




        // System.out.print(moment.getmComment()+"?????");
        mListView = (ListView) findViewById(R.id.list_moment);

        //   moments.add(new Moment("123",comments,"haha","heihei","hhh",123));
        // 模拟数据
        //moments.add(moment);
        // moments.add(new Moment("123",comments,"haha","heihei","hhh",123));

        //comments.add(new Comment(new User(i + 2, "用户" + i), "帅哥哥" + i, new User(i + 2, "用户" + i)));
        //comments.add(new Comment(new User(i + 100, "用户" + (i + 100)), "评论" + i, new User(i + 200, "用户" + (i + 200))));
        // comments.add(new Comment(new User(i + 200, "用户" + (i + 200)), "评论" + i, null));

        // comments.add(new Comment(new User(i + 300, "用户" + (i + 300)), "评论" + i, null));


        mAdapter = new MomentAdapter(this,dynamicArrayList ,moments, new CustomTagHandler(this, new CustomTagHandler.OnCommentClickListener() {
            @Override
            public void onCommentatorClick(View view, User commentator) {
                Toast.makeText(getApplicationContext(), commentator.mName, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onReceiverClick(View view, User receiver) {
                Toast.makeText(getApplicationContext(), receiver.mName, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onContentClick(View view, User commentator, User receiver) {
                if (commentator != null && commentator.mId == sUser.mId) { // 不能回复自己的评论
                    return;
                }
                inputComment(view, commentator);
            }
        }));
        System.out.print("为什么会空指针");

        mListView.setAdapter(mAdapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext(), "click " + position, Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void setColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window win = getWindow();
            WindowManager.LayoutParams winParams = win.getAttributes();
            final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
            winParams.flags |= bits;
            win.setAttributes(winParams);
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintEnabled(true);
            tintManager.setStatusBarTintResource(R.color.xinxilan);

        }
    }
    private void getAllDynamic() {
        final RequestParams request = new RequestParams(HttpUtils.hostLuoqingshanSchool+"/usys/DynamicServlt");
        x.http().post(request, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                ArrayList<Comment>   mymoments = new ArrayList<Comment>();

                Gson gson = new Gson();
                Type type = new TypeToken<List<Comment>>() {
                }.getType();
                mymoments=gson.fromJson(result,type);

                if (result != null) {
                    Log.e("看看数据====", mymoments.toString());
                    moments.addAll(mymoments);
                    mAdapter.notifyDataSetChanged();
                }


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

    public void inputComment(final View v) {
        inputComment(v, null);
    }

    public void inputComment(final View v, User receiver) {
        CommentFun.inputComment(ComMainActivity.this, mListView, v, receiver, new CommentFun.InputCommentListener() {
            @Override
            public void onCommitComment(Comment comment) {
                mAdapter.notifyDataSetChanged();
            }
        });
    }

    public void backonclick(View view) {
        finish();
    }
}
