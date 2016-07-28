package org.dclab.model;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class RequestBean {
	private UUID token;
	private int typeId;
	private int id;
	private int requestId;
	private int choiceId;
	private List<Integer> choiceIdList;
	private String choiceIdMap;
	private boolean ifCheck;
	
	
	public int getRequestId() {
		return requestId;
	}
	public void setRequestId(int requestId) {
		this.requestId = requestId;
	}
	public int getChoiceId() {
		return choiceId;
	}
	public void setChoiceId(int choiceId) {
		this.choiceId = choiceId;
	}
	public boolean isIfCheck() {
		return ifCheck;
	}
	public void setIfCheck(boolean ifCheck) {
		this.ifCheck = ifCheck;
	}
	public UUID getToken() {
		return token;
	}
	public void setToken(UUID token) {
		this.token = token;
	}
	public int getTypeId() {
		return typeId;
	}
	public void setTypeId(int typeId) {
		this.typeId = typeId;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public List<Integer> getChoiceIdList() {
		return choiceIdList;
	}
	public void setChoiceIdList(List<Integer> choiceIdList) {
		this.choiceIdList = choiceIdList;
	}
	public String getChoiceIdMap() {
		return choiceIdMap;
	}
	public void setChoiceIdMap(String choiceIdMap) {
		this.choiceIdMap = choiceIdMap;
	}
	
	
}
