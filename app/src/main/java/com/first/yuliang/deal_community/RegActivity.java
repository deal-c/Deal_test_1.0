package com.first.yuliang.deal_community;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.first.yuliang.deal_community.Util.MD5Utils;
import com.first.yuliang.deal_community.pojo.UserBean;
import com.google.gson.Gson;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;



public class RegActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btn_register;
    private Button btn_login;
    private Button btn_reg;
    private EditText et_username;
    private EditText et_psd;
    private CheckBox cb_remeberuser;
    private List<UserBean.User> users=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg);


        et_username = ((EditText) findViewById(R.id.et_username));
        et_psd = ((EditText) findViewById(R.id.et_psd));
        cb_remeberuser = ((CheckBox) findViewById(R.id.cb_remeberuser));
        btn_login = ((Button) findViewById(R.id.btn_login));

        btn_reg = ((Button) findViewById(R.id.btn_reg));

        btn_login.setOnClickListener(this);
        btn_reg.setOnClickListener(this);

        SharedPreferences preference=getSharedPreferences("shared_loginn_info", Context.MODE_PRIVATE);
        String userName = preference.getString("userName","");
        boolean remeberuser = preference.getBoolean("cb_remeberuser",false);
        et_username.setText(userName);
        cb_remeberuser.setChecked(remeberuser);
    }

    @Override
    public void onClick(View v) {

        switch(v.getId())
        {

            case R.id.btn_login:
                Login(v);
                break;
            case R.id.btn_reg:
                register(v);
                break;
        }
    }

    private void register(View v) {
        Intent intent=new Intent(RegActivity.this,regDetailActivity.class);
        startActivity(intent);
    }

    private void Login(View v) {

        RequestParams params=new RequestParams("http://10.40.5.21:8080/FourProject/servlet/loginApp");
//        params.addBodyParameter("username",et_username.getText().toString().trim());
//        params.addBodyParameter("psd",et_psd.getText().toString().trim());
        x.http().post(params, new Callback.CommonCallback<String>() {


            @Override
            public void onSuccess(String result) {
                Gson gson=new Gson();
                UserBean ub= gson.fromJson(result,UserBean.class);
                users.addAll(ub.userList);

                SharedPreferences preference=getSharedPreferences("shared_loginn_info", Context.MODE_PRIVATE);
                SharedPreferences.Editor edit=preference.edit();
                for (int i=0;i<users.size();i++)
                {
                    if(users.get(i).userName.equals(et_username.getText().toString().trim())&& users.get(i).userPsd.equals(MD5Utils.MD5(et_psd.getText().toString().trim())))
                    {
                        if(cb_remeberuser.isChecked())
                        {
                            edit.putString("userName", et_username.getText().toString().trim());
                            edit.putBoolean("cb_remeberuser",true);
                        }
                        else
                        {
                            edit.putString("userName", "");
                            edit.putBoolean("cb_remeberuser",false);
                        }
                        edit.commit();
                        Toast.makeText(RegActivity.this,"登录成功",Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent(RegActivity.this, mainActivity.class);
                        startActivity(intent);
                    }

                }

                Toast.makeText(RegActivity.this,"登录失败",Toast.LENGTH_SHORT).show();
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
