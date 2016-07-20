package org.dclab.model;

import java.io.Serializable;

/**
 * 单选题对象：
 * 1. 题干ID, 内容 (参考数据库表)
 * 2. 是否需要检查
 * 3. answer id
 * @author alvis
 *
 */

public class SingleChoiceBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7633906860972374279L;
	private int id;//题目id
	private String content;//题目内容
	private boolean ifCheck;//是否需要检查
	private int choiceId;//考生答案id
	private String choiceA;
	private int choiceAId;
	private String choiceB;
	private int choiceBId;
	private String choiceC;
	private int choiceCId;
	private String choiceD;
	private int choiceDId;
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
