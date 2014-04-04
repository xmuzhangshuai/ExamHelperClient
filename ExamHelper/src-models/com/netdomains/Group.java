package com.netdomains;

import java.util.HashSet;
import java.util.Set;

/**
 * Group entity. @author MyEclipse Persistence Tools
 */

public class Group implements java.io.Serializable {

	// Fields

	private Integer id;
	private Questiontype questiontype;
	private Section section;
	private Integer questionNum;
	private Integer finishedNum;
	private Integer errorNum;
	private String groupName;
	private Set groupquestions = new HashSet(0);

	// Constructors

	/** default constructor */
	public Group() {
	}

	/** minimal constructor */
	public Group(Questiontype questiontype, Section section) {
		this.questiontype = questiontype;
		this.section = section;
	}

	/** full constructor */
	public Group(Questiontype questiontype, Section section,
			Integer questionNum, Integer finishedNum, Integer errorNum,
			String groupName, Set groupquestions) {
		this.questiontype = questiontype;
		this.section = section;
		this.questionNum = questionNum;
		this.finishedNum = finishedNum;
		this.errorNum = errorNum;
		this.groupName = groupName;
		this.groupquestions = groupquestions;
	}

	// Property accessors

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Questiontype getQuestiontype() {
		return this.questiontype;
	}

	public void setQuestiontype(Questiontype questiontype) {
		this.questiontype = questiontype;
	}

	public Section getSection() {
		return this.section;
	}

	public void setSection(Section section) {
		this.section = section;
	}

	public Integer getQuestionNum() {
		return this.questionNum;
	}

	public void setQuestionNum(Integer questionNum) {
		this.questionNum = questionNum;
	}

	public Integer getFinishedNum() {
		return this.finishedNum;
	}

	public void setFinishedNum(Integer finishedNum) {
		this.finishedNum = finishedNum;
	}

	public Integer getErrorNum() {
		return this.errorNum;
	}

	public void setErrorNum(Integer errorNum) {
		this.errorNum = errorNum;
	}

	public String getGroupName() {
		return this.groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public Set getGroupquestions() {
		return this.groupquestions;
	}

	public void setGroupquestions(Set groupquestions) {
		this.groupquestions = groupquestions;
	}

}