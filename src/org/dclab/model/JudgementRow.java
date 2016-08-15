package org.dclab.model;

import org.dclab.common.Constants;

public class JudgementRow extends TopicRow {
	private int correctAnswer;	//1 true, 0 false
	public JudgementRow() {
	}

	public JudgementRow(int paperId) {
		super(paperId, Constants.JUDGEMENT);
	}

	public int getCorrectAnswer() {
		return correctAnswer;
	}

	public void setCorrectAnswer(int correctAnswer) {
		this.correctAnswer = correctAnswer;
	}

	
}
