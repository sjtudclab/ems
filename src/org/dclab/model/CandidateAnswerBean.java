/**
 * 
 */
package org.dclab.model;

/**
 * 考生答案 （用于存储在数据库中，阅卷时直接使用ExamBean
 * choiceId 是字符串，用于存储多选题答案
 * topicId 用于比对正确答案与否
 * 
 * @author zhaoz
 *
 */
public class CandidateAnswerBean {
	private int candidate_id ;
	private int topicId;
	private String choiceId;
	public int getCandidate_id() {
		return candidate_id;
	}
	public void setCandidate_id(int candidate_id) {
		this.candidate_id = candidate_id;
	}
	public int getTopicId() {
		return topicId;
	}
	public void setTopicId(int topicId) {
		this.topicId = topicId;
	}
	public String getChoiceId() {
		return choiceId;
	}
	public void setChoiceId(String choiceId) {
		this.choiceId = choiceId;
	}
	
	
}
