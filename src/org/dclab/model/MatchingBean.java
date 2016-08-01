package org.dclab.model;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 匹配题对象：
 * 1.题目内容及id；
 * 2.是否需要检查；
 * 3.包含考生答案id的list
 * @author alvis
 *
 */
public class MatchingBean implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1445501220162322852L;
	private int matchNum;//匹配题数目
	private int id;//整个题目的id
	private List<ContentBean> contentList;//题目的内容和id
	private boolean ifCheck;//是否需要检查
	private Map<Integer, Integer> choiceIdMap;//考生答案id的map
	private List<ChoicesBean> choiceList;//选项的内容和id的list
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
	public int getMatchNum() {
		return matchNum;
	}
	public void setMatchNum(int matchNum) {
		this.matchNum = matchNum;
	}
	public int getId() {
		return id;
	}
	public List<ChoicesBean> getChoiceList() {
		return choiceList;
	}
	public void setChoiceList(List<ChoicesBean> choiceList) {
		this.choiceList = choiceList;
	}
	public List<ContentBean> getContentList() {
		return contentList;
	}
	public void setContentList(List<ContentBean> contentList) {
		this.contentList = contentList;
	}
	public void setId(int id) {
		this.id = id;
	}
	public boolean isIfCheck() {
		return ifCheck;
	}
	public void setIfCheck(boolean ifCheck) {
		this.ifCheck = ifCheck;
	}
	public Map<Integer, Integer> getChoiceIdMap() {
		return choiceIdMap;
	}
	public void setChoiceIdMap(Map<Integer, Integer> choiceIdmap) {
		this.choiceIdMap = choiceIdmap;
	}
	
}

