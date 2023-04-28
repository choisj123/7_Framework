package edu.kh.comm.member.model.dao;

import org.slf4j.LoggerFactory;

import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import edu.kh.comm.member.model.vo.Member;

@Repository
public class MyPageDAO {
	
	@Autowired
	private SqlSessionTemplate sqlSession;

	private Logger logger = LoggerFactory.getLogger(MyPageDAO.class);
	
	/** 현재 비밀번호 얻어오기 DAO
	 * @param loginMember
	 * @return
	 */
	public String getPw(Member loginMember) {
		
		String memberPw = sqlSession.selectOne("myPageMapper.getPw", loginMember);
		return memberPw;
	}
	
	/** 비밀번호 변경 DAO
	 * @param loginMember
	 * @return result
	 */
	public int changePw(Member loginMember) {
		
		int result = sqlSession.update("myPageMapper.changePw", loginMember);
		
		return result;
	}

	/** 회원탈되 DAO
	 * @param loginMember
	 * @return result
	 */
	public int secession(Member loginMember) {
		int result = sqlSession.update("myPageMapper.secession", loginMember);
		
		return result;
	}

	/** 내정보 수정 - 닉네임 중복검사 DAO
	 * @param memberNickname
	 * @return result
	 */
	public int myPageNicknameDupCheck(String memberNickname) {
		
		return sqlSession.selectOne("myPageMapper.myPageNicknameDupCheck", memberNickname);
	}



	/** 내 정보 수정 - 닉네임 제외
	 * @param paramMap
	 * @return result
	 */
	public int updateMyinfoExceptNick(Map<String, Object> paramMap) {
		int result = sqlSession.update("myPageMapper.updateMyinfoExceptNick", paramMap);
		
		return result;
	}

	
	/** 내정보 수정
	 * @param paramMap
	 * @return result
	 */
	public int updateMyinfo(Map<String, Object> paramMap) {
		return sqlSession.update("myPageMapper.updateMyinfoNew", paramMap);
	}



}
