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
	 * @param reply
	 * @return
	 */
	public int insertReply(Reply reply) {

		return sqlSession.insert("reply-mapper.insertReply", reply);
	}

	
	/** 댓글 수정 서비스 DAO
	 * @param reply
	 * @return result
	 */
	public int updateReply(Reply reply) {
		
		return sqlSession.update("reply-mapper.updateReply", reply);
	}
	
	/** 댓글 삭제 서비스 DAO
	 * @param replyNo
	 * @return result
	 */
	public int deleteReply(int replyNo) {
		
		return sqlSession.delete("reply-mapper.deleteReply", replyNo);
	}





}
