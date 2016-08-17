package org.dclab.model;

import java.util.List;

import org.dclab.common.Constants;

public class MultiChoicesRow extends TopicRow {
	private List<String> choiceList;
	private String correctAnswerIndices;//original answer, such as 1,3,5	
	private int halfMark;	//incomplete answer, get almost half mark 
	
	public MultiChoicesRow() {
		this.TYPE	=	 Constants.MULTI_CHOICES;
	}

	public MultiChoicesRow(int paperId) {
		super(paperId, Constants.MULTI_CHOICES);
	}
	
	@Override
	public String toString() {
		return "MultiChoicesRow [choiceList=" + choiceList + ", correctAnswerIndices=" + correctAnswerIndices
				+ ", halfMark=" + halfMark + ", id=" + id + ", TYPE=" + TYPE + ", paperId=" + paperId + ", number="
				+ number + ", fullMark=" + fullMark + ", content=" + content + ", img=" + img + ", audio=" + audio
				+ ", video=" + video + "]";
	}

	public List<String> getChoiceList() {
		return choiceList;
	}

	public void setChoiceList(List<String> choiceList) {
		this.choiceList = choiceList;
	}

	public String getCorrectAnswerIndices() {
		return correctAnswerIndices;
	}

	public void setCorrectAnswerIndices(String correctAnswerIndices) {
		this.correctAnswerIndices = correctAnswerIndices;
	}

	public int getHalfMark() {
		return halfMark;
	}

	public void setHalfMark(int halfMark) {
		this.halfMark = halfMark;
	}

	
}
