package com.netdomains;

import java.util.HashSet;
import java.util.Set;

/**
 * Questiontype entity. @author MyEclipse Persistence Tools
 */

public class Questiontype implements java.io.Serializable {

	// Fields

	private Integer id;
	private String typeName;
	private Set examsections = new HashSet(0);
	private Set groups = new HashSet(0);
	private Set studyrecords = new HashSet(0);
	private Set collections = new HashSet(0);
	private Set queries = new HashSet(0);
	private Set notes = new HashSet(0);
	private Set errorquestionses = new HashSet(0);

	// Constructors

	/** default constructor */
	public Questiontype() {
	}

	/** minimal constructor */
	public Questiontype(String typeName) {
		this.typeName = typeName;
	}

	/** full constructor */
	public Questiontype(String typeName, Set examsections, Set groups,
			Set studyrecords, Set collections, Set queries, Set notes,
			Set errorquestionses) {
		this.typeName = typeName;
		this.examsections = examsections;
		this.groups = groups;
		this.studyrecords = studyrecords;
		this.collections = collections;
		this.queries = queries;
		this.notes = notes;
		this.errorquestionses = errorquestionses;
	}

	// Property accessors

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getTypeName() {
		return this.typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public Set getExamsections() {
		return this.examsections;
	}

	public void setExamsections(Set examsections) {
		this.examsections = examsections;
	}

	public Set getGroups() {
		return this.groups;
	}

	public void setGroups(Set groups) {
		this.groups = groups;
	}

	public Set getStudyrecords() {
		return this.studyrecords;
	}

	public void setStudyrecords(Set studyrecords) {
		this.studyrecords = studyrecords;
	}

	public Set getCollections() {
		return this.collections;
	}

	public void setCollections(Set collections) {
		this.collections = collections;
	}

	public Set getQueries() {
		return this.queries;
	}

	public void setQueries(Set queries) {
		this.queries = queries;
	}

	public Set getNotes() {
		return this.notes;
	}

	public void setNotes(Set notes) {
		this.notes = notes;
	}

	public Set getErrorquestionses() {
		return this.errorquestionses;
	}

	public void setErrorquestionses(Set errorquestionses) {
		this.errorquestionses = errorquestionses;
	}

}