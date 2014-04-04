package com.netdomains;

/**
 * Groupquestion entity. @author MyEclipse Persistence Tools
 */

public class Groupquestion implements java.io.Serializable {

	// Fields

	private Integer id;
	private Group group;
	private Integer questionId;

	// Constructors

	/** default constructor */
	public Groupquestion() {
	}

	/** full constructor */
	public Groupquestion(Group group, Integer questionId) {
		this.group = group;
		this.questionId = questionId;
	}

	// Property accessors

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Group getGroup() {
		return this.group;
	}

	public void setGroup(Group group) {
		this.group = group;
	}

	public Integer getQuestionId() {
		return this.questionId;
	}

	public void setQuestionId(Integer questionId) {
		this.questionId = questionId;
	}

}