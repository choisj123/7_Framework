package edu.kh.comm.member.model.dao;

import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class MyPageDAO {
	
	@Autowired
	private SqlSessionTemplate sqlSession;

	private Logger logger = LoggerFactory.getLogger(MyPageDAO.class);
	

	/** 현재 비밀번호 얻어오기 DAO
	 * @param memberNo
	 * @return
	 */
	public String selectEncPw(int memberNo) {
		
		return sqlSession.selectOne("myPageMapper.selectEncPw", memberNo);
	}

	
	
	/** 비밀번호 변경 DAO
	 * @param paramMap
	 * @return result
	 */
	public int changePw(Map<String, Object> paramMap) {
		
		return sqlSession.update("myPageMapper.changePw", paramMap);
	}

	
	/** 회원탈되 DAO
	 * @param memberNo
	 * @return result 
	 */
	public int secession(int memberNo) {
		return sqlSession.update("myPageMapper.secession", memberNo);
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

	
	/** 내정보 수정 DAO
	 * @param paramMap
	 * @return result
	 */
	public int updateMyinfo(Map<String, Object> paramMap) {
		return sqlSession.update("myPageMapper.updateMyinfoNew", paramMap);
	}



	/** 프로필 이미지 수정 DAO
	 * @param map
	 * @return
	 */
	public int updateProfile(Map<String, Object> map) {
		
		return sqlSession.update("myPageMapper.updateProfile", map);
	}



	/** 프로필 이미지 경로 가져오기
	 * @return
	 */
	public List<String> selectProfileImg() {
		return sqlSession.selectList("myPageMapper.selectProfile");
	}








}
