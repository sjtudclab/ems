package org.dclab.model;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

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
	private List<CandidateBean> canList;//该考场的考生信息list
	private List<AuthorityBean> authorityList;//权限列表
	private UUID token;
	private int Rid;//角色标识
	
	
	
	public int getRid() {
		return Rid;
	}
	public void setRid(int rid) {
		Rid = rid;
	}
	public List<AuthorityBean> getAuthorityList() {
		return authorityList;
	}
	public void setAuthorityList(List<AuthorityBean> authorityList) {
		this.authorityList = authorityList;
	}
	public UUID getToken() {
		return token;
	}
	public void setToken(UUID token) {
		this.token = token;
	}
	public List<CandidateBean> getCanList() {
		return canList;
	}
	public void setCanList(List<CandidateBean> canList) {
		this.canList = canList;
	}
	public int getRoomId() {
		return roomId;
	}
	public void setRoomId(int roomId) {
		this.roomId = roomId;
	}

	
	
}
