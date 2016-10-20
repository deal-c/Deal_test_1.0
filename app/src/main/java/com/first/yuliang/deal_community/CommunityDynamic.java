package com.first.yuliang.deal_community;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.amap.api.maps.model.Text;
import com.first.yuliang.deal_community.frament.pojo.Dynamic;

import java.util.ArrayList;
import java.util.List;

public class CommunityDynamic extends AppCompatActivity {
ListView DymlistView;
    BaseAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community_dynamic);

        DymlistView= (ListView) findViewById(R.id.DyItem);
        String[] strs = {"1","2","3","4","5","7","8","9"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_expandable_list_item_1,strs);
        DymlistView.setAdapter(adapter);










    }
}
