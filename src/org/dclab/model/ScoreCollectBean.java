package org.dclab.model;

/**
 * @author alvis
 *成绩汇总文件需要的数据
 */
public class ScoreCollectBean {
	private String Uid;
	private String subName;
	private int mark;
	
	
	
	public ScoreCollectBean(String uid, String subName, int mark) {
		super();
		Uid = uid;
		this.subName = subName;
		this.mark = mark;
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
