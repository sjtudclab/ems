package org.dclab.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class FillBlankBean implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -4829732312526075401L;
	private int id;//题目id
	private int number;
	private String content;//题干
	private boolean ifCheck;//检查标记
	private int fillNum;//需要填的空的个数
	private List<String> answerList;
	private int GapNum;//存储简答题数目
	private String img;//存储题目中可能存在的图片的地址
	private String audio;//存储题目中可能存在的音频的地址
	private String video;//存储题目中可能存在的视频的地址
	private boolean showPdf;
	private String pdf;
	
	
	
	public FillBlankBean() {
		super();
	}
	public FillBlankBean(int id,int number, String content, int fillNum, int gapNum, String img, String audio, String video, boolean showPdf,String pdf) {
		super();
		this.id = id;
		this.number = number;
		this.content = content;
		this.fillNum = fillNum;
		GapNum = gapNum;
		this.img = img;
		this.audio = audio;
		this.video = video;
		this.showPdf=showPdf;
		this.pdf=pdf;
	}
	@Override
	protected Object clone(){
		// TODO Auto-generated method stub
		return new FillBlankBean(this.id, this.number,this.content, this.fillNum, this.GapNum, this.img, this.audio, this.video,this.showPdf,this.pdf);
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
	public List<String> getAnswerList() {
		return answerList;
	}
	public void setAnswerList(List<String> answerList) {
		this.answerList = answerList;
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
	public int getFillNum() {
		return fillNum;
	}
	public void setFillNum(int fillNum) {
		this.fillNum = fillNum;
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
