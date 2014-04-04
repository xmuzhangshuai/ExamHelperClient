package com.netdomains;

import java.util.HashSet;
import java.util.Set;

/**
 * Examsection entity. @author MyEclipse Persistence Tools
 */

public class Examsection implements java.io.Serializable {

	// Fields

	private Integer id;
	private Questiontype questiontype;
	private Examination examination;
	private String request;
	private Integer questionNum;
	private Integer questionScore;
	private Set examquestions = new HashSet(0);

	// Constructors

	/** default constructor */
	public Examsection() {
	}

	/** minimal constructor */
	public Examsection(Questiontype questiontype, Examination examination) {
		this.questiontype = questiontype;
		this.examination = examination;
	}

	/** full constructor */
	public Examsection(Questiontype questiontype, Examination examination,
			String request, Integer questionNum, Integer questionScore,
			Set examquestions) {
		this.questiontype = questiontype;
		this.examination = examination;
		this.request = request;
		this.questionNum = questionNum;
		this.questionScore = questionScore;
		this.examquestions = examquestions;
	}

	// Property accessors

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Questiontype getQuestiontype() {
		return this.questiontype;
	}

	public void setQuestiontype(Questiontype questiontype) {
		this.questiontype = questiontype;
	}

	public Examination getExamination() {
		return this.examination;
	}

	public void setExamination(Examination examination) {
		this.examination = examination;
	}

	public String getRequest() {
		return this.request;
	}

	public void setRequest(String request) {
		this.request = request;
	}

	public Integer getQuestionNum() {
		return this.questionNum;
	}

	public void setQuestionNum(Integer questionNum) {
		this.questionNum = questionNum;
	}

	public Integer getQuestionScore() {
		return this.questionScore;
	}

	public void setQuestionScore(Integer questionScore) {
		this.questionScore = questionScore;
	}

	public Set getExamquestions() {
		return this.examquestions;
	}

	public void setExamquestions(Set examquestions) {
		this.examquestions = examquestions;
	}

}