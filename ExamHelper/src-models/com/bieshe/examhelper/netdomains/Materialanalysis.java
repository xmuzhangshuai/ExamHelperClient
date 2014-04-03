package com.bieshe.examhelper.netdomains;

import java.util.HashSet;
import java.util.Set;

/**
 * Materialanalysis entity. @author MyEclipse Persistence Tools
 */

public class Materialanalysis implements java.io.Serializable {

	// Fields

	private Integer id;
	private Section section;
	private String material;
	private String materialImage;
	private String remark;
	private Set questionsofmaterials = new HashSet(0);

	// Constructors

	/** default constructor */
	public Materialanalysis() {
	}

	/** minimal constructor */
	public Materialanalysis(String material) {
		this.material = material;
	}

	/** full constructor */
	public Materialanalysis(Section section, String material,
			String materialImage, String remark, Set questionsofmaterials) {
		this.section = section;
		this.material = material;
		this.materialImage = materialImage;
		this.remark = remark;
		this.questionsofmaterials = questionsofmaterials;
	}

	// Property accessors

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Section getSection() {
		return this.section;
	}

	public void setSection(Section section) {
		this.section = section;
	}

	public String getMaterial() {
		return this.material;
	}

	public void setMaterial(String material) {
		this.material = material;
	}

	public String getMaterialImage() {
		return this.materialImage;
	}

	public void setMaterialImage(String materialImage) {
		this.materialImage = materialImage;
	}

	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Set getQuestionsofmaterials() {
		return this.questionsofmaterials;
	}

	public void setQuestionsofmaterials(Set questionsofmaterials) {
		this.questionsofmaterials = questionsofmaterials;
	}

}