package org.dclab.model;

import java.util.List;
import java.util.UUID;

/**
 * @author alvis
 *考虑到监考操作误操作时可能传回来空的uidList，所以新建此bean用来接收请求参数
 */
public class SuperRequest {
	private UUID token;
	private List<String> uidList;
	private int delayTime;
	
	
	public UUID getToken() {
		return token;
	}
	public void setToken(UUID token) {
		this.token = token;
	}
	public List<String> getUidList() {
		return uidList;
	}
	public void setUidList(List<String> uidList) {
		this.uidList = uidList;
	}
	public int getDelayTime() {
		return delayTime;
	}
	public void setDelayTime(int delayTime) {
		this.delayTime = delayTime;
	}
	
	
}
