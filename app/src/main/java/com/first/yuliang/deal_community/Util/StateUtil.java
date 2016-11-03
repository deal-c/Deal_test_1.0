package com.first.yuliang.deal_community.Util;

/**
 * Created by vanex on 2016/11/3.
 */

public class StateUtil {

    public static String getBuyState(int state){
        String sta = "";
        if (state==0){
            sta="交易关闭";
        }
        if (state==1){
            sta="待付款";
        }
        if (state==2){
            sta="待发货";
        }
        if (state==3){
            sta="已发货";
        }
        if (state==4){
            sta="确认收货";
        }
        if (state==5){
            sta="未评价";
        }
        if (state==6){
            sta="交易成功";
        }
        return sta;
    }
    public static String getSellState(int state){
        String sta = "";
        if (state==0){
            sta="交易关闭";
        }
        if (state==1){
            sta="未付款";
        }
        if (state==2){
            sta="已付款";
        }
        if (state==3){
            sta="待收货";
        }
        if (state==4){
            sta="待确认";
        }
        if (state==5){
            sta="待评价";
        }
        if (state==6){
            sta="交易成功";
        }
        return sta;
    }
}
