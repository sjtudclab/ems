package org.dclab.model;

import java.io.Serializable;
import java.util.List;

/**
 * 试题信息类
 * 1. 考生考卷的内容
 * 2. 考生答题的情况
 * 3. 记录上一次修改的时间，便于中断后恢复
 * 
 * @author zhaoz
 *
 */
public class ExamBean implements Serializable{
	private static final long	serialVersionUID	= 15493198966L;
	private long lastModifiedTime;
	private List<SingleChoiceBean> singleChoiceList;
	private List<MultiChoicesBean> multiChoicesList;
	private List<MatchingBean> matchingList;
	private List<JudgementBean> judgementList;
	
	public ExamBean(){}

	//get single choice by Id
	public SingleChoiceBean getSingleChoiceById(int index){
		return singleChoiceList.get(index);
	}
	
	public MultiChoicesBean getMultiChoiceById(int index){
		return multiChoicesList.get(index);
	}
	
	public MatchingBean getMatchingById(int index){
		return matchingList.get(index);
	}
	
	public JudgementBean getJudgementById(int index){
		return judgementList.get(index);
	}

	public long getLastModifiedTime() {
		return lastModifiedTime;
	}
	public void setLastModifiedTime(long lastModifiedTime) {
		this.lastModifiedTime = lastModifiedTime;
	}
	public List<SingleChoiceBean> getSingleChoiceList() {
		return singleChoiceList;
	}
	public void setSingleChoiceList(List<SingleChoiceBean> singleChoiceList) {
		this.singleChoiceList = singleChoiceList;
	}
	public List<MultiChoicesBean> getMultiChoicesList() {
		return multiChoicesList;
	}
	public void setMultiChoicesList(List<MultiChoicesBean> multiChoicesList) {
		this.multiChoicesList = multiChoicesList;
	}
	public List<MatchingBean> getMatchingList() {
		return matchingList;
	}
	public void setMatchingList(List<MatchingBean> matchingList) {
		this.matchingList = matchingList;
	}
	public List<JudgementBean> getJudgementList() {
		return judgementList;
	}
	public void setJudgementList(List<JudgementBean> judgementList) {
		this.judgementList = judgementList;
	}
	
}
