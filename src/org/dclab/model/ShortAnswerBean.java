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
	private int number;
	private String content;//题干
	private boolean ifCheck;//检查标记
	private String answer;//考生答案
	private int shortNum;//存储简答题数目
	private String img;//存储题目中可能存在的图片的地址
	private String audio;//存储题目中可能存在的音频的地址
	private String video;//存储题目中可能存在的视频的地址
	private boolean showPdf;
	private String pdf;
	
	public ShortAnswerBean(){}
	
	public ShortAnswerBean(int id,int number,int shortNum,String content){
		this.id		=	id;
		this.number	=	number;
		this.content=	content;
		this.shortNum=	shortNum;
	}
	
	@Override
	public Object clone(){
		
		return new ShortAnswerBean(this.id,this.number,this.shortNum, this.content);
	}
	
	
	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public boolean isShowPdf() {
		return showPdf;
	}

	public void setShowPdf(boolean showPdf) {
		this.showPdf = showPdf;
	}

	public String getPdf() {
		return pdf;
	}

	public void setPdf(String pdf) {
		this.pdf = pdf;
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

	public int getShortNum() {
		return shortNum;
	}
	public void setShortNum(int shortNum) {
		this.shortNum = shortNum;
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
