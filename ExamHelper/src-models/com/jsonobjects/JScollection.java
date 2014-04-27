package com.jsonobjects;

public class JScollection {
	private Integer id;
	private Integer questiontypeId;
	private Integer sectionId;
	private Integer questionId;
	private Integer collectionNum;

	public JScollection() {
		// TODO Auto-generated constructor stub
	}

	public JScollection(Integer id, Integer questiontypeId, Integer sectionId, Integer questionId, Integer collectionNum) {
		super();
		this.id = id;
		this.questiontypeId = questiontypeId;
		this.sectionId = sectionId;
		this.questionId = questionId;
		this.collectionNum = collectionNum;
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

	public Integer getCollectionNum() {
		return collectionNum;
	}

	public void setCollectionNum(Integer collectionNum) {
		this.collectionNum = collectionNum;
	}

}
