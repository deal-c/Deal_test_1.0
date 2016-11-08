package com.first.yuliang.deal_community.pojo;

import com.first.yuliang.deal_community.frament.pojo.Post;

public class Responsess {

	private User user;
	private String content;
	private String time;
	private Integer userId;
	private Post post;
	private Community community;
	
	
	
	public Responsess(User user, String content, String time, Integer userId,
			Post post, Community community) {
		super();
		this.user = user;
		this.content = content;
		this.time = time;
		this.userId = userId;
		this.post = post;
		this.community = community;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public Integer getUserId() {
		return userId;
	}
	public void setUser2(Integer userId) {
		this.userId= userId;
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
	
	
}
