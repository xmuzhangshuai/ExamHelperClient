package com.jsonobjects;


import java.sql.Timestamp;

/**
 * Systemnotice entity. @author MyEclipse Persistence Tools
 */

public class Systemnotice implements java.io.Serializable {

	// Fields

	private Integer id;
	private String noticeContent;
	private String url;
	private Timestamp time;
	private Boolean valid;
	private Boolean haveDetail;

	// Constructors

	/** default constructor */
	public Systemnotice() {
	}

	/** full constructor */
	public Systemnotice(String noticeContent, String url, Timestamp time, Boolean valid, Boolean haveDetail) {
		this.noticeContent = noticeContent;
		this.url = url;
		this.time = time;
		this.valid = valid;
		this.haveDetail = haveDetail;
	}

	// Property accessors

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNoticeContent() {
		return this.noticeContent;
	}

	public void setNoticeContent(String noticeContent) {
		this.noticeContent = noticeContent;
	}

	public String getUrl() {
		return this.url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Timestamp getTime() {
		return this.time;
	}

	public void setTime(Timestamp time) {
		this.time = time;
	}

	public Boolean getValid() {
		return this.valid;
	}

	public void setValid(Boolean valid) {
		this.valid = valid;
	}

	public Boolean getHaveDetail() {
		return this.haveDetail;
	}

	public void setHaveDetail(Boolean haveDetail) {
		this.haveDetail = haveDetail;
	}

}