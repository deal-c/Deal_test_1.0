package com.first.yuliang.deal_community;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;

import com.first.yuliang.deal_community.Util.DateUtils;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class regBirthdayActivity extends AppCompatActivity {


    private DatePicker datePicker;
    private  int year;
    private  int month;
    private int day;
    private Button btn_reg_birthday_confirm;
    String date=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg_birthday);


        btn_reg_birthday_confirm = ((Button) findViewById(R.id.btn_reg_birthday_confirm));
        datePicker = ((DatePicker) findViewById(R.id.reg_datePicker));
        Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);

        datePicker.init(year, month, day, new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int month, int day) {
                regBirthdayActivity.this.year=year;
                regBirthdayActivity.this.month=month;
                regBirthdayActivity.this.day=day;
                Log.e("date", "onDateChanged: "+year );
                date= DateUtils.dateToString(new GregorianCalendar(year,month,day).getTime());

                Log.e("date",date);

                //ModifyBirthdayActivity.this.finish();
            }


        });
        btn_reg_birthday_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent mIntent=new Intent();
                mIntent.putExtra("date",date);



                regBirthdayActivity.this.setResult(5,mIntent);
                regBirthdayActivity.this.finish();
            }
        });
    }
}
