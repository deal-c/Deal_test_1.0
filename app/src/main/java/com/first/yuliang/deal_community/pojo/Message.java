package com.first.yuliang.deal_community.pojo;

public class Message {
	
	private int messageId;
	private String message;
	private int userId;
	
	
	public Message(){}
	
	
	public Message(int messageId, String message, int userId) {
		super();
		this.messageId = messageId;
		this.message = message;
		this.userId = userId;
	}
	public int getMessageId() {
		return messageId;
	}
	public void setMessageId(int messageId) {
		this.messageId = messageId;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	
	

}
