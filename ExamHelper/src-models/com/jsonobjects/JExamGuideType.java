package com.jsonobjects;

public class JExamGuideType {

	private Integer id;
	private Integer subjectId;
	private String typeName;

	public JExamGuideType() {
		// TODO Auto-generated constructor stub
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public int getSubjectId() {
		return subjectId;
	}

	public void setSubjectId(int subjectId) {
		this.subjectId = subjectId;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public JExamGuideType(Integer id, int subjectId, String typeName) {
		super();
		this.id = id;
		this.subjectId = subjectId;
		this.typeName = typeName;
	}

}
