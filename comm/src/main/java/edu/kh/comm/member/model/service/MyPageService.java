package edu.kh.comm.member.model.service;

import java.io.IOException;
import java.util.Map;

import edu.kh.comm.member.model.vo.Member;

public interface MyPageService {


	/** 비밀번호 변경 서비스
	 * @param paramMap
	 * @return result
	 */
	int changePw(Map<String, Object> paramMap);


	/** 회원탈퇴 서비스
	 * @param loginMember
	 * @return result
	 */
	int secession(Member loginMember);


	/** 마이페이지 내 정보 수정 - 닉네임 중복검사
	 * @param memberNickname
	 * @return result
	 */
	int myPageNicknameDupCheck(String memberNickname);


	/** 마이페이지 내 정보 수정 - 닉네임 제외
	 * @param paramMap
	 * @return result
	 */
	int updateMyinfoExceptNick(Map<String, Object> paramMap);

	
	/** 마이페이지 내 정보 수정
	 * @param paramMap
	 * @return result
	 */
	int updateMyinfo(Map<String, Object> paramMap);


	/** 프로필 이미지 수정 서비스
	 * @param map
	 * @return result
	 * @throws IOException 
	 */
	int updateProfile(Map<String, Object> map) throws IOException;






}
