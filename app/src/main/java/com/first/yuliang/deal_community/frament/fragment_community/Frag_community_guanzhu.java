package com.first.yuliang.deal_community.frament.fragment_community;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;

import com.first.yuliang.deal_community.R;

/**
 * Created by yuliang on 2016/9/22.
 */
public class Frag_community_guanzhu extends Fragment{
    private ListView lv_community_guanzhu;
    private Button btn_del_guanzhu;
    private CheckBox cb_guanzhu;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.frag_community_guanzhu,null);
        lv_community_guanzhu = ((ListView) view.findViewById(R.id.lv_community_guanzhu));

        lv_community_guanzhu.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return 10;
            }

            @Override
            public Object getItem(int position) {
                return null;
            }

            @Override
            public long getItemId(int position) {
                return 0;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view =View.inflate(getActivity(),R.layout.item_guanzhu,null);
                return view;
            }
        });

        return  view;

    }
}
