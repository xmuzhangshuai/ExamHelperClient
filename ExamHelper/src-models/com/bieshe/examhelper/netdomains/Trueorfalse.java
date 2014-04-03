package com.bieshe.examhelper.netdomains;

/**
 * Trueorfalse entity. @author MyEclipse Persistence Tools
 */

public class Trueorfalse implements java.io.Serializable {

	// Fields

	private Integer id;
	private Section section;
	private String questionStem;
	private Boolean answer;
	private String analysis;
	private String remark;

	// Constructors

	/** default constructor */
	public Trueorfalse() {
	}

	/** minimal constructor */
	public Trueorfalse(String questionStem) {
		this.questionStem = questionStem;
	}

	/** full constructor */
	public Trueorfalse(Section section, String questionStem, Boolean answer,
			String analysis, String remark) {
		this.section = section;
		this.questionStem = questionStem;
		this.answer = answer;
		this.analysis = analysis;
		this.remark = remark;
	}

	// Property accessors

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Section getSection() {
		return this.section;
	}

	public void setSection(Section section) {
		this.section = section;
	}

	public String getQuestionStem() {
		return this.questionStem;
	}

	public void setQuestionStem(String questionStem) {
		this.questionStem = questionStem;
	}

	public Boolean getAnswer() {
		return this.answer;
	}

	public void setAnswer(Boolean answer) {
		this.answer = answer;
	}

	public String getAnalysis() {
		return this.analysis;
	}

	public void setAnalysis(String analysis) {
		this.analysis = analysis;
	}

	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

}