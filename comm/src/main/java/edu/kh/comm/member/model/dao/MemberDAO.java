package edu.kh.comm.member.model.dao;


import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import edu.kh.comm.member.model.vo.Member;

@Repository // 영속성을 가지는 DB/파일 연결하는 클래스임을 명시 + bean 등록
public class MemberDAO {
	
	// DAO는 DB랑 연결하기 위한 Connection이 공통적으로 필요하다
	// -> 필드에 선언했었다
	// + Mybatis 영속성 프레임워크를 이용하려면 Connection을 이용해 만들어진 객체
	// SqlSessionTemplate을 사용

	@Autowired // root-context.xml에서 생성된 SqlSessionTemplate bean을 의존성 주입(DI)
	private SqlSessionTemplate sqlSession;
	
	private Logger logger = LoggerFactory.getLogger(MemberDAO.class);
	
	public Member login(Member inputMember) {
		
		// 1행 조회(파라미터 X) 방법
//		// int count = sqlSession.selectOne("namespace값.id");
//		int count = sqlSession.selectOne("memberMapper.test1");
//		logger.debug(count + ""); // 문자열로 만드는 거
		
		// 1행 조회(파라미터 O) 방법
//		String memberNickname = sqlSession.selectOne("memberMapper.test2", inputMember.getMemberEmail());
//		logger.debug(memberNickname + "");
		
		
		// 1행 조회(파라미터 VO인 경우)
//		String memberTel = sqlSession.selectOne("memberMapper.test3", inputMember);
//																// memberEmail, memberPw
//		if(memberTel != null) {
//			logger.debug(memberTel);
//			
//		}else {
//			logger.debug("일치하지 않습니다.");
//		}
		
		
		// 1행 조회(파라미터가 VO, 반환되는 결과도 VO)
		Member loginMember = sqlSession.selectOne("memberMapper.login", inputMember);
		
		return loginMember;
	}

	/** 이메일 중복검사 DAO
	 * @param memberEmail
	 * @return result
	 */
	public int emailDupCheck(String memberEmail) {
		return sqlSession.selectOne("memberMapper.emailDupCheck", memberEmail);
	}

	/** 닉네임 중복검사 DAO 
	 * @param memberNickname
	 * @return result
	 */
	public int nicknameDupCheck(String memberNickname) {
		
		return sqlSession.selectOne("memberMapper.nicknameDupCheck", memberNickname);

	}

	/** 회원가입 DAO
	 * @param inputMember
	 * @return result
	 */
	public int signUp(Member inputMember) {
		
		// INSERT, UPDATE, DELETE를 수행하기 위한 메서드 존재함
		// * insert() / update() / delete() 메서드의 반환값은 int형으로 고정
		// -> mapper에서도 resultType이 항상 _int로 고정
		// -> resultType 생략 가능 (묵시적으로 _int)
		
		int result = sqlSession.insert("memberMapper.signUp", inputMember);
		
		return result;
	}

	/** 회원 1명 조회 DAO
	 * @param memberEmail
	 * @return member
	 */
	public Member selectOne(String memberEmail) {
		Member member = sqlSession.selectOne("memberMapper.selectOne", memberEmail);
		System.out.println(member);
		return member;
	}

	/** 회원목록 조회 DAO
	 * @return memberListi
	 */
	public List<Member> selectAll() {
		
		List<Member> memberList = sqlSession.selectList("memberMapper.selectAll");
		
		return memberList;
	}

	/** 이메일 인증번호 저장 DAO : 처음으로 인증번호를 발급 받음 -> 삽입(INSERT)
	 * @param map
	 * @return
	 */
	public int insertCertification(Map<String, Object> map) {
		return sqlSession.insert("memberMapper.insertCertification", map);
	}

	/** 이메일 인증번호 저장 DAO (UPDATE)
	 * @param map
	 * @return
	 */
	public int updateCertification(Map<String, Object> map) {
		return sqlSession.update("memberMapper.updateCertification", map);
	}
	
	/** 이메일 인증번호 일치 확인 DAO
	 * @param map
	 * @return
	 */
	public int checkNumber(Map<String, Object> map) {
		
		return sqlSession.selectOne("memberMapper.checkNumber", map);
	}


}
