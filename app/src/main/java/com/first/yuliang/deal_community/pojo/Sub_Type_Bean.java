package com.first.yuliang.deal_community.pojo;

import java.util.List;

/**
 * Created by Administrator on 2016/9/22.
 */
public class Sub_Type_Bean {

    public List<Sub_Type> sub_types;

    public static class Sub_Type
    {
        public int typeId;
        public int sub_type_Id;
        public String sub_type_name;
        public String sub_type_pic;
    }
}
