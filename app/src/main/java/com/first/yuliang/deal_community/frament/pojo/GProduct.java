package com.first.yuliang.deal_community.frament.pojo;

public class GProduct {
	private int id;
	private int commodityid;
	private double latitude;
	private double longitude;
	
	
	public GProduct(){}
	
	public GProduct(int id, int commodityid, double latitude, double longitude) {
		super();
		this.id = id;
		this.commodityid = commodityid;
		this.latitude = latitude;
		this.longitude = longitude;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getCommodityid() {
		return commodityid;
	}
	public void setCommodityid(int commodityid) {
		this.commodityid = commodityid;
	}
	public double getLatitude() {
		return latitude;
	}
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	public double getLongitude() {
		return longitude;
	}
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	
	
	

}
