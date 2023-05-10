package edu.kh.comm.board.model.dao;

import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import edu.kh.comm.board.model.vo.Reply;

@Repository
public class ReplyDAO {
	
	@Autowired
	private SqlSessionTemplate sqlSession;

	/** 댓글 목록 조회 DAO
	 * @param boardNo
	 * @return replyList
	 */
	public List<Reply> selectReplyList(int boardNo) {
		return sqlSession.selectList("reply-mapper.selectReplyList", boardNo);
	}

	/** 댓글 등록 서비스 DAO
	 * @param replyContent
	 * @param memberNo
	 * @param boardNo
	 * @return result
	 */
	public int insertReply(String replyContent, int memberNo, int boardNo) {

		return sqlSession.insert("reply-mapper.insertReply");
	}

	/** 댓글 삭제 서비스 DAO
	 * @param replyNo
	 * @return result
	 */
	public int deleteReply(int replyNo) {
		
		return sqlSession.delete("reply-mapper.deleteReply", replyNo);
	}

}
