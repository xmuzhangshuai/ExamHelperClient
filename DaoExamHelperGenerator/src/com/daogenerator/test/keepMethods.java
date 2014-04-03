package com.daogenerator.test;

public class keepMethods {
	private Boolean answerA;
	private Boolean answerB;
	private Boolean answerC;
	private Boolean answerD;
	private Boolean answerE;
	private Boolean answerF;

	public String getRightAnswer() {
		String rightAnswer = "";
		if (answerA) {
			rightAnswer = rightAnswer + "A";
		}
		if (answerB) {
			rightAnswer = rightAnswer + "B";
		}
		if (answerC) {
			rightAnswer = rightAnswer + "C";
		}
		if (answerD) {
			rightAnswer = rightAnswer + "D";
		}
		if (answerE) {
			rightAnswer = rightAnswer + "E";
		}
		if (answerF) {
			rightAnswer = rightAnswer + "F";
		}
		return rightAnswer.trim();
	}
}
