package edu.kh.comm.member.controller;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import edu.kh.comm.member.model.service.MyPageService;
import edu.kh.comm.member.model.vo.Member;

@Controller
@RequestMapping("/member/myPage")
@SessionAttributes({"loginMember"}) // session scope에서 loginMember를 얻어옴
public class MypageController {
	
	private Logger logger = LoggerFactory.getLogger(MypageController.class);

	@Autowired
	private MyPageService service;
	
	// 내정보 페이지 이동
	@GetMapping("/info") 
	public String myinfo() {
		
		return "member/myPage-info";
		
	}
	
	// 닉네임 중복검사
	@ResponseBody
	@GetMapping("/myPageNicknameDupCheck")
	public int myPageNicknameDupCheck(@RequestParam("newNickname") String memberNickname) {
		
		int result = service.myPageNicknameDupCheck(memberNickname);
		System.out.println(result);
		return result;
	}
	
	
	// 회원 정보 수정
	@PostMapping("/info") 
	public String updatMyinfo(@ModelAttribute("loginMember") Member loginMember,
							  @RequestParam Map<String, Object> paramMap, // 요청시 전달된 파라미터를 구분하지 않고 모두 Map에 담아서 얻어옴
							  String[] newAddress,
							  RedirectAttributes ra){
		
		// 필요한 값
		// - 닉네임
		// - 전화번호
		// - 주소 (String[]로 얻어와 String.join()을 이용해서 문자열로 변경)
		// - 회원 번호(Session -> 로그인한 회원 정보를 통해서 얻어옴)
		//		--> @ModelAttribute, @SessionAttribute 필요
		
		// @SessionAttribute의 역할 2가지
		// 1) Model에 세팅된 데이터의 key값을 @SessionAttribute에 작성하면
		//    해당 key값과 같은 Model에 세팅된 데이터를 request -> session scope로 이동
		
		// 2) 기존에 session scope로 이동시킨 값을 얻어오는 역할
		// @ModelAttribute("loginMember") Member loginMember
		// 					[session key]
		// --> @SessionAttributes를 통해 session scope에 등록된 값을 얻어와
		//		오른쪽에 작성된 Member loginMember 변수에 대입
		//		단, @SessionAttributes({"loginMember"})가 클래스 위에 작성되어 있어야 가능
		
		
		// *** 매개변수를 이용해서 session, 파라미터 데이터를 동시에 얻어올 때 문제점 ***
		
		// session에서 객체를 얻어오기 위해 매개변수에 작성한 상태
		// @ModelAttribute("loginMember") Member loginMember
		
		// 파라미터의 name 속성값이 매개변수에 작성된 객체(loginMember)의 필드(변수명)와 일치하면
		// -> name = "memberNickname"
		
		// session에서 얻어온 객체의 필드에 덮어쓰기가 된다!
		
		// [해결방법] 파라미터의 name 속성을 변경해서 얻어오면 문제 해결!
		// 즉, 필드명이 겹쳐서 생긴 문제이니 겹치지 않게 하자
		// 그래서 jsp name-> 다 new~로 바꿈!!
	
		int result = 0;
		String message = null;
			
		// 파라미터를 저장한 paramMap에 회원번호, 주소를 추가
		String memberAddress = String.join(",,", newAddress); // 주소 배열 -> 문자열 반환
		
		// 주소가 미입력 되었을 때
		if(memberAddress.equals(",,,,")) memberAddress = null;

		paramMap.put("memberNo", loginMember.getMemberNo());
		paramMap.put("memberAddress", memberAddress);
		
		
		if(loginMember.getMemberNickname().equals((String)paramMap.get("newNickname"))) { // 닉네임 수정 X
			
			result = service.updateMyinfoExceptNick(paramMap);
			
		}else { // 닉네임 수정
			
			// 닉네임 중복검사
			int count = service.myPageNicknameDupCheck((String)paramMap.get("newNickname"));
			
			if(count == 1) { // 중복된 경우
				message = "중복된 닉네임입니다.";
				
			}else { // 중복 안 된 경우
			
				// 회원 정보 수정 서비스 호출
				result = service.updateMyinfo(paramMap);
			}
		}
			
		if(result > 0) { // 회원정보 수정 성공
			message = "성공적으로 변경되었습니다.";
			
			// DB - Session의 회원 정보 동기화
			loginMember.setMemberNickname((String)paramMap.get("newNickname"));
			loginMember.setMemberTel((String)paramMap.get("newTel"));
			loginMember.setMemberAddress(memberAddress);
			
		}else {
			message = "변경에 실패하였습니다.";
			
		}
		
		ra.addFlashAttribute("message", message);
		return "redirect:info";
		
	}
	
	
	// 프로필 변경 페이지 이동
	@GetMapping("/profile") 
	public String profile() {
		
		return "member/myPage-profile";
	}
	
