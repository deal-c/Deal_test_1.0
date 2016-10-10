package com.first.yuliang.deal_community;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

public class regDetailActivity extends AppCompatActivity {

    private Button btn_commit;
    private EditText et_reguser;
    private EditText et_regpsd;
    private RadioGroup rg_sex;
    private RadioButton rb_female;
    private RadioButton btn_male;
    private EditText et_oftenplace;
    private EditText et_recieveplace;
    private EditText et_birthday;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg_detail);

        btn_commit = ((Button) findViewById(R.id.btn_commit));
        et_reguser = ((EditText) findViewById(R.id.et_reguser));
        et_regpsd = ((EditText) findViewById(R.id.et_regPsd));

        rg_sex = ((RadioGroup) findViewById(R.id.rg_sex));
        rb_female = ((RadioButton) findViewById(R.id.rb_female));
        btn_male = ((RadioButton) findViewById(R.id.rb_male));
        et_oftenplace = ((EditText) findViewById(R.id.et_oftenplace));
        et_recieveplace = ((EditText) findViewById(R.id.et_receiveplace));
        et_birthday = ((EditText) findViewById(R.id.et_birthday));


        btn_commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RequestParams params=new RequestParams("http://10.40.5.21:8080/FourProject/servlet/RegAppServlet");
                params.addBodyParameter("regusername",et_reguser.getText().toString().trim());
                params.addBodyParameter("reguserpsd",et_regpsd.getText().toString().trim());
                params.addBodyParameter("userSex",rb_female.isChecked()?"false":"true");
                params.addBodyParameter("birthday",et_birthday.getText().toString().trim());
                params.addBodyParameter("userAddress_s",et_oftenplace.getText().toString().trim());
                params.addBodyParameter("userAddress_c",et_recieveplace.getText().toString().trim());
                x.http().post(params, new Callback.CommonCallback<String>() {
                    @Override
                    public void onSuccess(String result) {


                        Toast.makeText(regDetailActivity.this,result,Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent(regDetailActivity.this, mainActivity.class);
                        startActivity(intent);
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
        });


    }
}
