package com.first.yuliang.deal_community.frament.pojo;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

/**
 * Created by yuliang on 2016/11/9.
 */

public class Ad implements Parcelable {

        private int adid;
        private String adtitle;
        private String adcontent;
        private String adphoto;
        private String adhttp;
        private Date time;
        public Ad(){};
        public Ad(int adid, String adtitle, String adcontent, String adhttp) {
            this.adid = adid;
            this.adtitle = adtitle;
            this.adcontent = adcontent;
            this.adhttp = adhttp;
        }

        public int getAdid() {
            return adid;
        }

        public void setAdid(int adid) {
            this.adid = adid;
        }

        public String getAdtitle() {
            return adtitle;
        }

        public void setAdtitle(String adtitle) {
            this.adtitle = adtitle;
        }

        public String getAdcontent() {
            return adcontent;
        }

        public void setAdcontent(String adcontent) {
            this.adcontent = adcontent;
        }

        public String getAdphoto() {
            return adphoto;
        }

        public void setAdphoto(String adphoto) {
            this.adphoto = adphoto;
        }

        public String getAdhttp() {
            return adhttp;
        }

        public void setAdhttp(String adhttp) {
            this.adhttp = adhttp;
        }

        public Date getTime() {
            return time;
        }

        public void setTime(Date time) {
            this.time = time;
        }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.adid);
        dest.writeString(this.adtitle);
        dest.writeString(this.adcontent);
        dest.writeString(this.adphoto);
        dest.writeString(this.adhttp);
        dest.writeLong(this.time != null ? this.time.getTime() : -1);
    }

    protected Ad(Parcel in) {
        this.adid = in.readInt();
        this.adtitle = in.readString();
        this.adcontent = in.readString();
        this.adphoto = in.readString();
        this.adhttp = in.readString();
        long tmpTime = in.readLong();
        this.time = tmpTime == -1 ? null : new Date(tmpTime);
    }

    public static final Parcelable.Creator<Ad> CREATOR = new Parcelable.Creator<Ad>() {
        @Override
        public Ad createFromParcel(Parcel source) {
            return new Ad(source);
        }

        @Override
        public Ad[] newArray(int size) {
            return new Ad[size];
        }
    };
}
