package com.netdomains;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Query entity. @author MyEclipse Persistence Tools
 */

public class Query implements java.io.Serializable {

	// Fields

	private Integer id;
	private User user;
	private Questiontype questiontype;
	private Integer questionId;
	private String queryStem;
	private Date queryTime;
	private Integer integral;
	private Integer adoptUserId;
	private Set answerqueries = new HashSet(0);

	// Constructors

	/** default constructor */
	public Query() {
	}

	/** minimal constructor */
	public Query(User user, Questiontype questiontype, Integer questionId) {
		this.user = user;
		this.questiontype = questiontype;
		this.questionId = questionId;
	}

	/** full constructor */
	public Query(User user, Questiontype questiontype, Integer questionId,
			String queryStem, Date queryTime, Integer integral,
			Integer adoptUserId, Set answerqueries) {
		this.user = user;
		this.questiontype = questiontype;
		this.questionId = questionId;
		this.queryStem = queryStem;
		this.queryTime = queryTime;
		this.integral = integral;
		this.adoptUserId = adoptUserId;
		this.answerqueries = answerqueries;
	}

	// Property accessors

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public User getUser() {
		return this.user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Questiontype getQuestiontype() {
		return this.questiontype;
	}

	public void setQuestiontype(Questiontype questiontype) {
		this.questiontype = questiontype;
	}

	public Integer getQuestionId() {
		return this.questionId;
	}

	public void setQuestionId(Integer questionId) {
		this.questionId = questionId;
	}

	public String getQueryStem() {
		return this.queryStem;
	}

	public void setQueryStem(String queryStem) {
		this.queryStem = queryStem;
	}

	public Date getQueryTime() {
		return this.queryTime;
	}

	public void setQueryTime(Date queryTime) {
		this.queryTime = queryTime;
	}

	public Integer getIntegral() {
		return this.integral;
	}

	public void setIntegral(Integer integral) {
		this.integral = integral;
	}

	public Integer getAdoptUserId() {
		return this.adoptUserId;
	}

	public void setAdoptUserId(Integer adoptUserId) {
		this.adoptUserId = adoptUserId;
	}

	public Set getAnswerqueries() {
		return this.answerqueries;
	}

	public void setAnswerqueries(Set answerqueries) {
		this.answerqueries = answerqueries;
	}

}