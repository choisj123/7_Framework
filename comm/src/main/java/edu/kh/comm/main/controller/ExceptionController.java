package edu.kh.comm.main.controller;

import java.io.IOException;
import java.sql.SQLException;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionController {
	
//	@ExceptionHandler(Exception.class)
//	public String exceptionHandler(Exception e, Model model) {
//		e.printStackTrace();
//		
//		model.addAttribute("errorMessage", "서비스 이용 중 문제가 발생했습니다.");
//		model.addAttribute("e", e);
//		
//		return "common/error";
//		
//	}
	
	@ExceptionHandler(SQLException.class)
	public String SQLExceptionHandler(Exception e, Model model) {
		e.printStackTrace();
		
		model.addAttribute("errorMessage", "SQL 이용 중 문제가 발생했습니다.");
		model.addAttribute("e", e);
		
		return "common/error";
		
	}
	
	@ExceptionHandler(RuntimeException.class)
	public String IOExceptionHandler(Exception e, Model model) {
		e.printStackTrace();
		
		model.addAttribute("errorMessage", "IO 이용 중 문제가 발생했습니다.");
		model.addAttribute("e", e);
		
		return "common/error";
		
	}

}
