package com.jsonobjects;

/**
 * Entity mapped to table QUESTIONS_OF_MATERIAL.
 */
public class QuestionsOfMaterial {

	private Long id;
	private Integer qusetion_number;
	private String question_stem;
	private String answer;
	private String analysis;
	private Integer score;
	private long material_id;

	public QuestionsOfMaterial() {
	}

	public QuestionsOfMaterial(Long id) {
		this.id = id;
	}

	public QuestionsOfMaterial(Long id, Integer qusetion_number, String question_stem, String answer, String analysis,
			Integer score, long material_id) {
		this.id = id;
		this.qusetion_number = qusetion_number;
		this.question_stem = question_stem;
		this.answer = answer;
		this.analysis = analysis;
		this.score = score;
		this.material_id = material_id;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getQusetion_number() {
		return qusetion_number;
	}

	public void setQusetion_number(Integer qusetion_number) {
		this.qusetion_number = qusetion_number;
	}

	public String getQuestion_stem() {
		return question_stem;
	}

	public void setQuestion_stem(String question_stem) {
		this.question_stem = question_stem;
	}

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

	public String getAnalysis() {
		return analysis;
	}

	public void setAnalysis(String analysis) {
		this.analysis = analysis;
	}

	public Integer getScore() {
		return score;
	}

	public void setScore(Integer score) {
		this.score = score;
	}

	public long getMaterial_id() {
		return material_id;
	}

	public void setMaterial_id(long material_id) {
		this.material_id = material_id;
	}

}
