package org.dclab.model;

import java.util.List;

import org.dclab.common.Constants;

public class SingleChoiceRow extends TopicRow {
	private int choiceNum;
	private List<String> choiceList;	//a list of choice content
	private int correctAnswerIndex;		//correct answer index, start from 1
	
	public SingleChoiceRow(){}
	
	public SingleChoiceRow(int paperId){
		super(paperId, Constants.SINGLE_CHOICE);
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

	public int getCorrectAnswerIndex() {
		return correctAnswerIndex;
	}

	public void setCorrectAnswerIndex(int correctAnswerIndex) {
		this.correctAnswerIndex = correctAnswerIndex;
	}
	
	
}
