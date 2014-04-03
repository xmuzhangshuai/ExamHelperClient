package com.bieshe.examhelper.netdomains;

import java.util.Date;

/**
 * Note entity. @author MyEclipse Persistence Tools
 */

public class Note implements java.io.Serializable {

	// Fields

	private Integer id;
	private User user;
	private Questiontype questiontype;
	private Integer questionId;
	private Date noteTime;
	private String noteContent;

	// Constructors

	/** default constructor */
	public Note() {
	}

	/** minimal constructor */
	public Note(User user, Questiontype questiontype, Integer questionId) {
		this.user = user;
		this.questiontype = questiontype;
		this.questionId = questionId;
	}

	/** full constructor */
	public Note(User user, Questiontype questiontype, Integer questionId,
			Date noteTime, String noteContent) {
		this.user = user;
		this.questiontype = questiontype;
		this.questionId = questionId;
		this.noteTime = noteTime;
		this.noteContent = noteContent;
	}

	// Property accessors

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public User getUser() {
		return this.user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Questiontype getQuestiontype() {
		return this.questiontype;
	}

	public void setQuestiontype(Questiontype questiontype) {
		this.questiontype = questiontype;
	}

	public Integer getQuestionId() {
		return this.questionId;
	}

	public void setQuestionId(Integer questionId) {
		this.questionId = questionId;
	}

	public Date getNoteTime() {
		return this.noteTime;
	}

	public void setNoteTime(Date noteTime) {
		this.noteTime = noteTime;
	}

	public String getNoteContent() {
		return this.noteContent;
	}

	public void setNoteContent(String noteContent) {
		this.noteContent = noteContent;
	}

}