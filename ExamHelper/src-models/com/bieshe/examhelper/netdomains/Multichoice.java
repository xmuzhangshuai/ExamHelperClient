package com.bieshe.examhelper.netdomains;

/**
 * Multichoice entity. @author MyEclipse Persistence Tools
 */

public class Multichoice implements java.io.Serializable {

	// Fields

	private Integer id;
	private Section section;
	private String questionStem;
	private String optionA;
	private String optionB;
	private String optionC;
	private String optionD;
	private String optionE;
	private String optionF;
	private Boolean answerA;
	private Boolean answerB;
	private Boolean answerC;
	private Boolean answerD;
	private Boolean answerE;
	private Boolean answerF;
	private String analysis;
	private String remark;

	// Constructors

	/** default constructor */
	public Multichoice() {
	}

	/** minimal constructor */
	public Multichoice(String questionStem) {
		this.questionStem = questionStem;
	}

	/** full constructor */
	public Multichoice(Section section, String questionStem, String optionA,
			String optionB, String optionC, String optionD, String optionE,
			String optionF, Boolean answerA, Boolean answerB, Boolean answerC,
			Boolean answerD, Boolean answerE, Boolean answerF, String analysis,
			String remark) {
		this.section = section;
		this.questionStem = questionStem;
		this.optionA = optionA;
		this.optionB = optionB;
		this.optionC = optionC;
		this.optionD = optionD;
		this.optionE = optionE;
		this.optionF = optionF;
		this.answerA = answerA;
		this.answerB = answerB;
		this.answerC = answerC;
		this.answerD = answerD;
		this.answerE = answerE;
		this.answerF = answerF;
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

	public String getOptionA() {
		return this.optionA;
	}

	public void setOptionA(String optionA) {
		this.optionA = optionA;
	}

	public String getOptionB() {
		return this.optionB;
	}

	public void setOptionB(String optionB) {
		this.optionB = optionB;
	}

	public String getOptionC() {
		return this.optionC;
	}

	public void setOptionC(String optionC) {
		this.optionC = optionC;
	}

	public String getOptionD() {
		return this.optionD;
	}

	public void setOptionD(String optionD) {
		this.optionD = optionD;
	}

	public String getOptionE() {
		return this.optionE;
	}

	public void setOptionE(String optionE) {
		this.optionE = optionE;
	}

	public String getOptionF() {
		return this.optionF;
	}

	public void setOptionF(String optionF) {
		this.optionF = optionF;
	}

	public Boolean getAnswerA() {
		return this.answerA;
	}

	public void setAnswerA(Boolean answerA) {
		this.answerA = answerA;
	}

	public Boolean getAnswerB() {
		return this.answerB;
	}

	public void setAnswerB(Boolean answerB) {
		this.answerB = answerB;
	}

	public Boolean getAnswerC() {
		return this.answerC;
	}

	public void setAnswerC(Boolean answerC) {
		this.answerC = answerC;
	}

	public Boolean getAnswerD() {
		return this.answerD;
	}

	public void setAnswerD(Boolean answerD) {
		this.answerD = answerD;
	}

	public Boolean getAnswerE() {
		return this.answerE;
	}

	public void setAnswerE(Boolean answerE) {
		this.answerE = answerE;
	}

	public Boolean getAnswerF() {
		return this.answerF;
	}

	public void setAnswerF(Boolean answerF) {
		this.answerF = answerF;
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