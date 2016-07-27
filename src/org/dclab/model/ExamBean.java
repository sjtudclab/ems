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
	private static int EXAM_TIME;	//整场考试的时长，记得加载时初始化 
	//剩余时长, 装填试卷时将其初始化，每次写log时，获取前台剩余时间，重新setDuration，延时或者减少时间时直接修改这个值
	private int duration;	//该考生剩余的时长，用于灾备
	private long startTime;//该考生开始考试的时间
	private int uid; //用户准考证号
	private int sid;//考试科目id
	
	private boolean ifLogin;//考生是否登录了，主要是用在监考教师的界面显示上

	private boolean allowStart;//任何时间开始
	private boolean allowTerminate;//任何时间终止
	private boolean isFinished = false; //是否交卷

	private boolean isVIP;	//用户可在任何时间开始考试，数据库中字段
	private long lastModifiedTime;
	private List<SingleChoiceBean> singleChoiceList;
	private List<MultiChoicesBean> multiChoicesList;
	private List<MatchingBean> matchingList;
	private List<JudgementBean> judgementList;
	
	public ExamBean(int id, int sid2){
		uid=id;
		sid=sid2;
	}
	
	
	public static int getEXAM_TIME() {
		return EXAM_TIME;
	}


	public static void setEXAM_TIME(int eXAM_TIME) {
		EXAM_TIME = eXAM_TIME;
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


	public int getUid() {
		return uid;
	}

	public void setUid(int uid) {
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

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public boolean isFinished() {
		return isFinished;
	}

	public void setFinished(boolean isFinished) {
		this.isFinished = isFinished;
	}

	public boolean isVIP() {
		return isVIP;
	}

	public void setVIP(boolean isVIP) {
		this.isVIP = isVIP;
	}

	
}
