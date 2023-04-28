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
	public int changePw(String currentPw, String newPw, Member loginMember) {
		
		int result = 0;
		String memberPw = dao.getPw(loginMember);
		System.out.println("로그인멤버 비번 가져오기 :" + memberPw);
		
		if(bcrypt.matches(currentPw, memberPw)) {
			
			loginMember.setMemberPw(bcrypt.encode(newPw));
			System.out.println("새로운 비번: " + loginMember.getMemberPw());
			
			result = dao.changePw(loginMember);
			
		}else {
			
			result = 0;
		}
		
		return result;
	}

	// 회원탈퇴 서비스 구현
	@Override
	public int secession(String currentPw, Member loginMember) {
		

		int result = 0;
		String memberPw = dao.getPw(loginMember);
		
		if(bcrypt.matches(currentPw, memberPw)) {
			
			result = dao.secession(loginMember);
			
		}else {
			
			result = 0;
		}
		
		return result;
		
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
