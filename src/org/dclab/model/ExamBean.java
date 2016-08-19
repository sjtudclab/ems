package org.dclab.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
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
	private static final long	serialVersionUID= 15493198966L;
	private int EXAM_TIME;	//整场考试的时长，记得加载时初始化 
	private int earliestSubmit;//最早可交卷时间
	//剩余时长, 装填试卷时将其初始化，每次写log时，获取前台剩余时间，重新setDuration，延时或者减少时间时直接修改这个值
	private int extraTime;	//额外添加的时间，用于延时操作
	private long startTime;//该考生开始考试的时间
	private String uid; //用户准考证号
	private int sid;//考试科目id
	private int paperId;//考生所考试卷的试卷号
	private int mark;//考生的成绩
	
	private int topicNum;
	private HashSet<Integer> finishTopic;
	
	private boolean ifLogin;//考生是否登录了，主要是用在监考教师的界面显示上

	private boolean allowStart;//任何时间开始
	private boolean allowTerminate;//任何时间终止
	private boolean isFinished = false; //是否已交卷
	
	private long lastModifiedTime;
	private List<SingleChoiceBean> singleChoiceList;
	private List<MultiChoicesBean> multiChoicesList;
	private List<MatchingBean> matchingList;
	private List<JudgementBean> judgementList;
	private List<ShortAnswerBean> shortAnswerList;
	private List<FillBlankBean> fillBlankList;
	private List<MachineTestBean> machineList;
	
	

	
	public ExamBean() {
		super();
	}


	public int getPaperId() {
		return paperId;
	}

	public void setPaperId(int paperId) {
		this.paperId = paperId;
	}

	public int getEarliestSubmit() {
		return earliestSubmit;
	}


	public void setEarliestSubmit(int earliestSubmit) {
		this.earliestSubmit = earliestSubmit;
	}


	public int getMark() {
		return mark;
	}


	public void setMark(int mark) {
		this.mark = mark;
	}


	public List<ShortAnswerBean> getShortAnswerList() {
		return shortAnswerList;
	}


	public void setShortAnswerList(List<ShortAnswerBean> shortAnswerList) {
		this.shortAnswerList = shortAnswerList;
	}


	
	public HashSet<Integer> getFinishTopic() {
		return finishTopic;
	}


	public void setFinishTopic(HashSet<Integer> finishTopic) {
		this.finishTopic = finishTopic;
	}


	public int getTopicNum() {
		return topicNum;
	}


	public void setTopicNum(int topicNum) {
		this.topicNum = topicNum;
	}

	public int getEXAM_TIME() {
		return EXAM_TIME;
	}


	public void setEXAM_TIME(int eXAM_TIME) {
		this.EXAM_TIME = eXAM_TIME;
	}


	public long getStartTime() {
		return startTime;
	}


	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}


	public boolean isIfLogin() {
		return ifLogin;
	}


	public void setIfLogin(boolean ifLogin) {
		this.ifLogin = ifLogin;
	}


	public int getSid() {
		return sid;
	}


	public void setSid(int sid) {
		this.sid = sid;
	}


	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}
	public boolean isAllowStart() {
		return allowStart;
	}

	public void setAllowStart(boolean allowStart) {
		this.allowStart = allowStart;
	}

	public boolean isAllowTerminate() {
		return allowTerminate;
	}

	public void setAllowTerminate(boolean allowTerminate) {
		this.allowTerminate = allowTerminate;
	}
	
	public FillBlankBean getFillBlankById(int index){
		return fillBlankList.get(index);
	}
	public MachineTestBean getMachineTestById(int index){
		return machineList.get(index);
	}
	
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
	public ShortAnswerBean getShortAnswerById(int index){
		return shortAnswerList.get(index);
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



	public List<FillBlankBean> getFillBlankList() {
		return fillBlankList;
	}


	public void setFillBlankList(List<FillBlankBean> fillBlankList) {
		this.fillBlankList = fillBlankList;
	}


	public List<MachineTestBean> getMachineList() {
		return machineList;
	}


	public void setMachineList(List<MachineTestBean> machineList) {
		this.machineList = machineList;
	}


	public int getExtraTime() {
		return extraTime;
	}


	public void setExtraTime(int extraTime) {
		this.extraTime = extraTime;
	}


	public boolean isFinished() {
		return isFinished;
	}

	public void setFinished(boolean isFinished) {
		this.isFinished = isFinished;
	}

	@Override
	public String toString() {
		return "ExamBean [EXAM_TIME=" + EXAM_TIME + ", earliestSubmit=" + earliestSubmit + ", extraTime=" + extraTime
				+ ", startTime=" + startTime + ", uid=" + uid + ", sid=" + sid + ", paperId=" + paperId + ", mark="
				+ mark + ", topicNum=" + topicNum + ", finishTopic=" + finishTopic + ", ifLogin=" + ifLogin
				+ ", allowStart=" + allowStart + ", allowTerminate=" + allowTerminate + ", isFinished=" + isFinished
				+ ", lastModifiedTime=" + lastModifiedTime + ", singleChoiceList=" + singleChoiceList
				+ ", multiChoicesList=" + multiChoicesList + ", matchingList=" + matchingList + ", judgementList="
				+ judgementList + ", shortAnswerList=" + shortAnswerList + "]";
	}

	
}
