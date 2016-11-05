package com.first.yuliang.deal_community.frament.pojo;

public class Oldnew {
	private  Integer oldnewid;
	private String oldnewtitle;
	private String oldnewinfo;
	private String oldnewimg;
	private String oldnewtime;
	private String place;
	
	
	public Oldnew(){}
	public Oldnew(Integer oldnewid, String oldnewtitle, String oldnewinfo,
			String oldnewimg, String oldnewtime,String place ) {
		super();
		this.oldnewid = oldnewid;
		this.oldnewtitle = oldnewtitle;
		this.oldnewinfo = oldnewinfo;
		this.oldnewimg = oldnewimg;
		this.oldnewtime = oldnewtime;
		this.place = place;
		
	}
	
	public String getPlace() {
		return place;
	}
	public void setPlace(String place) {
		this.place = place;
	}
	public Integer getOldnewid() {
		return oldnewid;
	}
	public void setOldnewid(Integer oldnewid) {
		this.oldnewid = oldnewid;
	}
	public String getOldnewtitle() {
		return oldnewtitle;
	}
	public void setOldnewtitle(String oldnewtitle) {
		this.oldnewtitle = oldnewtitle;
	}
	public String getOldnewinfo() {
		return oldnewinfo;
	}
	public void setOldnewinfo(String oldnewinfo) {
		this.oldnewinfo = oldnewinfo;
	}
	public String getOldnewimg() {
		return oldnewimg;
	}
	public void setOldnewimg(String oldnewimg) {
		this.oldnewimg = oldnewimg;
	}
	public String getOldnewtime() {
		return oldnewtime;
	}
	public void setOldnewtime(String oldnewtime) {
		this.oldnewtime = oldnewtime;
	}
	

}
