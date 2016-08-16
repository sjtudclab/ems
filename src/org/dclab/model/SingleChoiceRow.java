package org.dclab.model;

import java.util.List;

import org.dclab.common.Constants;

public class SingleChoiceRow extends TopicRow {
	private List<String> choiceList;	//a list of choice content
	private int correctAnswerIndex;		//correct answer index, start from 1
	
	public SingleChoiceRow(){
		this.TYPE	=	Constants.SINGLE_CHOICE;
	}
	
	public SingleChoiceRow(int paperId){
		super(paperId, Constants.SINGLE_CHOICE);
	}

	public List<String> getChoiceList() {
		return choiceList;
	}

	public void setChoiceList(List<String> choiceList) {
		this.choiceList = choiceList;
	}

	public int getCorrectAnswerIndex() {
		return correctAnswerIndex;
	}

	public void setCorrectAnswerIndex(int correctAnswerIndex) {
		this.correctAnswerIndex = correctAnswerIndex;
	}

	@Override
	public String toString() {
		return "SingleChoiceRow [choiceList=" + choiceList + ", correctAnswerIndex=" + correctAnswerIndex + ", id=" + id
				+ ", TYPE=" + TYPE + ", paperId=" + paperId + ", number=" + number + ", fullMark=" + fullMark
				+ ", content=" + content + ", img=" + img + ", audio=" + audio + ", video=" + video + "]";
	}
	
	
}
