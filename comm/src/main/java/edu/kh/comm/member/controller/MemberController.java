package edu.kh.comm.member.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.mail.internet.MimeMessage;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import edu.kh.comm.member.model.service.MemberService;
import edu.kh.comm.member.model.vo.Member;

import com.google.gson.Gson;

// POJO 기반 프레임워크 : 외부 라이브러리 상속 X == extends 하는 건 POJO 원칙 위배

// class  : 객체를 만들기 위한 설계도
// -> 객체로 생성 되어야지 기능 수행 가능하다.
// --> IOC (제어의 역전, 객체 생명주기를 스프링이 관리)를 이용하여 객체 생성
// ** 이때, 스프링이 생성한 객체를 bean이라고 한다. **

// bean 등록 == 스프링이 객체로 만들어서 가지고 있어라
// @Component // 해당 클래스를 bean으로 등록하라는 프로그램에게 알려주는 주석 (Annotation)

@Controller // 생성된 bean이 Controller임을 명시 + bean 등록
@RequestMapping("/member") // localhost:8080/comm/member 이하의 요청을 처리하는 컨트롤러
//localhost:8080/comm/member
//localhost:8080/comm/member/login
//localhost:8080/comm/member/signUp

@SessionAttributes( {"loginMember"} ) // model에 추가된 값의 key와 어노테이션에 작성된 값이 같으면
												// 해당 값을 session scope로 이동시키는 역할
public class MemberController {
	
	private Logger logger = LoggerFactory.getLogger(MemberController.class);
	
	// private MemberService service = new MemberServiceImpl();
	//			부모						자식   => 다형성
	// new 연산자를 통해 개발자가 직접 객체 생성 X, 스프링이 해준다!
	// -> 특징 : IOC (제어의 역전) -> MemberServiceImpl 가서 bean 지정!
	
	@Autowired // bean으로 등록된 객체 중 타입이 같거나, 상속관계인 bean을 주입해주는 역할
  	private MemberService service; // DI (의존성 주입)
	
	// 이메일 인증
	@Autowired
	private JavaMailSender mailSender;
	
	
	// Controller : 요청/응답을 제어하는 역할을 하는 클래스 
	
	/* @RequestMapping : 클라이언트의 요청(url)에 맞는 클래스 or 메소드를 연결시켜주는 어노테이션
	 * 
	 * [ 위치에 따른 해석 ]
	 * - 클래스 레벨 : 공통 주소 
	 * - 메소드 레벨 : 공통 주소 외 나머지 주소
	 * 
	 * 단, 클래스레벨에 @RequestMapping이 존재하지 않는다면 (@RequestMapping("/member"))
	 * - 메소드 레벨 : 단독 요청 처리 주소 (localhost:8080/comm/login)
	 * 
	 * [ 작성법에 따른 해석 ]
	 * 1) @RequestMapping("url")
	 * --> 요청 방식(GET/POST) 관계 없이 url이 일치하는 요청 처리
	 * 
	 * 2) @RequestMapping(value = "url", method = RequestMethod.GET | POST)
	 * --> 요청 방식에 따라 요청 처리함
	 * 
	 * ** 매소드 레벨에서 GET/POST 방식을 구분하여 매핑할 경우 **
	 * @GetMapping("url") / @PostMappting("url") 사용하는 것이 일반적
	 * (메소드 레벨에서만 작성 가능!)
	 * 
	 */
	
	// 로그인
	// 요청 시 파라미터를 얻어오는 방법 1
	// -> HttpServletRequest 이용
	
	
//	@RequestMapping("/login")
//	public String login(HttpServletRequest req) {
//		logger.info("로그인 요청됨");
//		
//		String inputEmail = req.getParameter("inputEmail");
//		String inputPw = req.getParameter("inputPw");
//		
//		logger.debug("inputEmail : " + inputEmail);
//		logger.debug("inputPw : " + inputPw);
//		
//		return "redirect:/"; // sendRedirect 안써도 된다. 이게 리다이렉트 구문임
//	}
	
	// 요청 시 파라미터를 얻어오는 방법 2
	// -> @RequestParam("name 속성값") 자료형 변수명
	// - 클라이언트 요청 시 같이 전달된 파라미터를 변수에 저장
	// --> 어떤 파라미터를 변수에 저장할지는 "name속성값"을 이용해 지정
	
	// 매개변수 지정 시 데이터 타입 파싱을 자유롭게 진행할 수 있음 ex) String -> int로 변환
	
	// [속성]
	// value : input 태그의 name 속성값
	// required : 입력된 name 속성값이 필수적으로 파라미터에 포함되어야 하는지 지정
	//			required= true(기본값) / false
	// defaultValue : required가 false인 상태에서 파라미터가 존재하지 않을 경우의 값을 지정
	
//	@RequestMapping("/login")
//	public String login(@RequestParam("inputEmail") String inputEmail,
//						@RequestParam("inputPw") String inputPw,
//						@RequestParam(value="inputName", required = false, defaultValue="홍길동") String inputName
//						) {
//		logger.debug("email: " + inputEmail);
//		logger.debug("pw: " + inputPw);
//		logger.debug("name: " + inputName);
//		
//		// email 숫자만 입력받는다고 가정
////		logger.debug(inputEmail + 100);
//		
//		return "redirect:/"; 
//		
//	}

