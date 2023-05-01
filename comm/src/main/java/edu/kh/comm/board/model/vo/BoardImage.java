package edu.kh.comm.board.model.vo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class BoardImage {
	
	private int imageNo;
	private String imageReName;
	private String imageOriginal;
	private int imageLevel;
	private int boardNo;


}
