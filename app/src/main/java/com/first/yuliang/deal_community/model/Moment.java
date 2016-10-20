package com.first.yuliang.deal_community.model;

import java.util.ArrayList;
import java.util.Date;

/**
 * 评论对象
 */
public class Moment {
    public String MonentId;
    public int status;
    public String imgs;
    public String  date;


    public String getMonentId() {
        return MonentId;
    }

    public void setMonentId(String monentId) {
        MonentId = monentId;
    }

    public ArrayList<Comment> getmComment() {
        return mComment;
    }

    public void setmComment(ArrayList<Comment> mComment) {
        this.mComment = mComment;
    }

    public String getmContent() {
        return mContent;
    }

    public void setmContent(String mContent) {
        this.mContent = mContent;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getImgs() {
        return imgs;
    }

    public void setImgs(String imgs) {
        this.imgs = imgs;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String mContent;
    public  ArrayList<Comment> mComment; // 评论列表

    public Moment(String mContent, ArrayList<Comment> mComment) {
        this.mComment = mComment;
        this.mContent = mContent;
    }
    public Moment(){}

    public Moment(String monentId, ArrayList<Comment> mComment, String mContent, String date, String imgs, int status) {
        MonentId = monentId;
        this.mComment = mComment;
        this.mContent = mContent;
        this.date = date;
        this.imgs = imgs;
        this.status = status;
    }

    @Override
    public String toString() {
        return "Moment{" +
                "MonentId='" + MonentId + '\'' +
                ", status=" + status +
                ", imgs='" + imgs + '\'' +
                ", date='" + date + '\'' +
                ", mContent='" + mContent + '\'' +
                ", mComment=" + mComment +
                '}';
    }
}
