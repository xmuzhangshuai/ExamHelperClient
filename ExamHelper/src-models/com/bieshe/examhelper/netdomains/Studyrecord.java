package com.bieshe.examhelper.netdomains;
/**
 * Studyrecord entity. @author MyEclipse Persistence Tools
 */

public class Studyrecord implements java.io.Serializable {

	// Fields

	private Integer id;
	private User user;
	private Questiontype questiontype;
	private Integer questionId;

	// Constructors

	/** default constructor */
	public Studyrecord() {
	}

	/** full constructor */
	public Studyrecord(User user, Questiontype questiontype, Integer questionId) {
		this.user = user;
		this.questiontype = questiontype;
		this.questionId = questionId;
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

}