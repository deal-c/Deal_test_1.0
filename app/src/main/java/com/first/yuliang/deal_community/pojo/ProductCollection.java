package com.first.yuliang.deal_community.pojo;

import java.util.Date;

public class ProductCollection {

	private int commodityCollectionId;
	private Product product;
	private int userId;
	private Date collectionTime;
	
	public ProductCollection(){}
	public ProductCollection(int commodityCollectionId, Product product,
			int userId, Date collectionTime) {
		super();
		this.commodityCollectionId = commodityCollectionId;
		this.product = product;
		this.userId = userId;
		this.collectionTime = collectionTime;
	}
	public int getCommodityCollectionId() {
		return commodityCollectionId;
	}
	public void setCommodityCollectionId(int commodityCollectionId) {
		this.commodityCollectionId = commodityCollectionId;
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
	public Date getCollectionTime() {
		return collectionTime;
	}
	public void setCollectionTime(Date collectionTime) {
		this.collectionTime = collectionTime;
	}
	
	
}
