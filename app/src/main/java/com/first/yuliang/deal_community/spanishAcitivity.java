package com.first.yuliang.deal_community;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

public class spanishAcitivity extends AppCompatActivity {
    boolean isFirstIn = false;
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_spanish_acitivity);
        SharedPreferences preferences = getSharedPreferences("first_pref",
                MODE_PRIVATE);
        isFirstIn = preferences.getBoolean("isFirstIn", true);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isFirstIn) {
                    // start guideactivity1
                    intent = new Intent(spanishAcitivity.this, ViewPagerActivity.class);
                } else {
                    // start TVDirectActivity
                    intent = new Intent(spanishAcitivity.this, mainActivity.class);
                }
                spanishAcitivity.this.startActivity(intent);
                spanishAcitivity.this.finish();
            }
        },1000);
    }

}
