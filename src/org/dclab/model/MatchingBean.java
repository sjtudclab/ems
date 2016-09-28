package org.dclab.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sun.org.apache.xerces.internal.impl.xpath.regex.Match;

/**
 * 匹配题对象：
 * 1.题目内容及id；
 * 2.是否需要检查；
 * 3.包含考生答案id的list
 * @author alvis
 *
 */
public class MatchingBean implements Serializable, Cloneable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1445501220162322852L;
	private int matchNum;//匹配题数目
	private int id;//整个题目的id
	private int number;
	private String content;//题目的主题干
	private List<ContentBean> contentList;//题目的内容和id
	private boolean ifCheck;//是否需要检查
	private Map<Integer, Integer> choiceIdMap;//考生答案id的map
	private List<ChoicesBean> choiceList;//选项的内容和id的list
	private String img;//存储题目中可能存在的图片的地址
	private String audio;//存储题目中可能存在的音频的地址
	private String video;//存储题目中可能存在的视频的地址
	
	public MatchingBean(){}
	
	public MatchingBean(int id,int number,int matchNum, List<ContentBean> contents, List<ChoicesBean> choices, String img, String audio, String video){
		this.id				=	id;
		this.number 		= 	number;
		this.contentList	=	contents;
		this.choiceList		=	choices;
		this.img			=	img;
		this.audio			=	audio;
		this.video			=	video;
		this.choiceIdMap	= 	new HashMap<Integer, Integer>();
		this.matchNum      	= 	matchNum;
		
	}
	
	@Override
	public Object clone() {
		return new MatchingBean(this.id,this.number,this.matchNum, this.contentList, this.choiceList, this.img, this.audio, this.video);
	}
	
	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
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

