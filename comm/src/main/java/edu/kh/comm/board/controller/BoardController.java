package edu.kh.comm.board.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import edu.kh.comm.member.controller.MypageController;

@Controller
@RequestMapping("/board")
public class BoardController {
	
private Logger logger = LoggerFactory.getLogger(BoardController.class);

	
	@GetMapping("/list") 
	public String boardList() {
		
		return "board/boardList";
		
	}
	

}
