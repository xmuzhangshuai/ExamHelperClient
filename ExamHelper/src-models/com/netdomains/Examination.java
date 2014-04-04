package com.netdomains;

import java.util.HashSet;
import java.util.Set;

/**
 * Examination entity. @author MyEclipse Persistence Tools
 */

public class Examination implements java.io.Serializable {

	// Fields

	private Integer id;
	private Subject subject;
	private String examType;
	private String examName;
	private String examRequest;
	private Integer examTime;
	private Set examsections = new HashSet(0);

	// Constructors

	/** default constructor */
	public Examination() {
	}

	/** minimal constructor */
	public Examination(Subject subject) {
		this.subject = subject;
	}

	/** full constructor */
	public Examination(Subject subject, String examType, String examName,
			String examRequest, Integer examTime, Set examsections) {
		this.subject = subject;
		this.examType = examType;
		this.examName = examName;
		this.examRequest = examRequest;
		this.examTime = examTime;
		this.examsections = examsections;
	}

	// Property accessors

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Subject getSubject() {
		return this.subject;
	}

	public void setSubject(Subject subject) {
		this.subject = subject;
	}

	public String getExamType() {
		return this.examType;
	}

	public void setExamType(String examType) {
		this.examType = examType;
	}

	public String getExamName() {
		return this.examName;
	}

	public void setExamName(String examName) {
		this.examName = examName;
	}

	public String getExamRequest() {
		return this.examRequest;
	}

	public void setExamRequest(String examRequest) {
		this.examRequest = examRequest;
	}

	public Integer getExamTime() {
		return this.examTime;
	}

	public void setExamTime(Integer examTime) {
		this.examTime = examTime;
	}

	public Set getExamsections() {
		return this.examsections;
	}

	public void setExamsections(Set examsections) {
		this.examsections = examsections;
	}

}