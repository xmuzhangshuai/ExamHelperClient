package com.bieshe.examhelper.netdomains;

/**
 * Examquestion entity. @author MyEclipse Persistence Tools
 */

public class Examquestion implements java.io.Serializable {

	// Fields

	private Integer id;
	private Examsection examsection;
	private Integer questionNumber;
	private Integer questionId;

	// Constructors

	/** default constructor */
	public Examquestion() {
	}

	/** full constructor */
	public Examquestion(Examsection examsection, Integer questionNumber,
			Integer questionId) {
		this.examsection = examsection;
		this.questionNumber = questionNumber;
		this.questionId = questionId;
	}

	// Property accessors

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Examsection getExamsection() {
		return this.examsection;
	}

	public void setExamsection(Examsection examsection) {
		this.examsection = examsection;
	}

	public Integer getQuestionNumber() {
		return this.questionNumber;
	}

	public void setQuestionNumber(Integer questionNumber) {
		this.questionNumber = questionNumber;
	}

	public Integer getQuestionId() {
		return this.questionId;
	}

	public void setQuestionId(Integer questionId) {
		this.questionId = questionId;
	}

}