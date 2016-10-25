package com.first.yuliang.deal_community.pojo;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2016/10/18.
 */
public class Address implements Parcelable {
    private int addressId;
    private String contactPhoneNum;
    private String city;
    private String userName;
    private String addressDetail;
    private boolean isdefault;
    private User user;

    public Address(){}

    public Address(int addressId, String contactPhoneNum, String city, String userName, String addressDetail, boolean isdefault) {
        this.addressId = addressId;

        this.contactPhoneNum = contactPhoneNum;
        this.city = city;
        this.userName = userName;
        this.addressDetail = addressDetail;
        this.isdefault = isdefault;
    }

    public int getAddressId() {
        return addressId;
    }

    public void setAddressId(int addressId) {
        this.addressId = addressId;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getContactPhoneNum() {
        return contactPhoneNum;
    }

    public void setContactPhoneNum(String contactPhoneNum) {
        this.contactPhoneNum = contactPhoneNum;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getAddressDetail() {
        return addressDetail;
    }

    public void setAddressDetail(String addressDetail) {
        this.addressDetail = addressDetail;
    }

    public boolean isdefault() {
        return isdefault;
    }

    public void setIsdefault(boolean isdefault) {
        this.isdefault = isdefault;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.addressId);
        dest.writeString(this.contactPhoneNum);
        dest.writeString(this.city);
        dest.writeString(this.userName);
        dest.writeString(this.addressDetail);
        dest.writeByte(this.isdefault ? (byte) 1 : (byte) 0);
        dest.writeParcelable(this.user, flags);
    }

    protected Address(Parcel in) {
        this.addressId = in.readInt();
        this.contactPhoneNum = in.readString();
        this.city = in.readString();
        this.userName = in.readString();
        this.addressDetail = in.readString();
        this.isdefault = in.readByte() != 0;
        this.user = in.readParcelable(User.class.getClassLoader());
    }

    public static final Creator<Address> CREATOR = new Creator<Address>() {
        @Override
        public Address createFromParcel(Parcel source) {
            return new Address(source);
        }

        @Override
        public Address[] newArray(int size) {
            return new Address[size];
        }
    };
}
