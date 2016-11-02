package com.first.yuliang.deal_community.home_button_activity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.RadioGroup;

import com.first.yuliang.deal_community.R;
import com.first.yuliang.deal_community.home_button_activity.juan_fragment.juandongtai_fragment;
import com.first.yuliang.deal_community.home_button_activity.juan_fragment.jun_shengming_fagment;

public class juan_dongtai extends AppCompatActivity {

    private RadioGroup choose;
    Fragment newfragement;
    Fragment oldfragement;
    Fragment  dongtai;
    Fragment  shengming;
    private Fragment[] fragments;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_juan_dongtai);

        dongtai=new juandongtai_fragment();
        shengming=new jun_shengming_fagment();
        fragments = new Fragment[]{dongtai,shengming};

        switchfragment(dongtai);
        choose = ((RadioGroup) findViewById(R.id.rg_twobutton));

        choose.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.juan_xinxi:
                        newfragement=dongtai;
                        break;
                    case R.id.juan_shengming:
                        newfragement=shengming;
                        break;

                }
                switchfragment(newfragement);
            }
        });





    }
    private void switchfragment(Fragment fragment) {
        // 设置一个默认值
        if(fragment == null){
            fragment = dongtai;
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
                ft.add(R.id.fl_juan, fragment);
            }
        }
        oldfragement = newfragement;
        ft.commit();

}

}
