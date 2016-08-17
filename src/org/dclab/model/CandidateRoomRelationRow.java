package org.dclab.model;

import java.sql.Timestamp;

public class CandidateRoomRelationRow {
	private String roomName;
	private Timestamp startTime;
	private int seatNum;
	private String ip;
	private String Uid;
	
	public CandidateRoomRelationRow() {
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

	public int getSeatNum() {
		return seatNum;
	}

	public void setSeatNum(int seatNum) {
		this.seatNum = seatNum;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getUid() {
		return Uid;
	}

	public void setUid(String uid) {
		Uid = uid;
	}

	@Override
	public String toString() {
		return "CandidateRoomRelationRow [roomName=" + roomName + ", startTime=" + startTime + ", seatNum=" + seatNum
				+ ", ip=" + ip + ", Uid=" + Uid + "]";
	}

}
