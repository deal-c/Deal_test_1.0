package com.first.yuliang.deal_community.home_button_activity.juan_fragment;

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
 * Created by yuliang on 2016/11/2.
 */

public class juandongtai_fragment extends Fragment {
    private ListView juan_listview;
    private BaseAdapter baseAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.jun_dongtai_fragment, null);
        juan_listview = ((ListView) view.findViewById(R.id.juan_dongtai_listview));

         baseAdapter=new BaseAdapter() {
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
                 View v=View.inflate(getActivity(),R.layout.jun_lv_item,null);

                 return v;
             }
         };
        juan_listview.setAdapter(baseAdapter);


        return view;

    }
}
