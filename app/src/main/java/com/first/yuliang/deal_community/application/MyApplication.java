package com.first.yuliang.deal_community.application;

import android.app.Application;

import com.first.yuliang.deal_community.pojo.User;

import org.xutils.BuildConfig;
import org.xutils.x;

import io.rong.imkit.RongIM;

/**
 * Created by yuliang on 2016/9/27.
 */
public class MyApplication  extends Application{
    @Override
    public void onCreate() {
        super.onCreate();
        RongIM.init(this);
        x.Ext.init(this);
        x.Ext.setDebug(BuildConfig.DEBUG); // 是否输出debug日志, 开启debug会影响性能
    }

    public User user=new User();






}
