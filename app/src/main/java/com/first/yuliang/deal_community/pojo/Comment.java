package com.first.yuliang.deal_community.pojo;


import android.os.Parcel;
import android.os.Parcelable;

public class Comment implements Parcelable {

	private int commentId;
	private User user;
	private int maijiaId;
	private int productId;
	private String comment;
	
	public Comment(){}
	public Comment(User user, int maijiaId, int productId,
			String comment) {
		super();
		
		this.user = user;
		this.maijiaId = maijiaId;
		this.productId = productId;
		this.comment = comment;
	}
	public Comment(int commentId, User user, int maijiaId, int productId,
			String comment) {
		super();
		this.commentId = commentId;
		this.user = user;
		this.maijiaId = maijiaId;
		this.productId = productId;
		this.comment = comment;
	}

	public int getCommentId() {
		return commentId;
	}

	public void setCommentId(int commentId) {
		this.commentId = commentId;
	}

	public int getMaijiaId() {
		return maijiaId;
	}

	public void setMaijiaId(int maijiaId) {
		this.maijiaId = maijiaId;
	}

	public int getProductId() {
		return productId;
	}

	public void setProductId(int productId) {
		this.productId = productId;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}


	public String getComment() {
		return comment;
	}
	public void Comment(String comment) {
		this.comment =comment;
	}


	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(this.commentId);
		dest.writeParcelable(this.user, flags);
		dest.writeInt(this.maijiaId);
		dest.writeInt(this.productId);
		dest.writeString(this.comment);
	}

	protected Comment(Parcel in) {
		this.commentId = in.readInt();
		this.user = in.readParcelable(User.class.getClassLoader());
		this.maijiaId = in.readInt();
		this.productId = in.readInt();
		this.comment = in.readString();
	}

	public static final Parcelable.Creator<Comment> CREATOR = new Parcelable.Creator<Comment>() {
		@Override
		public Comment createFromParcel(Parcel source) {
			return new Comment(source);
		}

		@Override
		public Comment[] newArray(int size) {
			return new Comment[size];
		}
	};
}
