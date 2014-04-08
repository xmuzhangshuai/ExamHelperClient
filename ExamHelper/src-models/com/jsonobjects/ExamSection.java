package com.jsonobjects;

/**
 * Entity mapped to table EXAM_SECTION.
 */
public class ExamSection {

	private Long id;
	private String request;
	private Integer question_num;
	private Integer question_score;
	private long questionType_id;
	private long exam_id;

	public ExamSection() {
	}

	public ExamSection(Long id) {
		this.id = id;
	}

	public ExamSection(Long id, String request, Integer question_num, Integer question_score, long questionType_id,
			long exam_id) {
		this.id = id;
		this.request = request;
		this.question_num = question_num;
		this.question_score = question_score;
		this.questionType_id = questionType_id;
		this.exam_id = exam_id;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getRequest() {
		return request;
	}

	public void setRequest(String request) {
		this.request = request;
	}

	public Integer getQuestion_num() {
		return question_num;
	}

	public void setQuestion_num(Integer question_num) {
		this.question_num = question_num;
	}

	public Integer getQuestion_score() {
		return question_score;
	}

	public void setQuestion_score(Integer question_score) {
		this.question_score = question_score;
	}

	public long getQuestionType_id() {
		return questionType_id;
	}

	public void setQuestionType_id(long questionType_id) {
		this.questionType_id = questionType_id;
	}

	public long getExam_id() {
		return exam_id;
	}

	public void setExam_id(long exam_id) {
		this.exam_id = exam_id;
	}

}
