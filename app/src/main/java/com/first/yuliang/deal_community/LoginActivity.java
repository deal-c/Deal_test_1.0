package com.first.yuliang.deal_community;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    private EditText et_loginname;
    private EditText et_loginpsd;
    private CheckBox cb_remeber;
    private Button btn_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        System.out.print("sun测试");
        et_loginname = ((EditText) findViewById(R.id.et_loginname));
        et_loginpsd = ((EditText) findViewById(R.id.et_loginpsd));
        cb_remeber = ((CheckBox) findViewById(R.id.cb_remeber));
        btn_login = ((Button) findViewById(R.id.btn_login));

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkLogin(v);
            }
        });

        SharedPreferences preference=getSharedPreferences("shared_login_info", Context.MODE_PRIVATE);
        String loginName = preference.getString("name","");
        boolean remeberName = preference.getBoolean("cb_remeber",false);
        et_loginname.setText(loginName);
        cb_remeber.setChecked(remeberName);



    }

    private void checkLogin(View v) {

        SharedPreferences preference=getSharedPreferences("shared_login_info", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit=preference.edit();
        if("jack".equals(et_loginname.getText().toString().trim())&&"123".equals(et_loginpsd.getText().toString().trim()))
        {

            if(cb_remeber.isChecked()) {

                edit.putString("name", et_loginname.getText().toString().trim());
                edit.putBoolean("cb_remeber",true);
            }
            else
            {
                edit.putString("name", "");
                edit.putBoolean("cb_remeber",false);
            }
            edit.commit();
            Toast.makeText(this,"登陆成功",Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(this,"登陆失败",Toast.LENGTH_SHORT).show();
        }
    }
}
