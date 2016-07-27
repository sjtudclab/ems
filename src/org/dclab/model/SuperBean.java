package org.dclab.model;

import java.io.Serializable;
import java.util.List;

import org.dclab.User;

/**
 * @author alvis
 *监考信息类
 *包括考场号，考场考生的一些信息
 */
public class SuperBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8957618769067770034L;
	private int roomId;//考场号
	private List<User> userList;//该考场的考生list,这里的user对象还需考虑，注意！！！！！
	private List<Integer> uidSeatList;//考场考生的准考证号list，下标加一就是座位号。如果座位有空可以用map。
	public int getRoomId() {
		return roomId;
	}
	public void setRoomId(int roomId) {
		this.roomId = roomId;
	}
	
	public List<Integer> getUidSeatList() {
		return uidSeatList;
	}
	public void setUidSeatList(List<Integer> uidSeatList) {
		this.uidSeatList = uidSeatList;
	}
	public List<User> getUserList() {
		return userList;
	}
	public void setUserList(List<User> userList) {
		this.userList = userList;
	}
	
	
}
