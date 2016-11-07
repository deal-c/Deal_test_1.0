package com.first.yuliang.deal_community.frament.pojo;

import android.os.Parcel;
import android.os.Parcelable;

import com.first.yuliang.deal_community.pojo.User;

public class Post implements Parcelable {
	private Integer postId;
	private User user;
    private	String  postTitle;
    private String  imgs;
    private String postInfo;
    private String postTime;
    private Integer communityId;
    
    
    
    
    public Post(){};
    
   
	
	public Post(Integer postId, User user, String postTitle, String imgs,
			String postInfo, String postTime, Integer communityId) {
		super();
		this.postId = postId;
		this.user = user;
		this.postTitle = postTitle;
		this.imgs = imgs;
		this.postInfo = postInfo;
		this.postTime = postTime;
		this.communityId = communityId;
	}



	public User getUser() {
		return user;
	}



	public void setUser(User user) {
		this.user = user;
	}



	public Integer getPostId() {
		return postId;
	}
	public void setPostId(Integer postId) {
		this.postId = postId;
	}
	
	public String getPostTitle() {
		return postTitle;
	}
	public void setPostTitle(String postTitle) {
		this.postTitle = postTitle;
	}
	public String getImgs() {
		return imgs;
	}
	public void setImgs(String imgs) {
		this.imgs = imgs;
	}
	public String getPostInfo() {
		return postInfo;
	}
	public void setPostInfo(String postInfo) {
		this.postInfo = postInfo;
	}
	public String getPostTime() {
		return postTime;
	}
	public void setPostTime(String postTime) {
		this.postTime = postTime;
	}
	public Integer getCommunityId() {
		return communityId;
	}
	public void setCommunityId(Integer communityId) {
		this.communityId = communityId;
	}


	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeValue(this.postId);
		dest.writeParcelable(this.user, flags);
		dest.writeString(this.postTitle);
		dest.writeString(this.imgs);
		dest.writeString(this.postInfo);
		dest.writeString(this.postTime);
		dest.writeValue(this.communityId);
	}

	protected Post(Parcel in) {
		this.postId = (Integer) in.readValue(Integer.class.getClassLoader());
		this.user = in.readParcelable(User.class.getClassLoader());
		this.postTitle = in.readString();
		this.imgs = in.readString();
		this.postInfo = in.readString();
		this.postTime = in.readString();
		this.communityId = (Integer) in.readValue(Integer.class.getClassLoader());
	}

	public static final Parcelable.Creator<Post> CREATOR = new Parcelable.Creator<Post>() {
		@Override
		public Post createFromParcel(Parcel source) {
			return new Post(source);
		}

		@Override
		public Post[] newArray(int size) {
			return new Post[size];
		}
	};
}
