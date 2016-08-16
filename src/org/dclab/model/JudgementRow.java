package org.dclab.model;

import org.dclab.common.Constants;

public class JudgementRow extends TopicRow {
	private int correctAnswer;	//1 true, 0 false
	public JudgementRow() {
		this.TYPE =	 Constants.JUDGEMENT;
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

	@Override
	public String toString() {
		return "JudgementRow [correctAnswer=" + correctAnswer + ", id=" + id + ", TYPE=" + TYPE + ", paperId=" + paperId
				+ ", number=" + number + ", fullMark=" + fullMark + ", content=" + content + ", img=" + img + ", audio="
				+ audio + ", video=" + video + "]";
	}
}
