package edu.kh.comm.board.model.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import edu.kh.comm.board.model.dao.BoardDAO;
import edu.kh.comm.board.model.vo.Board;
import edu.kh.comm.board.model.vo.BoardDetail;
import edu.kh.comm.board.model.vo.BoardImage;
import edu.kh.comm.board.model.vo.BoardType;
import edu.kh.comm.board.model.vo.Pagination;
import edu.kh.comm.common.Util;


@Service
public class BoardServiceImpl implements BoardService{
	
	private Logger logger = LoggerFactory.getLogger(BoardServiceImpl.class);
	
	@Autowired
	private BoardDAO dao;
	
	
	// 	게시판 코드, 이름 조회 서비스 구현
	@Override
	public List<BoardType> selectBoardType() {
		
		return dao.selectBoardType();
	}
	
	
	// 게시글 목록 조회 서비스 구현
	@Override
	public Map<String, Object> selectBoardList(int cp, int boardCode) {
		// 1) 게시판 이름 조회 -> 인터셉처로 application에 올려둔 boardTypeList 쓸 수 있을듯
		
		// 2) 페이지네이션 객체 생성(listCount)
		int listCount = dao.getListCount(boardCode);
		// 페이지네이션 생성자의 매개변수로 전달
		
		Pagination pagination = new Pagination(cp, listCount);
		
		
		// 3) 게시글 목록 조회
		List<Board> boardList = dao.selectBoardList( pagination, boardCode );
		
		// map 만들어 담기
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("pagination", pagination);
		map.put("boardList", boardList);
		
		return map;
	}
	
	// 게시글 상세조회 서비스 구현
	@Override
	public BoardDetail selectBoardDetail(int boardNo) {
		
		return dao.selectBoardDetail(boardNo);
	}
	
	// 조회수 증가 서비스 구현
	@Override
	public int updateReadCount(int boardNo) {
		
		return dao.updateReadCount(boardNo);
	}
	
	// 검색 게시글 목록 조회 서비스 구현
	@Override
	public Map<String, Object> searchBoardList(Map<String, Object> paramMap) {
		
		// 검색 조건에 맞는 게시글 목록의 전체 개수 조회
		int listCount = dao.searchListCount(paramMap);
		
		// 페이지네이션 객체 생성
		Pagination pagination = new Pagination((int)paramMap.get("cp"), listCount);
		
		// 검색 조건에 맞는 게시글 목록 조회 (페이징 처리 적용)
		List<Board> boardList = dao.searchBoardList(paramMap, pagination);
		
		
		// map에 담기
		Map<String, Object> map = new HashMap<>();
		map.put("pagination", pagination);
		map.put("boardList", boardList);
		
		return map;
	}
	
	// 게시글 삽입 + 이미지 삽입 서비스 구현
	@Override
	public int insertBoard(BoardDetail detail, List<MultipartFile> imageList, String webPath, String folderPath) {
		
		// 1. 게시글 삽입
		
		// 1) XSS 방지처리 + 개행문자 처리
		detail.setBoardTitle( Util.XSSHandling(detail.getBoardTitle()) );
		detail.setBoardContent( Util.XSSHandling(detail.getBoardContent()) );
		detail.setBoardContent( Util.newLineHandling(detail.getBoardContent()));
		
		// 2) 게시글 삽입 DAO 호출 후 게시글 번호 반환 받기
		
		// * 게시글 번호를 먼저 생성했던 이유
		// 1. 서비스 결과 반환 후 컨트롤러 상세조회로 리다이렉트 하기 위해
		// 2. 동일한 시간에 삽입이 2회 이상 진행된 경우 시퀀스 번호가 의도와 달리 여러번 증가해서
		//		이후에 작성된 이미지 삽입 코드에 영향을 미치는 걸 방지하기 위해서
		
		int boardNo = dao.insertBoard(detail);
		
		if(boardNo > 0) {
			// 이미지 삽입
			
			// imageList : 실제로 파일이 담겨있는 리스트
			// boardImageList : DB에 삽입할 이미지 정보만 담겨있는 리스트
			// reNameList : 변경된 파일명이 담겨있는 리스트
			
			List<BoardImage> boardImageList = new ArrayList<>();
			List<String> reNameList = new ArrayList<>();
			
			// imageList에 담겨있는 파일 정보 중 실제 업로드된 파일만 분류하는 작업
			for(int i = 0; i < imageList.size(); i++) {
				if(imageList.get(i).getSize() > 0) { // i번째 요소에 업로드된 이미지가 있을 경우
					// 변경된 파일명 저장
					//202305091234_451
					String reName = Util.fileRename(imageList.get(i).getOriginalFilename());
					reNameList.add(reName);
					
					// BoardImage 객체를 생성하여 값 세팅 후 boardImageList에 추가
					BoardImage img = new BoardImage();
					img.setBoardNo(boardNo); // 게시글 번호
					img.setImageLevel(i); // 이미지 순서
					img.setImageOriginal(imageList.get(i).getOriginalFilename()); // 원본 파일명
					img.setImageReName(webPath + reName); // 웹 접근 경로 + 변경된 파일명
				
					boardImageList.add(img);
				}
			}
			
			// 분류 작업 종료 후 boardImageList가 비어있지 않은 경우 
			
			
			
		}
		
		
		return 0;
	}
	
	
	
}
