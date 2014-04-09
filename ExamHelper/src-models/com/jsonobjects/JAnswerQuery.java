package com.jsonobjects;

/**
 * Entity mapped to table ANSWER_QUERY.
 */
public class JAnswerQuery {

	private Long id;
	private String answer_content;
	private java.util.Date answer_time;
	private long user_id;
	private long query_id;

	public JAnswerQuery() {
	}

	public JAnswerQuery(Long id) {
		this.id = id;
	}

	public JAnswerQuery(Long id, String answer_content, java.util.Date answer_time, long user_id, long query_id) {
		this.id = id;
		this.answer_content = answer_content;
		this.answer_time = answer_time;
		this.user_id = user_id;
		this.query_id = query_id;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getAnswer_content() {
		return answer_content;
	}

	public void setAnswer_content(String answer_content) {
		this.answer_content = answer_content;
	}

	public java.util.Date getAnswer_time() {
		return answer_time;
	}

	public void setAnswer_time(java.util.Date answer_time) {
		this.answer_time = answer_time;
	}

	public long getUser_id() {
		return user_id;
	}

	public void setUser_id(long user_id) {
		this.user_id = user_id;
	}

	public long getQuery_id() {
		return query_id;
	}

	public void setQuery_id(long query_id) {
		this.query_id = query_id;
	}

}
