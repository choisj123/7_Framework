package edu.kh.comm.member.controller;

import java.util.Random;

import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class MailController {
	
	@Autowired
	private JavaMailSender mailSender;

	// mailSending 코드
	@GetMapping("/member/sendEmail")
	@ResponseBody
	public String mailSending(String inputEmail) {

		//뷰에서 넘어왔는지 확인
		//System.out.println("이메일 전송");
		
//		//난수 생성(인증번호)
//		Random r = new Random();
//		int num = r.nextInt(888888) + 111111;  //111111 ~ 999999
//		System.out.println("인증번호:" + num);
		
        // 인증번호 6자리 생성코드(영어 대/소문 + 숫자)
        String cNumber = "";
        for(int i=0 ; i< 6 ; i++) {
           
           int sel1 = (int)(Math.random() * 3); // 0:숫자 / 1,2:영어
           
           if(sel1 == 0) {
              
              int num = (int)(Math.random() * 10); // 0~9
              cNumber += num;
              
           }else {
              
              char ch = (char)(Math.random() * 26 + 65); // A~Z
              
              int sel2 = (int)(Math.random() * 2); // 0:소문자 / 1:대문자
              
              if(sel2 == 0) {
                 ch = (char)(ch + ('a' - 'A')); // 대문자로 변경
              }
              
              cNumber += ch;
           }
           
        }
		
		/* 이메일 보내기 */
        String setFrom = "shareverni@gmail.com"; //보내는 이메일
        String toMail = inputEmail; //받는 사람 이메일
        String title = "회원가입 인증 이메일 입니다.";
        String content = 
                "kh정보교육원 홈페이지를 방문해주셔서 감사합니다." +
                "<br><br>" + 
                "인증 번호는 " + cNumber + "입니다." + 
                "<br>" + 
                "해당 인증번호를 인증번호 확인란에 기입하여 주세요.";
        
        try {
            
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "utf-8");
            helper.setFrom(setFrom);
            helper.setTo(toMail);
            helper.setSubject(title);
            helper.setText(content,true);
            mailSender.send(message);
            
        }catch(Exception e) {
            e.printStackTrace();
        }
        
//        String rnum = Integer.toString(cNumber);  //view로 다시 반환할 때 String만 가능
        
        return cNumber;
 
		
	}

}
