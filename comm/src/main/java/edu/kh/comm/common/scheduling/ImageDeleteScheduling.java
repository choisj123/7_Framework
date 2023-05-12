package edu.kh.comm.common.scheduling;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import javax.servlet.ServletContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import edu.kh.comm.board.model.service.BoardService;
import edu.kh.comm.member.model.service.MyPageService;

@Component // bean 등록
public class ImageDeleteScheduling {
	
	private Logger logger = LoggerFactory.getLogger(ImageDeleteScheduling.class);

	// BOARD_IMG 테이블에서 삭제되었으나
	// 서버/resources/images/board 폴더에는 존재하는
	// 이미지 파일을 정시마다 삭제
	
	// 1) BOARD_IMG에 존재하는 모든 이미지 목록 조회
	// 2) /resource/images/board 폴더에 존재하는 모든 이미지 파일 목록 조회
	// 3) 두 목록을 비교해서 일치하지 않는 이미지 파일을 삭제
	// (DB에는 없는데 서버 폴더에 있으면 삭제)
	
	@Autowired
	private BoardService bService;
	
	@Autowired
	private MyPageService mService;
	
	@Autowired
	private ServletContext application; // application scope 객체 -> 서버 폴더 경로 얻어오기에 사용
	
	// 스케쥴링에 사용되는 메서드는 무조건 public void 메서드명()
	@Scheduled(cron = "0 0 * * * *") // 매일 정각
	public void serverImageDelete() {
		
		// 1-1) BOARD_IMG에 존재하는 모든 이미지 목록 조회
		List<String> dbList = bService.selectDBList();
		
		// 1-2) MEMBER_S에 존재하는 프로필이미지 조회
		List<String> profileImgDBList = mService.selectProfileImg();
		
		// 2-1) /resource/images/board 폴더에 존재하는 모든 이미지 파일 목록 조회
		String folderPath = application.getRealPath("/resources/images/board");
		
		// 2-2)  /resource/images/memberProfile 폴더에 존재하는 모든 이미지 파일 목록 조회
		String profileFolderPath = application.getRealPath("/resources/images/memberProfile");
		
		File path = new File(folderPath); // resource/images/board 폴더를 참조하는 객체
		
		File profilePath = new File(profileFolderPath); // resource/images/memberProfile 폴더를 참조하는 객체
		
		File[] arr = path.listFiles(); // path가 참조하는 폴더에 있는 모든 파일을 얻어와 File 배열 반환
		
		File[] profileArr = profilePath.listFiles(); // profilePath가 참조하는 폴더에 있는 모든 파일을 얻어와 File 배열 반환
		
		List<File> serverList = Arrays.asList(arr); // arr을 List로 변환
		
		List<File> profileServerList = Arrays.asList(profileArr); // profileArr을 List로 변환
		
		// 3-1) 두 목록을 비교해서 일치하지 않으면 이미지 파일을 삭제
		// (DB에는 없는데 서버 폴더에 있으면 삭제)
		if(!serverList.isEmpty()) {
			
			// server : /resources/images/board
			// DB : /resources/images/board/sample1.jpg
			
			for(File serverImage : serverList) {
				
				String name = "/resources/images/board/" + serverImage.getName(); // 파일명만 얻어오기
				
				if(dbList.indexOf(name) == -1) { // List에 value와 같은 값이 있으면 인덱스 반환 / 없으면 -1 반환
					
					// dbList에는 없는데 servcerList에만 파일이 존재하는 경우
					logger.info(serverImage.getName() + " 삭제");
					serverImage.delete(); // 파일 삭제
					
				}
			}
			
			logger.info("----------------board 서버 이미지 삭제 완료----------------");
			
		}
		// 3-2) 두 목록을 비교해서 일치하지 않으면 이미지 파일을 삭제
		// (DB에는 없는데 서버 폴더에 있으면 삭제)
		if(!profileServerList.isEmpty()) {
			
			for(File profileServerImg : profileServerList) {
				String name = "/resources/images/memberProfile/" + profileServerImg.getName(); // 파일명만 얻어오기
				
				if(profileImgDBList.indexOf(name) == -1) { // List에 value와 같은 값이 있으면 인덱스 반환 / 없으면 -1 반환
					
					// dbList에는 없는데 servcerList에만 파일이 존재하는 경우
					logger.info(profileServerImg.getName() + " 삭제");
					profileServerImg.delete(); // 파일 삭제
					
				}
			}
			logger.info("-----------------profile 서버 이미지 삭제 완료----------------");
		}
			
	}
}
