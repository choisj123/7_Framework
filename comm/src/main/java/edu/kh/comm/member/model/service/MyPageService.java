package edu.kh.comm.member.model.service;

import edu.kh.comm.member.model.vo.Member;

public interface MyPageService {

	/** 비밀번호 변경 서비스
	 * @param newPw 
	 * @param currentPw 
	 * @param loginMember
	 * @return result
	 */
	int changePw(String currentPw, String newPw, Member loginMember);

	/** 회원탈퇴 서비스
	 * @param currentPw
	 * @param loginMember
	 * @return result
	 */
	int secession(String currentPw, Member loginMember);

	/** 마이페이지 내 정보 수정 - 닉네임 중복검사
	 * @param memberNickname
	 * @return result
	 */
	int myPageNicknameDupCheck(String memberNickname);

	/** 마이페이지 내 정보 수정
	 * @param loginMember
	 * @return result
	 */
	int updateMyinfo(Member loginMember);

	/** 마이페이지 내 정보 수정 - 닉네임 제외
	 * @param loginMember
	 * @return
	 */
	int updateMyinfoExceptNick(Member loginMember);

}
