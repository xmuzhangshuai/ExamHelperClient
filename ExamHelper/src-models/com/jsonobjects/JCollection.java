package com.jsonobjects;

/**
 * Entity mapped to table COLLECTION.
 */
public class JCollection {

	private Long id;
	private long question_id;
	private java.util.Date collect_time;
	private long user_id;
	private long questionType_id;
	private long section_id;

	public JCollection() {
	}

	public JCollection(Long id) {
		this.id = id;
	}

	public JCollection(Long id, long question_id, java.util.Date collect_time, long user_id, long questionType_id,
			long section_id) {
		this.id = id;
		this.question_id = question_id;
		this.collect_time = collect_time;
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

	public java.util.Date getCollect_time() {
		return collect_time;
	}

	public void setCollect_time(java.util.Date collect_time) {
		this.collect_time = collect_time;
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
