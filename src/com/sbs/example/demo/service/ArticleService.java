package com.sbs.example.demo.service;

import java.util.List;

import com.sbs.example.demo.dao.ArticleDao;
import com.sbs.example.demo.dto.Article;
import com.sbs.example.demo.dto.ArticleReply;
import com.sbs.example.demo.dto.Board;
import com.sbs.example.demo.factory.Factory;

public class ArticleService {
	private ArticleDao articleDao;

	public ArticleService() {
		articleDao = Factory.getArticleDao();
	}

	public List<Article> getArticlesByBoardCode(String code) {
		return articleDao.getArticlesByBoardCode(code);
	}

	public List<Board> getBoards() {
		return articleDao.getBoards();
	}

	public int makeBoard(String name, String code) {
		Board oldBoard = articleDao.getBoardByCode(code);

		if (oldBoard != null) {
			return -1;
		}

		Board board = new Board(name, code);
		return articleDao.saveBoard(board);
	}

	public Board getBoard(int id) {
		return articleDao.getBoard(id);
	}

	public int write(int boardId, int memberId, String title, String body) {
		Article article = new Article(boardId, memberId, title, body);
		return articleDao.save(article);
	}

	public List<Article> getArticles() {
		return articleDao.getArticles();
	}

	public void makeBoardIfNotExists(String name, String code) {
		Board board = articleDao.getBoardByCode(code);
		
		if ( board == null ) {
			makeBoard(name, code);
		}
	}

	public Board getBoardByCode(String boardCode) {
		return articleDao.getBoardByCode(boardCode);
	}

	public void delete(int id) {
		articleDao.delete(id);
	}

	public void modify(int id, String title, String body) {
		articleDao.modify(id, title, body);
	}

	public Article getArticleById(int id) {
		return articleDao.getArticleById(id);
	}
	

	public void writeReply(String body, int memberId, int articleId) {
		ArticleReply reply = new ArticleReply(body, memberId, articleId);
		articleDao.saveReply(reply);
	}

	public void modifyReply(int articleId, String newBody) {
		articleDao.modifyReply(articleId, newBody);
		
	}

	public void deleteReply(int articleId) {
		articleDao.deleteReply(articleId);
	}
}