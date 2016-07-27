package org.dclab.model;

/**
 * @author alvis
 *每道题的选项内容及id
 *
 */
public class ChoicesBean {
	private int choiceId;
	private String content;
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
