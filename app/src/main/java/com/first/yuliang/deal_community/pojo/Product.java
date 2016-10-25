package com.first.yuliang.deal_community.pojo;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

public class Product implements Parcelable {

	/**
	 * 
	 */

	private int productId;
	//private int releaseUserId;
	private User releaseUser;
	private String productTitle;
	private double price;
	private String productDescribe;
	private String productImg;
	private Date releaseTime;
	private String location;
	private int buyWay;
	private String productClass;
	private int buyUserId;
	private int statement;
	
	public Product(){}
	public Product(int productId, User releaseUser, String productTitle,
			double price, String productDescribe, String productImg,
			Date releaseTime, String location, int buyWay,String productClass, int buyUserId,
			int statement) {
		super();
		this.productId = productId;
		this.releaseUser = releaseUser;
		this.productTitle = productTitle;
		this.price = price;
		this.productDescribe = productDescribe;
		this.productImg = productImg;
		this.releaseTime = releaseTime;
		this.location = location;
		this.buyWay = buyWay;
		this.productClass=productClass;
		this.buyUserId = buyUserId;
		this.statement = statement;
	}
	public int getProductId() {
		return productId;
	}
	public void setProductId(int productId) {
		this.productId = productId;
	}
	
	public User getReleaseUser() {
		return releaseUser;
	}
	public void setReleaseUser(User releaseUser) {
		this.releaseUser = releaseUser;
	}
	public String getProductTitle() {
		return productTitle;
	}
	public void setProductTitle(String productTitle) {
		this.productTitle = productTitle;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public String getProductDescribe() {
		return productDescribe;
	}
	public void setProductDescribe(String productDescribe) {
		this.productDescribe = productDescribe;
	}
	public String getProductImg() {
		return productImg;
	}
	public void setProductImg(String productImg) {
		this.productImg = productImg;
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
	public void setBuyWay(int buyWay) {
		this.buyWay = buyWay;
	}
	public int getBuyUserId() {
		return buyUserId;
	}
	public void setBuyUserId(int buyUserId) {
		this.buyUserId = buyUserId;
	}
	public int getStatement() {
		return statement;
	}
	public void setStatement(int statement) {
		this.statement = statement;
	}
	public String getProductClass() {
		return productClass;
	}
	public void setProductClass(String productClass) {
		this.productClass = productClass;
	}


	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(this.productId);
		dest.writeParcelable(this.releaseUser, flags);
		dest.writeString(this.productTitle);
		dest.writeDouble(this.price);
		dest.writeString(this.productDescribe);
		dest.writeString(this.productImg);
		dest.writeLong(this.releaseTime != null ? this.releaseTime.getTime() : -1);
		dest.writeString(this.location);
		dest.writeInt(this.buyWay);
		dest.writeString(this.productClass);
		dest.writeInt(this.buyUserId);
		dest.writeInt(this.statement);
	}

	protected Product(Parcel in) {
		this.productId = in.readInt();
		this.releaseUser = in.readParcelable(User.class.getClassLoader());
		this.productTitle = in.readString();
		this.price = in.readDouble();
		this.productDescribe = in.readString();
		this.productImg = in.readString();
		long tmpReleaseTime = in.readLong();
		this.releaseTime = tmpReleaseTime == -1 ? null : new Date(tmpReleaseTime);
		this.location = in.readString();
		this.buyWay = in.readInt();
		this.productClass = in.readString();
		this.buyUserId = in.readInt();
		this.statement = in.readInt();
	}

	public static final Parcelable.Creator<Product> CREATOR = new Parcelable.Creator<Product>() {
		@Override
		public Product createFromParcel(Parcel source) {
			return new Product(source);
		}

		@Override
		public Product[] newArray(int size) {
			return new Product[size];
		}
	};
}
