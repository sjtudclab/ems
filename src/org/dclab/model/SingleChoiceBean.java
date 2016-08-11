package org.dclab.model;

import java.io.Serializable;
import java.util.List;

/**
 * 单选题对象：
 * 1. 题干ID, 内容 (参考数据库表)
 * 2. 是否需要检查
 * 3. answer id
 * @author alvis
 *
 */

public class SingleChoiceBean implements Serializable,Cloneable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7633906860972374279L;
	private int singleNum;//存储单选题数目
	private int id;//题目id
	private String content;//题目内容
	private boolean ifCheck;//是否需要检查
	private int choiceId;//考生答案id
	private List<ChoicesBean> choiceList;//选项的内容和id
	private String img;
	private String audio;
	private String video;
	
	public SingleChoiceBean() {
		// TODO Auto-generated constructor stub
		
	}
	

	@Override
	public Object clone()  {
		// TODO Auto-generated method stub
		return new SingleChoiceBean();
	}
	
	
	
	public String getVideo() {
		return video;
	}
	public void setVideo(String vedio) {
		this.video = vedio;
	}
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
	public int getSingleNum() {
		return singleNum;
	}
	public void setSingleNum(int singleNum) {
		this.singleNum = singleNum;
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
	@Override
	public String toString() {
		return "SingleChoiceBean [id=" + id +  ", choiceId=" + choiceId  + "]";
	}
	

}

