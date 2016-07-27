package org.dclab.model;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class RequestBean {
	private UUID token;
	private Integer typeId;
	private Integer id;
	private List<Integer> choiceId;
	private Map<Integer, Integer> choiceIdMap;
	private boolean ifCheck;
	
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
	public Integer getTypeId() {
		return typeId;
	}
	public void setTypeId(Integer typeId) {
		this.typeId = typeId;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public List<Integer> getChoiceIdList() {
		return choiceId;
	}
	public void setChoiceIdList(List<Integer> choiceIdList) {
		this.choiceId = choiceIdList;
	}
	public Map<Integer, Integer> getChoiceIdMap() {
		return choiceIdMap;
	}
	public void setChoiceIdMap(Map<Integer, Integer> choiceIdMap) {
		this.choiceIdMap = choiceIdMap;
	}
	
	
}
