package com.first.yuliang.deal_community.MyCenter.modify;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.Toast;

import com.first.yuliang.deal_community.R;
import com.first.yuliang.deal_community.ToolsClass;
import com.first.yuliang.deal_community.Util.DateUtils;
import com.first.yuliang.deal_community.frament.utiles.HttpUtile;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.Calendar;
import java.util.GregorianCalendar;




public class ModifyBirthdayActivity extends AppCompatActivity {

    private DatePicker datePicker;
    private  int year;
    private  int month;
    private int day;
    String date=null;

    int userId=0;
    String birthday=null;
    private ImageView iv_modify_birthday;
    private Button btn_birthday_confirm;

    Dialog progressDialog;
    SharedPreferences preference=null;
    SharedPreferences.Editor edit=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_birthday);
        Intent intent=getIntent();
        userId=Integer.parseInt(intent.getStringExtra("userId").toString().trim());
        birthday=intent.getStringExtra("birthday");

        Log.e("birthday","++++++++"+birthday);


        preference=getSharedPreferences("shared_loginn_info", Context.MODE_PRIVATE);
        edit=preference.edit();
        edit.putInt("isConfirm",0);
        edit.putString("birthday",birthday.toString().trim());
        edit.commit();

        iv_modify_birthday = ((ImageView) findViewById(R.id.iv_modify_birthday));
        datePicker = ((DatePicker) findViewById(R.id.datePicker));
        btn_birthday_confirm = ((Button) findViewById(R.id.btn_birthday_confirm));
        Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);

        datePicker.init(year, month, day, new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int month, int day) {
                ModifyBirthdayActivity.this.year=year;
                ModifyBirthdayActivity.this.month=month;
                ModifyBirthdayActivity.this.day=day;
                Log.e("date", "onDateChanged: "+year );
                date=DateUtils.dateToString(new GregorianCalendar(year,month,day).getTime());
                Log.e("date",date);



                //ModifyBirthdayActivity.this.finish();
            }


        });

        iv_modify_birthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(preference.getInt("isConfirm",0)!=0) {
                    Intent mIntent = new Intent();
                    mIntent.putExtra("date", date);
                    ModifyBirthdayActivity.this.setResult(1,mIntent);
                }

                else
                {
                    Intent mIntent = new Intent();
                    mIntent.putExtra("date", preference.getString("birthday","").toString().trim());
                    ModifyBirthdayActivity.this.setResult(1,mIntent);
                }

                ModifyBirthdayActivity.this.finish();
            }
        });

        btn_birthday_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ModifyBirthday();
            }
        });


    }

    private void ModifyBirthday() {

        progressDialog = ToolsClass.createLoadingDialog(ModifyBirthdayActivity.this, "加载中...", true,
                0);
        progressDialog.show();
        edit.putInt("isConfirm",1);
        edit.commit();

        RequestParams params=new RequestParams(HttpUtile.zy+"servlet/ModifyBirthdayServlet");
        params.addBodyParameter("userId",userId+"");
        params.addBodyParameter("birthday",date);
        x.http().post(params, new Callback.CacheCallback<String>() {


            @Override
            public void onSuccess(String result) {



                if(Boolean.parseBoolean(result.toString().trim()))
                {
                    Toast.makeText(ModifyBirthdayActivity.this,"修改成功",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    edit.putInt("isConfirm",0);
                    edit.commit();
                    Toast.makeText(ModifyBirthdayActivity.this,"修改失败",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {


                Toast.makeText(ModifyBirthdayActivity.this,"无法访问网络",Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

                progressDialog.dismiss();
            }

            @Override
            public boolean onCache(String result) {
                return false;
            }
        });

       // params
    }

    @Override
    public void onBackPressed() {


        if(preference.getInt("isConfirm",0)!=0) {
            Intent mIntent = new Intent();
            mIntent.putExtra("date", date);
            ModifyBirthdayActivity.this.setResult(1,mIntent);
        }

        else
        {
            Intent mIntent = new Intent();
            mIntent.putExtra("date", preference.getString("birthday","").toString().trim());
            ModifyBirthdayActivity.this.setResult(1,mIntent);
        }


        ModifyBirthdayActivity.this.finish();
    }
}
