package org.dclab.model;

import java.io.Serializable;

/**
 * 判断题对象：
 * 1. 题干ID, 内容 ；
 * 2. 是否需要检查；
 * 3. 考生答案id。
 * @author alvis
 *
 */
public class JudgementBean implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1233847995962873452L;
	private int id;//题目id
	private String content;//题目内容
	private boolean ifCheck;//是否需要检查
	private int choiceId;//考生答案id
	
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
	public int getChoiceId() {
		return choiceId;
	}
	public void setChoiceId(int choiceId) {
		this.choiceId = choiceId;
	}
	
	
}
