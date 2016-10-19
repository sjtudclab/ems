package org.dclab.model;

import java.sql.Timestamp;

public class SessionBean {
	private int id;
	private String roomName;
	private Timestamp startTime;
	private String Uid;
	
	private int status;//考场状态
	private int testNum;//应考人数
	private int registeredNum;//已登录人数
	private int unRegisteredNum;//未登录人数
	
	public SessionBean() {
		super();
	}
	public SessionBean(String roomName, Timestamp startTime,String Uid) {
		super();
		this.roomName = roomName;
		this.startTime = startTime;
		this.Uid = Uid;
	}
	
	
	
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public int getTestNum() {
		return testNum;
	}
	public void setTestNum(int testNum) {
		this.testNum = testNum;
	}
	public int getRegisteredNum() {
		return registeredNum;
	}
	public void setRegisteredNum(int registeredNum) {
		this.registeredNum = registeredNum;
	}
	public int getUnRegisteredNum() {
		return unRegisteredNum;
	}
	public void setUnRegisteredNum(int unRegisteredNum) {
		this.unRegisteredNum = unRegisteredNum;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}

	public String getRoomName() {
		return roomName;
	}
	public void setRoomName(String roomName) {
		this.roomName = roomName;
	}
	public Timestamp getStartTime() {
		return startTime;
	}
	public void setStartTime(Timestamp startTime) {
		this.startTime = startTime;
	}
	public String getUid() {
		return Uid;
	}
	public void setUid(String uid) {
		Uid = uid;
	}
	
	
}
