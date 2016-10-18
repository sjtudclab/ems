package org.dclab.model;

public class PaperInfoBean {
	private int paperId;
	private String proName;
	private String proId;
	private String subName;
	private String subId;
	private String paperNum;
	private int totalScore;//总分
	private String typeNumScore;//各题型题数与总分
	
	
	
	public int getPaperId() {
		return paperId;
	}
	public void setPaperId(int paperId) {
		this.paperId = paperId;
	}
	public String getProName() {
		return proName;
	}
	public void setProName(String proName) {
		this.proName = proName;
	}
	public String getProId() {
		return proId;
	}
	public void setProId(String proId) {
		this.proId = proId;
	}
	public String getSubName() {
		return subName;
	}
	public void setSubName(String subName) {
		this.subName = subName;
	}
	public String getSubId() {
		return subId;
	}
	public void setSubId(String subId) {
		this.subId = subId;
	}
	public String getPaperNum() {
		return paperNum;
	}
	public void setPaperNum(String paperNum) {
		this.paperNum = paperNum;
	}
	public int getTotalScore() {
		return totalScore;
	}
	public void setTotalScore(int totalScore) {
		this.totalScore = totalScore;
	}
	public String getTypeNumScore() {
		return typeNumScore;
	}
	public void setTypeNumScore(String typeNumScore) {
		this.typeNumScore = typeNumScore;
	}
	
	
	
}
