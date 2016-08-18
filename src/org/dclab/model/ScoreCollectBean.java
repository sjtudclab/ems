package org.dclab.model;

/**
 * @author alvis
 *成绩汇总文件需要的数据
 */
public class ScoreCollectBean {
	private String proName;
	private String paperNum;
	private String Uname;
	private String Uid;
	private String subName;
	private int mark;
	
	
	
	
	public String getProName() {
		return proName;
	}
	public void setProName(String proName) {
		this.proName = proName;
	}
	public String getPaperNum() {
		return paperNum;
	}
	public void setPaperNum(String paperNum) {
		this.paperNum = paperNum;
	}
	public String getUname() {
		return Uname;
	}
	public void setUname(String uname) {
		Uname = uname;
	}
	public String getUid() {
		return Uid;
	}
	public void setUid(String uid) {
		Uid = uid;
	}
	public String getSubName() {
		return subName;
	}
	public void setSubName(String subName) {
		this.subName = subName;
	}
	public int getMark() {
		return mark;
	}
	public void setMark(int mark) {
		this.mark = mark;
	}
	@Override
	public String toString() {
		return "ScoreCollectBean [Uid=" + Uid + ", subName=" + subName + ", mark=" + mark + "]";
	}
	
	
}
