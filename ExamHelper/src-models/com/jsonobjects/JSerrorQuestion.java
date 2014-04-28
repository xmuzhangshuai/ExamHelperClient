package com.jsonobjects;

public class JSerrorQuestion {

	private Integer id;
	private Integer questiontypeId;
	private Integer sectionId;
	private Integer questionId;
	private Integer errorNum;

	public JSerrorQuestion() {
		// TODO Auto-generated constructor stub
	}

	public JSerrorQuestion(Integer id, Integer questiontypeId, Integer sectionId, Integer questionId, Integer errorNum) {
		super();
		this.id = id;
		this.questiontypeId = questiontypeId;
		this.sectionId = sectionId;
		this.questionId = questionId;
		this.errorNum = errorNum;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getQuestiontypeId() {
		return questiontypeId;
	}

	public void setQuestiontypeId(Integer questiontypeId) {
		this.questiontypeId = questiontypeId;
	}

	public Integer getSectionId() {
		return sectionId;
	}

	public void setSectionId(Integer sectionId) {
		this.sectionId = sectionId;
	}

	public Integer getQuestionId() {
		return questionId;
	}

	public void setQuestionId(Integer questionId) {
		this.questionId = questionId;
	}

	public Integer getErrorNum() {
		return errorNum;
	}

	public void setErrorNum(Integer errorNum) {
		this.errorNum = errorNum;
	}

}
