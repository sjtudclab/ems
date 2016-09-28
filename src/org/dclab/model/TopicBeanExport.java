package org.dclab.model;

/**
 * @author alvis
 *主要用在导出考生试卷时
 *需要points和correctanswer这两个exambean中没有的数据
 */
public class TopicBeanExport {
	private int id;
	private String points;
	private String correctAnswer;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getPoints() {
		return points;
	}
	public void setPoints(String points) {
		this.points = points;
	}
	public String getCorrectAnswer() {
		return correctAnswer;
	}
	public void setCorrectAnswer(String correctAnswer) {
		this.correctAnswer = correctAnswer;
	}
	
	
}
