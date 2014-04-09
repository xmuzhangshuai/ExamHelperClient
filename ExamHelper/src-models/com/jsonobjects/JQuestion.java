package com.jsonobjects;

import java.io.Serializable;

public class JQuestion implements Serializable{
	String question_type;

	public JQuestion() {
		// TODO Auto-generated constructor stub
	}

	public String getQuestion_type() {
		return question_type;
	}

	public void setQuestion_type(String question_type) {
		this.question_type = question_type;
	}

}
