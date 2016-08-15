package org.dclab.model;

import org.dclab.common.Constants;

public class MachineTestRow extends TopicRow {
	private String correctAnswerFile;	// a rar file's path
	public MachineTestRow() {
	}

	public MachineTestRow(int paperId) {
		super(paperId, Constants.MACHINE_TEST);
	}

	public String getCorrectAnswerFile() {
		return correctAnswerFile;
	}

	public void setCorrectAnswerFile(String correctAnswerFile) {
		this.correctAnswerFile = correctAnswerFile;
	}

	
}
