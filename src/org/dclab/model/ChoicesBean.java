package org.dclab.model;

/**
 * @author alvis
 *每道题的选项内容及id
 *
 */
public class ChoicesBean implements Cloneable{
	private int choiceId;
	private String content;
	private int topicId;
	
	public ChoicesBean() {
		super();
	}
	
	public ChoicesBean(String content) {
		super();
		this.content = content;
	}

	public ChoicesBean(String content, int topicId) {
		super();
		this.content = content;
		this.topicId = topicId;
	}
	
	@Override
	protected Object clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		return super.clone();
	}
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
