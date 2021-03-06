package com.first.yuliang.deal_community.frament;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioGroup;

import com.first.yuliang.deal_community.R;
import com.first.yuliang.deal_community.frament.Community_Activity.Community_search;
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
    private Fragment[] fragments;
    Fragment dongtai;
    Fragment guanzhu;
    Fragment shoucang;
    private ImageView iv_search_community;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.activity_community,null);

        dongtai=new Frag_community_dongtai();
        guanzhu=new Frag_community_guanzhu();
        shoucang=new Frag_community_shoucang();
        fragments = new Fragment[]{dongtai, guanzhu,shoucang};

        iv_search_community = ((ImageView) view.findViewById(R.id.iv_search_community));
        iv_search_community.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),Community_search.class);
                startActivity(intent);
            }
        });
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
                ft.add(R.id.fl_community_blank, fragment);
            }
        }
        oldfragment = newfragment;
        ft.commit();
    }


}
