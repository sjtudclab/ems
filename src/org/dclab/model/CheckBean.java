package org.dclab.model;

/**
 * @author alvis
 *这个bean主要用在检查页面，id为题目id，status为题目状态（1为已作答且不需要检查，2为检查，3为未做答）
 */
public class CheckBean {
	private int id;
	private int status;
	
	public CheckBean(int i,int s){
		id=i;
		status=s;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}
}
