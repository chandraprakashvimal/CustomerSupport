package com.Tapzo.CustomerSupport;

import java.util.List;

class FaqReponse {

	int categoryId;
	String categoryName;
	String transactionDetails;
	List<Topics> topics;

	public List<Topics> getTopics() {
		return topics;
	}

	public void setTopics(List<Topics> topics) {
		this.topics = topics;
	}

	public int getCategoryId() {
		return categoryId;
	}
	
	public void setCategoryId(int categoryId) {
		this.categoryId=categoryId;
	}
	
	public int getCategoryName() {
		return categoryId;
	}
	
	public void setCategoryName(String categoryName) {
		this.categoryName=categoryName;
	}
	
	public int getTransactionDetails() {
		return categoryId;
	}
	
	public void setTransactionDetails(String transactionDetails) {
		this.transactionDetails=transactionDetails;
	}
	
	
}

