package org.dclab.model;

import org.dclab.common.Constants;

public class ShortAnswerRow extends TopicRow{
	private String correctAnswer;
	private String pdf;
	
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

	public String getPdf() {
		return pdf;
	}

	public void setPdf(String pdf) {
		this.pdf = pdf;
	}

	@Override
	public String toString() {
		return "ShortAnswerRow [correctAnswer=" + correctAnswer + ", pdf=" + pdf + ", id=" + id + ", TYPE=" + TYPE
				+ ", paperId=" + paperId + ", number=" + number + ", fullMark=" + fullMark + ", content=" + content
				+ ", img=" + img + ", audio=" + audio + ", video=" + video + "]";
	}


}
