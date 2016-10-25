package com.first.yuliang.deal_community.pojo;

import android.os.Parcel;
import android.os.Parcelable;

public class Community implements Parcelable {
	private int communityId;
	private String communityName;
	private String communityInfo;
	private String comImg;
	private String comCreatTime;

	@Override
	public String toString() {
		return "Community{" +
				"communityId=" + communityId +
				", communityName='" + communityName + '\'' +
				", communityInfo='" + communityInfo + '\'' +
				", comImg='" + comImg + '\'' +
				", comCreatTime='" + comCreatTime + '\'' +
				'}';
	}

	public Community(){};
	
	public Community(int communityId, String communityName,
					 String communityInfo, String comImg, String comCreatTime) {
		super();
		this.communityId = communityId;
		this.communityName = communityName;
		this.communityInfo = communityInfo;
		this.comImg = comImg;
		this.comCreatTime = comCreatTime;
	}
	public int getCommunityId() {
		return communityId;
	}
	public void setCommunityId(int communityId) {
		this.communityId = communityId;
	}
	public String getCommunityName() {
		return communityName;
	}
	public void setCommunityName(String communityName) {
		this.communityName = communityName;
	}
	public String getCommunityInfo() {
		return communityInfo;
	}
	public void setCommunityInfo(String communityInfo) {
		this.communityInfo = communityInfo;
	}
	public String getComImg() {
		return comImg;
	}
	public void setComImg(String comImg) {
		this.comImg = comImg;
	}
	public String getComCreatTime() {
		return comCreatTime;
	}
	public void setComCreatTime(String comCreatTime) {
		this.comCreatTime = comCreatTime;
	}


	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(this.communityId);
		dest.writeString(this.communityName);
		dest.writeString(this.communityInfo);
		dest.writeString(this.comImg);
		dest.writeString(this.comCreatTime);
	}

	protected Community(Parcel in) {
		this.communityId = in.readInt();
		this.communityName = in.readString();
		this.communityInfo = in.readString();
		this.comImg = in.readString();
		this.comCreatTime = in.readString();
	}

	public static final Creator<Community> CREATOR = new Creator<Community>() {
		@Override
		public Community createFromParcel(Parcel source) {
			return new Community(source);
		}

		@Override
		public Community[] newArray(int size) {
			return new Community[size];
		}
	};
}
