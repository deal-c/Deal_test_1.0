package com.first.yuliang.deal_community.frament.Community_Activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.first.yuliang.deal_community.R;
import com.first.yuliang.deal_community.ToolsClass;
import com.first.yuliang.deal_community.Util.DateUtils;
import com.first.yuliang.deal_community.Util.NoScrollGridView;
import com.first.yuliang.deal_community.frament.pojo.Post;
import com.first.yuliang.deal_community.frament.pojo.Reply;
import com.first.yuliang.deal_community.frament.pojo.Reply_son;
import com.first.yuliang.deal_community.frament.utiles.HttpUtile;
import com.first.yuliang.deal_community.frament.utiles.HttpUtils;
import com.first.yuliang.deal_community.frament.utiles.ToastUtil;
import com.first.yuliang.deal_community.pojo.User;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.image.ImageOptions;
import org.xutils.x;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static android.widget.ImageView.ScaleType.CENTER_CROP;
import static com.first.yuliang.deal_community.R.id.btn_fabiao;

public class tieziactivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView shoucang_id;
    private RelativeLayout postbottom;
    private RelativeLayout post_remark;
    private InputMethodManager imm;
    private ScrollView tiezi;
    private RelativeLayout zhengwen;
    private NoScrollGridView relist_post;
    private BaseAdapter gvadapter;
    private Button fabiao;
    private EditText et_huifu;
    private List<Reply> replyList = new ArrayList<>();
    Dialog progressDialog;
    private User user;
    private int id;
    private Post post;
    private ImageView head_louzhu;
    private TextView louzhu_name;
    private TextView post_time;
    private TextView post_title;
    private TextView post_content;
    private ImageView post_img;
    private ImageView my_head;
    private TextView reply_num;
    private boolean stage;
    private int status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tieziactivity);
        id = this.getSharedPreferences("shared_loginn_info", Context.MODE_PRIVATE).getInt("id", 0);

        if (id != 0) {
            getusre(id);
        }
        Intent intent = getIntent();
        post = intent.getParcelableExtra("post");
        Log.e("postid", post.getPostId() + "");
        ImageOptions options1 = new ImageOptions.Builder()
                .setImageScaleType(CENTER_CROP)
                .setFailureDrawableId(R.drawable.head_)
                .setLoadingDrawableId(R.drawable.head_)
                .setCircular(true)
                .build();
        boolean find=getAllzan();

        shoucang_id = (ImageView) findViewById(R.id.shoucang_id);

        head_louzhu = ((ImageView) findViewById(R.id.head_louzhu));
        x.image().bind(head_louzhu, HttpUtile.zy1 + post.getUser().getUserImg(), options1);

        reply_num = ((TextView) findViewById(R.id.reply_num));

        louzhu_name = ((TextView) findViewById(R.id.louzhu_name));
        louzhu_name.setText(post.getUser().getUserName());

        post_time = ((TextView) findViewById(R.id.post_time));
        String time = DateUtils.dateToString(DateUtils.stringToDate(post.getPostTime(), "yyyy-MM-dd hh:mm:ss"), "MM月dd日 HH:mm:ss");
        post_time.setText(time);

        post_title = ((TextView) findViewById(R.id.post_tiltle));
        post_title.setText(post.getPostTitle());

        post_content = ((TextView) findViewById(R.id.post_content));
        post_content.setText(post.getPostInfo());

        post_img = ((ImageView) findViewById(R.id.post_img));
        x.image().bind(post_img, HttpUtile.yu + post.getImgs());


        my_head = ((ImageView) findViewById(R.id.my_head));


        postbottom = ((RelativeLayout) findViewById(R.id.post_bottom));
        post_remark = ((RelativeLayout) findViewById(R.id.post_remark));
        et_huifu = ((EditText) findViewById(R.id.et_huifu));
        tiezi = ((ScrollView) findViewById(R.id.scrollview_tiezi));
        zhengwen = ((RelativeLayout) findViewById(R.id.zhengwen));
        relist_post = ((NoScrollGridView) findViewById(R.id.post_relist));
        fabiao = ((Button) findViewById(btn_fabiao));

        shoucang_id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                switch (status) {
                    case 0:
                        shoucang_id.setBackgroundResource(R.drawable.shoucang_yes);
                        status = 1;
                        addstatus();
                        Log.e("看我的数据", "删除成功了");
                        break;
                    case 1:
                        shoucang_id.setBackgroundResource(R.drawable.shoucang_no);
                        status = 0;
                        deleteZan();
                        Log.e("看我的数据", "添加成功了");
                        break;
                }
            }
        });




        gvadapter = new BaseAdapter() {

            private ImageView reply_userphoto;
            private ImageView huifu_img;
            private TextView more_huifu1;
            private NoScrollGridView son_listview;
            private TextView re_time;
            private TextView lou_num;
            private TextView re_content;
            private TextView re_name;


            @Override
            public int getCount() {
                Log.e("getCount", replyList.size() + "");
                return replyList.size();
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
            public View getView(final int position, View convertView, ViewGroup parent) {
                View v = View.inflate(tieziactivity.this, R.layout.post_remark_item, null);
                re_name = ((TextView) v.findViewById(R.id.remark_name));
                re_content = ((TextView) v.findViewById(R.id.remark_content));
                lou_num = ((TextView) v.findViewById(R.id.lou_num));
                re_time = ((TextView) v.findViewById(R.id.remark_time));
                son_listview = ((NoScrollGridView) v.findViewById(R.id.zihuifulist));
                more_huifu1 = ((TextView) v.findViewById(R.id.more_huifu));
                huifu_img = ((ImageView) v.findViewById(R.id.huifu_img));
                reply_userphoto = ((ImageView) findViewById(R.id.reply_userphoto));


                final Integer po = position;

                if (replyList.size() != 0) {
                    final Reply reply = replyList.get(position);
                    huifu_img.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(tieziactivity.this, son_reply_activity.class);
                            Integer a = position;
                            intent.putExtra("reply", reply);
                            intent.putExtra("position", a + "");
                            intent.putExtra("key", -1 + "");
                            intent.putExtra("tag", "楼主");

                            startActivityForResult(intent, a);
                            //  startActivity(intent);

                        }
                    });

                    more_huifu1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(tieziactivity.this, son_reply_activity.class);
                            Integer a = position;
                            intent.putExtra("reply", reply);
                            intent.putExtra("position", a + "");
                            intent.putExtra("tag", "查看");
                            intent.putExtra("key", -1 + "");
                            startActivityForResult(intent, a);
                        }
                    });


                    re_name.setText(reply.getReplyuser().getUserName());
                    ImageOptions options1=new ImageOptions.Builder()
                            .setImageScaleType(CENTER_CROP)
                            .setFailureDrawableId(R.drawable.head_)
                            .setLoadingDrawableId(R.drawable.head_)
                            .setCircular(true)
                            .build();
                    Log.e("reply_userphoto", reply.getReplyuser().getUserName() + "::" + HttpUtile.zy1 + reply.getReplyuser().getUserImg());


                    x.image().bind(reply_userphoto, HttpUtile.zy1 + reply.getReplyuser().getUserImg(), options1);
                    x.image().bind(reply_userphoto,HttpUtile.zy1+ reply.getReplyuser().getUserImg(),options1);



                    re_content.setText(reply.getReplycontent());
                    lou_num.setText("第" + (position + 2) + "楼");
                    String time = DateUtils.dateToString(DateUtils.stringToDate(reply.getTime(), "yyyy-MM-dd hh:mm:ss"), "MM月dd日 HH:mm:ss");
                    re_time.setText(time);
                    if (reply.rslist.size() != 0) {

                        final List<Reply_son> son_list = reply.rslist;
                        BaseAdapter sonadapter = new BaseAdapter() {
                            private TextView huifu_content;
                            private TextView fusername;

                            @Override
                            public int getCount() {
                                int a = -1;
                                if (son_list.size() <= 2) {
                                    a = son_list.size();
                                } else {
                                    a = 2;
                                    more_huifu1.setVisibility(View.VISIBLE);
                                    more_huifu1.setText("更多" + (son_list.size() - 2) + "条回复");
                                }
                                return a;
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
                                View view = View.inflate(tieziactivity.this, R.layout.reply_son_item, null);
                                Reply_son reply_son = son_list.get(position);
                                fusername = ((TextView) view.findViewById(R.id.son_fromusername));
                                huifu_content = ((TextView) view.findViewById(R.id.son_huifu));
                                fusername.setText(reply_son.getRs_from_user().getUserName() + ":");
                                if (reply.getReplyuser().getUserId() == reply_son.getRs_to_user().getUserId()) {
                                    huifu_content.setText(reply_son.getRs_content());
                                } else {
                                    huifu_content.setText("回复 " + reply_son.getRs_to_user().getUserName() + ":" +
                                            reply_son.getRs_content());
                                }


                                return view;
                            }
                        };
                        son_listview.setAdapter(sonadapter);
                        son_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                Intent intent = new Intent(tieziactivity.this, son_reply_activity.class);
                                Integer a = position;
                                intent.putExtra("reply", reply);
                                intent.putExtra("position", po + "");
                                intent.putExtra("key", a + "");
                                intent.putExtra("tag", son_list.get(position).getRs_from_user().getUserName());
                                startActivityForResult(intent, po);
                            }
                        });

                    }


                }
                return v;
            }
        };

        relist_post.setAdapter(gvadapter);
        getReplylist(post.getPostId());
        tiezi.setOnTouchListener(new TouchListenerImpl());
        fabiao.setOnClickListener(this);
        postbottom.setOnClickListener(this);
        tiezi.setOnClickListener(this);
        zhengwen.setOnClickListener(this);
        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        et_huifu.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    imm.showSoftInput(v, InputMethodManager.SHOW_FORCED);

                } else {
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

                }
            }
        });
    }


    private void deleteZan() {
        RequestParams request = new RequestParams(HttpUtils.hostLuoqingshanSchool + "csys/Dddmyzan");
        request.addBodyParameter("userId", String.valueOf(id));
        request.addBodyParameter("dynamicId", String.valueOf(post.getPostId()));
        x.http().post(request, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.e("看我删除的数据", ex.toString());
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });


    }


    private void addstatus() {
        RequestParams request = new RequestParams(HttpUtils.hostLuoqingshanSchool + "csys/Addmyzan");
        request.addBodyParameter("userId", String.valueOf(id));
        request.addBodyParameter("dynamicId", String.valueOf(post.getPostId()));
        x.http().post(request, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.e("看我添加的数据", "onError: " + ex);
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    private boolean getAllzan() {
        final RequestParams request = new RequestParams(HttpUtils.hostLuoqingshanSchool + "csys/Getmyzan");
        request.addBodyParameter("userId", String.valueOf(id));
        request.addBodyParameter("dynamicId", String.valueOf(post.getPostId()));
        x.http().post(request, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                if (result.equals("1")) {
                    shoucang_id.setBackgroundResource(R.drawable.shoucang_yes);
                    Log.e("看我得到的数据呢", "出现了");
                    status=1;
                }


            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.e("看我的数据1111", ex.toString());
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
        return stage;

    }


    private void getusre(int id) {
        progressDialog = ToolsClass.createLoadingDialog(tieziactivity.this, "加载中...", true,
                0);
        progressDialog.show();
        RequestParams params = new RequestParams(HttpUtile.yu + "/community/togetuserbyid?userid=" + id);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                user = gson.fromJson(result, User.class);
                ImageOptions options1 = new ImageOptions.Builder()
                        .setImageScaleType(CENTER_CROP)
                        .setFailureDrawableId(R.drawable.head_)
                        .setLoadingDrawableId(R.drawable.head_)
                        .setCircular(true)
                        .build();
                x.image().bind(my_head, HttpUtile.zy1 + user.getUserImg(), options1);
                ToastUtil.show(tieziactivity.this, "加载成功");

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                ToastUtil.show(tieziactivity.this, "加载出错");
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
                progressDialog.dismiss();
            }
        });

    }

    private void getReplylist(int postid) {
        RequestParams params = new RequestParams(HttpUtile.yu + "/community/togetreplybyid?postid=" + postid);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                Type type = new TypeToken<List<Reply>>() {
                }.getType();
                replyList = gson.fromJson(result, type);
                reply_num.setText("(已有" + replyList.size() + "条)");

                gvadapter.notifyDataSetChanged();
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
        switch (v.getId()) {
            case R.id.post_bottom:

                postbottom.setVisibility(View.GONE);
                post_remark.setVisibility(View.VISIBLE);
//                et_huifu.setFocusable(true);
                et_huifu.requestFocus();
                break;
            case R.id.post_remark:

                break;
            case R.id.zhengwen:
//                ToastUtil.show(tieziactivity.this,"可以点");
                et_huifu.clearFocus();
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                postbottom.setVisibility(View.VISIBLE);
                post_remark.setVisibility(View.GONE);
                break;

            case R.id.btn_fabiao:
                if (id != 0) {
                    if (!et_huifu.getText().toString().equals("")) {
//                  public Reply(Integer replyid, Integer postid, User replyuser,
//                          String replycontent, String replyimg, String time)

                        Reply reply = new Reply(null, post.getPostId(), user, et_huifu.getText().toString(), null, DateUtils.dateToString(new Date(), "yyyy-MM-dd hh:mm:ss"));
                        replyList.add(reply);
                        addreply(reply);
                        gvadapter.notifyDataSetChanged();
                        et_huifu.clearFocus();
                        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                        postbottom.setVisibility(View.VISIBLE);
                        post_remark.setVisibility(View.GONE);

                    } else {
                        ToastUtil.show(tieziactivity.this, "回复不能为空");
                    }
                } else {
                    ToastUtil.show(tieziactivity.this, "请先登录");
                }
                break;
        }
    }

    private void addreply(Reply reply) {

        progressDialog = ToolsClass.createLoadingDialog(tieziactivity.this, "发布中...", true,
                0);
        progressDialog.show();
        RequestParams params = new RequestParams(HttpUtile.yu + "/community/toaddreply");
        params.addQueryStringParameter("postid", reply.getPostid() + "");
        params.addQueryStringParameter("userid", reply.getReplyuser().getUserId() + "");
        params.addQueryStringParameter("replycontent", reply.getReplycontent());
        params.addQueryStringParameter("time", reply.getTime());

        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                ToastUtil.show(tieziactivity.this, "成功");
                post.getUser().getUserId();

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                replyList.remove(replyList.size() - 1);
                gvadapter.notifyDataSetChanged();
                ToastUtil.show(tieziactivity.this, "失败");
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
                progressDialog.dismiss();
            }
        });

    }

    private class TouchListenerImpl implements View.OnTouchListener {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    break;
                case MotionEvent.ACTION_MOVE:
                    et_huifu.clearFocus();
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    postbottom.setVisibility(View.VISIBLE);
                    post_remark.setVisibility(View.GONE);
                    break;

                default:
                    break;
            }
            return false;
        }

    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {


//        Intent intent= getIntent();
        List<Reply_son> aaa = data.getParcelableArrayListExtra("son_list");

        replyList.get(requestCode).rslist = aaa;
        gvadapter.notifyDataSetChanged();
        ToastUtil.show(tieziactivity.this, requestCode + "回来了" + aaa.size());
    }


}
