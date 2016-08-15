package org.dclab.model;

import org.dclab.common.Constants;

public class ShortAnswerRow extends TopicRow{
	private String correctAnswer;
	
	public ShortAnswerRow() {
	}

	public ShortAnswerRow(int paperId){
		super(paperId, Constants.SHORT_ANSWER);
	}

	public String getCorrectAnswer() {
		return correctAnswer;
	}

	public void setCorrectAnswer(String correctAnswer) {
		this.correctAnswer = correctAnswer;
	}
	
	
}
