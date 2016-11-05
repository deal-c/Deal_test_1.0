package com.first.yuliang.deal_community.frament;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.first.yuliang.deal_community.R;

/**
 * Created by Administrator on 2016/11/1.
 */

public class fragment_tiezi extends Fragment {

    private ListView lv_recent_tiezi;
    BaseAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_recent_tiezi, null);
        lv_recent_tiezi = ((ListView) view.findViewById(R.id.lv_recent_tiezi));

        adapter=new BaseAdapter() {
            @Override
            public int getCount() {
                return 0;
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
                return null;
            }
        };
        lv_recent_tiezi.setAdapter(adapter);
        getAllTiezi();
        return  view;
    }

    private void getAllTiezi() {


    }
}
