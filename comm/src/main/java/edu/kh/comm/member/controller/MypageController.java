package edu.kh.comm.member.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/member/myPage")
public class MypageController {
	
	private Logger logger = LoggerFactory.getLogger(MypageController.class);

	
	@GetMapping("/info") 
	public String myinfo() {
		
		return "member/myPage-info";
		
	}
	
	
	
	@GetMapping("/profile") 
	public String profile() {
		
		return "member/myPage-profile";
		
	}
	
	@GetMapping("/changePw") 
	public String changePw() {
		
		return "member/myPage-changePw";
		
	}
	
	@GetMapping("/secession") 
	public String secession() {
		
		return "member/myPage-secession";
		
	}
}
