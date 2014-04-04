package com.netdomains;


import java.util.Date;

/**
 * Collection entity. @author MyEclipse Persistence Tools
 */

public class Collection implements java.io.Serializable {

	// Fields

	private Integer id;
	private User user;
	private Questiontype questiontype;
	private Section section;
	private Integer questionId;
	private Date collectTime;

	// Constructors

	/** default constructor */
	public Collection() {
	}

	/** minimal constructor */
	public Collection(User user, Questiontype questiontype, Section section,
			Integer questionId) {
		this.user = user;
		this.questiontype = questiontype;
		this.section = section;
		this.questionId = questionId;
	}

	/** full constructor */
	public Collection(User user, Questiontype questiontype, Section section,
			Integer questionId, Date collectTime) {
		this.user = user;
		this.questiontype = questiontype;
		this.section = section;
		this.questionId = questionId;
		this.collectTime = collectTime;
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

	public Section getSection() {
		return this.section;
	}

	public void setSection(Section section) {
		this.section = section;
	}

	public Integer getQuestionId() {
		return this.questionId;
	}

	public void setQuestionId(Integer questionId) {
		this.questionId = questionId;
	}

	public Date getCollectTime() {
		return this.collectTime;
	}

	public void setCollectTime(Date collectTime) {
		this.collectTime = collectTime;
	}

}