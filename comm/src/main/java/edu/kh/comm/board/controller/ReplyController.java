package edu.kh.comm.board.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.google.gson.Gson;

import edu.kh.comm.board.model.service.ReplyService;
import edu.kh.comm.board.model.vo.Reply;

// REST (Reprasentational State Transfer)
// - 자원을 이름으로 구분(Representational, 자원의 표현)하여
//   자원의 상태(State)를 주고 받는 것(Transfer)

// -> 특정한 이름(주소)로 요청이 오면 값으로 응답

// @RestController : 요청에 따른 응답이 모두 데이터(값) 자체인 컨트롤러
//  -> @Controller + @ResponseBody

@RestController
@RequestMapping("/reply")
@SessionAttributes({"loginMember"})
public class ReplyController {
	
	@Autowired
	private ReplyService service;
	
	// 댓글 목록조회
	@GetMapping("/selectReplyList")
	public String selectReplyList(int boardNo) {
		
		List<Reply> replyList = new ArrayList<>();
		
		replyList = service.selectReplyList(boardNo);
		
		return new Gson().toJson(replyList);
	}
	
	// 댓글 등록
	@PostMapping("/insert")
	public String insertReply(String replyContent,
								int memberNo, 
								int boardNo) {
		
		int result = service.insertReply(replyContent, memberNo, boardNo);
		
		return new Gson().toJson(result);
		
	}
	
	// 댓글 수정
	
	// 댓글 삭제
	@GetMapping("/delete")
	public String deleteReply(int replyNo) {
		
		int result = service.deleteReply(replyNo);
		
		return new Gson().toJson(result);
		
	}

}
