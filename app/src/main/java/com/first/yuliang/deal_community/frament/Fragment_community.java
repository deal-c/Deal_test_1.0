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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.activity_community,null);

        switchCommunityFragment(new Frag_community_dongtai());
        rg_community_tab = ((RadioGroup)view.findViewById(R.id.rg_community_tab));
        rg_community_tab.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                Fragment fragment=null;
                switch (checkedId){
                    case R.id.rb_community_dongtai:
                        fragment=new Frag_community_dongtai();
                        break;
                    case R.id.rb_community_guanzhu:
                        fragment=new Frag_community_guanzhu();
                        break;
                    case R.id.rb_community_shoucang:
                        fragment=new Frag_community_shoucang();
                        break;
                }
                switchCommunityFragment(fragment);

//                FragmentManager fm= getActivity().getFragmentManager();
//                FragmentTransaction ft= fm.beginTransaction();
//                ft.replace(R.id.fl_community_blank,fragment).commit();

            }
        });

        return view;
    }

    private void switchCommunityFragment(Fragment fragment) {
        FragmentManager fm= getActivity().getFragmentManager();
        FragmentTransaction ft= fm.beginTransaction();
        ft.replace(R.id.fl_community_blank,fragment).commit();
    }


}
