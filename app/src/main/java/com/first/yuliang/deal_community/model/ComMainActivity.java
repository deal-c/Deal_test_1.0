package com.first.yuliang.deal_community.model;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.first.yuliang.deal_community.R;
import com.first.yuliang.deal_community.frament.pojo.MyComminity;
import com.google.gson.Gson;

import org.w3c.dom.Comment;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;

public class ComMainActivity extends Activity {

    public static User sUser = new User(1, "走向远方"); // 当前登录用户

    public ListView mListView;
    public MomentAdapter mAdapter;
    public Moment moment;
    public Moment mymoment;
    ArrayList<Comment> comments = new ArrayList<Comment>();
    ;
    ArrayList<Moment> moments = new ArrayList<>();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.main);
        getAllDynamic();
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


        mAdapter = new MomentAdapter(this, moments, new CustomTagHandler(this, new CustomTagHandler.OnCommentClickListener() {
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

    private void getAllDynamic() {
        final RequestParams request = new RequestParams("http://10.40.5.61:8080/usys/DynamicServlt");
        x.http().post(request, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                //  moments = new ArrayList<Moment>();

                Gson gson = new Gson();

                Toast.makeText(ComMainActivity.this, result + "", Toast.LENGTH_LONG).show();
                moment = gson.fromJson(result, Moment.class);//将获得的数据封装进入社区动态bean
                // Toast.makeText(DongtaiActivity.this,result,Toast.LENGTH_LONG).show();

                System.out.println(moment.getDate() + "????");
                //comments.addAll(moment.mComment);
                // moments.add(moment);
                if (result != null) {

                    moments.add(moment);
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
            public void onCommitComment() {
                mAdapter.notifyDataSetChanged();
            }
        });
    }
}
