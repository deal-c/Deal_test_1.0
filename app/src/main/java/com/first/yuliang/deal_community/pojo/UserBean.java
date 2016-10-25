package com.first.yuliang.deal_community.pojo;

import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2016/9/28.
 */
public class UserBean {
    public List<User> userList;

    public static class User{

        public int userId;
        public String userName;
        public String userPsd;
        public String userImg;
        public boolean userSex;
        public Date birthday;
        public String userAddress_s;
        public String userAddress_c;
        public int LabelId;

        public String getUserImg() {
            return userImg;
        }

        public void setUserImg(String userImg) {
            this.userImg = userImg;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }
    }
}
