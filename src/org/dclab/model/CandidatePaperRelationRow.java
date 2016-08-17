package org.dclab.model;

public class CandidatePaperRelationRow {
	private String Uid;
	private String Uname;
	private String gender;
	private String Cid;
	private String photo;
	private int proId;
	private int subId;
	private int paperId;	//paperId in DB
	
	public CandidatePaperRelationRow() {
	}

	public String getUid() {
		return Uid;
	}

	public void setUid(String string) {
		Uid = string;
	}

	public String getUname() {
		return Uname;
	}

	public void setUname(String uname) {
		Uname = uname;
	}

	public String getCid() {
		return Cid;
	}

	public void setCid(String cid) {
		Cid = cid;
	}

	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}

	public int getProId() {
		return proId;
	}

	public void setProId(int proId) {
		this.proId = proId;
	}

	public int getSubId() {
		return subId;
	}

	public void setSubId(int subId) {
		this.subId = subId;
	}

	public int getPaperId() {
		return paperId;
	}

	public void setPaperId(int paperId) {
		this.paperId = paperId;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

}
