package org.dclab.model;

import java.io.Serializable;
import java.util.*;

import org.junit.experimental.theories.Theories;

/**
 * 多选题对象：
 * 1.题干内容以及id。
 * 2.包含考生答案id的list。
 * 3.是否需要检查
 * @author alvis
 *
 */
public class MultiChoicesBean implements Serializable, Cloneable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6078271522167915749L;
	public int multiNum;//存储多选题数目
	private int id;//题目id
	private int number;//题目序号
	private String content;//题目内容
	private boolean ifCheck;//是否需要检查
	private List<Integer> choiceIdList;//考生答案id的list
	private List<ChoicesBean> choiceList;//题目选项的内容和id的list
	private String img;//存储题目中可能存在的图片的地址
	private String audio;//存储题目中可能存在的音频的地址
	private String video;//存储题目中可能存在的视频的地址
	
	public MultiChoicesBean(){}

	public MultiChoicesBean(int id, int number,int multiNum, String content, List<ChoicesBean> choices, String img, String audio, String video){
		this.id		=	id;
		this.number = number;
		this.img	=	img;
		this.audio	=	audio;
		this.video	=	video;
		this.content	= 	content;
		this.choiceList	=	choices;
		this.choiceIdList	=	new ArrayList<Integer>();	//init
		this.multiNum = multiNum;
	}
	
	@Override
	public Object clone(){
		return new MultiChoicesBean(this.id,this.number,this.multiNum, this.content, this.choiceList, this.img, this.audio, this.video);
	}
	
	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
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
	public int getMultiNum() {
		return multiNum;
	}
	public void setMultiNum(int multiNum) {
		this.multiNum = multiNum;
	}
	public List<Integer> getChoiceIdList() {
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

	@Override
	public String toString() {
		return "MultiChoicesBean [id=" + id + ", content=" + content + "]";
	}

	
}

