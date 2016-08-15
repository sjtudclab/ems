package org.dclab.model;

import java.util.List;

import org.dclab.common.Constants;

public class MultiChoicesRow extends TopicRow {
	private int choiceNum;
	private List<String> choiceList;
	private String correctAnswerIndices;//original answer, such as 1,3,5	
	private int halfMark;	//incomplete answer, get almost half mark 
	
	public MultiChoicesRow() {
	}

	public MultiChoicesRow(int paperId) {
		super(paperId, Constants.MULTI_CHOICES);
	}

	public int getChoiceNum() {
		return choiceNum;
	}

	public void setChoiceNum(int choiceNum) {
		this.choiceNum = choiceNum;
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
