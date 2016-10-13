package com.first.yuliang.deal_community.frament;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import com.first.yuliang.deal_community.R;
import com.first.yuliang.deal_community.frament.fragment_community.Frag_community_dongtai;
import com.first.yuliang.deal_community.frament.fragment_community.Frag_community_guanzhu;
import com.first.yuliang.deal_community.frament.fragment_community.Frag_community_shoucang;

/**
 * Created by yuliang on 2016/9/21.
 */
public class Fragment_community  extends Fragment{

    private RadioGroup rg_community_tab;
    Fragment newfragment;
    Fragment oldfragment;

    Fragment dongtai;
    Fragment guanzhu;
    Fragment shoucang;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.activity_community,null);

        dongtai=new Frag_community_dongtai();
        guanzhu=new Frag_community_guanzhu();
        shoucang=new Frag_community_shoucang();



        switchCommunityFragment(dongtai);
        rg_community_tab = ((RadioGroup)view.findViewById(R.id.rg_community_tab));
        rg_community_tab.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                switch (checkedId){
                    case R.id.rb_community_dongtai:
                        newfragment=dongtai;
                        break;
                    case R.id.rb_community_guanzhu:
                        newfragment=guanzhu;
                        break;
                    case R.id.rb_community_shoucang:
                        newfragment=shoucang;
                        break;
                }
                switchCommunityFragment(newfragment);


            }
        });

        return view;
    }

    private void switchCommunityFragment(Fragment fragment) {
        // 设置一个默认值
        if(fragment == null){
            fragment = dongtai;
        }

        FragmentManager fm = this.getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        if(oldfragment!=null && !oldfragment.isHidden() && oldfragment.isAdded()){
            ft.hide(oldfragment);
        }
        if(fragment.isAdded()&&fragment.isHidden()){
            ft.show(fragment);
        }else {
            if (!fragment.isAdded()){
                ft.add(R.id.fl_community_blank,fragment);
            }
        }
        oldfragment=newfragment;
        ft.commit();
    }


}
