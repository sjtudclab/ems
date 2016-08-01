package org.dclab.model;

import java.awt.Choice;
import java.io.Serializable;
import java.util.List;

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
	private int judgeNum;//存储判断题数目
	private int id;//题目id(数据库中id)
	private String content;//题目内容
	private boolean ifCheck;//是否需要检查
	private int choiceId;//考生答案id
	private List<ChoicesBean> choiceList;//题目的内容和id的list
	private String img;//存储题目中可能存在的图片的地址
	private String audio;//存储题目中可能存在的音频的地址
	
	
	
	public String getImg() {
		return img;
	}
	public void setImg(String img) {
		this.img = img;
	}
	public String getAudio() {
		return audio;
	}
	public void setAudio(String audio) {
		this.audio = audio;
	}
	public int getJudgeNum() {
		return judgeNum;
	}
	public void setJudgeNum(int judgeNum) {
		this.judgeNum = judgeNum;
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
	public int getChoiceId() {
		return choiceId;
	}
	public void setChoiceId(int choiceId) {
		this.choiceId = choiceId;
	}
	public List<ChoicesBean> getChoiceList() {
		return choiceList;
	}
	public void setChoiceList(List<ChoicesBean> choiceList) {
		this.choiceList = choiceList;
	}
	
	
}

