package com.first.yuliang.deal_community;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.first.yuliang.deal_community.Util.DateUtils;
import com.first.yuliang.deal_community.frament.utiles.HttpUtile;
import com.first.yuliang.deal_community.pojo.Comment;
import com.first.yuliang.deal_community.pojo.Product;
import com.first.yuliang.deal_community.pojo.User;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import io.rong.imkit.RongIM;
import io.rong.imlib.model.UserInfo;

public class MaijiaInfoActivity extends AppCompatActivity implements View.OnClickListener{

    private ImageView iv_sichat;
    private User user;

    int id=0;

    private ImageView iv_shangjia_tx;
    private TextView tv_shangjia_often;
    private ImageView iv_guanzhu;
    private ListView lv_shangjia_Info;

    List<Product> productList=new ArrayList<>();
    List<Comment> commentList=new ArrayList<>();
    BaseAdapter adapter;
    int commodityId=0;
    int buyerId=0;
    private InputMethodManager imm;
    private Button btn_comment;
    private RelativeLayout rl_comment;
    private EditText et_comment;
    private ListView lv_shangjia_comment;

    private TextView tv_shangjia_name;
    private RelativeLayout rl_guanzhu_pinglun;
    private TextView tv_comment_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maijia_info);

        iv_sichat = ((ImageView) findViewById(R.id.iv_sichat));
        iv_guanzhu = ((ImageView) findViewById(R.id.iv_guanzhu));
        iv_shangjia_tx = ((ImageView) findViewById(R.id.iv_shangjia_tx));
        tv_shangjia_name = ((TextView) findViewById(R.id.tv_shangjia_name));
        tv_shangjia_often = ((TextView) findViewById(R.id.tv_shangjia_often));
        lv_shangjia_Info = ((ListView) findViewById(R.id.lv_shangjia_Info));
        lv_shangjia_comment = ((ListView) findViewById(R.id.lv_shangjia_comment));
        btn_comment = ((Button) findViewById(R.id.btn_comment));
        rl_comment = ((RelativeLayout) findViewById(R.id.rl_comment));
        rl_guanzhu_pinglun = ((RelativeLayout) findViewById(R.id.rl_guanzhu_pinglun));


        et_comment = ((EditText) findViewById(R.id.et_comment));

        iv_sichat.setOnClickListener(this);
        SharedPreferences preference = getSharedPreferences("shared_loginn_info", Context.MODE_PRIVATE);
        if (preference.getInt("commentToMaijia", 0) == 0) {

            Intent intent = getIntent();
            user = intent.getParcelableExtra("bundle");

            id = user.getUserId();
            RongIM.getInstance().refreshUserInfoCache(new UserInfo(user.getUserId() + "", user.getUserName(), Uri.parse(HttpUtile.zy1 + user.getUserImg())));

            x.image().bind(iv_shangjia_tx, HttpUtile.zy1 + user.getUserImg());
            tv_shangjia_name.setText(user.getUserName());
            Log.e("jiushini", "+++++" + user.getUserAddress_s());
            tv_shangjia_often.setText(user.getUserAddress_s());


            rl_guanzhu_pinglun.setOnClickListener(this);

            adapter = new BaseAdapter() {
                @Override
                public int getCount() {
                    return productList.size();
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
                    View view = View.inflate(getApplicationContext(), R.layout.activity_publish_lv_item, null);

                    TextView tv_publish_product_name = ((TextView) view.findViewById(R.id.tv_publish_product_name));
                    TextView tv_publish_product_price = ((TextView) view.findViewById(R.id.tv_publish_product_price));
                    TextView tv_publish_product_class = ((TextView) view.findViewById(R.id.tv_publish_product_class));
                    TextView tv_publish_product_desc = ((TextView) view.findViewById(R.id.tv_publish_product_desc));
                    TextView tv_publish_product_time = ((TextView) view.findViewById(R.id.tv_publish_product_time));
                    ImageView iv_publish_product = ((ImageView) view.findViewById(R.id.iv_publish_product));
                    Product product = productList.get(position);

                    tv_publish_product_name.setText(product.getProductTitle());
                    tv_publish_product_price.setText(product.getPrice() + "");
                    tv_publish_product_class.setText(product.getProductClass());
                    tv_publish_product_time.setText(DateUtils.dateToString(product.getReleaseTime()));
                    tv_publish_product_desc.setText(product.getProductDescribe());
                    x.image().bind(iv_publish_product, HttpUtile.szj + product.getProductImg());


                    return view;
                }
            };

            lv_shangjia_Info.setAdapter(adapter);

            getAllProduct();


        } else {
            lv_shangjia_Info.setVisibility(View.GONE);
            rl_comment.setVisibility(View.VISIBLE);
            lv_shangjia_comment.setVisibility(View.VISIBLE);
            imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

            et_comment.requestFocus();

            et_comment.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (hasFocus) {
                        imm.showSoftInput(v, InputMethodManager.SHOW_FORCED);

                    } else {
                        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

                    }
                }
            });


            Intent intent = getIntent();
            user = intent.getParcelableExtra("user");
            x.image().bind(iv_shangjia_tx, HttpUtile.zy1 + user.getUserImg());
            tv_shangjia_name.setText(user.getUserName());
            //Log.e("jiushini", "+++++" + user.getUserAddress_s());
            tv_shangjia_often.setText(user.getUserAddress_s());


            id = user.getUserId();
            RongIM.getInstance().refreshUserInfoCache(new UserInfo(user.getUserId() + "", user.getUserName(), Uri.parse(HttpUtile.zy1 + user.getUserImg())));

            commodityId = Integer.parseInt(intent.getStringExtra("commodityId"));
            buyerId = Integer.parseInt(intent.getStringExtra("buyerId"));


            btn_comment.setOnClickListener(this);

            adapter = new BaseAdapter() {
                @Override
                public int getCount() {
                    return commentList.size();
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
                    View view = View.inflate(MaijiaInfoActivity.this, R.layout.comment_list_item, null);


                    TextView tv_comment_name = ((TextView) view.findViewById(R.id.tv_comment_name));
                    TextView tv_comment = ((TextView) view.findViewById(R.id.tv_comment));

                    tv_comment_name.setText(commentList.get(position).getUser().getUserName());
                    tv_comment.setText(commentList.get(position).getComment());
                    return view;
                }
            };
            lv_shangjia_comment.setAdapter(adapter);


            getAllComment(buyerId, commodityId);


        }



    }

    private void getAllComment(int buyerId,int commodityId) {



        RequestParams params=new RequestParams(HttpUtile.zy+"servlet/SelectCommentServlet");
        params.addBodyParameter("id",buyerId+"");
        params.addBodyParameter("commodityId",commodityId+"");
        x.http().post(params, new Callback.CacheCallback<String>() {


            @Override
            public void onSuccess(String result) {

                Gson gson=new Gson();
                Type type=new TypeToken<List<Comment>>(){}.getType();
                List<Comment> newcommentList=new ArrayList<Comment>();
                newcommentList=gson.fromJson(result,type);
                commentList.clear();
                commentList.addAll(newcommentList);
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

    private void getAllProduct() {

        RequestParams params=new RequestParams(HttpUtile.zy+"/servlet/SelectAllProductServlet");
        params.addBodyParameter("userId",id+"");
        x.http().post(params, new Callback.CacheCallback<String>() {


            @Override
            public void onSuccess(String result) {

                Gson gson=new Gson();
                Type type=new TypeToken<List<Product>>(){}.getType();
                List<Product> newProductList=new ArrayList<Product>();
                newProductList=gson.fromJson(result,type);
                productList.clear();
                productList.addAll(newProductList);



                adapter.notifyDataSetChanged();


            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {


                Toast.makeText(MaijiaInfoActivity.this,"网络访问失败",Toast.LENGTH_LONG).show();
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
    public void onClick(View v) {
        switch(v.getId())
        {
            case R.id.iv_sichat:
                start();
                break;

            case R.id.btn_comment:
                addComment();
                break;
            case R.id.rl_guanzhu_pinglun:
                showComment();
                break;
        }
    }

    private void showComment() {
        lv_shangjia_Info.setVisibility(View.GONE);
        lv_shangjia_comment.setVisibility(View.VISIBLE);
        rl_comment.setVisibility(View.GONE);

        adapter = new BaseAdapter() {
            @Override
            public int getCount() {
                return commentList.size();
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
                View view = View.inflate(MaijiaInfoActivity.this, R.layout.comment_list_item, null);


                TextView tv_comment_name = ((TextView) view.findViewById(R.id.tv_comment_name));
                TextView tv_comment = ((TextView) view.findViewById(R.id.tv_comment));

                tv_comment_name.setText(commentList.get(position).getUser().getUserName());
                tv_comment.setText(commentList.get(position).getComment());
                return view;
            }
        };
        lv_shangjia_comment.setAdapter(adapter);

        QueryAllComment();

    }

    private void QueryAllComment() {

        RequestParams params=new RequestParams(HttpUtile.zy+"servlet/SelectAllCommentServlet");
        params.addBodyParameter("id",id+"");
        x.http().post(params, new Callback.CacheCallback<String>() {


            @Override
            public void onSuccess(String result) {

                Gson gson=new Gson();
                Type type=new TypeToken<List<Comment>>(){}.getType();
                List<Comment> newcommentList=new ArrayList<Comment>();
                newcommentList=gson.fromJson(result,type);
                commentList.clear();
                commentList.addAll(newcommentList);
                if(commentList.size()==0)
                {
                    Toast.makeText(MaijiaInfoActivity.this,"暂时没有评论",Toast.LENGTH_SHORT).show();
                }
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

    private void addComment() {

        RequestParams params=new RequestParams(HttpUtile.zy+"servlet/addCommentServlet");
        params.addBodyParameter("commodityId",commodityId+"");
        params.addBodyParameter("buyerId",buyerId+"");
        params.addBodyParameter("releaseId",id+"");
        params.addBodyParameter("comment",et_comment.getText().toString().trim());
        x.http().post(params, new Callback.CacheCallback<String>() {


            @Override
            public void onSuccess(String result) {

                if(Boolean.parseBoolean(result.trim().toString()))
                {
                    Toast.makeText(MaijiaInfoActivity.this,"评论成功",Toast.LENGTH_SHORT).show();
                    rl_comment.setVisibility(View.GONE);
                    //et_comment.getText().clear();
                    //et_comment.clearFocus();
                    getAllComment(buyerId,commodityId);

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

            @Override
            public boolean onCache(String result) {
                return false;
            }
        });
    }

    private void start() {

        if(RongIM.getInstance()!=null)
        {
            RongIM.getInstance().startPrivateChat(MaijiaInfoActivity.this,id+"",user.getUserName());
        }
    }


}
