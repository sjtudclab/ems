package org.dclab.model;

import org.dclab.common.Constants;

public class ShortAnswerRow extends TopicRow{
	private String correctAnswer;
	
	public ShortAnswerRow() {
		this.TYPE	=	Constants.SHORT_ANSWER;
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

	@Override
	public String toString() {
		return "ShortAnswerRow [correctAnswer=" + correctAnswer + ", id=" + id + ", TYPE=" + TYPE + ", paperId="
				+ paperId + ", number=" + number + ", fullMark=" + fullMark + ", content=" + content + ", img=" + img
				+ ", audio=" + audio + ", video=" + video + "]";
	}
	
}
