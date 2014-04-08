package com.jsonobjects;

/**
 * Entity mapped to table SINGLE_CHOICE.
 */
public class SingleChoice extends Question {

	private Long id;
	/** Not-null value. */
	private String question_stem;
	private String optionA;
	private String optionB;
	private String optionC;
	private String optionD;
	private String optionE;
	private String answer;
	private String analysis;
	private String remark;
	private Boolean flag;
	private long section_id;

	public SingleChoice() {
	}

	public SingleChoice(Long id) {
		this.id = id;
	}

	public SingleChoice(Long id, String question_stem, String optionA, String optionB, String optionC, String optionD,
			String optionE, String answer, String analysis, String remark, Boolean flag, long section_id) {
		this.id = id;
		this.question_stem = question_stem;
		this.optionA = optionA;
		this.optionB = optionB;
		this.optionC = optionC;
		this.optionD = optionD;
		this.optionE = optionE;
		this.answer = answer;
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
		return "SingleChoice [id=" + id + ", question_stem=" + question_stem + ", optionA=" + optionA + ", optionB="
				+ optionB + ", optionC=" + optionC + ", optionD=" + optionD + ", optionE=" + optionE + ", answer="
				+ answer + ", analysis=" + analysis + ", remark=" + remark + ", flag=" + flag + ", section_id="
				+ section_id + "]";
	}

}
