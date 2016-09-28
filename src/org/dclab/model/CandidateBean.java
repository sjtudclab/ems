package org.dclab.model;

/**
 * @author alvis
 *为了给每个监考老师装载所监考教室的考生的名单创建的bean
 *监考那个界面显示的学生信息列表
 */
public class CandidateBean {
	private String roomName;//房间号
	private int seatNum;//座位号
	private String Uname;//姓名
	private String gender;//性别
	private String Uid;//准考证号
	private String Cid;//身份证号
	private int status;//考生状态
	private long finishTime;//考试结束时间
	
		
	public long getFinishTime() {
		return finishTime;
	}
	public void setFinishTime(long finishTime) {
		this.finishTime = finishTime;
	}
	public String getRoomName() {
		return roomName;
	}
	public void setRoomName(String roomName) {
		this.roomName = roomName;
	}
	public int getSeatNum() {
		return seatNum;
	}
	public void setSeatNum(int seatNum) {
		this.seatNum = seatNum;
	}
	public String getUname() {
		return Uname;
	}
	public void setUname(String uname) {
		Uname = uname;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getUid() {
		return Uid;
	}
	public void setUid(String uid) {
		Uid = uid;
	}
	public String getCid() {
		return Cid;
	}
	public void setCid(String cid) {
		Cid = cid;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	
	
}
