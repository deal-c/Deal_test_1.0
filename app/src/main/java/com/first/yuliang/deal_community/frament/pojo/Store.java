package com.first.yuliang.deal_community.frament.pojo;

public class Store {
	private int id;
	private String title;
	private String content;
    private double latitude;
    private double longitude;
    
    
    
    public Store(){}
    
	public Store(int id, String title, String content, double latitude,
			double longitude) {
		super();
		this.id = id;
		this.title = title;
		this.content = content;
		this.latitude = latitude;
		this.longitude = longitude;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
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