	// 요청 시 파라미터를 얻어오는 방법 3
	// -> @ModelAttribute 어노테이션 사용
	
	// @ModelAttribute VO타입 변수명
	// -> 파라미터 중 name 속성값이 VO의 필드와 일치하면
	// 해당 VO 객체의 필드에 값을 세팅
	
	// *** @ModelAttribute를 이용해서 객체에 값을 직접 담는 경우에 대한 주의사항 ***
	// -- 반드시 필요한 내용
	// - VO 기본 생성자
	// - VO 필드에 대한 Setter
	
	@PostMapping("/login")
	public String login(/*@ModelAttribute()*/ Member inputMember,
						Model model,
						RedirectAttributes ra,
						HttpServletResponse resp,
						HttpServletRequest req,
						@RequestParam(value="saveId", required=false) String saveId
						) {
		// @ModelAttribute 생략 가능
		// 커맨드 객체
		// @ModelAttribute 생략된 상태에서 파라미터가 필드에 세팅된 객체
		
		// break point 지정
		// debug as > debug on sever
		logger.info("로그인 기능 수행됨");
		
		// 아이디, 비밀번호가 일치하는 회원 정보를 조회하는 Service 호출 후 결과 반환 받기
		Member loginMember = service.login(inputMember);
		
		/* Model : 데이터를 맵 형식(K:V)형태로 담아 전달하는 용도의 객체
		 * -> request, session을 대체하는 객체
		 * 
		 * - 기본 scope : request
		 * - session scope로 변환하고 싶은 경우
		 * 클래스 레벨로 @SessionAttributes를 작성하면 된다. 
		 * == session.setAttribute("loginMember", loginMember);
		 *
		 * 즉, @SessionAttributes 미작성 -> request scope
		 */
		if(loginMember != null) { //로그인 성공 시
			
			model.addAttribute("loginMember", loginMember); // == request scope
			// == req.setAttribute("loginMember", loginMember);
			
			// 로그인 성공 시 무조건 쿠키 생성
			// 단, 아이디 저장 체크 여부에 따라서 쿠키의 유지시간을 조정
			
			Cookie cookie = new Cookie("saveId", loginMember.getMemberEmail());
			
			if(saveId != null) { // 아이디 저장 체크 되었을 때
				
				cookie.setMaxAge(60 * 60 * 24 * 365); //초단위 지정(1년)
				
			}else { // 체크되지 않았을 때 
				cookie.setMaxAge(0); // 0초 -> 생성되자마자 사라짐 == 쿠키 삭제
				
			}
			
			// 쿠키가 적용될 범위(경로) 지정
			cookie.setPath(req.getContextPath()); // /comm 아래로는 전부 쿠키 저장
			
			// 쿠키를 응답 시 클라이언트에게 전달
			resp.addCookie(cookie);
			
			
			
		}else {
			//model.addAttribute("message", "아이디 또는 비밀번호가 일치하지 않습니다.");
			// 이렇게만 쓰면 request scope 
			// session scope로 쓰려면 꼭 @SessionAttributes에 써주기!
			
			
			// redirect 시에도 request scope로 세팅된 데이터가 유지될 수 있도록 하는 방법을
			// Spring에서 제공해줌
			// -> RedirectAttributes 객체(컨트롤러 매개변수에 작성하면 사용 가능)
			ra.addFlashAttribute("message", "아이디 또는 비밀번호가 일치하지 않습니다.");
			
		}
		
		
		return "redirect:/"; // 메인페이지 재요청
	}
	
	
	
	// 로그아웃
	@GetMapping("/logout")
	public String logout(/*HttpSession session*/ 
						SessionStatus status) {
		// 로그아웃 == 세션을 없애는 것
		
		// * @SessionAttributes을 이용해서 session scope에 배치된 데이터는
		// 	 SessionStatus라는 별도 객체를 이용해야만 없앨 수 있다.
		
		logger.info("로그아웃 수행됨");
	
		//session.invalidate(); //기존 세션 무효화 방식으로는 X
		
		status.setComplete(); // 세션이 할일이 완료됨 -> 없앰
		
		return "redirect:/"; // 메인페이지 재요청
	}
	
	
	
	
	// 회원 가입 화면 전환
	@GetMapping("/signUp") // Get 방식 : /comm/member/signUp 요청
	public String signUp() {
		
		return "member/signUp";
		
	}
	

