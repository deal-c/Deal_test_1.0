package com.first.yuliang.deal_community.MyCenter.modify;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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

public class ModifyNameActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText et_nicheng;
    private ImageView iv_remove;
    private TextView tv_nicheng;
    private TextView tv_keep_name;
    private ImageView iv_modify_name_back;
    int userId=0;
    private Toolbar toolbar_modify_name;

    Dialog progressDialog;


    SharedPreferences preference=null;
    SharedPreferences.Editor edit=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_name);
        toolbar_modify_name = ((Toolbar) findViewById(R.id.toolbar_modify_name));
        et_nicheng = ((EditText) findViewById(R.id.et_nicheng));
        iv_remove = ((ImageView) findViewById(R.id.iv_remove));
        tv_nicheng = ((TextView) findViewById(R.id.tv_nicheng));
        tv_keep_name = ((TextView) findViewById(R.id.tv_keep_name));
        iv_modify_name_back = ((ImageView) findViewById(R.id.iv_modify_name_back));

        preference= getSharedPreferences("shared_loginn_info", Context.MODE_PRIVATE);
        edit=preference.edit();

        Intent intent=getIntent();
        String userName=intent.getStringExtra("name");
        userId=Integer.parseInt(intent.getStringExtra("userId").trim());


        et_nicheng.setText(userName);
        Log.e("modifyNameBefore","++++"+et_nicheng.getText().toString().trim());
        edit.putString("modifyNameBefore",userName);
        edit.putInt("iskeepName",0);
        edit.commit();

        iv_remove.setOnClickListener(this);


        iv_modify_name_back.setOnClickListener(this);

        tv_keep_name.setOnClickListener(this);


    }

    private void getUserNameData() {



        progressDialog = ToolsClass.createLoadingDialog(ModifyNameActivity.this, "修改中...", true,
                0);
        progressDialog.show();
        RequestParams params=new RequestParams(HttpUtile.zy+"/servlet/ModifyUserServlet");
        try {
            params.addBodyParameter("userName", URLEncoder.encode(et_nicheng.getText().toString(),"UTF-8"));

            params.addBodyParameter("userId",userId+"");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        x.http().post(params, new Callback.CommonCallback<String>() {

            @Override
            public void onSuccess(String result) {



                if(result!="") {

                    progressDialog.hide();
                    edit.putInt("iskeepName",1);
                    edit.commit();
                    Toast.makeText(ModifyNameActivity.this,"修改成功",Toast.LENGTH_SHORT).show();

                   // ModifyNameActivity.this.finish();
                }
                else
                {

                    progressDialog.hide();
                    edit.putInt("iskeepName",0);
                    edit.commit();
                    Toast.makeText(ModifyNameActivity.this,"修改失败",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

                edit.putInt("iskeepName",0);
                edit.commit();
                Toast.makeText(ModifyNameActivity.this,"无法修改",Toast.LENGTH_SHORT).show();

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
    public void onClick(View v) {
        switch(v.getId())
        {
            case R.id.iv_remove:
                removeContent();
                 break;
            case R.id.tv_keep_name:

                getUserNameData();
                break;
            case R.id.iv_modify_name_back:

                if(preference.getInt("iskeepName",0)!=0) {
                    Intent intentName = new Intent();
                    intentName.putExtra("name", et_nicheng.getText().toString().trim());
                    ModifyNameActivity.this.setResult(2, intentName);
                    ModifyNameActivity.this.finish();
                }
                else
                {


                    Intent intentName = new Intent();
                    intentName.putExtra("name", preference.getString("modifyNameBefore","").toString().trim());
                    ModifyNameActivity.this.setResult(2, intentName);
                    ModifyNameActivity.this.finish();
                }
                break;

        }
    }

    private void removeContent() {

       et_nicheng.getText().clear();

    }

    @Override
    public void onBackPressed() {

        if(preference.getInt("iskeepName",0)!=0) {
            Intent intentName = new Intent();
            intentName.putExtra("name", et_nicheng.getText().toString().trim());
            ModifyNameActivity.this.setResult(2, intentName);
            ModifyNameActivity.this.finish();
        }
        else
        {
            Intent intentName = new Intent();
            intentName.putExtra("name", preference.getString("modifyNameBefore","").toString().trim());
            ModifyNameActivity.this.setResult(2, intentName);
            ModifyNameActivity.this.finish();
        }
    }
}
