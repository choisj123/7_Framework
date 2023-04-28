package edu.kh.comm.member.model.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import edu.kh.comm.member.model.dao.MemberDAO;
import edu.kh.comm.member.model.dao.MyPageDAO;
import edu.kh.comm.member.model.vo.Member;


@Service
public class MyPageServiceImpl implements MyPageService{
	
	@Autowired
	private MyPageDAO dao;
	
	@Autowired
	private BCryptPasswordEncoder bcrypt;


	
	// 비밀번호 변경 서비스 구현 
	@Override
	public int changePw(Map<String, Object> paramMap) {
		
		// 1) DB에서 현재 회원의 비밀번호를 조회
		String encPw = dao.selectEncPw( (int)paramMap.get("memberNo") );
		
		// 2) 입력된 현재 비밀번호(평문)와 조회된 비밀번호 (암호화)를 비교 
		// bcrypt.matches() 이용
		
		// 3) 비교 결과가 일치하면
		// 새 비밀번호를 암호화해서 update 구문 수행
		if(bcrypt.matches((String)paramMap.get("currenPw"), encPw)) {
			
			paramMap.put("newPw", bcrypt.encode( (String)paramMap.get("newPw")) );
			// Map에 이미 같은 key가 존재하면
			// value만 덮어씌움
			
			return dao.changePw(paramMap);
		}
		
		// 비교 결과가 일치하지 않으면 0 반환
		// -> "현재 비밀번호가 일치하지 않습니다"
		return 0;
	}

	// 회원탈퇴 서비스 구현
	@Override
	public int secession(Member loginMember) {
		
		// 1) DB에서 암호화된 비밀번호를 조회하여 입력받은 비밀번호와 비교
		String encPw = dao.selectEncPw( loginMember.getMemberNo() );
		
		// jsp에서 넘겨받은 비밀번호 name이 memberPw로 같아서 덮어씌워짐
		if(bcrypt.matches(loginMember.getMemberPw(), encPw)) {
			// 2) 비밀번호가 일치하면 회원 번호를 이용해서 탈퇴 진행
			return dao.secession(loginMember.getMemberNo());
			
		}
		
		// 3) 비밀번호가 일치하지 않으면 0 반환
		return 0;
	}
	
	
	
	// 내 정보 수정 - 닉네임 중복 검사
	@Override
	public int myPageNicknameDupCheck(String memberNickname) {
		
		return dao.myPageNicknameDupCheck(memberNickname);
	}
	

	// 내 정보 수정 - 닉네임 제외
	@Override
	public int updateMyinfoExceptNick(Map<String, Object> paramMap) {
		
		int result = dao.updateMyinfoExceptNick(paramMap);
		
		return result;
	}
	
	// 내 정보 수정

	@Override
	public int updateMyinfo(Map<String, Object> paramMap) {
		
		int result = dao.updateMyinfo(paramMap);
		
		return result;
	}



}
