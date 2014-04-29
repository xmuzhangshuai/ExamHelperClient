package com.jsonobjects;

/**
 * Entity mapped to table MULTI_CHOICE.
 */
public class JMultiChoice extends JQuestion {

	private Long id;
	/** Not-null value. */
	private String question_stem;
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
	private Boolean flag;
	private long section_id;

	// KEEP FIELDS - put your custom fields here
	// KEEP FIELDS END

	public JMultiChoice() {
	}

	public JMultiChoice(Long id) {
		this.id = id;
	}

	public JMultiChoice(Long id, String question_stem, String optionA, String optionB, String optionC, String optionD,
			String optionE, String optionF, Boolean answerA, Boolean answerB, Boolean answerC, Boolean answerD,
			Boolean answerE, Boolean answerF, String analysis, String remark, Boolean flag, long section_id) {
		this.id = id;
		this.question_stem = question_stem;
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
		this.flag = flag;
		this.section_id = section_id;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	/** Not-null value. */
	public String getQuestion_stem() {
		return question_stem;
	}

	/** Not-null value; ensure this value is available before it is saved to the database. */
	public void setQuestion_stem(String question_stem) {
		this.question_stem = question_stem;
	}

	public String getOptionA() {
		return optionA;
	}

	public void setOptionA(String optionA) {
		this.optionA = optionA;
	}

	public String getOptionB() {
		return optionB;
	}

	public void setOptionB(String optionB) {
		this.optionB = optionB;
	}

	public String getOptionC() {
		return optionC;
	}

	public void setOptionC(String optionC) {
		this.optionC = optionC;
	}

	public String getOptionD() {
		return optionD;
	}

	public void setOptionD(String optionD) {
		this.optionD = optionD;
	}

	public String getOptionE() {
		return optionE;
	}

	public void setOptionE(String optionE) {
		this.optionE = optionE;
	}

	public String getOptionF() {
		return optionF;
	}

	public void setOptionF(String optionF) {
		this.optionF = optionF;
	}

	public Boolean getAnswerA() {
		return answerA;
	}

	public void setAnswerA(Boolean answerA) {
		this.answerA = answerA;
	}

	public Boolean getAnswerB() {
		return answerB;
	}

	public void setAnswerB(Boolean answerB) {
		this.answerB = answerB;
	}

	public Boolean getAnswerC() {
		return answerC;
	}

	public void setAnswerC(Boolean answerC) {
		this.answerC = answerC;
	}

	public Boolean getAnswerD() {
		return answerD;
	}

	public void setAnswerD(Boolean answerD) {
		this.answerD = answerD;
	}

	public Boolean getAnswerE() {
		return answerE;
	}

	public void setAnswerE(Boolean answerE) {
		this.answerE = answerE;
	}

	public Boolean getAnswerF() {
		return answerF;
	}

	public void setAnswerF(Boolean answerF) {
		this.answerF = answerF;
	}

	public String getAnalysis() {
		return analysis;
	}

	public void setAnalysis(String analysis) {
		this.analysis = analysis;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Boolean getFlag() {
		return flag;
	}

	public void setFlag(Boolean flag) {
		this.flag = flag;
	}

	public long getSection_id() {
		return section_id;
	}

	public void setSection_id(long section_id) {
		this.section_id = section_id;
	}

	@Override
	public String toString() {
		return "MultiChoice [id=" + id + ", question_stem=" + question_stem + ", optionA=" + optionA + ", optionB="
				+ optionB + ", optionC=" + optionC + ", optionD=" + optionD + ", optionE=" + optionE + ", optionF="
				+ optionF + ", answerA=" + answerA + ", answerB=" + answerB + ", answerC=" + answerC + ", answerD="
				+ answerD + ", answerE=" + answerE + ", answerF=" + answerF + ", analysis=" + analysis + ", remark="
				+ remark + ", flag=" + flag + ", section_id=" + section_id + "]";
	}

}