	// 이메일 중복검사
	@ResponseBody
	@GetMapping("/emailDupCheck")
	public int emailDupCheck(/*@RequestParam("memberEmail")*/ String memberEmail) {
								// 파라미터 key값(받아오는 변수)와 저장하려는 변수 이름이 같으면 생략 가능
		// int result = service.emailDupCheck(memberEmail);
		
		// 컨트롤러에서 반환된는 값은 forward 또는 redirect를 위한 경로인 경우가 일반적
		// -> 반환되는 값은 경로로 인식됨
		// -> 이를 해결하기 위한 어노테이션 @ResponseBody가 존재함
		
		// @ResponseBody : 반환되는 값을 응답의 몸통(body)에 추가하여
		//					이전 요청 주소로 돌아감
		// -> 컨트롤러에서 반환되는 값이 경로가 아닌 "값 자체"로 인식됨
		// Ajax로 알아서 돌아감 !!
		
		return service.emailDupCheck(memberEmail);
	}
	
	// 닉네임 중복검사
	@ResponseBody
	@GetMapping("/nicknameDupCheck")
	public int nicknameDupCheck(@RequestParam("memberNickname") String memberNickname) {
		
		
		return service.nicknameDupCheck(memberNickname);
	}

	
	
	// 회원가입
	@PostMapping("/signUp")
	public String signUp(Member inputMember,
						String[] memberAddress,
						HttpServletRequest req,
						RedirectAttributes ra) {
		
		logger.info("회원가입 기능 수행됨");
		
		// inpuMember만 받아올 때
//		String[] address = req.getParameterValues("memberAddress");
//		String memberAddress = null;
//		if(!address[0].equals("")) { // 우편번호가 빈칸이 아니라면 == 주소 작성
//			memberAddress = String.join(",,", address);
//		}
//		System.out.println(memberAddress);
//		inputMember.setMemberAddress(memberAddress);
		
		
		// 커맨드 객체를 이용해서 입력된 회원 정보를 잘 받아옴
		// 단, 같은 name을 가진 주소가 하나의 문자열로 합쳐서 세팅되어 들어옴
		// -> 도로명 주소에 "," 기호가 포함되는 경우가 있어 이를 구분자로 사용할 수 없다
		//
		inputMember.setMemberAddress(String.join(",,", memberAddress));
		// String.join("구분자", 배열)
		// 배열을 하나의 문자열로 합치는 메서드
		// 값 중간중간에 들어가서 하나의 문자열로 합쳐줌
		// [a,b,c] -> join 진행 -> "a,,b,,c"
		
		if(inputMember.getMemberAddress().equals(",,,,")) { // 주소가 입력되지 않은 경우
			
			inputMember.setMemberAddress(null); // null로 변환
		}
		
		// 회원가입 서비스 호출
		int result = service.signUp(inputMember);
		
		String message = null;
		String path = null;
		
		if(result > 0) { // 회원가입 성공
			
			message = inputMember.getMemberNickname() + "님, 환영합니다!";
			path = "redirect:/"; // 메인페이지
			
		}else {
			
			message = "회원가입에 실패하였습니다.";
			path =  "redirect:/member/signUp"; //회원 가입 페이지
		}
		
		ra.addFlashAttribute("message", message);
		
		return path;
		
	}
	
	
	// 회원 1명 정보 조회(ajax)
	@ResponseBody
	@PostMapping("/selectOne")
	public String selectOne(String memberEmail) {
		
		Member member = service.selectOne(memberEmail);
		System.out.println(member);
	
		
		return new Gson().toJson(member);
		
		
	}
	
	// 회원 목록 조회(ajax)
	@ResponseBody
	@GetMapping("/selectAll")
	public String selectAll() {
		List<Member> memberList = new ArrayList<>();
		
		memberList = service.selectAll();
		
		return new Gson().toJson(memberList);
		
	}

	
	
	/* 스프링 예외 처리 방법 (3가지, 중복 사용 가능)
	 * 
	 * 1순위 : 메서드 별로 예외처리(try-catch / throws)
	 * 
	 * 2순위 : 하나의 컨트롤러에서 발생하는 예외를 모아서 처리	
	 * 		-> @ExceptionHandler (메서드에 작성)
	 * 
	 * 3순위 : 전역(웹 애플리케이션)에서 발생하는 예외를 모아서 처리
	 * 		-> @ControllerAdvice (클래스에 작성)
	 * 
	 */
	
	// 회원 컨트롤러에서 발생하는 모든 예외를 모아서 처리
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
	


	// mailSending 코드
	@GetMapping("/sendEmail")
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
        String setFrom = "admin@gmail.com"; //보내는 이메일
        String toMail = inputEmail; //받는 사람 이메일
        String title = "회원가입 인증 이메일 입니다.";
        String content = 
                "<p>kh정보교육원 홈페이지를 방문해주셔서 감사합니다.</p>" +
                "<br><br>" + 
                "<h3>인증 번호는  <span style='color:red'>" + cNumber + "</span> 입니다.</h3>" + 
                "<br>" + 
                "<p>해당 인증번호를 인증번호 확인란에 기입하여 주세요.</p>";
        
        try {
            
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "utf-8");
            helper.setFrom(setFrom); // 보내는 사람 지정
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
