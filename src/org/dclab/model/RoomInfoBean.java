package org.dclab.model;

import java.sql.Timestamp;

public class RoomInfoBean {
	private int id;//场次id
	private Timestamp startTime;
	private String roomName;
	private int size;
	private String Uid;
	private int status;
	private int loadStatus;
	private int examStatus;
	
	

	public int getLoadStatus() {
		return loadStatus;
	}
	public void setLoadStatus(int loadStatus) {
		this.loadStatus = loadStatus;
	}
	public int getExamStatus() {
		return examStatus;
	}
	public void setExamStatus(int examStatus) {
		this.examStatus = examStatus;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public Timestamp getStartTime() {
		return startTime;
	}
	public void setStartTime(Timestamp startTime) {
		this.startTime = startTime;
	}
	public String getRoomName() {
		return roomName;
	}
	public void setRoomName(String roomName) {
		this.roomName = roomName;
	}
	public int getSize() {
		return size;
	}
	public void setSize(int size) {
		this.size = size;
	}
	public String getUid() {
		return Uid;
	}
	public void setUid(String uid) {
		Uid = uid;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}

	
}
