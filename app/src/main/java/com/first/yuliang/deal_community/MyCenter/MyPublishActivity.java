package com.first.yuliang.deal_community.MyCenter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.first.yuliang.deal_community.BuySuccessActivity;
import com.first.yuliang.deal_community.Order;
import com.first.yuliang.deal_community.R;
import com.first.yuliang.deal_community.Util.StateUtil;
import com.first.yuliang.deal_community.frament.utiles.HttpUtile;
import com.first.yuliang.deal_community.pojo.CommodityBean;
import com.first.yuliang.deal_community.pojo.CommodityInfoUser;
import com.first.yuliang.deal_community.pojo.OrderBean;
import com.first.yuliang.deal_community.pojo.User;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;


public class MyPublishActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_publish);
    }
}
