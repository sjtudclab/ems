package org.dclab.model;

import java.io.Serializable;

public class MachineTestBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3749552251308834763L;
	private int id;
	private int number;
	private String content;
	private String fileName;//考生上传的文件名
	private boolean ifCheck;
	private int machineNum;
	private String img;//存储题目中可能存在的图片的地址
	private String audio;//存储题目中可能存在的音频的地址
	private String video;//存储题目中可能存在的视频的地址
	private boolean showPdf;
	private String pdf;
	
	
	
	public MachineTestBean() {
		super();
	}
	public MachineTestBean(int id,int number, String content, int machineNum, String img, String audio, String video,boolean showPdf,String pdf) {
		super();
		this.id = id;
		this.number	=	number;
		this.content = content;
		this.machineNum = machineNum;
		this.img = img;
		this.audio = audio;
		this.video = video;
		this.showPdf=showPdf;
		this.pdf=pdf;
	}
	@Override
	protected Object clone() {
		// TODO Auto-generated method stub
		return new MachineTestBean(this.id,this.number, this.content, this.machineNum, this.img, this.audio, this.video, this.showPdf,this.pdf);
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
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public boolean isIfCheck() {
		return ifCheck;
	}
	public void setIfCheck(boolean ifCheck) {
		this.ifCheck = ifCheck;
	}

	public int getMachineNum() {
		return machineNum;
	}
	public void setMachineNum(int machineNum) {
		this.machineNum = machineNum;
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
