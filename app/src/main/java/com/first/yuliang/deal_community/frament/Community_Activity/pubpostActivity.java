package com.first.yuliang.deal_community.frament.Community_Activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.first.yuliang.deal_community.R;
import com.first.yuliang.deal_community.Util.NoScrollGridView;
import com.first.yuliang.deal_community.frament.utiles.ToastUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import me.nereo.multi_image_selector.MultiImageSelector;

public class pubpostActivity extends AppCompatActivity implements View.OnClickListener{

    private TextView back;
    private TextView title;
    private TextView desc;
    private Button pub_post;
    private NoScrollGridView gv_photos;
    private static final int REQUEST_IMAGE = 2;

    private ArrayList<String> imgs = new ArrayList<String>();
    Dialog progressDialog;
    private BaseAdapter madapter;
    private List<File> files = new ArrayList<File>();
    private File file;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pubpost);
        back.setOnClickListener(this);
        initview();
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
                    BitmapFactory.Options options = new BitmapFactory.Options();

                    options.inJustDecodeBounds = false;

                    options.inSampleSize = 10;   // width，hight设为原来的十分一


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
        gv_photos.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if (position != imgs.size()) {
                    initDialog(position);
                }

                return true;
            }
        });
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
        title = ((TextView) findViewById(R.id.pub_post_title));
        desc = ((TextView) findViewById(R.id.pub_post_desc));
        pub_post = ((Button) findViewById(R.id.btn_punish_post));
        gv_photos = ((NoScrollGridView) findViewById(R.id.gv_photos_post));

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
        });}

    private void pickImage() {
        MultiImageSelector selector = MultiImageSelector.create(pubpostActivity.this);
        selector.showCamera(true);
        selector.count(1);
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
        }
    }
}
