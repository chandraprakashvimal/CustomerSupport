package com.Tapzo.CustomerSupport;

import java.util.HashMap;
import java.util.Map;

public class Topics {

	private Integer id;
	private Object defaultImage;
	private Integer type;
	private String question;
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Object getDefaultImage() {
		return defaultImage;
	}

	public void setDefaultImage(Object defaultImage) {
		this.defaultImage = defaultImage;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	public Map<String, Object> getAdditionalProperties() {
		return this.additionalProperties;
	}

	public void setAdditionalProperty(String name, Object value) {
		this.additionalProperties.put(name, value);
	}

}