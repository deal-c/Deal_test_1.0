package com.first.yuliang.deal_community.frament.pojo;


import android.os.Parcel;
import android.os.Parcelable;

import com.first.yuliang.deal_community.pojo.UserBean;

import java.io.Serializable;

public class Dynamic implements Serializable {

    /**
     *
     */

    public int status;



    String dynamicId;
    String title;
    String content;
    String pic;
    String publishTime;
    UserBean.User userId;
    public int getStatus() {
        return status;
    }
    public void setStatus(int status) {
        this.status = status;
    }
    public String getDynamicId() {
        return dynamicId;
    }
    public void setDynamicId(String dynamicId) {
        this.dynamicId = dynamicId;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public String getPic() {
        return pic;
    }
    public void setPic(String pic) {
        this.pic = pic;
    }
    public String getPublishTime() {
        return publishTime;
    }
    public void setPublishTime(String publishTime) {
        this.publishTime = publishTime;
    }
    public UserBean.User getUserId() {
        return userId;
    }
    public void setUserId(UserBean.User userId) {
        this.userId = userId;
    }
    public Dynamic(int status, String dynamicId, String title,
                   String content, String pic, String publishTime, UserBean.User userId) {
        super();
        this.status = status;
        this.dynamicId = dynamicId;
        this.title = title;
        this.content = content;
        this.pic = pic;
        this.publishTime = publishTime;
        this.userId = userId;
    }
    public Dynamic(String dynamicId, String title, String content,
                   String pic, String publishTime, UserBean.User userId) {
        super();
        this.dynamicId = dynamicId;
        this.title = title;
        this.content = content;
        this.pic = pic;
        this.publishTime = publishTime;
        this.userId = userId;
    }
    @Override
    public String toString() {
        return "Dynamic [status=" + status + ", dynamicId=" + dynamicId
                + ", title=" + title + ", content=" + content + ", pic="
                + pic + ", publishTime=" + publishTime + ", userId="
                + userId + "]";
    }

    public Dynamic(){}



}
