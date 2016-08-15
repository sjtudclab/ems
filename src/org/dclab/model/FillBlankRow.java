package org.dclab.model;

import org.dclab.common.Constants;

public class FillBlankRow extends TopicRow {
	private int blankNum;
	private String correctAnswer;
	
	public FillBlankRow() {
	}

	public FillBlankRow(int paperId, byte type) {
		super(paperId, Constants.FILL_BLANK);
	}

	public int getBlankNum() {
		return blankNum;
	}

	public void setBlankNum(int blankNum) {
		this.blankNum = blankNum;
	}

	public String getCorrectAnswer() {
		return correctAnswer;
	}

	public void setCorrectAnswer(String correctAnswer) {
		this.correctAnswer = correctAnswer;
	}

	
}
