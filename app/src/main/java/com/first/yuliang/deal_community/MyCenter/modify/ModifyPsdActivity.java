package com.first.yuliang.deal_community.MyCenter.modify;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.first.yuliang.deal_community.R;
import com.first.yuliang.deal_community.frament.utiles.HttpUtile;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;



public class ModifyPsdActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText et_new_psd;
    private EditText et_again_confirm;
    private Button btn_queren;

    int userId=0;
    private ImageView iv_modify_psd_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_psd);


        Intent intent=getIntent();
        userId=Integer.parseInt(intent.getStringExtra("userId"));


        initView();

        initEvent();


        initData();

    }

    private void initData() {



    }

    private void initEvent() {

        btn_queren.setOnClickListener(this);
        iv_modify_psd_back.setOnClickListener(this);
    }

    private void initView() {


        et_new_psd = ((EditText) findViewById(R.id.et_new_psd));
        et_again_confirm = ((EditText) findViewById(R.id.et_again_confirm));
        iv_modify_psd_back = ((ImageView) findViewById(R.id.iv_modify_psd_back));
        btn_queren = ((Button) findViewById(R.id.btn_queren));
    }

    @Override
    public void onClick(View v) {
        switch(v.getId())
        {
            case R.id.btn_queren:
                if(et_new_psd.getText().toString().trim().equals(et_again_confirm.getText().toString().trim())){
                    getUserPsdData(v);
                }else
                {
                    Toast.makeText(ModifyPsdActivity.this,"两次密码不匹配,请重新输入",Toast.LENGTH_SHORT).show();
                    et_again_confirm.getText().clear();
                }
                break;
            case R.id.iv_modify_psd_back:
                ModifyPsdActivity.this.finish();
                break;
        }
    }

    private void getUserPsdData(View v) {


        RequestParams params=new RequestParams(HttpUtile.zy+"/servlet/SelectUserPsdServlet");
        params.addBodyParameter("userId",userId+"");
        params.addBodyParameter("userPsd",et_new_psd.getText().toString().trim());
        x.http().post(params, new Callback.CommonCallback<String>() {


            @Override
            public void onSuccess(String result) {

                if(Boolean.parseBoolean(result.toString().trim()))
                {
                    Toast.makeText(ModifyPsdActivity.this,"密码修改成功",Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(ModifyPsdActivity.this,"密码修改失败",Toast.LENGTH_SHORT).show();
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
        });


    }
}
