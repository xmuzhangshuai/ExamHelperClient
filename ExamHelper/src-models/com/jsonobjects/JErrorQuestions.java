package com.jsonobjects;

/**
 * Entity mapped to table ERROR_QUESTIONS.
 */
public class JErrorQuestions {

	private Long id;
	private long question_id;
	private java.util.Date error_time;
	private Integer error_num;
	private long user_id;
	private long questionType_id;
	private long section_id;

	public JErrorQuestions() {
	}

	public JErrorQuestions(Long id) {
		this.id = id;
	}

	public JErrorQuestions(Long id, long question_id, java.util.Date error_time, Integer error_num, long user_id,
			long questionType_id, long section_id) {
		this.id = id;
		this.question_id = question_id;
		this.error_time = error_time;
		this.error_num = error_num;
		this.user_id = user_id;
		this.questionType_id = questionType_id;
		this.section_id = section_id;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public long getQuestion_id() {
		return question_id;
	}

	public void setQuestion_id(long question_id) {
		this.question_id = question_id;
	}

	public java.util.Date getError_time() {
		return error_time;
	}

	public void setError_time(java.util.Date error_time) {
		this.error_time = error_time;
	}

	public Integer getError_num() {
		return error_num;
	}

	public void setError_num(Integer error_num) {
		this.error_num = error_num;
	}

	public long getUser_id() {
		return user_id;
	}

	public void setUser_id(long user_id) {
		this.user_id = user_id;
	}

	public long getQuestionType_id() {
		return questionType_id;
	}

	public void setQuestionType_id(long questionType_id) {
		this.questionType_id = questionType_id;
	}

	public long getSection_id() {
		return section_id;
	}

	public void setSection_id(long section_id) {
		this.section_id = section_id;
	}

}
