package com.first.yuliang.deal_community.pojo;

import java.util.List;

/**
 * Created by vanex on 2016/11/1.
 */

public class CommodityCollection {

    public Integer status;
    public List<CommodityCollect> cc;
    public class CommodityCollect{

        public Integer commodityCollectionId;

        public Integer userId;

        public Integer commodityId;

        public String collectionTime;
    }
}
