package com.first.yuliang.deal_community.model;

import java.util.List;

/**
 * Created by Administrator on 2016/10/17.
 */
public class dynamic {
    private int dynamicId;
    private String title;
    private String content;
    private String pic;
    private String publishTime;
    private int userId;
    public List<dynamic> moment;

    public List<dynamic> getMoment() {
        return moment;
    }

    public void setMoment(List<dynamic> moment) {
        this.moment = moment;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getDynamicId() {
        return dynamicId;
    }

    public void setDynamicId(int dynamicId) {
        this.dynamicId = dynamicId;
    }
}
