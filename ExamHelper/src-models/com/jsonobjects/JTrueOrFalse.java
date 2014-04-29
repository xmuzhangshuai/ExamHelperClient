package com.jsonobjects;


/**
 * Entity mapped to table TRUE_OR_FALSE.
 */
public class JTrueOrFalse extends JQuestion {

	private Long id;
	/** Not-null value. */
	private String question_stem;
	private Boolean answer;
	private String analysis;
	private String remark;
	private Boolean flag;
	private long section_id;

	public JTrueOrFalse() {
	}

	public JTrueOrFalse(Long id) {
		this.id = id;
	}

	public JTrueOrFalse(Long id, String question_stem, Boolean answer, String analysis, String remark, Boolean flag,
			long section_id) {
		this.id = id;
		this.question_stem = question_stem;
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

	public Boolean getAnswer() {
		return answer;
	}

	public void setAnswer(Boolean answer) {
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
		return "TrueOrFalse [id=" + id + ", question_stem=" + question_stem + ", answer=" + answer + ", analysis="
				+ analysis + ", remark=" + remark + ", flag=" + flag + ", section_id=" + section_id + "]";
	}

}
