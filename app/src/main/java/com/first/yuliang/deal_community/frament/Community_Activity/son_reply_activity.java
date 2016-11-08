package com.first.yuliang.deal_community.frament.Community_Activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
import com.first.yuliang.deal_community.frament.pojo.Reply;
import com.first.yuliang.deal_community.frament.pojo.Reply_son;
import com.first.yuliang.deal_community.frament.utiles.HttpUtile;
import com.first.yuliang.deal_community.frament.utiles.ToastUtil;
import com.first.yuliang.deal_community.pojo.User;
import com.google.gson.Gson;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.image.ImageOptions;
import org.xutils.x;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.List;

import static android.widget.ImageView.ScaleType.CENTER_CROP;
import static com.first.yuliang.deal_community.R.id.back_son;

public class son_reply_activity extends AppCompatActivity implements View.OnClickListener {

    private TextView lou_num;
    private ImageView back;
    private ImageView photo;
    private TextView son_name;
    private TextView son_content;
    private TextView son_lou_num;
    private TextView son_time;
    private Reply reply;
    private NoScrollGridView son_gview;
    private RelativeLayout son_remark;
    private EditText son_et_huifu;
    private Button btn_fabiao_son;
    private InputMethodManager imm;
    private ScrollView sv_sonlist;
    private ImageView huifu_son_img;
    private Integer _key;

    private User user;
    private  int _id;
    private BaseAdapter sonadapter;
    Dialog progressDialog;
    private List<Reply_son> son_list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_son_reply_activity);
        lou_num = ((TextView) findViewById(R.id.son_num));
        back = ((ImageView) findViewById(back_son));
        huifu_son_img = ((ImageView) findViewById(R.id.huifu_img2));
        photo = ((ImageView) findViewById(R.id.son_userphoto));
        son_name = ((TextView) findViewById(R.id.son_name));
        son_content = ((TextView) findViewById(R.id.son_content));
        son_lou_num = ((TextView) findViewById(R.id.son_lou_num));
        son_time = ((TextView) findViewById(R.id.son_time));
        son_gview = ((NoScrollGridView) findViewById(R.id.son_gridview));
        sv_sonlist = ((ScrollView) findViewById(R.id.son_sv_second));
        //回复相关控件
        son_remark = ((RelativeLayout) findViewById(R.id.son_remark));
        son_et_huifu = ((EditText) findViewById(R.id.son_et_huifu));
        btn_fabiao_son = ((Button) findViewById(R.id.btn_fabiao_son));
        _id = this.getSharedPreferences("shared_loginn_info", Context.MODE_PRIVATE).getInt("id", 0);

        if (_id!=0){
            getusre(_id);
        }

        //滑动监听事件
        sv_sonlist.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                son_et_huifu.clearFocus();
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                son_remark.setVisibility(View.GONE);
                return false;
            }
        });
        //
        huifu_son_img.setOnClickListener(this);

        //键盘相关
        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        son_et_huifu.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    imm.showSoftInput(v, InputMethodManager.SHOW_FORCED);

                } else {
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

                }
            }
        });
        //et_huifu.requestFocus();

        Intent intent = getIntent();
        reply = intent.getParcelableExtra("reply");
        Integer a = Integer.parseInt(intent.getStringExtra("position"));
        String tag = intent.getStringExtra("tag");
        _key = Integer.parseInt(intent.getStringExtra("key"));


        ImageOptions options1=new ImageOptions.Builder()
                .setImageScaleType(CENTER_CROP)
                .setFailureDrawableId(R.drawable.head_)
                .setLoadingDrawableId(R.drawable.head_)
                .setCircular(true)
                .build();

         x.image().bind(photo,HttpUtile.zy1+reply.getReplyuser().getUserImg(),options1);

        if (tag.equals("楼主")) {
            son_et_huifu.requestFocus();
        } else if (tag.equals("查看")) {
            son_remark.setVisibility(View.GONE);
        } else {
            //   intent.putExtra("key"

            son_et_huifu.setText("回复 " + tag + ":");
            son_et_huifu.requestFocus();
        }


        son_name.setText(reply.getReplyuser().getUserName());
        son_content.setText(reply.getReplycontent());
        lou_num.setText("第" + (a + 1) + "楼");
        String time = DateUtils.dateToString(DateUtils.stringToDate(reply.getTime(), "yyyy-MM-dd hh:mm:ss"), "MM月dd日 HH:mm:ss");
        son_time.setText(time);
