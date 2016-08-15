package org.dclab.model;

import java.util.List;

import org.dclab.common.Constants;

public class MatchingRow extends TopicRow {
	private int matchNum;
	private String correctAnswerIndices;	//default: order of items
	private List<String> itemList;
	private List<String> choiceList;

	public MatchingRow() {
	}

	public MatchingRow(int paperId) {
		super(paperId, Constants.MATCHING);
	}

	public int getMatchNum() {
		return matchNum;
	}

	public void setMatchNum(int matchNum) {
		this.matchNum = matchNum;
	}

	public String getCorrectAnswerIndices() {
		return correctAnswerIndices;
	}

	public void setCorrectAnswerIndices(String correctAnswerIndices) {
		this.correctAnswerIndices = correctAnswerIndices;
	}

	public List<String> getItemList() {
		return itemList;
	}

	public void setItemList(List<String> itemList) {
		this.itemList = itemList;
	}

	public List<String> getChoiceList() {
		return choiceList;
	}

	public void setChoiceList(List<String> choiceList) {
		this.choiceList = choiceList;
	}

	
}
