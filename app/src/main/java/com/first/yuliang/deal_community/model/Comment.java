package com.first.yuliang.deal_community.model;

/**
 * 评论对象
 */
public class Comment {

    public User mCommentator; // 评论者
    public String mContent;   // 评论内容
    public User mReceiver; // 接收者（即回复谁）

    @Override
    public String toString() {
        return "Comment{" +
                "mCommentator=" + mCommentator +
                ", mContent='" + mContent + '\'' +
                ", mReceiver=" + mReceiver +
                '}';
    }

    public Comment(User mCommentator, String mContent, User mReceiver) {
        this.mCommentator = mCommentator;
        this.mContent = mContent;
        this.mReceiver = mReceiver;
    }
}
