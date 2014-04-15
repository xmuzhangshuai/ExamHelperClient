package com.jsonobjects;

import java.util.Date;

/**
 * Entity mapped to table QUERYS.
 */
public class JQuerys {

	private Long id;
	private long question_id;
	private java.util.Date query_time;
	private String query_stem;
	private Integer integral;
	private Long adopt_user_id;
	private String query_image;
	private long user_id;
	private long questionType_id;

	public JQuerys() {
	}

	public JQuerys(Long id) {
		this.id = id;
	}

	public JQuerys(Long id, long question_id, Date query_time, String query_stem, Integer integral, Long adopt_user_id,
			String query_image, long user_id, long questionType_id) {
		super();
		this.id = id;
		this.question_id = question_id;
		this.query_time = query_time;
		this.query_stem = query_stem;
		this.integral = integral;
		this.adopt_user_id = adopt_user_id;
		this.query_image = query_image;
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

	public java.util.Date getQuery_time() {
		return query_time;
	}

	public void setQuery_time(java.util.Date query_time) {
		this.query_time = query_time;
	}

	public String getQuery_stem() {
		return query_stem;
	}

	public void setQuery_stem(String query_stem) {
		this.query_stem = query_stem;
	}

	public Integer getIntegral() {
		return integral;
	}

	public void setIntegral(Integer integral) {
		this.integral = integral;
	}

	public Long getAdopt_user_id() {
		return adopt_user_id;
	}

	public void setAdopt_user_id(Long adopt_user_id) {
		this.adopt_user_id = adopt_user_id;
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

	public String getQuery_image() {
		return query_image;
	}

	public void setQuery_image(String query_image) {
		this.query_image = query_image;
	}

}
