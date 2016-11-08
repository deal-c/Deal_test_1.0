package com.first.yuliang.deal_community.pojo;

import java.util.Date;



public class product_h_tb {
	
	private int commodity_b_h_id;
	private Product product;
	private int userId;
	private Date browsingTime;

	public product_h_tb(){}

	public product_h_tb(int commodity_b_h_id, Product product, int userId, Date browsingTime) {
		this.commodity_b_h_id = commodity_b_h_id;
		this.product = product;
		this.userId = userId;
		this.browsingTime = browsingTime;
	}

	public int getCommodity_b_h_id() {
		return commodity_b_h_id;
	}

	public void setCommodity_b_h_id(int commodity_b_h_id) {
		this.commodity_b_h_id = commodity_b_h_id;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public Date getBrowsingTime() {
		return browsingTime;
	}

	public void setBrowsingTime(Date browsingTime) {
		this.browsingTime = browsingTime;
	}
}
