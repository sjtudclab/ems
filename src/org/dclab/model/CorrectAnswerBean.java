package org.dclab.model;

/**
 * 考试的正确答案及分数
 * 注意：
 * 1. choiceId 是一个用逗号分隔choice id的字符串（出于多选的考虑， 1,3,5 代表这个多选的正确答案包含的选项是1,3,5）
 * 2. points 也是一个用逗号分隔分数的字符串 （出于多选 给分不同的考虑 例如：3,5 代表少选得3分，全对得5分）
 * @author zhaoz
 *
 */
public class CorrectAnswerBean {
	private int id;
	private int topicId;
	private String choiceId;
	private String points;
	
	public CorrectAnswerBean(){}
	public CorrectAnswerBean(String choiceId, String points){
		this.choiceId = choiceId;
		this.points	  = points;	
		
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
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
	public String getPoints() {
		return points;
	}
	public void setPoints(String points) {
		this.points = points;
	}
	@Override
	public String toString() {
		return "CorrectAnswer [id=" + id + ", topicId=" + topicId + ", choiceId=" + choiceId + ", points=" + points
				+ "]";
	}
	
}
