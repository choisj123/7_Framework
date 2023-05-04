package edu.kh.comm.board.controller;

import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import edu.kh.comm.board.model.service.BoardService;
import edu.kh.comm.board.model.vo.BoardDetail;
import edu.kh.comm.member.controller.MypageController;
import edu.kh.comm.member.model.vo.Member;

@Controller
@RequestMapping("/board")
public class BoardController {
	
	private Logger logger = LoggerFactory.getLogger(BoardController.class);
	
	@Autowired
	private BoardService service; 

	// 게시글 목록 조회
	// PathVariable("value") : URL 경로에 포함되어 있는 값을 변수로 사용할 수 있게하는 역할
	// -> 자동으로 request scope에 등록됨 -> jsp에서 ${value} EL 작성 가능
	
	// PathVariable : 요청 자원을 식별하는 경우
	// QueryString : 정렬, 검색 등의 필터링 옵셥
	
	
	@GetMapping("/list/{boardCode}") 
	public String boardList(@PathVariable("boardCode") int boardCode,
							// request scope / 주소 변수
							@RequestParam(value="cp", required= false, defaultValue="1") int cp,
							Model model /*,
							@RequestParam Map<String, Object> paramMap */) {
							// key, query, cp
		
		// 게시글 목록 조회 서비스 호출
		// 1) 게시판 이름 조회 -> 인터셉처로 application에 올려둔 boardTypeList 쓸 수 있을듯
		// 2) 페이지네이션 객체 생성(listCount)
		// 3) 게시글 목록 조회
		
		Map<String, Object> map = null;
		
		map = service.selectBoardList(cp, boardCode);
		
		model.addAttribute("map", map);
		
		return "board/boardList";
		
	}
	
	// 게시글 상세조회
	@GetMapping("/detail/{boardCode}/{boardNo}")
	public String boardDetail(@PathVariable("boardCode") int boardCode,
							@PathVariable("boardNo") int boardNo,
							@RequestParam(value="cp", required=false, defaultValue="1") int cp,
							Model model ,
							HttpSession session) {
		
		// 게시글 상세 조회 서비스 호출
		BoardDetail detail = service.selectBoardDetail(boardNo);
		
		
		// 쿠키를 이용해 조회수 중복 증가 방지 코드 + 본인의 글은 조회수 증가 X
		
		// @ModelAttribute("loginMember") Member loginMember (사용 불가)
		// -> @ModelAttribute는 별도의 required 속성이 없어서 무조건 필수!!
		//		-> 세션에 loginMember가 없으면 예외 발생
		
		// 해결방법 : HttpSession을 이용
		// -> session.getAttribute("loginMember")
//		Member loginMember = (Member) session.getAttribute("loginMember");
//		
//		int memberNo = 0;
//		
//		
//		// 상세 조회 성공 시
//		if(detail != null) {
//			if(loginMember != null) {
//				memberNo = loginMember.getMemberNo();
//			}
//			
//			if(memberNo != detail.getMemberNo()) {
//				detail.getReadCount();
//				
//				if(true)) {
//					Cookie cookie = new Cookie("readBoardNo", detail.getBoardNo());
//				}
//				
//			}
//		
			// 로그인(세션)이 있는지 없는지
				//세션이 있으면 memberNo 세팅
		
			// 글쓴이와 현재 클라이언트(로그인한 사람)이 같은지 아닌지 
				// (같지 않으면 조회 수 증가 / 같으면 조회 수 증가X)
				// 쿠키가 있는지 없는지
					// 있다면 쿠키 이름이 "readBoardNo" 있는지?
					// 없다면 만들어라
					// 있다면 쿠키에 저장된 값 뒤쪽에 현재 조회된 게시글 번호를 추가
					// -> 단, 기존 쿠키값에 중복되는 번호 없어야함
		
			// 이미 조회된 데이터 DB와 동기화
			// + 쿠키 maxAge 1시간
		
		
//		}
		return "board/boardDetail";
		
	}
	

}
