package com.bishe.examhelper.test;


public class ExamGuide {
	private Long id;
	private String title;
	private String content;
	private Long typeId;

	public ExamGuide() {
		// TODO Auto-generated constructor stub
	}

	public ExamGuide(Long id, String title, String content, Long typeId) {
		super();
		this.id = id;
		this.title = title;
		this.content = content;
		this.typeId = typeId;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Long getTypeId() {
		return typeId;
	}

	public void setTypeId(Long typeId) {
		this.typeId = typeId;
	}

}
