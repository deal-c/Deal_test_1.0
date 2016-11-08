package com.first.yuliang.deal_community.MyCenter;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.RadioGroup;

import com.first.yuliang.deal_community.R;
import com.first.yuliang.deal_community.frament.fragment_product;
import com.first.yuliang.deal_community.frament.fragment_tiezi;




public class MyRecentActivity extends AppCompatActivity {

    private RadioGroup rg_recent;



    private Fragment[] fragments;
    Fragment product;

    Fragment tiezi;
    Fragment newFragment;
    Fragment oldFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_recent);

        rg_recent = ((RadioGroup) findViewById(R.id.rg_recent));
        product=new fragment_product();
        tiezi=new fragment_tiezi();
        fragments = new Fragment[]{product,tiezi};

        switchfragment(product);
        rg_recent.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId)
                {
                    case R.id.rb_product:
                        newFragment=product;
                        break;
                    case R.id.rb_tiezi:
                        newFragment=tiezi;
                        break;
                }
                switchfragment(newFragment);
            }
        });

    }

    private void switchfragment(Fragment fragment) {

        if(fragment == null){
            fragment = product;
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
                ft.add(R.id.fl_recent, fragment);
            }
        }
        oldFragment= newFragment;
        ft.commit();

    }
}
