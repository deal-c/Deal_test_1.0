package com.first.yuliang.deal_community.pojo;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

/**
 * Created by Administrator on 2016/9/18.
 */
public class User implements Parcelable {

    private int userId;
    private String userName;
    private String userPsd;
    private String userImg;
    private boolean userSex;
    private Date birthday;
    private String userAddress_s;

    private int LabelId;
    private String token;


    public User(){}

    public User(int userId, String userName, String userPsd, String userImg, boolean userSex, Date birthday, String userAddress_s, int labelId,String token) {
        this.userId = userId;
        this.userName = userName;
        this.userPsd = userPsd;
        this.userImg = userImg;
        this.userSex = userSex;
        this.birthday=birthday;
        this.userAddress_s = userAddress_s;

        LabelId = labelId;
        this.token=token;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPsd() {
        return userPsd;
    }

    public void setUserPsd(String userPsd) {
        this.userPsd = userPsd;
    }

    public String getUserImg() {
        return userImg;
    }

    public void setUserImg(String userImg) {
        this.userImg = userImg;
    }

    public boolean isUserSex() {
        return userSex;
    }

    public void setUserSex(boolean userSex) {
        this.userSex = userSex;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getUserAddress_s() {
        return userAddress_s;
    }

    public void setUserAddress_s(String userAddress_s) {
        this.userAddress_s = userAddress_s;
    }

    public int getLabelId() {
        return LabelId;
    }

    public void setLabelId(int labelId) {
        LabelId = labelId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.userId);
        dest.writeString(this.userName);
        dest.writeString(this.userPsd);
        dest.writeString(this.userImg);
        dest.writeByte(this.userSex ? (byte) 1 : (byte) 0);
        dest.writeLong(this.birthday != null ? this.birthday.getTime() : -1);
        dest.writeString(this.userAddress_s);
        dest.writeInt(this.LabelId);
        dest.writeString(this.token);
    }

    protected User(Parcel in) {
        this.userId = in.readInt();
        this.userName = in.readString();
        this.userPsd = in.readString();
        this.userImg = in.readString();
        this.userSex = in.readByte() != 0;
        long tmpUserBirthday = in.readLong();
        this.birthday = tmpUserBirthday == -1 ? null : new Date(tmpUserBirthday);
        this.userAddress_s = in.readString();
        this.LabelId = in.readInt();
        this.token=in.readString();
    }

    public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {
        @Override
        public User createFromParcel(Parcel source) {
            return new User(source);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };
}
