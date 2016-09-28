package org.dclab.model;

import java.util.ArrayList;
import java.util.List;

public class SubjectRow {
	private int paperId;
	public String proName;
	public String proId;
	public String subName;
	public String subId;
	public String paperNum;	//试卷编号
	public int duration;
	public int earliestSubmit;
	public int latestLogin;
	public int showMark;	//1 yes, 0 no
	
	@Override
	public int hashCode() {

		return paperNum.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SubjectRow other = (SubjectRow) obj;
		//only when major id, subject id and paper NO are the same, then they equals
		if (proId.equals(other.getProId()) && subId.equals(other.getSubId()) && paperNum.equals(other.getPaperNum())) {
			return true;
		}
		return false;
	}
	
	public List<Object> toList(){
		List<Object> list = new ArrayList<Object>();
		list.add(this.proName);
		list.add(this.proId);
		list.add(this.subName);
		list.add(this.subId);
		list.add(this.paperNum);
		list.add(this.duration);
		list.add(this.earliestSubmit);
		list.add(this.latestLogin);
		list.add(this.showMark);
		return list;
	}
	

	@Override
	public String toString() {
		return "SubjectRow [paperId=" + paperId + ", proName=" + proName + ", proId=" + proId + ", subName=" + subName
				+ ", subId=" + subId + ", paperNum=" + paperNum + ", duration=" + duration + ", earliestSubmit="
				+ earliestSubmit + ", latestLogin=" + latestLogin + ", showMark=" + showMark + "]";
	}

	public int getPaperId() {
		return paperId;
	}

	public void setPaperId(int paperId) {
		this.paperId = paperId;
	}

	public SubjectRow() {
		super();
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
