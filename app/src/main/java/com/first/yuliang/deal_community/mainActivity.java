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
    Fragment newfragement;
    Fragment oldfragement;
    Fragment home;
    Fragment fujin;
    Fragment community;
    Fragment mine;

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
           home=new Fragment_home();
           fujin=new Fragment_fujin();
           community=new Fragment_community();
           mine =new Fragment_mine();

        }

        switchfragment(home);
        radiogroup = ((RadioGroup) findViewById(R.id.radioGroup));

        radiogroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                switch (checkedId){
                    case R.id.radio0:
                        newfragement=home;
                        break;
                    case R.id.radio1:
                        newfragement=fujin;
                        break;
                    case R.id.radio3:
                        newfragement=community;
                        break;
                    case R.id.radio4:
                        newfragement=mine;
                        break;

                }
                switchfragment(newfragement);

            }

        });




    }

    private void switchfragment(Fragment fragment) {
        // 设置一个默认值
        if(fragment == null){
            fragment = home;
        }

        FragmentManager fm = this.getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        if(oldfragement!=null && !oldfragement.isHidden() && oldfragement.isAdded()){
            ft.hide(oldfragement);
        }
        if(fragment.isAdded()&&fragment.isHidden()){
            ft.show(fragment);
        }else {
            if (!fragment.isAdded()){
                ft.add(R.id.fl_content,fragment);
            }
        }
        oldfragement=newfragement;
        ft.commit();
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
