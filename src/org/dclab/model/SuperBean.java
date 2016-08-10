package org.dclab.model;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
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
	private String name;
	private Map<Integer, CandidateBean> canMap;//该考场的考生信息list
	private List<AuthorityBean> authorityList;//权限列表
	private List<Integer> freeSeatList;//考场空闲座位list
	private UUID token;
	private int Rid;//角色标识
	private int sign;//检测监考老师是否登录的标志
	
	
	
	
	public int getSign() {
		return sign;
	}
	public void setSign(int sign) {
		this.sign = sign;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Map<Integer, CandidateBean> getCanMap() {
		return canMap;
	}
	public void setCanMap(Map<Integer, CandidateBean> canMap) {
		this.canMap = canMap;
	}
	public List<Integer> getFreeSeatList() {
		return freeSeatList;
	}
	public void setFreeSeatList(List<Integer> freeSeatList) {
		this.freeSeatList = freeSeatList;
	}
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
	public int getRoomId() {
		return roomId;
	}
	public void setRoomId(int roomId) {
		this.roomId = roomId;
	}

	
	
}
