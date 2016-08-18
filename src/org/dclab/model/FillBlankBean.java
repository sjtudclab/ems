package org.dclab.model;

import java.io.Serializable;
import java.util.List;

public class FillBlankBean implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -4829732312526075401L;
	private int id;//题目id
	private String content;//题干
	private boolean ifCheck;//检查标记
	private int fillNum;//需要填的空的个数
	private List<String> choiceIdList;
	private int GapNum;//存储简答题数目
	private String img;//存储题目中可能存在的图片的地址
	private String audio;//存储题目中可能存在的音频的地址
	private String video;//存储题目中可能存在的视频的地址
	
	
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
	public int getFillNum() {
		return fillNum;
	}
	public void setFillNum(int fillNum) {
		this.fillNum = fillNum;
	}
	public List<String> getChoiceIdList() {
		return choiceIdList;
	}
	public void setChoiceIdList(List<String> choiceIdList) {
		this.choiceIdList = choiceIdList;
	}
	public int getGapNum() {
		return GapNum;
	}
	public void setGapNum(int gapNum) {
		GapNum = gapNum;
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
	public String getVideo() {
		return video;
	}
	public void setVideo(String video) {
		this.video = video;
	}
	
	
}
