package org.dclab.model;

import org.dclab.common.Constants;

public class MachineTestRow extends TopicRow {
	private String correctAnswerFile;	// a rar file's path
	public MachineTestRow() {
		this.TYPE	=	Constants.MACHINE_TEST;
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

	@Override
	public String toString() {
		return "MachineTestRow [correctAnswerFile=" + correctAnswerFile + ", id=" + id + ", TYPE=" + TYPE + ", paperId="
				+ paperId + ", number=" + number + ", fullMark=" + fullMark + ", content=" + content + ", img=" + img
				+ ", audio=" + audio + ", video=" + video + "]";
	}

}
