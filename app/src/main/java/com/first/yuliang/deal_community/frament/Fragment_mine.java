package com.first.yuliang.deal_community.frament;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.first.yuliang.deal_community.R;

/**
 * Created by yuliang on 2016/9/21.
 */
public class Fragment_mine extends Fragment{
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.activity_mine,null);
        return view;
    }
}
