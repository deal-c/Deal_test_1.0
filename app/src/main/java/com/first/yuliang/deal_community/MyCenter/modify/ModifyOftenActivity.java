package com.first.yuliang.deal_community.MyCenter.modify;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.first.yuliang.deal_community.R;
import com.first.yuliang.deal_community.ToolsClass;
import com.first.yuliang.deal_community.frament.utiles.HttpUtile;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;


public class ModifyOftenActivity extends AppCompatActivity implements View.OnClickListener{

    private TextView tv_often_keep_name;
    private EditText et_often_live;
    private ImageView iv_remove_often_live;
    private ImageView iv_modify_often_back;
    int userId=0;

    SharedPreferences preference=null;
    SharedPreferences.Editor edit=null;
    Dialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_often);

        tv_often_keep_name = ((TextView) findViewById(R.id.tv_often_keep_name));
        et_often_live = ((EditText) findViewById(R.id.et_often_live));
        iv_remove_often_live = ((ImageView) findViewById(R.id.iv_remove_often_live));
        iv_modify_often_back = ((ImageView) findViewById(R.id.iv_modify_often_back));

        preference= getSharedPreferences("shared_loginn_info", Context.MODE_PRIVATE);
        edit=preference.edit();

        Intent intent=getIntent();
        String userAddress_s=intent.getStringExtra("userAddress_s");
        userId=Integer.parseInt(intent.getStringExtra("userId").trim());

        et_often_live.setText(userAddress_s);
        edit.putString("modifyOftenBefore",et_often_live.getText().toString());
        edit.commit();

        iv_remove_often_live.setOnClickListener(this);


        iv_modify_often_back.setOnClickListener(this);

        tv_often_keep_name.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId())
        {
            case R.id.iv_remove_often_live:
                et_often_live.getText().clear();
                break;
            case R.id.tv_often_keep_name:
                Log.e("ModifyNameActivity","++++++++++hhhhhhhhh");
                getUserOftenData();
                break;
            case R.id.iv_modify_often_back:

                if(preference.getInt("iskeepOften",0)!=0) {
                    Intent intentName = new Intent();
                    intentName.putExtra("often", et_often_live.getText().toString().trim());
                    ModifyOftenActivity.this.setResult(3, intentName);
                    ModifyOftenActivity.this.finish();
                }else
                {
                    Intent intentName = new Intent();
                    intentName.putExtra("often", preference.getString("modifyOftenBefore","").toString().trim());
                    ModifyOftenActivity.this.setResult(3, intentName);
                    ModifyOftenActivity.this.finish();
                }
                break;

        }
    }

    private void getUserOftenData() {


        progressDialog = ToolsClass.createLoadingDialog(ModifyOftenActivity.this, "修改中...", true,
                0);
        progressDialog.show();
        edit.putInt("iskeepOften",1);
        edit.commit();

        RequestParams params=new RequestParams(HttpUtile.zy+"/servlet/ModifyUserOftenServlet");
        try {
            params.addBodyParameter("userAddress_s", URLEncoder.encode(et_often_live.getText().toString(),"UTF-8"));

            params.addBodyParameter("userId",userId+"");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        x.http().post(params, new Callback.CommonCallback<String>() {

            @Override
            public void onSuccess(String result) {



                if(result!="") {



                    progressDialog.hide();
                    Toast.makeText(ModifyOftenActivity.this,"修改成功",Toast.LENGTH_SHORT).show();

                    // ModifyNameActivity.this.finish();
                }
                else
                {
                    progressDialog.hide();
                    edit.putInt("iskeepOften",0);
                    edit.commit();
                    Toast.makeText(ModifyOftenActivity.this,"修改失败",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

                Toast.makeText(ModifyOftenActivity.this,"无法修改",Toast.LENGTH_SHORT).show();

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

    @Override
    public void onBackPressed() {
        if(preference.getInt("iskeepOften",0)!=0) {
            Intent intentName = new Intent();
            intentName.putExtra("often", et_often_live.getText().toString().trim());
            ModifyOftenActivity.this.setResult(3, intentName);
            ModifyOftenActivity.this.finish();
        }else
        {
            Intent intentName = new Intent();
            intentName.putExtra("often", preference.getString("modifyOftenBefore","").toString().trim());
            ModifyOftenActivity.this.setResult(3, intentName);
            ModifyOftenActivity.this.finish();
        }
    }
}
