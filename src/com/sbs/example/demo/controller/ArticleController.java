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
		} else if (reqeust.getActionName().equals("modify")) {
			actionModify(reqeust);
		} else if (reqeust.getActionName().equals("delete")) {
			actionDelete(reqeust);
		} else if (reqeust.getActionName().equals("detail")) {
			actionDetail(reqeust);
		} else if (reqeust.getActionName().equals("listBoard")) {
			actionListBoard(reqeust);
		} else if (reqeust.getActionName().equals("changeBoard")) {
			actionChangeBoard(reqeust);
		} else if (reqeust.getActionName().equals("currentBoard")) {
			actionCurrentBoard(reqeust);
		} else if (reqeust.getActionName().equals("makeBoard")) {
			actionMakeBoard(reqeust);
		} else if (reqeust.getActionName().equals("writeReply")) {
			actionWriteReply(reqeust);
		} else if (reqeust.getActionName().equals("modifyReply")) {
			actionModifyReply(reqeust);
		} else if (reqeust.getActionName().equals("deleteReply")) {
			actionDeleteReply(reqeust);
		}
	}

	private void actionDeleteReply(Request reqeust) {
		int articleId = Integer.parseInt(reqeust.getArg1());
		articleService.deleteReply(articleId);
	}

	private void actionModifyReply(Request reqeust) {
		int articleId = Integer.parseInt(reqeust.getArg1());

		System.out.printf("댓글 수정: ");
		String newBody = Factory.getScanner().nextLine();
		articleService.modifyReply(articleId, newBody);
	}

	private void actionWriteReply(Request reqeust) {
		int id = Integer.parseInt(reqeust.getArg1());
		int articleId = articleService.getArticleById(id).getId();
		int memberId = Factory.getSession().getLoginedMember().getId();

		System.out.printf("댓글 내용: ");
		String body = Factory.getScanner().nextLine();
		articleService.writeReply(body, memberId, articleId);
	}

	private void actionListBoard(Request reqeust) {
		System.out.println("== 게시판 리스트 ==");
		List<Board> boards = articleService.getBoards();
		for (Board board : boards) {
			System.out.printf("id: %d, name: %s, code: %s\n", board.getId(), board.getName(), board.getCode());
		}
	}

	private void actionMakeBoard(Request reqeust) {
		System.out.println("==게시판 생성==");
		System.out.printf("게시판 이름: ");
		String name = Factory.getScanner().nextLine().trim();
		System.out.printf("게시판 코드: ");
		String code = Factory.getScanner().nextLine().trim();
		if (articleService.makeBoard(name, code) == -1) {
			System.out.println("이미 사용중인 코드입니다");
		} else {
			articleService.makeBoard(name, code);
			System.out.printf("%s 게시판이 생성되었습니다.\n", name);
		}
	}

	private void actionDetail(Request reqeust) {
		int id ;
		
		try {
			id = Integer.parseInt(reqeust.getArg1());
		} catch (NumberFormatException e) {
			System.out.println("숫자를 입력해주세요");
			return;
		}
		System.out.println("==게시물 상세==");
		Article article = articleService.getArticleById(id);
		if (article == null) {
			System.out.println("게시물이 존재하지 않습니다");
			return;
		}
		String writerName = Factory.getMemberService().getMember(article.getMemberId()).getName();
//		System.out.println(member.getName());
//		String writerName = member.getName();
		System.out.printf("번호 : %d | 작성자: %s\n제목: %s | 내용: %s\n", article.getId(), writerName, article.getTitle(), article.getBody());
	}

	private void actionDelete(Request reqeust) {
		int id = Integer.parseInt(reqeust.getArg1());
		articleService.delete(id);
	}

	private void actionModify(Request reqeust) {
		int id = Integer.parseInt(reqeust.getArg1());
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