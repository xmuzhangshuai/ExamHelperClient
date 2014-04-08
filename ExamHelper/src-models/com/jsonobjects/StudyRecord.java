package com.jsonobjects;

/**
 * Entity mapped to table STUDY_RECORD.
 */
public class StudyRecord {

	private Long id;
	private long question_id;
	private String my_answer;
	private Boolean is_right;
	private java.util.Date record_time;
	private long user_id;
	private long questionType_id;

	public StudyRecord() {
	}

	public StudyRecord(Long id) {
		this.id = id;
	}

	public StudyRecord(Long id, long question_id, String my_answer, Boolean is_right, java.util.Date record_time,
			long user_id, long questionType_id) {
		this.id = id;
		this.question_id = question_id;
		this.my_answer = my_answer;
		this.is_right = is_right;
		this.record_time = record_time;
		this.user_id = user_id;
		this.questionType_id = questionType_id;
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

	public String getMy_answer() {
		return my_answer;
	}

	public void setMy_answer(String my_answer) {
		this.my_answer = my_answer;
	}

	public Boolean getIs_right() {
		return is_right;
	}

	public void setIs_right(Boolean is_right) {
		this.is_right = is_right;
	}

	public java.util.Date getRecord_time() {
		return record_time;
	}

	public void setRecord_time(java.util.Date record_time) {
		this.record_time = record_time;
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

}
