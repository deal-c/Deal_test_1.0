package com.first.yuliang.deal_community.frament.Community_Activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.first.yuliang.deal_community.R;
import com.first.yuliang.deal_community.ToolsClass;
import com.first.yuliang.deal_community.Util.NoScrollGridView;
import com.first.yuliang.deal_community.frament.pojo.Post;
import com.first.yuliang.deal_community.frament.utiles.HttpUtile;
import com.first.yuliang.deal_community.frament.utiles.ToastUtil;
import com.first.yuliang.deal_community.pojo.User;
import com.google.gson.Gson;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import me.nereo.multi_image_selector.MultiImageSelector;

import static com.first.yuliang.deal_community.frament.testpic.Bimp.revitionImageSize;

public class pubpostActivity extends AppCompatActivity implements View.OnClickListener{

    private TextView back;
    private EditText title;
    private EditText desc;
    private Button pub_post;
    private NoScrollGridView gv_photos;
    private static final int REQUEST_IMAGE = 2;

    private ArrayList<String> imgs = new ArrayList<String>();
    Dialog progressDialog;
    private BaseAdapter madapter;
    private List<File> files = new ArrayList<File>();
    private File file;
    private int comid;
    private int userid;
    private Intent intent;
    private ImageView biaoqian;
    private TextView choose_way;
    private PopupWindow popupWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pubpost);
        initview();
        intent=getIntent();


        comid=Integer.parseInt(intent.getStringExtra("comid"));
        userid = this.getSharedPreferences("shared_loginn_info", Context.MODE_PRIVATE).getInt("id", 0);

        madapter = new BaseAdapter() {
            private ImageView product_photo;

            @Override
            public int getCount() {
                return imgs.size() + 1;
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
                View view = View.inflate(pubpostActivity.this, R.layout.choosephotoitem, null);
                product_photo = ((ImageView) view.findViewById(R.id.product_photo));
                if (position != imgs.size()) {

                    String img_path = imgs.get(position);

                    //对图片进行压缩
//                    BitmapFactory.Options options = new BitmapFactory.Options();
//
//                    options.inJustDecodeBounds = false;
//
//                 options.inSampleSize =2;   // width，hight设为原来的十分一
//
//
//                    //  获取资源图片
//                    Bitmap bmp = BitmapFactory.decodeFile(img_path, options);
                    Bitmap bmp1=null;
                    try {
                        bmp1=revitionImageSize(img_path);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    product_photo.setImageBitmap(bmp1);
                    return view;
                } else {
                    return view;
                }
            }
        };
        gv_photos.setAdapter(madapter);
        gv_photos.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if (position != imgs.size()) {
                    initDialog(position);
                }
                return true;
            }
        });
        back.setOnClickListener(this);
        pub_post.setOnClickListener(this);
        biaoqian.setOnClickListener(this);
        gv_photos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == imgs.size()) {
                    pickImage();
                } else {
                    ToastUtil.show(pubpostActivity.this, "长按取消图片");
                }
            }
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE) {
            if (resultCode == RESULT_OK) {
                imgs = data.getStringArrayListExtra(MultiImageSelector.EXTRA_RESULT);

                for (String s : imgs) {

                    File file = new File(s);

                    files.add(file);
                }


                madapter.notifyDataSetChanged();
            }
        }
    }

    private void initview() {
        back = ((TextView) findViewById(R.id.backcom_));
        title = ((EditText) findViewById(R.id.pub_post_title));
        desc = ((EditText) findViewById(R.id.pub_post_desc));
        pub_post = ((Button) findViewById(R.id.btn_punish_post));
        gv_photos = ((NoScrollGridView) findViewById(R.id.gv_photos_post));
        biaoqian = ((ImageView) findViewById(R.id.biaoqian));
        choose_way = ((TextView) findViewById(R.id.choose_way));
    }
    private void initDialog(final int position) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("提示"); //设置标题
        builder.setMessage("是否删除图片？"); //设置内容

        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();//关闭dialog
                Toast.makeText(pubpostActivity.this, "已取消", Toast.LENGTH_SHORT).show();
                imgs.remove(position);
                files.remove(position);
                madapter.notifyDataSetChanged();

            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() { //设置取消按钮
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    private void pickImage() {
        MultiImageSelector selector = MultiImageSelector.create(pubpostActivity.this);
        selector.showCamera(true);
        selector.count(3);
        selector.multi();
        selector.origin(imgs);
        selector.start(pubpostActivity.this, REQUEST_IMAGE);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.backcom_:
                finish();
                break;
            case R.id.btn_punish_post:
                if (userid!=0){

                    indialog();
                }else {
                    ToastUtil.show(pubpostActivity.this, "尚未登录");

                }

                break;
            case R.id.biaoqian:
                initpopupwindow(v);
                break;
            case R.id.btn_1:
                choose_way.setVisibility(View.GONE);
                popupWindow.dismiss();
                break;
            case R.id.btn_2:
                choose_way.setVisibility(View.VISIBLE);
                popupWindow.dismiss();

                break;
        }
    }

    private void initpopupwindow(View view) {
        View v = LayoutInflater.from(this).inflate(R.layout.popwindow_item, null);
        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);

        popupWindow= new PopupWindow(v,ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT);

        //popupwiondow外面点击，popupwindow消失
        popupWindow.setFocusable(true);
        popupWindow.update();
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());


        popupWindow.showAsDropDown(view);
        ((Button) v.findViewById(R.id.btn_1)).setOnClickListener(this);
        ((Button) v.findViewById(R.id.btn_2)).setOnClickListener(this);

    }

    private void addPost() {

        String postTitle="";
        User user=new User(userid);

        if (choose_way.isShown()){
            postTitle="[再利用]"+title.getText().toString();
            ToastUtil.show(pubpostActivity.this, "再利用");

        }else {
            postTitle=title.getText().toString();
            ToastUtil.show(pubpostActivity.this, "普通帖");

        }
        String postInfo=desc.getText().toString();
        Post post=new Post(null,user,postTitle,null,postInfo,null,comid);

        RequestParams requestParams = new RequestParams(HttpUtile.yu + "/uploadpro/upimg");
        requestParams.setMultipart(true);

        requestParams.setConnectTimeout(8*1000);

        for (int i = 0; i < files.size(); i++) {
            File f = files.get(i);
            requestParams.addBodyParameter("file", f);
        }

        Gson gson=new Gson();
        String info=gson.toJson(post);
        requestParams.addBodyParameter("info", info);
        requestParams.addBodyParameter("latitude","22.2");
        requestParams.addBodyParameter("longitude","22.2");

        x.http().post(requestParams, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.i("ModifyPersonInfo", "onSuccess: ");
                progressDialog.dismiss();
                ToastUtil.show(pubpostActivity.this, "发布成功");
                  finish();

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                progressDialog.dismiss();
                ToastUtil.show(pubpostActivity.this, "发布失败");
            }

            @Override
            public void onCancelled(CancelledException cex) {


            }

            @Override
            public void onFinished() {

            }
        });




    }
    private void indialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(pubpostActivity.this);
        builder.setTitle("提示"); //设置标题
        builder.setMessage("是否确定发布？"); //设置内容
        ToastUtil.show(pubpostActivity.this, title.getText().toString()+desc.getText().toString());
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();//关闭dialog
                progressDialog = ToolsClass.createLoadingDialog(pubpostActivity.this, "发布中...", true,
                        0);
                progressDialog.show();

               addPost();


                Toast.makeText(getApplicationContext(), "已确定", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() { //设置取消按钮
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }
    }
