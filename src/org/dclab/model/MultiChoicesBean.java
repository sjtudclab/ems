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
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6078271522167915749L;
	private static int multiNum;//存储多选题数目
	private int id;//题目id
	private String content;//题目内容
	private boolean ifCheck;//是否需要检查
	private List<Integer> choiceIdList;//考生答案id的list
	private List<ChoicesBean> choiceList;//题目选项的内容和id的list
	
	
	public static int getMultiNum() {
		return multiNum;
	}
	public static void setMultiNum(int multiNum) {
		MultiChoicesBean.multiNum = multiNum;
	}
	public List<?> getChoiceIdList() {
		return choiceIdList;
	}
	public void setChoiceIdList(List<Integer> choiceIdList) {
		this.choiceIdList = choiceIdList;
	}
	public List<ChoicesBean> getChoiceList() {
		return choiceList;
	}
	public void setChoiceList(List<ChoicesBean> choiceList) {
		this.choiceList = choiceList;
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

	
}

