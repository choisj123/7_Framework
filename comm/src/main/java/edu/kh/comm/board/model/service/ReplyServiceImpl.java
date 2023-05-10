package edu.kh.comm.board.model.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.kh.comm.board.model.dao.ReplyDAO;
import edu.kh.comm.board.model.vo.Reply;

@Service
public class ReplyServiceImpl implements ReplyService{

	@Autowired
	private ReplyDAO dao;
	
	
	// 댓글 목록 서비스 구현
	@Override
	public List<Reply> selectReplyList(int boardNo) {
		
		return dao.selectReplyList(boardNo);
	}
	
	// 댓글 등록 서비스 구현
	@Override
	public int insertReply(String replyContent, int memberNo, int boardNo) {
		
		return dao.insertReply(replyContent, memberNo, boardNo);
	}
	
	// 댓글 삭제 서비스 구현
	@Override
	public int deleteReply(int replyNo) {
		
		return dao.deleteReply(replyNo);
	}
}
