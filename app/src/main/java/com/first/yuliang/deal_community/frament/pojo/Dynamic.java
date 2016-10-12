package com.first.yuliang.deal_community.frament.pojo;

import java.util.List;

/**
 * Created by Administrator on 2016/10/11.
 */
public class Dynamic {
    public int status;
    public List<myDongtai> dynamiclist;

    public static class myDongtai{

        public String dynamicId;
        public String title;
        public String content;
        public String pic;
        public String publishTime;
        public int userId;




    }
}
