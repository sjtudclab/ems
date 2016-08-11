package org.dclab.model;

import java.io.Serializable;

/**
 * @author alvis
 *简答题
 */
public class ShortAnswerBean implements Serializable, Cloneable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5347641227372398886L;
	private int id;//题目id
	private String content;//题干
	private boolean ifCheck;//检查标记
	private String answer;//考生答案
	private static int shortNum;//存储简答题数目
	
	public ShortAnswerBean(){}
	
	public ShortAnswerBean(int id, String content){
		this.id		=	id;
		this.content=	content;
		
	}
	
	@Override
	public Object clone(){
		
		return new ShortAnswerBean(this.id, this.content);
	}
	public static int getShortNum() {
		return shortNum;
	}
	public static void setShortNum(int shortNum) {
		ShortAnswerBean.shortNum = shortNum;
	}
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
