package com.first.yuliang.deal_community;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

public class ShequActivity extends AppCompatActivity {

    private ListView lv_shequ_list;
    BaseAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shequ);

        lv_shequ_list = ((ListView) findViewById(R.id.lv_shequ_list));

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

        getAllShequResponses();
        lv_shequ_list.setAdapter(adapter);
    }

    private void getAllShequResponses() {


    }


}
