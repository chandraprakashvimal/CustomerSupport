
package com.Tapzo.CustomerSupport;

import java.util.HashMap;
import java.util.List;
import java.util.Map;



public class AnswerResponse {

	private Answer answer;
	private List<String> answerParams = null;
	private Object DeepLinkParams;
	public Object getDeepLinkParams() {
		return DeepLinkParams;
	}

	public void setDeepLinkParams(Object deepLinkParams) {
		DeepLinkParams = deepLinkParams;
	}

	private Object faqCtaDTO;
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	public Answer getAnswer() {
		return answer;
	}

	public void setAnswer(Answer answer) {
		this.answer = answer;
	}

	public List<String> getAnswerParams() {
		return answerParams;
	}

	public void setAnswerParams(List<String> answerParams) {
		this.answerParams = answerParams;
	}

	public Object getFaqCtaDTO() {
		return faqCtaDTO;
	}

	public void setFaqCtaDTO(Object faqCtaDTO) {
		this.faqCtaDTO = faqCtaDTO;
	}

	public Map<String, Object> getAdditionalProperties() {
		return this.additionalProperties;
	}

	public void setAdditionalProperty(String name, Object value) {
		this.additionalProperties.put(name, value);
	}

}

