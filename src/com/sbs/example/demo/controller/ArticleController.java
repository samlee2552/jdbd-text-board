package com.sbs.example.demo.controller;

import java.util.List;

import com.sbs.example.demo.dto.Article;
import com.sbs.example.demo.dto.Board;
import com.sbs.example.demo.factory.Factory;
import com.sbs.example.demo.service.ArticleService;

public class ArticleController extends Controller {
	private ArticleService articleService;

	public ArticleController() {
		articleService = Factory.getArticleService();
	}

	public void doAction(Request reqeust) {
		if (reqeust.getActionName().equals("list")) {
			actionList(reqeust);
		} else if (reqeust.getActionName().equals("write")) {
			actionWrite(reqeust);
		} else if (reqeust.getActionName().equals("changeBoard")) {
			actionChangeBoard(reqeust);
		} else if (reqeust.getActionName().equals("currentBoard")) {
			actionCurrentBoard(reqeust);
		} else if (reqeust.getActionName().equals("modify")) {
			if (reqeust.getArg1() == null) {
				System.out.println("게시물 번호를 입력해 주세요.");
			} else {
				int id = Integer.parseInt(reqeust.getArg1());
				actionModify(id);
			}
		} else if (reqeust.getActionName().equals("delete")) {
			if (reqeust.getArg1() == null) {
				System.out.println("게시물 번호를 입력해 주세요.");
			} else {
				int id = Integer.parseInt(reqeust.getArg1());
				actionDelete(id);
			}
		} else if (reqeust.getActionName().equals("detail")) {
			if (reqeust.getArg1() == null) {
				System.out.println("게시물 번호를 입력해 주세요.");
			} else {
				int id = Integer.parseInt(reqeust.getArg1());
				actionDetail(id);
			}
			
		}
	}

	private void actionDetail(int id) {
		Article article = articleService.getArticleById(id);
		if(article == null) {
			System.out.println("게시물이 존재하지 않습니다");
		} else {
			System.out.println(article);
		}
	}

	private void actionDelete(int id) {
		articleService.delete(id);
	}

	private void actionModify(int id) {
		System.out.println("==게시물 수정==");
		System.out.print("새 제목 : ");
		String title = Factory.getScanner().nextLine().trim();
		System.out.print("새 내용 : ");
		String body = Factory.getScanner().nextLine().trim();
		articleService.modify(id, title, body);
	}

	private void actionCurrentBoard(Request reqeust) {
		Board board = Factory.getSession().getCurrentBoard();
		System.out.printf("현재 게시판 : %s\n", board.getName());
	}

	private void actionChangeBoard(Request reqeust) {
		String boardCode = reqeust.getArg1();

		Board board = articleService.getBoardByCode(boardCode);

		if (board == null) {
			System.out.println("해당 게시판이 존재하지 않습니다.");
		} else {
			System.out.printf("%s 게시판으로 변경되었습니다.\n", board.getName());
			Factory.getSession().setCurrentBoard(board);
		}
	}

	private void actionList(Request reqeust) {
		Board currentBoard = Factory.getSession().getCurrentBoard();
		List<Article> articles = articleService.getArticlesByBoardCode(currentBoard.getCode());

		System.out.printf("== %s 게시물 리스트 시작 ==\n", currentBoard.getName());
		for (Article article : articles) {
			System.out.printf("%d, %s, %s\n", article.getId(), article.getRegDate(), article.getTitle());
		}
		System.out.printf("== %s 게시물 리스트 끝 ==\n", currentBoard.getName());
	}

	private void actionWrite(Request reqeust) {
		System.out.printf("제목 : ");
		String title = Factory.getScanner().nextLine();
		System.out.printf("내용 : ");
		String body = Factory.getScanner().nextLine();

		// 현재 게시판 id 가져오기
		int boardId = Factory.getSession().getCurrentBoard().getId();

		// 현재 로그인한 회원의 id 가져오기
		int memberId = Factory.getSession().getLoginedMember().getId();
		int newId = articleService.write(boardId, memberId, title, body);

		System.out.printf("%d번 글이 생성되었습니다.\n", newId);
	}
}