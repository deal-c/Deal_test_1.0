package com.first.yuliang.deal_community.pojo;

import java.util.List;

/**
 * Created by vanex on 2016/10/13.
 */
public class CommodityBean {
    public List<Commodity> commodities;

    public static class Commodity{
        public Integer commodityId;
        public Integer releaseUserId;
        public String commodityTitle;
        public Double price;
        public String commodityDescribe;
        public String commodityImg;
        public String releaseTime;
        public String location;
        public Integer buyWay;
        public String commodityClass;
        public Integer buyUserId;
        public Integer statement;
        public Integer dianzan;
    }
}
