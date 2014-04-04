package com.netdomains;

import java.util.HashSet;
import java.util.Set;

/**
 * Section entity. @author MyEclipse Persistence Tools
 */

public class Section implements java.io.Serializable {

	// Fields

	private Integer id;
	private String sectionName;
	private Integer subjectId;
	private Set groups = new HashSet(0);
	private Set singlechoices = new HashSet(0);
	private Set trueorfalses = new HashSet(0);
	private Set multichoices = new HashSet(0);
	private Set collections = new HashSet(0);
	private Set materialanalysises = new HashSet(0);
	private Set errorquestionses = new HashSet(0);
	

	// Constructors

	/** default constructor */
	public Section() {
	}

	/** minimal constructor */
	public Section(String sectionName, Integer subjectId) {
		this.sectionName = sectionName;
		this.subjectId = subjectId;
	}

	/** full constructor */
	public Section(String sectionName, Integer subjectId, Set groups,
			Set singlechoices, Set trueorfalses, Set multichoices,
			Set collections, Set materialanalysises, Set errorquestionses) {
		this.sectionName = sectionName;
		this.subjectId = subjectId;
		this.groups = groups;
		this.singlechoices = singlechoices;
		this.trueorfalses = trueorfalses;
		this.multichoices = multichoices;
		this.collections = collections;
		this.materialanalysises = materialanalysises;
		this.errorquestionses = errorquestionses;
	}

	// Property accessors

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getSectionName() {
		return this.sectionName;
	}

	public void setSectionName(String sectionName) {
		this.sectionName = sectionName;
	}

	public Integer getSubjectId() {
		return this.subjectId;
	}

	public void setSubjectId(Integer subjectId) {
		this.subjectId = subjectId;
	}

	public Set getGroups() {
		return this.groups;
	}

	public void setGroups(Set groups) {
		this.groups = groups;
	}

	public Set getSinglechoices() {
		return this.singlechoices;
	}

	public void setSinglechoices(Set singlechoices) {
		this.singlechoices = singlechoices;
	}

	public Set getTrueorfalses() {
		return this.trueorfalses;
	}

	public void setTrueorfalses(Set trueorfalses) {
		this.trueorfalses = trueorfalses;
	}

	public Set getMultichoices() {
		return this.multichoices;
	}

	public void setMultichoices(Set multichoices) {
		this.multichoices = multichoices;
	}

	public Set getCollections() {
		return this.collections;
	}

	public void setCollections(Set collections) {
		this.collections = collections;
	}

	public Set getMaterialanalysises() {
		return this.materialanalysises;
	}

	public void setMaterialanalysises(Set materialanalysises) {
		this.materialanalysises = materialanalysises;
	}

	public Set getErrorquestionses() {
		return this.errorquestionses;
	}

	public void setErrorquestionses(Set errorquestionses) {
		this.errorquestionses = errorquestionses;
	}

}