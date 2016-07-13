package org.dclab.model;

import java.io.Serializable;
import java.util.*;

/**
 * 多选题对象：
 * 1.题干内容以及id。
 * 2.包含考生答案id的list。
 * 3.是否需要检查
 * @author alvis
 *
 */
public class MultiChoicesBean implements Serializable {
	
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
	public List<?> getChoiceId() {
		return choiceIdList;
	}
	public void setChoiceId(List<?> choiceId) {
		this.choiceIdList = choiceId;
	}
	
	
}
