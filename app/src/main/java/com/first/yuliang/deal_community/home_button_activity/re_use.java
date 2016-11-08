package com.first.yuliang.deal_community.home_button_activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.first.yuliang.deal_community.R;

public class re_use extends AppCompatActivity {

    private ListView zailiyong_listview;
    BaseAdapter madapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_re_use);
        zailiyong_listview = ((ListView) findViewById(R.id.zailiyong_listview));

        madapter=new BaseAdapter() {
            @Override
            public int getCount() {
                return 8;
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
                View v;
//                if (position==0){
//                   v=View.inflate(re_use.this,R.layout.zailiyong_best_item,null);
//                }else {
//                    v=View.inflate(re_use.this,R.layout.zailiyong_item,null);
//                }
                v=View.inflate(re_use.this,R.layout.zailiyong_item,null);

                return v;
            }
        };
        View v=View.inflate(re_use.this,R.layout.zailiyong_best_item,null);
       zailiyong_listview.addHeaderView(v);
        zailiyong_listview.setAdapter(madapter);

    }
}
