package com.first.yuliang.deal_community.model;

/**
 * Created by Administrator on 2016/10/27.
 */

public class User {
    public int  mId;
    public String   mName;

    public User(int mId, String mName) {
        this.mId = mId;
        this.mName = mName;
    }

    public int getmId() {
        return mId;
    }

    public void setmId(int mId) {
        this.mId = mId;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }
}
