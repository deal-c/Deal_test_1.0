package com.first.yuliang.deal_community.frament.pojo;

import android.os.Parcel;
import android.os.Parcelable;

import com.first.yuliang.deal_community.pojo.User;

import java.util.ArrayList;
import java.util.List;

public class Reply implements Parcelable {
	private Integer replyid;
	private Integer postid;
	private User replyuser;
	private String replycontent;
	private String replyimg;
	private String time;
	public List<Reply_son> rslist=new ArrayList<Reply_son>();



	public Reply(){}

	public Reply(Integer replyid, Integer postid, User replyuser,
			String replycontent, String replyimg, String time) {
		super();
		this.replyid = replyid;
		this.postid = postid;
		this.replyuser = replyuser;
		this.replycontent = replycontent;
		this.replyimg = replyimg;
		this.time = time;
	}

	public User getReplyuser() {
		return replyuser;
	}

	public void setReplyuser(User replyuser) {
		this.replyuser = replyuser;
	}

	public Integer getReplyid() {
		return replyid;
	}
	public void setReplyid(Integer replyid) {
		this.replyid = replyid;
	}
	public Integer getPostid() {
		return postid;
	}
	public void setPostid(Integer postid) {
		this.postid = postid;
	}

	public String getReplycontent() {
		return replycontent;
	}
	public void setReplycontent(String replycontent) {
		this.replycontent = replycontent;
	}
	public String getReplyimg() {
		return replyimg;
	}
	public void setReplyimg(String replyimg) {
		this.replyimg = replyimg;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}


	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeValue(this.replyid);
		dest.writeValue(this.postid);
		dest.writeParcelable(this.replyuser, flags);
		dest.writeString(this.replycontent);
		dest.writeString(this.replyimg);
		dest.writeString(this.time);
		dest.writeTypedList(this.rslist);
	}

	protected Reply(Parcel in) {
		this.replyid = (Integer) in.readValue(Integer.class.getClassLoader());
		this.postid = (Integer) in.readValue(Integer.class.getClassLoader());
		this.replyuser = in.readParcelable(User.class.getClassLoader());
		this.replycontent = in.readString();
		this.replyimg = in.readString();
		this.time = in.readString();
		this.rslist = in.createTypedArrayList(Reply_son.CREATOR);
	}

	public static final Parcelable.Creator<Reply> CREATOR = new Parcelable.Creator<Reply>() {
		@Override
		public Reply createFromParcel(Parcel source) {
			return new Reply(source);
		}

		@Override
		public Reply[] newArray(int size) {
			return new Reply[size];
		}
	};
}
