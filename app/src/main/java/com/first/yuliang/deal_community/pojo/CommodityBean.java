package com.first.yuliang.deal_community.pojo;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by vanex on 2016/10/13.
 */
public class CommodityBean {
    public List<Commodity> commodities;

    public static class Commodity implements Parcelable {
        public Integer commodityId;
        public Integer releaseUserId;
        public String commodityTitle;
        public Double price;
        public String commodityDescribe;
        public String commodityImg;
        public String releaseTime;
        public String location;
        public Integer buyWay;
        public String commodityClass;
        public Integer buyUserId;
        public Integer statement;
        public Integer dianzan;

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeValue(this.commodityId);
            dest.writeValue(this.releaseUserId);
            dest.writeString(this.commodityTitle);
            dest.writeValue(this.price);
            dest.writeString(this.commodityDescribe);
            dest.writeString(this.commodityImg);
            dest.writeString(this.releaseTime);
            dest.writeString(this.location);
            dest.writeValue(this.buyWay);
            dest.writeString(this.commodityClass);
            dest.writeValue(this.buyUserId);
            dest.writeValue(this.statement);
            dest.writeValue(this.dianzan);
        }

        public Commodity() {
        }

        protected Commodity(Parcel in) {
            this.commodityId = (Integer) in.readValue(Integer.class.getClassLoader());
            this.releaseUserId = (Integer) in.readValue(Integer.class.getClassLoader());
            this.commodityTitle = in.readString();
            this.price = (Double) in.readValue(Double.class.getClassLoader());
            this.commodityDescribe = in.readString();
            this.commodityImg = in.readString();
            this.releaseTime = in.readString();
            this.location = in.readString();
            this.buyWay = (Integer) in.readValue(Integer.class.getClassLoader());
            this.commodityClass = in.readString();
            this.buyUserId = (Integer) in.readValue(Integer.class.getClassLoader());
            this.statement = (Integer) in.readValue(Integer.class.getClassLoader());
            this.dianzan = (Integer) in.readValue(Integer.class.getClassLoader());
        }

        public static final Parcelable.Creator<Commodity> CREATOR = new Parcelable.Creator<Commodity>() {
            @Override
            public Commodity createFromParcel(Parcel source) {
                return new Commodity(source);
            }

            @Override
            public Commodity[] newArray(int size) {
                return new Commodity[size];
            }
        };
    }
}
