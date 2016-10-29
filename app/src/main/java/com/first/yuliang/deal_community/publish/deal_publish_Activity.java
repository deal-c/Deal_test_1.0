package com.first.yuliang.deal_community.publish;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.first.yuliang.deal_community.R;
import com.first.yuliang.deal_community.frament.pojo.CommodityInfo;
import com.first.yuliang.deal_community.frament.utiles.ToastUtil;
import com.google.gson.Gson;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;

import me.nereo.multi_image_selector.MultiImageSelector;

public class deal_publish_Activity extends AppCompatActivity implements View.OnClickListener{

        private GridView gv_photos;
        BaseAdapter madapter;
        ArrayList<String> imgs = new ArrayList<String>();
        private static final int REQUEST_IMAGE = 2;
        private Button punish;
       File file;
    private RadioButton juan;
    private RadioButton zeng;
    private RadioButton huan;
    private RadioButton  mai;
    private ImageView backtomain;
    private EditText pro_title;
    private EditText pro_desc;
    private EditText location;
    private Spinner pro_type;
    private EditText pro_price;
    private int userid;
    private Integer buyway;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deal_publish_);

        backtomain = ((ImageView) findViewById(R.id.backTomain));
        juan = ((RadioButton) findViewById(R.id.rb_juan));
        huan = ((RadioButton) findViewById(R.id.rb_huan));
        zeng = ((RadioButton) findViewById(R.id.rb_zeng));
        mai = ((RadioButton) findViewById(R.id.rb_mai));
        punish = ((Button) findViewById(R.id.btn_punish));

        gv_photos = ((GridView) findViewById(R.id.gv_photos));
        pro_title = ((EditText) findViewById(R.id.pub_product_title));
        pro_desc = ((EditText) findViewById(R.id.pub_pro_desc));
        location = ((EditText) findViewById(R.id.ways_desc));
        pro_type = ((Spinner) findViewById(R.id.punish_type));
        pro_price = ((EditText) findViewById(R.id.publish_price));



        backtomain.setOnClickListener(this);
        Intent intent=getIntent();
        String key=intent.getStringExtra("key");
        switch (key){
            case "juan":
                juan.setChecked(true);
                buyway=1;
                break;
            case "zeng":
                zeng.setChecked(true);
                buyway=2;
                break;
            case "huan":
                huan.setChecked(true);
                buyway=3;
                break;
            case "mai":
                mai.setChecked(true);
                buyway=4;
                break;
        }

        //发布按钮的点击事件
        punish.setOnClickListener(this);

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
                View view = View.inflate(deal_publish_Activity.this, R.layout.choosephotoitem, null);
                product_photo = ((ImageView) view.findViewById(R.id.product_photo));
                if (position != imgs.size()) {
                    String img_path = imgs.get(position);

                //对图片进行压缩
                    BitmapFactory.Options options = new  BitmapFactory.Options();

                    options.inJustDecodeBounds =  false;

                    options.inSampleSize =  10;   // width，hight设为原来的十分一


                    //  获取资源图片
                    Bitmap bmp = BitmapFactory.decodeFile(img_path, options);
                    product_photo.setImageBitmap(bmp);
                    return view;
                } else {
                    return view;
                }
            }
        };

        gv_photos.setAdapter(madapter);
        gv_photos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == imgs.size()) {
                    pickImage();
                }
            }
        });

    }

    private void pickImage() {
        MultiImageSelector selector = MultiImageSelector.create(deal_publish_Activity.this);
        selector.showCamera(true);
        selector.count(6);
        selector.multi();
        selector.origin(imgs);
        selector.start(deal_publish_Activity.this, REQUEST_IMAGE);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE) {
            if (resultCode == RESULT_OK) {
                imgs = data.getStringArrayListExtra(MultiImageSelector.EXTRA_RESULT);
                 file=new File(imgs.get(0));
                madapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void onClick(View v) {
      switch (v.getId()){
          case R.id.btn_punish:
              userid=this.getSharedPreferences("shared_loginn_info", Context.MODE_PRIVATE).getInt("id",0);
           if (userid!=0){
               initDialog();
           } else{
               ToastUtil.show(this,"还未登录");
           }

              break;
          case R.id.backTomain:
              finish();
              break;
      }
    }

    private void initDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(deal_publish_Activity.this);
        builder.setTitle("提示"); //设置标题
        builder.setMessage("是否确定发布？"); //设置内容

        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();//关闭dialog

                uploadImage();
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


    public void uploadImage(){

        RequestParams requestParams=new RequestParams("http://192.168.191.1:8080/uploadpro/upimg");
        requestParams.setMultipart(true);

        CommodityInfo pro=new CommodityInfo(userid,
                pro_title.getText().toString(),
        Double.parseDouble(pro_price.getText().toString()),
                pro_desc.getText().toString(),
                null,new Date(),
                location.getText().toString(),
                buyway,pro_type.getSelectedItem().toString(),null,null
                );
        Gson gson=new Gson();
        String info=gson.toJson(pro);
        requestParams.addBodyParameter("file",file);
        requestParams.addBodyParameter("info",info);

        x.http().post(requestParams, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.i("ModifyPersonInfo", "onSuccess: ");
                ToastUtil.show(deal_publish_Activity.this,result);
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
    }



