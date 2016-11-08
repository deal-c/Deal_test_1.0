package com.first.yuliang.deal_community.MyCenter.modify;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;

import com.first.yuliang.deal_community.R;
import com.first.yuliang.deal_community.frament.utiles.HttpUtils;

import org.xutils.http.RequestParams;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;



public class ModifyBirthdayActivity extends AppCompatActivity {

    private DatePicker datePicker;
    private  int year;
    private  int month;
    private int day;
    String date=null;
    private Button btn_modify_birthday_confirm;
    int userId=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_birthday);
        Intent intent=getIntent();
        userId=Integer.parseInt(intent.getStringExtra("userId").toString().trim());

        btn_modify_birthday_confirm = ((Button) findViewById(R.id.btn_modify_birthday_confirm));

        datePicker = ((DatePicker) findViewById(R.id.datePicker));
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

                SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
                date=sdf.format(new Date(year,month,day));
                Intent mIntent=new Intent();
                mIntent.putExtra("date",date);



                ModifyBirthdayActivity.this.setResult(1,mIntent);
                //ModifyBirthdayActivity.this.finish();
            }


        });

        btn_modify_birthday_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //ModifyBirthday();
                Log.e("Birthday","+++++"+year+"+++++"+month+"++++++"+day);
                Log.e("Birthday","******"+date);

                ModifyBirthdayActivity.this.finish();
            }
        });

    }

    private void ModifyBirthday() {
        RequestParams params=new RequestParams(HttpUtils.host+"servlet/ModifyBirthdayServlet");
        params.addBodyParameter("userId",userId+"");
       // params
    }
}
