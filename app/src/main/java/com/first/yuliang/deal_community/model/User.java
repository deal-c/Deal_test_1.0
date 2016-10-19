package com.first.yuliang.deal_community.model;

/**
 * 用户
 */
public class User {

    public long mId; // id
    public String mName; // 用户名

    @Override
    public String toString() {
        return "User{" +
                "mId=" + mId +
                ", mName='" + mName + '\'' +
                '}';
    }

    public User(long mId, String mName) {
        this.mId = mId;
        this.mName = mName;
    }
}
