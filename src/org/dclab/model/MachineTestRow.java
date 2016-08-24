package org.dclab.model;

import org.dclab.common.Constants;

public class MachineTestRow extends TopicRow {
	private String correctAnswerFile;	// a rar file's path
	private String pdf;
	
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

	public String getPdf() {
		return pdf;
	}

	public void setPdf(String pdf) {
		this.pdf = pdf;
	}

	@Override
	public String toString() {
		return "MachineTestRow [correctAnswerFile=" + correctAnswerFile + ", pdf=" + pdf + ", id=" + id + ", TYPE="
				+ TYPE + ", paperId=" + paperId + ", number=" + number + ", fullMark=" + fullMark + ", content="
				+ content + ", img=" + img + ", audio=" + audio + ", video=" + video + "]";
	}

}
