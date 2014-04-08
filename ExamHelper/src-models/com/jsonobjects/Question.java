package com.jsonobjects;

import java.io.Serializable;

public class Question implements Serializable{
	String question_type;

	public Question() {
		// TODO Auto-generated constructor stub
	}

	public String getQuestion_type() {
		return question_type;
	}

	public void setQuestion_type(String question_type) {
		this.question_type = question_type;
	}

}
