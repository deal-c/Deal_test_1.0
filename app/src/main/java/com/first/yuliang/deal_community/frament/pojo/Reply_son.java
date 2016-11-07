package com.first.yuliang.deal_community.frament.pojo;

import android.os.Parcel;
import android.os.Parcelable;

import com.first.yuliang.deal_community.pojo.User;

public class Reply_son implements Parcelable {

	private Integer rs_id;
	private Integer replyid;
	private User rs_from_user;
	private User rs_to_user;
	private String rs_content;
	private String time;

	public Reply_son() {
	}
	
	
	
	

	public Reply_son(Integer rsId, Integer replyid, User rsFromUser,
			User rsToUser, String rsContent, String time) {
		super();
		rs_id = rsId;
		this.replyid = replyid;
		rs_from_user = rsFromUser;
		rs_to_user = rsToUser;
		rs_content = rsContent;
		this.time = time;
	}





	public Integer getReplyid() {
		return replyid;
	}





	public void setReplyid(Integer replyid) {
		this.replyid = replyid;
	}





	public Reply_son(Integer rsId, User rsFromUser, User rsToUser,
			String rsContent, String time) {
		super();
		rs_id = rsId;
		rs_from_user = rsFromUser;
		rs_to_user = rsToUser;
		rs_content = rsContent;
		this.time = time;
	}





	public Integer getRs_id() {
		return rs_id;
	}

	public void setRs_id(Integer rsId) {
		rs_id = rsId;
	}

	public User getRs_from_user() {
		return rs_from_user;
	}

	public void setRs_from_user(User rsFromUser) {
		rs_from_user = rsFromUser;
	}

	public User getRs_to_user() {
		return rs_to_user;
	}

	public void setRs_to_user(User rsToUser) {
		rs_to_user = rsToUser;
	}

	public String getRs_content() {
		return rs_content;
	}

	public void setRs_content(String rsContent) {
		rs_content = rsContent;
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
		dest.writeValue(this.rs_id);
		dest.writeValue(this.replyid);
		dest.writeParcelable(this.rs_from_user, flags);
		dest.writeParcelable(this.rs_to_user, flags);
		dest.writeString(this.rs_content);
		dest.writeString(this.time);
	}

	protected Reply_son(Parcel in) {
		this.rs_id = (Integer) in.readValue(Integer.class.getClassLoader());
		this.replyid = (Integer) in.readValue(Integer.class.getClassLoader());
		this.rs_from_user = in.readParcelable(User.class.getClassLoader());
		this.rs_to_user = in.readParcelable(User.class.getClassLoader());
		this.rs_content = in.readString();
		this.time = in.readString();
	}

	public static final Parcelable.Creator<Reply_son> CREATOR = new Parcelable.Creator<Reply_son>() {
		@Override
		public Reply_son createFromParcel(Parcel source) {
			return new Reply_son(source);
		}

		@Override
		public Reply_son[] newArray(int size) {
			return new Reply_son[size];
		}
	};
}
