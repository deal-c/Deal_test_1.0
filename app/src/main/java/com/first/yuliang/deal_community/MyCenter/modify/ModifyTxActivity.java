package com.first.yuliang.deal_community.MyCenter.modify;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.first.yuliang.deal_community.R;
import com.first.yuliang.deal_community.frament.utiles.HttpUtile;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;


public class ModifyTxActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView iv_tx_camora;
    private File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)+"/"+
            getPhotoFileName());
    private static final int PHOTO_REQUEST = 1;
    private static final int CAMERA_REQUEST = 2;
    private static final int PHOTO_CLIP = 3;
    int userId=0;
    String userImg=null;
    private ImageView iv_modify_tx_back;
    private TextView tv_keep_tx;
    SharedPreferences preference=null;
    SharedPreferences.Editor edit=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_tx);

        preference= getSharedPreferences("shared_loginn_info", Context.MODE_PRIVATE);
        edit=preference.edit();
        Intent intent=getIntent();



        userId=Integer.parseInt(intent.getStringExtra("userId"));
        userImg=intent.getStringExtra("userImg");

        edit.putString("userImgBefore",userImg);
        edit.commit();

        iv_tx_camora = ((ImageView) findViewById(R.id.iv_tx_camora));
        x.image().bind(iv_tx_camora,HttpUtile.zy1+userImg);

        iv_modify_tx_back = ((ImageView) findViewById(R.id.iv_modify_tx_back));
        tv_keep_tx = ((TextView) findViewById(R.id.tv_keep_tx));
        iv_tx_camora.setOnClickListener(this);
        iv_modify_tx_back.setOnClickListener(this);
        tv_keep_tx.setOnClickListener(this);
    }

    private void showSelect(View v) {
        View contentView= LayoutInflater.from(this).inflate(R.layout.activity_select_item,null);
        Button btn_camera=((Button) contentView.findViewById(R.id.btn_camera));
        Button btn_choose=((Button) contentView.findViewById(R.id.btn_choose));

        btn_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePhoto();
            }
        });
        btn_choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPicFromPhoto();
            }
        });

        final PopupWindow popupWindow = new PopupWindow(contentView,
                ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT, true);

        popupWindow.setTouchable(true);

        popupWindow.setTouchInterceptor(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                return false;

            }
        });
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());


        popupWindow.showAtLocation(v, Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL,0, 0);

    }

    private void takePhoto() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // 下面这句指定调用相机拍照后的照片存储的路径
        // System.out.println("getPicFromCamera==========="+file.getAbsolutePath());
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
        startActivityForResult(intent, CAMERA_REQUEST);

    }

    private void getPicFromPhoto() {
        Intent intent = new Intent(Intent.ACTION_PICK, null);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                "image/*");
        startActivityForResult(intent, PHOTO_REQUEST);
    }


    private String getPhotoFileName() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");

        return sdf.format(date)+"_"+ UUID.randomUUID() + ".png";
    }


    private void photoClip(Uri uri) {

        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // 下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 90);
        intent.putExtra("outputY", 90);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, PHOTO_CLIP);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId())
        {
            case R.id.iv_tx_camora:
                showSelect(v);
                break;
            case R.id.iv_modify_tx_back:

                if(preference.getInt("iskeepTx",0)==0) {
                    Intent intent=new Intent();
                    intent.putExtra("data",preference.getString("userImgBefore",""));
                    ModifyTxActivity.this.setResult(4,intent);
                    ModifyTxActivity.this.finish();
                }
                else
                {
                    Intent intent=new Intent();
                    intent.putExtra("data",preference.getString("userImgAfter",""));
                    ModifyTxActivity.this.setResult(4,intent);
                    ModifyTxActivity.this.finish();
                }
                break;
            case R.id.tv_keep_tx:
                updateTx();
                break;
        }
    }

    private void updateTx() {

        edit.putInt("iskeepTx",1);
        edit.commit();

        RequestParams params=new RequestParams(HttpUtile.zy1+"/Project/servlet/updateTxServlet");
        params.setMultipart(true);
        params.addBodyParameter("file",file);

        params.addBodyParameter("userId",userId+"");

        x.http().post(params, new Callback.CacheCallback<String>() {


            @Override
            public void onSuccess(String result) {

                if(result!="")
                {
                    Toast.makeText(ModifyTxActivity.this,"修改成功",Toast.LENGTH_SHORT).show();
                    edit.putString("userImgAfter",result);
                    edit.commit();
                    Intent intent=new Intent();
                    intent.putExtra("data",result);
                    ModifyTxActivity.this.setResult(4,intent);

                    //ModifyTxActivity.this.finish();
                }
                else
                {

                    edit.putInt("iskeepTx",0);
                    edit.commit();
                    Toast.makeText(ModifyTxActivity.this,"修改失败",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

                Toast.makeText(ModifyTxActivity.this,"onError",Toast.LENGTH_SHORT).show();
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        switch (requestCode) {
            case CAMERA_REQUEST:
                switch (resultCode) {
                    case -1://-1表示拍照成功  固定
                        System.out.println("CAMERA_REQUEST"+file.getAbsolutePath());
                        if (file.exists()) {
                            photoClip(Uri.fromFile(file));
                        }
                        break;
                    default:
                        break;
                }
                break;
            case PHOTO_REQUEST:
                if (data != null) {
                    photoClip(data.getData());

                }
                break;
            case PHOTO_CLIP:
                if (data != null) {
                    Bundle extras = data.getExtras();
                    if (extras != null) {
                        Log.w("test", "data");
                        Bitmap photo = extras.getParcelable("data");

                        saveImageToGallery(getApplication(),photo);//保存bitmap到本地
                        iv_tx_camora.setImageBitmap(photo);


                    }
                }
                break;

            default:
                break;
        }

    }

    private void saveImageToGallery(Context context, Bitmap bmp) {


        try {
            FileOutputStream fos = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 其次把文件插入到系统图库
        try {
            MediaStore.Images.Media.insertImage(context.getContentResolver(),
                    file.getAbsolutePath(), file.getName(), null);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        // 最后通知图库更新
        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + file.getAbsolutePath())));
    }

    @Override
    public void onBackPressed() {

        if(preference.getInt("iskeepTx",0)==0) {
            Intent intent=new Intent();
            intent.putExtra("data",preference.getString("userImgBefore",""));
            ModifyTxActivity.this.setResult(4,intent);
          ModifyTxActivity.this.finish();
        }
        else
        {
            Intent intent=new Intent();
            intent.putExtra("data",preference.getString("userImgAfter",""));
            ModifyTxActivity.this.setResult(4,intent);
            ModifyTxActivity.this.finish();
        }
    }
}
