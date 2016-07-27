package org.dclab.model;

import java.io.Serializable;

/**
 * @author alvis
 *简答题
 */
public class shortAnswerBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5347641227372398886L;
	private int id;//题目id
	private String content;//题干
	private boolean ifCheck;//检查标记
	private String answer;//考生答案
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
	public String getAnswer() {
		return answer;
	}
	public void setAnswer(String answer) {
		this.answer = answer;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	

}
