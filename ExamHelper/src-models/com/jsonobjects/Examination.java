package com.jsonobjects;

/**
 * Entity mapped to table EXAMINATION.
 */
public class Examination {

	private Long id;
	private String exam_type;
	private String exam_name;
	private String exam_request;
	private Integer exam_time;
	private long subject_id;

	public Examination() {
	}

	public Examination(Long id) {
		this.id = id;
	}

	public Examination(Long id, String exam_type, String exam_name, String exam_request, Integer exam_time,
			long subject_id) {
		this.id = id;
		this.exam_type = exam_type;
		this.exam_name = exam_name;
		this.exam_request = exam_request;
		this.exam_time = exam_time;
		this.subject_id = subject_id;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getExam_type() {
		return exam_type;
	}

	public void setExam_type(String exam_type) {
		this.exam_type = exam_type;
	}

	public String getExam_name() {
		return exam_name;
	}

	public void setExam_name(String exam_name) {
		this.exam_name = exam_name;
	}

	public String getExam_request() {
		return exam_request;
	}

	public void setExam_request(String exam_request) {
		this.exam_request = exam_request;
	}

	public Integer getExam_time() {
		return exam_time;
	}

	public void setExam_time(Integer exam_time) {
		this.exam_time = exam_time;
	}

	public long getSubject_id() {
		return subject_id;
	}

	public void setSubject_id(long subject_id) {
		this.subject_id = subject_id;
	}

}
