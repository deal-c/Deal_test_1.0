package com.first.yuliang.deal_community.model;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * 评论对象
 */
public class Comment {


    public Comment(String imgs, String remaekTime, int status,
                   ArrayList<Comment> list) {
        super();
        this.imgs = imgs;
        this.remaekTime = remaekTime;
        this.status = status;
        this.list = list;
    }



    public Comment(String imgs, String remaekTime, User mCommentator,
                   String mContent, User mReceiver, ArrayList<Comment> list) {
        super();
        this.imgs = imgs;
        this.remaekTime = remaekTime;
        this.mCommentator = mCommentator;
        this.mContent = mContent;
        this.mReceiver = mReceiver;
        this.list = list;
    }



    public Comment(String imgs, String remaekTime, int status,
                   User mCommentator, String mContent, User mReceiver,
                   ArrayList<Comment> list) {
        super();
        this.imgs = imgs;
        this.remaekTime = remaekTime;
        this.status = status;
        this.mCommentator = mCommentator;
        this.mContent = mContent;
        this.mReceiver = mReceiver;
        this.list = list;
    }

    public	String	imgs;
    public	String	remaekTime;
    public	int	status;

    public String getImgs() {
        return imgs;
    }

    public void setImgs(String imgs) {
        this.imgs = imgs;
    }

    public String getRemaekTime() {
        return remaekTime;
    }

    public void setRemaekTime(String remaekTime) {
        this.remaekTime = remaekTime;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public User mCommentator; // 评论者
    public String mContent;   // 评论内容
    public User mReceiver; // 接收者（即回复谁）

    @Override
    public String toString() {
        return "Comment [imgs=" + imgs + ", remaekTime=" + remaekTime
                + ", status=" + status + ", mCommentator=" + mCommentator
                + ", mContent=" + mContent + ", mReceiver=" + mReceiver
                + ", list=" + list + "]";
    }

    ArrayList<Comment>	list;

    public Comment(User mCommentator, String mContent, User mReceiver) {
        this.mCommentator = mCommentator;
        this.mContent = mContent;
        this.mReceiver = mReceiver;
    }

    public Comment(){}

    public Comment(User mCommentator, String mContent, User mReceiver,
                   ArrayList<Comment> list) {
        super();
        this.mCommentator = mCommentator;
        this.mContent = mContent;
        this.mReceiver = mReceiver;
        this.list = list;
    }

    public User getmCommentator() {
        return mCommentator;
    }

    public void setmCommentator(User mCommentator) {
        this.mCommentator = mCommentator;
    }

    public String getmContent() {
        return mContent;
    }

    public void setmContent(String mContent) {
        this.mContent = mContent;
    }

    public User getmReceiver() {
        return mReceiver;
    }

    public void setmReceiver(User mReceiver) {
        this.mReceiver = mReceiver;
    }

    public ArrayList<Comment> getList() {
        return list;
    }

    public void setList(ArrayList<Comment> list) {
        this.list = list;
    }


}



