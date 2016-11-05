package com.first.yuliang.deal_community.pojo;

import java.util.Date;

/**
 * Created by vanex on 2016/11/3.
 */

public class CommodityInfo {

    private Integer commodityId;
    private User user;
    private String commodityTitle;
    private Double price;
    private String commodityDescribe;
    private String commodityImg;
    private Date releaseTime;
    private String location;
    private Integer buyWay;
    private String commodityClass;
    private Integer buyUserId;
    private Integer statement;

    public CommodityInfo() {
    }

    public CommodityInfo(User user, String commodityTitle,
                             Double price, String commodityDescribe, String commodityImg,
                             Date releaseTime, String location, Integer buyWay,
                             String commodityClass, Integer buyUserId, Integer statement) {
        super();
        this.user = user;
        this.commodityTitle = commodityTitle;
        this.price = price;
        this.commodityDescribe = commodityDescribe;
        this.commodityImg = commodityImg;
        this.releaseTime = releaseTime;
        this.location = location;
        this.buyWay = buyWay;
        this.commodityClass = commodityClass;
        this.buyUserId = buyUserId;
        this.statement = statement;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getCommodityId() {
        return commodityId;
    }

    public void setCommodityId(Integer commodityId) {
        this.commodityId = commodityId;
    }

    public String getCommodityTitle() {
        return commodityTitle;
    }

    public void setCommodityTitle(String commodityTitle) {
        this.commodityTitle = commodityTitle;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getCommodityDescribe() {
        return commodityDescribe;
    }

    public void setCommodityDescribe(String commodityDescribe) {
        this.commodityDescribe = commodityDescribe;
    }

    public String getCommodityImg() {
        return commodityImg;
    }

    public void setCommodityImg(String commodityImg) {
        this.commodityImg = commodityImg;
    }

    public Date getReleaseTime() {
        return releaseTime;
    }

    public void setReleaseTime(Date releaseTime) {
        this.releaseTime = releaseTime;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getBuyWay() {
        return buyWay;
    }

    public void setBuyWay(Integer buyWay) {
        this.buyWay = buyWay;
    }

    public String getCommodityClass() {
        return commodityClass;
    }

    public void setCommodityClass(String commodityClass) {
        this.commodityClass = commodityClass;
    }

    public int getBuyUserId() {
        return buyUserId;
    }

    public void setBuyUserId(Integer buyUserId) {
        this.buyUserId = buyUserId;
    }

    public int getStatement() {
        return statement;
    }

    public void setStatement(Integer statement) {
        this.statement = statement;
    }
}


