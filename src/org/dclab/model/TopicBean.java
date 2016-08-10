package org.dclab.model;

/**
 * @author alvis
 *和数据库中的topic表对应的实体类
 *主要是用于admin插入topic的操作
 */
public class TopicBean {
	private int id;
	private int num;
	private String content;
	private int typeId;
	private String img;
	private String audio;
	private String video;
	private int subjectId;
	
	
	public TopicBean(String content, int typdId, int subjectId) {
		super();
		this.content = content;
		this.typeId = typdId;
		this.subjectId = subjectId;
	}
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getNum() {
		return num;
	}
	public void setNum(int num) {
		this.num = num;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public int getTypeId() {
		return typeId;
	}
	public void setTypeId(int typdId) {
		this.typeId = typdId;
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
	public int getSubjectId() {
		return subjectId;
	}
	public void setSubjectId(int subjectId) {
		this.subjectId = subjectId;
	}
	
	
	
}
