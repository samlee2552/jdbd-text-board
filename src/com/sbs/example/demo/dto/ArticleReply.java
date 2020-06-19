package com.sbs.example.demo.dto;

public class ArticleReply extends Dto {
	private int articleId;
	private int memberId;
	private String body;

	public ArticleReply(String body, int memberId, int articleId) {
		this.body = body;
		this.memberId = memberId;
		this.articleId = articleId;
	}
	
	public ArticleReply() {

	}

	public int getArticleId() {
		return articleId;
	}

	public void setArticleId(int articleId) {
		this.articleId = articleId;
	}

	public int getMemberId() {
		return memberId;
	}

	public void setMemberId(int memberId) {
		this.memberId = memberId;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

}