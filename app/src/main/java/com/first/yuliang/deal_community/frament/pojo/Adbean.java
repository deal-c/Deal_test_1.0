package com.first.yuliang.deal_community.frament.pojo;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by yuliang on 2016/9/27.
 */
public class Adbean {
    public ArrayList<Ad> adlist ;

    public static class Ad{
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
    }
}