//        if (reply.rslist.size() != 0) {
           son_list = reply.rslist;


            //填充子回复
            sonadapter = new BaseAdapter() {
                private TextView huifu_content;
                private TextView fusername;

                @Override
                public int getCount() {

                    return son_list.size();
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
                    View view = View.inflate(son_reply_activity.this, R.layout.reply_son_item, null);
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
            son_gview.setAdapter(sonadapter);
            son_gview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    son_remark.setVisibility(View.VISIBLE);
                    son_et_huifu.requestFocus();
                    _key = position;
                    son_et_huifu.setText("回复 " + son_list.get(position).getRs_from_user().getUserName() + ":");
                    son_et_huifu.setSelection(("回复 " + son_list.get(position).getRs_from_user().getUserName() + ":").length());

                }
            });



        back.setOnClickListener(this);
        btn_fabiao_son.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_son:
                Intent intent =new Intent();
                List<Reply_son> ss=son_list;
                intent.putExtra("son_list",(Serializable)ss);

                son_reply_activity.this.setResult(0,intent);
                son_reply_activity.this.finish();
                break;
            case R.id.huifu_img2:
                _key = -1;
                son_et_huifu.setText("");
                son_remark.setVisibility(View.VISIBLE);
                son_et_huifu.requestFocus();
                break;
            case R.id.btn_fabiao_son:
                if (_id!=0){

                if (son_et_huifu.getText().toString().equals("")) {
                    ToastUtil.show(son_reply_activity.this, "输入不能为空");
                } else {
                    String[] str = son_et_huifu.getText().toString().split(":");
                    String s = "";
                    User userfrom = user;
                    User touser;
                    if (_key == -1) {
                        s = son_et_huifu.getText().toString();
                        touser = reply.getReplyuser();
                    } else {
                        for (int i = 1; i < str.length; i++) {
                            s += str[i];
                        }
                        touser = reply.rslist.get(_key).getRs_from_user();
                    }


                    Reply_son son = new Reply_son(null, reply.getReplyid(), userfrom, touser, s, DateUtils.dateToString(new Date(), "yyyy-MM-dd hh:mm:ss"));
                    reply.rslist.add(son);
                    sonadapter.notifyDataSetChanged();
                    son_et_huifu.clearFocus();
                    sendSonReply(son);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    son_remark.setVisibility(View.GONE);

                }

                ToastUtil.show(son_reply_activity.this, "回复人的id：" + _key);


                }
                break;
        }

    }

    @Override
    public void onBackPressed() {
        Intent intent =new Intent();
        List<Reply_son> ss=son_list;
        intent.putExtra("son_list",(Serializable)ss);

        son_reply_activity.this.setResult(0,intent);
        son_reply_activity.this.finish();
    }

    private void sendSonReply(Reply_son son) {

        final Reply_son son1=son;

        progressDialog = ToolsClass.createLoadingDialog(son_reply_activity.this, "发布中...", true,
                0);
        progressDialog.show();
        //  Reply_son son = new Reply_son(null, reply.getReplyid(), userfrom, touser, s, DateUtils.dateToString(new Date(), "yyyy-MM-dd hh:mm:ss"));
        RequestParams params=new RequestParams(HttpUtile.yu+"/community/toaddreplyson");
        params.addQueryStringParameter("replyid",son.getReplyid()+"");
        params.addQueryStringParameter("fromuserid",son.getRs_from_user().getUserId()+"");
        params.addQueryStringParameter("touserid",son.getRs_to_user().getUserId()+"");
        params.addQueryStringParameter("content",son.getRs_content());
        params.addQueryStringParameter("time",son.getTime());

        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {

                sendMessage(son1.getRs_to_user().getUserId());
                ToastUtil.show(son_reply_activity.this,"成功");
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

                reply.rslist.remove( reply.rslist.size()-1);
                sonadapter.notifyDataSetChanged();
                ToastUtil.show(son_reply_activity.this,"失败");
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

    private void sendMessage(int userId) {

        RequestParams params=new RequestParams(HttpUtile.zy+"servlet/servlet/SendMessageShequServlet");
        params.addBodyParameter("userId",userId+"");
        try {
            params.addBodyParameter("str", URLEncoder.encode("您收到一条新的社区回复","utf-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        x.http().get(params, new Callback.CacheCallback<String>() {


            @Override
            public void onSuccess(String result) {

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

    private void getusre(int id) {
        progressDialog = ToolsClass.createLoadingDialog(son_reply_activity.this, "加载中...", true,
                0);
        progressDialog.show();
        RequestParams params=new RequestParams(HttpUtile.yu+"/community/togetuserbyid?userid="+id);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Gson gson=new Gson();
                user=gson.fromJson(result,User.class);
                ToastUtil.show(son_reply_activity.this,"加载成功");

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                ToastUtil.show(son_reply_activity.this,"加载出错");
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
}
