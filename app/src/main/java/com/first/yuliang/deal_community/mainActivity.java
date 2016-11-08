package com.first.yuliang.deal_community;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.first.yuliang.deal_community.frament.Fragment_community;
import com.first.yuliang.deal_community.frament.Fragment_fujin;
import com.first.yuliang.deal_community.frament.Fragment_home;
import com.first.yuliang.deal_community.frament.Fragment_mine;
import com.first.yuliang.deal_community.publish.deal_publish_Activity;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

public class mainActivity extends AppCompatActivity implements View.OnClickListener{


    private RadioGroup radiogroup;
    private long mExitTime;
    Fragment newfragement;
    Fragment oldfragement;
    Fragment home;
    Fragment fujin;
    Fragment community;
    Fragment mine;
    private Fragment[] fragments;
    private Button dealButton;
    private Button juan;
    private Button zeng;
    private Button huan;
    private Button mai;
    private ImageView iv_black;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        int id=this.getSharedPreferences("shared_loginn_info", Context.MODE_PRIVATE).getInt("id",0);
        JPushInterface.setDebugMode(true);//如果时正式版就改成false
        JPushInterface.init(this);

        setAlias(this,id+"");



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
        home=new Fragment_home();
        fujin=new Fragment_fujin();
        community=new Fragment_community();
        mine =new Fragment_mine();
        fragments = new Fragment[]{home, fujin, community, mine};
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

        iv_black = ((ImageView) findViewById(R.id.iv_blackmain));
        dealButton = ((Button) findViewById(R.id.radio2));
        dealButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                innitPoupwindow(v);
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
        for (Fragment fragment1 : fragments) {
            if (fragment1 != fragment) {
                if (fragment1 != null && !fragment1.isHidden() && fragment1.isAdded()) {
                    ft.hide(fragment1);
                }
            }
        }

        if (fragment.isAdded() && fragment.isHidden()) {
            ft.show(fragment);
        } else {
            if (!fragment.isAdded()) {
                ft.add(R.id.fl_content, fragment);
            }
        }
        oldfragement = newfragement;
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
    private void innitPoupwindow(View view) {
        View v = LayoutInflater.from(this).inflate(R.layout.deal_button, null);
        WindowManager wm = (WindowManager)getSystemService(Context.WINDOW_SERVICE);
        juan = ((Button) v.findViewById(R.id.btn_juan));
        zeng = ((Button) v.findViewById(R.id.btn_zeng));
        huan = ((Button) v.findViewById(R.id.btn_huan));
        mai = ((Button) v.findViewById(R.id.btn_mai));
        juan.setOnClickListener(this);

        zeng.setOnClickListener(this);

        huan.setOnClickListener(this);

        mai.setOnClickListener(this);

        final PopupWindow popupWindow = new PopupWindow(v,ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT);

        //popupwiondow外面点击，popupwindow消失
        popupWindow.setFocusable(true);
        popupWindow.update();
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        v.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        int popupWidth = v.getMeasuredWidth();
        int popupHeight = v.getMeasuredHeight();
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        WindowManager.LayoutParams lp = getWindow().getAttributes();

        getWindow().setAttributes(lp);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

            @Override
            public void onDismiss() {
                iv_black.setVisibility(View.GONE);
                Animation animation = new AlphaAnimation(1,0);
                animation.setDuration(500);
                iv_black.startAnimation(animation);
            }
        });
        iv_black.setVisibility(View.VISIBLE);
        Animation animation = new AlphaAnimation(0,1);
        animation.setDuration(500);
        iv_black.startAnimation(animation);
        popupWindow.setAnimationStyle(R.style.publish);
        popupWindow.showAtLocation(view, Gravity.CENTER_HORIZONTAL,0, location[1] - popupHeight-100);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_juan:
                Intent intent=new Intent(mainActivity.this, deal_publish_Activity.class);
                intent.putExtra("key","juan");
                startActivity(intent);
                break;
            case R.id.btn_zeng:

                Intent intent1=new Intent(mainActivity.this, deal_publish_Activity.class);
                intent1.putExtra("key","zeng");
                startActivity(intent1);
                break;
            case R.id.btn_huan:
                Intent intent2=new Intent(mainActivity.this, deal_publish_Activity.class);
                intent2.putExtra("key","huan");
                startActivity(intent2);
                break;
            case R.id.btn_mai:
                Intent intent3=new Intent(mainActivity.this, deal_publish_Activity.class);
                intent3.putExtra("key","mai");
                startActivity(intent3);
                break;

        }

    }

    public static void setAlias(final Context cotext, String id){
        JPushInterface.setAliasAndTags(cotext, id, null, new TagAliasCallback() {
            @Override
            public void gotResult(int i, String s, Set<String> set) {
                if(i==0){
                    //ToastCommom.ToastShow(cotext,null,"tag注册成功");
                    Log.e("alias", "注册成功" );
                }else {
                    Log.e("alias", "注册失败"+i );
                }
            }
        });
    }


}