	// 프로필 변경 
	@PostMapping("/profile") 
	public String updateProfile( @ModelAttribute("loginMember") Member loginMember,
								 @RequestParam("uploadImage") MultipartFile uploadImage, /*업로드 파일*/
								 @RequestParam Map<String, Object> map,
								 HttpServletRequest req, /*파일 저장 경로 탐색용*/
								 RedirectAttributes ra )  throws IOException{
		
		// 경로작성하기
		// 1) 웹 접근 경로 ( /comm/resources/images/memberProfile/ )
		String webPath = "/resources/images/memberProfile/";
		
		// 2) 서버 저장 폴더 경로
		// /Users/sujinchoi/workspace/7_Framework/comm/src/main/webapp/resources/images/memberProfile
		String folderPath = req.getSession().getServletContext().getRealPath(webPath);
		
		// 경로 2개, 이미지, delete, 회원번호 map에 담기
		map.put("webPath", webPath);
		map.put("folderPath", folderPath);
		map.put("uploadImage", uploadImage);
		map.put("memberNo", loginMember.getMemberNo());
		
		int result = service.updateProfile(map);
		
		String message = null;
		
		if(result > 0) {
			message = "프로필 이미지가 변경되었습니다.";
			
			// DB - 세션 동기화
			loginMember.setProfileImage((String)map.get("profileImage"));
			
		}else {
			message = "프로필 이미지가 변경에 실패하였습니다.";
		}
		
		ra.addFlashAttribute("message", message);
		
		return "redirect:profile";
	}
	
	
	
	
	
	
	// 비밀번호 변경 페이지 이동
	@GetMapping("/changePw") 
	public String changePw() {
		
		return "member/myPage-changePw";
		
	}
	
	// 비밀번호 변경 
	@PostMapping("/changePw")
	public String changePw(@RequestParam Map<String,Object> paramMap,
						   @ModelAttribute("loginMember") Member loginMember,
						   RedirectAttributes ra ){
		
		// 로그인된 회원의 번호를 paramMap 추가
		paramMap.put("memberNo", loginMember.getMemberNo());
		
		// 비밀번호 변경 서비스 호출
		int result = service.changePw(paramMap);
		
		String path = null;
		String message = null;
		
		if(result > 0) { // 비밀번호 변경 성공
			message = "비밀번호 변경에 성공하였습니다.";
			
			path = "redirect:info";
			
		}else {
			message = "현재 비밀번호가 일치하지 않습니다.";
			path = "redirect:changePw";
		}
		
		ra.addFlashAttribute("message", message);
		return path;
		
	}
	
	
	// 회원탈퇴 페이지 이동
	@GetMapping("/secession") 
	public String secession() {
		
		return "member/myPage-secession";
		
	}
	
	
	// 회원탈퇴 기능
	@PostMapping("/secession")  // session의 회원 정보 + 입력받은 파라미터(비밀번호)
	public String secession(@ModelAttribute("loginMember") Member loginMember,
							RedirectAttributes ra,
							SessionStatus status,
							HttpServletRequest req,
							HttpServletResponse resp ) {
		
		// 회원탈퇴 서비스 호출
		int result = service.secession(loginMember);
		
		String message = null;
		String path = null;
		
		if(result > 0) { // 회원탈퇴 성공
			message = "탈퇴되었습니다.";
			path = "redirect:/";
			
			// 세션 없애기
			status.setComplete(); //세션 만료 (로그아웃)
			
			// 쿠키 없애기
			Cookie cookie = new Cookie("saveId", "");
			cookie.setMaxAge(0);
			cookie.setPath(req.getContextPath());
			resp.addCookie(cookie);
			
			
		}else {
			message = "현재 비밀번호가 일치하지 않습니다.";
			path = "redirect:/member/myPage/secession";
		}
		
		ra.addFlashAttribute("message", message);
		
		return path;
		
	}
	
	
	
	
}

