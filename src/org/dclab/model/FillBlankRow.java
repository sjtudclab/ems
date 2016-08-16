package org.dclab.model;

import org.dclab.common.Constants;

public class FillBlankRow extends TopicRow {
	private int blankNum;
	private String correctAnswer;
	
	public FillBlankRow() {
		this.TYPE	=	Constants.FILL_BLANK;
	}

	public FillBlankRow(int paperId) {
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

	@Override
	public String toString() {
		return "FillBlankRow [blankNum=" + blankNum + ", correctAnswer=" + correctAnswer + ", id=" + id + ", TYPE="
				+ TYPE + ", paperId=" + paperId + ", number=" + number + ", fullMark=" + fullMark + ", content="
				+ content + ", img=" + img + ", audio=" + audio + ", video=" + video + "]";
	}

}
