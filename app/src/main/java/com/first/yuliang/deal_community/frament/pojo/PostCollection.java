package com.first.yuliang.deal_community.frament.pojo;

import com.first.yuliang.deal_community.pojo.Community;

import java.util.Date;

public class PostCollection {
public	Integer	postCollectionId;
public	Integer	userId;
public	Integer	postId;
public	Date	collectionTime;
public	Post	post;
public Community community;
public Integer getPostCollectionId() {
	return postCollectionId;
}
public void setPostCollectionId(Integer postCollectionId) {
	this.postCollectionId = postCollectionId;
}
public Integer getUserId() {
	return userId;
}
public void setUserId(Integer userId) {
	this.userId = userId;
}
public Integer getPostId() {
	return postId;
}
public void setPostId(Integer postId) {
	this.postId = postId;
}
public Date getCollectionTime() {
	return collectionTime;
}
public void setCollectionTime(Date collectionTime) {
	this.collectionTime = collectionTime;
}
public Post getPost() {
	return post;
}
public void setPost(Post post) {
	this.post = post;
}
public Community getCommunity() {
	return community;
}
public void setCommunity(Community community) {
	this.community = community;
}
public PostCollection(Integer userId, Integer postId, Date collectionTime,
		Post post, Community community) {
	super();
	this.userId = userId;
	this.postId = postId;
	this.collectionTime = collectionTime;
	this.post = post;
	this.community = community;
}












}
