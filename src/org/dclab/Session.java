package org.dclab;

import java.sql.Timestamp;
import java.util.Date;

public class Session {
	private int id;//场次id
	private String name;//科目名称
	private int duration;//考试时长
	private Timestamp startTime;//考试开始时间
	private int subId;//科目id
	private int earliestSubmit;//考试结束前**分钟即可交卷
	private int latestLogin;//考试开始后**分钟前仍可登录
	

	public int getSubId() {
		return subId;
	}
	public void setSubId(int subId) {
		this.subId = subId;
	}
	public int getEarliestSubmit() {
		return earliestSubmit;
	}
	public void setEarliestSubmit(int earliestSubmit) {
		this.earliestSubmit = earliestSubmit;
	}
	public int getLatestLogin() {
		return latestLogin;
	}
	public void setLatestLogin(int latestLogin) {
		this.latestLogin = latestLogin;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getDuration() {
		return duration;
	}
	public void setDuration(int duration) {
		this.duration = duration;
	}
	public Timestamp getStartTime() {
		return startTime;
	}
	public void setStartTime(Timestamp startTime) {
		this.startTime = startTime;
	}
	
	
	
}
