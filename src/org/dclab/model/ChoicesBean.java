package org.dclab.model;

/**
 * @author alvis
 *每道题的选项内容及id
 *
 */
public class ChoicesBean {
	private int choiceId;
	private String content;
	private int topicId;
	
	
	public int getTopicId() {
		return topicId;
	}
	public void setTopicId(int topicId) {
		this.topicId = topicId;
	}
	public int getChoiceId() {
		return choiceId;
	}
	public void setChoiceId(int choiceId) {
		this.choiceId = choiceId;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	
}
