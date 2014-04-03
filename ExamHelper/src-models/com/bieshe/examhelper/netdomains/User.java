package com.bieshe.examhelper.netdomains;

import java.util.HashSet;
import java.util.Set;

/**
 * User entity. @author MyEclipse Persistence Tools
 */

public class User implements java.io.Serializable {

	// Fields

	private Integer id;
	private String mail;
	private String password;
	private String nickname;
	private String realname;
	private Integer age;
	private String phone;
	private String gender;
	private String avatar;
	private String smallAvatar;
	private String userState;
	private String profession;
	private String area;
	private Integer integral;
	private Set collections = new HashSet(0);
	private Set notes = new HashSet(0);
	private Set queries = new HashSet(0);
	private Set errorquestionses = new HashSet(0);
	private Set answerqueries = new HashSet(0);
	private Set studyrecords = new HashSet(0);

	// Constructors

	/** default constructor */
	public User() {
	}

	/** minimal constructor */
	public User(String mail, String password) {
		this.mail = mail;
		this.password = password;
	}

	/** full constructor */
	public User(String mail, String password, String nickname, String realname,
			Integer age, String phone, String gender, String avatar,
			String smallAvatar, String userState, String profession,
			String area, Integer integral, Set collections, Set notes,
			Set queries, Set errorquestionses, Set answerqueries,
			Set studyrecords) {
		this.mail = mail;
		this.password = password;
		this.nickname = nickname;
		this.realname = realname;
		this.age = age;
		this.phone = phone;
		this.gender = gender;
		this.avatar = avatar;
		this.smallAvatar = smallAvatar;
		this.userState = userState;
		this.profession = profession;
		this.area = area;
		this.integral = integral;
		this.collections = collections;
		this.notes = notes;
		this.queries = queries;
		this.errorquestionses = errorquestionses;
		this.answerqueries = answerqueries;
		this.studyrecords = studyrecords;
	}

	// Property accessors

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getMail() {
		return this.mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getNickname() {
		return this.nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getRealname() {
		return this.realname;
	}

	public void setRealname(String realname) {
		this.realname = realname;
	}

	public Integer getAge() {
		return this.age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public String getPhone() {
		return this.phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getGender() {
		return this.gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getAvatar() {
		return this.avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public String getSmallAvatar() {
		return this.smallAvatar;
	}

	public void setSmallAvatar(String smallAvatar) {
		this.smallAvatar = smallAvatar;
	}

	public String getUserState() {
		return this.userState;
	}

	public void setUserState(String userState) {
		this.userState = userState;
	}

	public String getProfession() {
		return this.profession;
	}

	public void setProfession(String profession) {
		this.profession = profession;
	}

	public String getArea() {
		return this.area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public Integer getIntegral() {
		return this.integral;
	}

	public void setIntegral(Integer integral) {
		this.integral = integral;
	}

	public Set getCollections() {
		return this.collections;
	}

	public void setCollections(Set collections) {
		this.collections = collections;
	}

	public Set getNotes() {
		return this.notes;
	}

	public void setNotes(Set notes) {
		this.notes = notes;
	}

	public Set getQueries() {
		return this.queries;
	}

	public void setQueries(Set queries) {
		this.queries = queries;
	}

	public Set getErrorquestionses() {
		return this.errorquestionses;
	}

	public void setErrorquestionses(Set errorquestionses) {
		this.errorquestionses = errorquestionses;
	}

	public Set getAnswerqueries() {
		return this.answerqueries;
	}

	public void setAnswerqueries(Set answerqueries) {
		this.answerqueries = answerqueries;
	}

	public Set getStudyrecords() {
		return this.studyrecords;
	}

	public void setStudyrecords(Set studyrecords) {
		this.studyrecords = studyrecords;
	}

}