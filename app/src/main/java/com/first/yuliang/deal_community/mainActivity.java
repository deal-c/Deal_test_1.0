package com.first.yuliang.deal_community;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.first.yuliang.deal_community.frament.Fragment_community;
import com.first.yuliang.deal_community.frament.Fragment_fujin;
import com.first.yuliang.deal_community.frament.Fragment_home;
import com.first.yuliang.deal_community.frament.Fragment_mine;
import com.readystatesoftware.systembartint.SystemBarTintManager;

public class mainActivity extends AppCompatActivity {


    private RadioGroup radiogroup;
    private long mExitTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //设置信息栏的颜色
       if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window win = getWindow();
            WindowManager.LayoutParams winParams = win.getAttributes();
            final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
            winParams.flags |= bits;
            win.setAttributes(winParams);
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintEnabled(true);
            tintManager.setStatusBarTintResource(R.color.xinxilan);
//            tintManager.setTintColor(Color.parseColor("#009966"));

        }

        switchfragment(new Fragment_home());
        radiogroup = ((RadioGroup) findViewById(R.id.radioGroup));

        radiogroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                Fragment fragment=null;
                switch (checkedId){
                    case R.id.radio0:
                        fragment=new Fragment_home();
                        break;
                    case R.id.radio1:
                        fragment=new Fragment_fujin();
                        break;
                    case R.id.radio3:
                        fragment=new Fragment_community();
                        break;
                    case R.id.radio4:
                        fragment=new Fragment_mine();
                        break;

                }
                if (fragment==null){
                    fragment=new Fragment_community();
                };
                switchfragment(fragment);

            }

        });




    }

    private void switchfragment(Fragment fragment) {
        FragmentManager fm=  this.getFragmentManager();
        FragmentTransaction ft= fm.beginTransaction();
        ft.replace(R.id.fl_content,fragment).commit();
    }
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ((System.currentTimeMillis() - mExitTime) > 2000) {

                Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                mExitTime = System.currentTimeMillis();

            } else {
                moveTaskToBack(false);
//                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
