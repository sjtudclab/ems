package org.dclab.model;

/**
 * Abstract of topic line in excel file
 * 
 * @author zhaoz
 *
 */
public abstract class TopicRow {
	protected int id;
	protected byte TYPE;
	protected int paperId;
	protected int number;
	protected int fullMark;	//full mark
	protected String content;
	protected String img;
	protected String audio;
	protected String video;
	
	public TopicRow(){}
	public TopicRow(int paperId, byte type){
		this.paperId = paperId;
		this.TYPE 	= type;
	}
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public byte getTYPE() {
		return TYPE;
	}
	public void setTYPE(byte tYPE) {
		TYPE = tYPE;
	}
	public int getPaperId() {
		return paperId;
	}
	public void setPaperId(int paperId) {
		this.paperId = paperId;
	}
	public int getNumber() {
		return number;
	}
	public void setNumber(int number) {
		this.number = number;
	}
	public int getFullMark() {
		return fullMark;
	}
	public void setFullMark(int fullMark) {
		this.fullMark = fullMark;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
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
