package com.netdomains;

/**
 * Questionsofmaterial entity. @author MyEclipse Persistence Tools
 */

public class Questionsofmaterial implements java.io.Serializable {

	// Fields

	private Integer id;
	private Materialanalysis materialanalysis;
	private Integer questionNumber;
	private String questionStem;
	private String answer;
	private String analysis;
	private Integer score;


	// Constructors

	/** default constructor */
	public Questionsofmaterial() {
	}

	/** minimal constructor */
	public Questionsofmaterial(Materialanalysis materialanalysis,
			Integer questionNumber, String questionStem) {
		this.materialanalysis = materialanalysis;
		this.questionNumber = questionNumber;
		this.questionStem = questionStem;
	}

	/** full constructor */
	public Questionsofmaterial(Materialanalysis materialanalysis,
			Integer questionNumber, String questionStem, String answer,
			String analysis, Integer score) {
		this.materialanalysis = materialanalysis;
		this.questionNumber = questionNumber;
		this.questionStem = questionStem;
		this.answer = answer;
		this.analysis = analysis;
		this.score = score;
	}

	// Property accessors

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Materialanalysis getMaterialanalysis() {
		return this.materialanalysis;
	}

	public void setMaterialanalysis(Materialanalysis materialanalysis) {
		this.materialanalysis = materialanalysis;
	}

	public Integer getQuestionNumber() {
		return this.questionNumber;
	}

	public void setQuestionNumber(Integer questionNumber) {
		this.questionNumber = questionNumber;
	}

	public String getQuestionStem() {
		return this.questionStem;
	}

	public void setQuestionStem(String questionStem) {
		this.questionStem = questionStem;
	}

	public String getAnswer() {
		return this.answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

	public String getAnalysis() {
		return this.analysis;
	}

	public void setAnalysis(String analysis) {
		this.analysis = analysis;
	}

	public Integer getScore() {
		return this.score;
	}

	public void setScore(Integer score) {
		this.score = score;
	}

}