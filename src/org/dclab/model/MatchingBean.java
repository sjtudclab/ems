package org.dclab.model;

import java.io.Serializable;
import java.util.List;

/**
 * 匹配题对象：
 * 1.题目内容及id；
 * 2.是否需要检查；
 * 3.包含考生答案id的list
 * @author alvis
 *
 */
public class MatchingBean implements Serializable {
	private int id;//题目id
	private String content;//题目内容
	private boolean ifCheck;//是否需要检查
	private List<?> choiceIdList;//考生答案id的list
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public boolean isIfCheck() {
		return ifCheck;
	}
	public void setIfCheck(boolean ifCheck) {
		this.ifCheck = ifCheck;
	}
	public List<?> getChoiceIdList() {
		return choiceIdList;
	}
	public void setChoiceIdList(List<?> choiceIdList) {
		this.choiceIdList = choiceIdList;
	}
	
}
