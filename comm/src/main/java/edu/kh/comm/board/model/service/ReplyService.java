package edu.kh.comm.board.model.service;

import java.util.List;

import edu.kh.comm.board.model.vo.Reply;

public interface ReplyService {

	/** 댓글 목록 서비스
	 * @param boardNo
	 * @return replyList
	 */
	List<Reply> selectReplyList(int boardNo);

	/** 댓글 등록 서비스
	 * @param replyContent
	 * @param memberNo
	 * @param boardNo
	 * @return result
	 */
	int insertReply(String replyContent, int memberNo, int boardNo);

	/** 댓글 삭제 서비스
	 * @param replyNo
	 * @return result
	 */
	int deleteReply(int replyNo);

}
