package com.first.yuliang.deal_community.frament.pojo;

import java.util.List;

/**社区表
 * Created by Administrator on 2016/10/11.
 */
public class MyComminity {
    public int status;
    public List<myDongtai> comlist;

    public static class myDongtai{

        public String comImg;
        public int communityId;
        public String communityName;
        public String communityInfo;
        public String	comCreateTime;




    }
}
