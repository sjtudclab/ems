package org.dclab.model;

public class SubjectRow {
	private String proName;
	private int proId;
	private String subName;
	private int subId;
	private int paperNum;	//试卷编号
	private int duration;
	private int earliestSubmit;
	private int latestLogin;
	private int showMark;	//1 yes, 0 no
	
	public SubjectRow() {
		super();
	}

	public String getProName() {
		return proName;
	}

	public void setProName(String proName) {
		this.proName = proName;
	}

	public int getProId() {
		return proId;
	}

	public void setProId(int proId) {
		this.proId = proId;
	}

	public String getSubName() {
		return subName;
	}

	public void setSubName(String subName) {
		this.subName = subName;
	}

	public int getSubId() {
		return subId;
	}

	public void setSubId(int subId) {
		this.subId = subId;
	}

	public int getPaperNum() {
		return paperNum;
	}

	public void setPaperNum(int paperNum) {
		this.paperNum = paperNum;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public int getEarliestSubmit() {
		return earliestSubmit;
	}

	public void setEarliestSubmit(int earliestSubmit) {
		this.earliestSubmit = earliestSubmit;
	}

	public int getLatestLogin() {
		return latestLogin;
	}

	public void setLatestLogin(int latestLogin) {
		this.latestLogin = latestLogin;
	}

	public int getShowMark() {
		return showMark;
	}

	public void setShowMark(int showMark) {
		this.showMark = showMark;
	}
	
	
}
