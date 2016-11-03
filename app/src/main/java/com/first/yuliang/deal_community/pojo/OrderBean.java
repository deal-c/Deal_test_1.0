package com.first.yuliang.deal_community.pojo;

import java.util.Date;

/**
 * Created by vanex on 2016/11/3.
 */

public class OrderBean {

    private int orderId;

    private int userId;

    private Date changeTime;

    private int state;

    private String addressInfo;

    private String tips;

    private double price;

    private CommodityInfo commodityInfo;

    public OrderBean() {
    }

    public OrderBean(int orderId, int userId, CommodityInfo commodityInfo, Date changeTime,
                 int state, String addressInfo, String tips, double price) {
        super();
        this.orderId = orderId;
        this.userId = userId;
        this.changeTime = changeTime;
        this.state = state;
        this.addressInfo = addressInfo;
        this.tips = tips;
        this.price = price;
        this.commodityInfo = commodityInfo;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public CommodityInfo getCommodityInfo() {
        return commodityInfo;
    }

    public void setCommodityInfo(CommodityInfo commodityInfo) {
        this.commodityInfo = commodityInfo;
    }

    public Date getChangeTime() {
        return changeTime;
    }

    public void setChangeTime(Date changeTime) {
        this.changeTime = changeTime;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getAddressInfo() {
        return addressInfo;
    }

    public void setAddressInfo(String addressInfo) {
        this.addressInfo = addressInfo;
    }

    public String getTips() {
        return tips;
    }

    public void setTips(String tips) {
        this.tips = tips;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
