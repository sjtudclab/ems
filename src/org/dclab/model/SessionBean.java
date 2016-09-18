package org.dclab.model;

import java.sql.Timestamp;

public class SessionBean {
	private int id;
	private String roomName;
	private Timestamp startTime;
	private String Uid;
	
	public SessionBean() {
		super();
	}
	public SessionBean(String roomName, Timestamp startTime,String Uid) {
		super();
		this.roomName = roomName;
		this.startTime = startTime;
		this.Uid = Uid;
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
