package com.first.yuliang.deal_community.pojo;

import java.io.Serializable;
import java.util.Date;

public class CommodityInfoUser implements Serializable {

	/**
	 * 
	 */
	private Integer commodityId;
	private User user_r;
	private String commodityTitle;
	private Double price;
	private String commodityDescribe;
	private String commodityImg;
	private Date releaseTime;
	private String location;
	private Integer buyWay;
	private String commodityClass;
	private User user_b;
	private Integer statement;

	public CommodityInfoUser() {
	}

	public CommodityInfoUser(User user_r, String commodityTitle,
			Double price, String commodityDescribe, String commodityImg,
			Date releaseTime, String location, Integer buyWay,
			String commodityClass, User user_b, Integer statement) {
		super();
		this.user_r = user_r;
		this.commodityTitle = commodityTitle;
		this.price = price;
		this.commodityDescribe = commodityDescribe;
		this.commodityImg = commodityImg;
		this.releaseTime = releaseTime;
		this.location = location;
		this.buyWay = buyWay;
		this.commodityClass = commodityClass;
		this.user_b = user_b;
		this.statement = statement;
	}

	public User getUser_r() {
		return user_r;
	}

	public void setUser_r(User user_r) {
		this.user_r = user_r;
	}

	public int getCommodityId() {
		return commodityId;
	}

	public void setCommodityId(Integer commodityId) {
		this.commodityId = commodityId;
	}

	public String getCommodityTitle() {
		return commodityTitle;
	}

	public void setCommodityTitle(String commodityTitle) {
		this.commodityTitle = commodityTitle;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public String getCommodityDescribe() {
		return commodityDescribe;
	}

	public void setCommodityDescribe(String commodityDescribe) {
		this.commodityDescribe = commodityDescribe;
	}

	public String getCommodityImg() {
		return commodityImg;
	}

	public void setCommodityImg(String commodityImg) {
		this.commodityImg = commodityImg;
	}

	public Date getReleaseTime() {
		return releaseTime;
	}

	public void setReleaseTime(Date releaseTime) {
		this.releaseTime = releaseTime;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public int getBuyWay() {
		return buyWay;
	}

	public void setBuyWay(Integer buyWay) {
		this.buyWay = buyWay;
	}

	public String getCommodityClass() {
		return commodityClass;
	}

	public void setCommodityClass(String commodityClass) {
		this.commodityClass = commodityClass;
	}

	public User getUser_b() {
		return user_b;
	}

	public void setUser_b(User user_b) {
		this.user_b = user_b;
	}

	public int getStatement() {
		return statement;
	}

	public void setStatement(Integer statement) {
		this.statement = statement;
	}

}
