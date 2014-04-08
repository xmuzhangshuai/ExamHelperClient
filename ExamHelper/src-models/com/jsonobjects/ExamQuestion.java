package com.jsonobjects;

/**
 * Entity mapped to table EXAM_QUESTION.
 */
public class ExamQuestion {

	private Long id;
	private int question_number;
	private long question_id;
	private long exanSection_id;

	public ExamQuestion() {
	}

	public ExamQuestion(Long id) {
		this.id = id;
	}

	public ExamQuestion(Long id, int question_number, long question_id, long exanSection_id) {
		this.id = id;
		this.question_number = question_number;
		this.question_id = question_id;
		this.exanSection_id = exanSection_id;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public int getQuestion_number() {
		return question_number;
	}

	public void setQuestion_number(int question_number) {
		this.question_number = question_number;
	}

	public long getQuestion_id() {
		return question_id;
	}

	public void setQuestion_id(long question_id) {
		this.question_id = question_id;
	}

	public long getExanSection_id() {
		return exanSection_id;
	}

	public void setExanSection_id(long exanSection_id) {
		this.exanSection_id = exanSection_id;
	}

}
