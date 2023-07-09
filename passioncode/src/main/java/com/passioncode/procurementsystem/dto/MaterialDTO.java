package com.passioncode.procurementsystem.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 품목정보등록 화면을 위한 품목DTO 클래스 (10개+2개) <br>
 * 품목코드, 품목명, 대, 중, 규격, 재질, 제작사양, 도면번호, 도면Image, 공용여부 <br>
 * 대분류코드, 중분류코드, 
 * @author KSH
 * 
 */ 
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Data
public class MaterialDTO { 
		
	/**
	 * 품목코드 (품목)
	 */
	private String code;	
	
	/**
	 * 품목명 (품목)
	 */
	private String name;
	
	/**
	 * 공용여부 (품목) <br>
	 * 기본값 = 1, 0 : 공용, 1 : 전용
	 */
	private String shareStatus;		
	
	/**
	 * 규격 (품목)
	 */
	private String size;	
	
	/**
	 * 재질 (품목)
	 */
	private String quality;		
	
	/**
	 * 제작사양 (품목)
	 */
	private String spec;	
	
	/**
	 * 도면번호 (품목)
	 */
	private String drawingNo;	
	
	/**
	 * 도면 (품목)
	 */
	private String drawingFile; 	
	
	/**
	 * 도면업로드
	 */
	@Builder.Default
	private DrawingFileDTO drawingFileDTO = new DrawingFileDTO();
	
	/**
	 * 대분류 종류 (대분류)
	 */
	private String largeCategoryName;
	
	/**
	 * 중분류 종류 (중분류)
	 */
	private String middleCategoryName;
	
	/**
	 * 대분류 코드 (대분류)
	 */
	private String largeCategoryCode;
	
	/**
	 * 중분류 코드 (중분류)
	 */
	private String middleCategoryCode;
	
	/**
	 * 중분류 리스트 <br>
	 * 수정화면에서 뿌려줄, 해당 품목의 중분류에서, 그 해당 대분류에 해당하는 중분류들 만! 리스트로 만들기
	 */
	private List<MiddleCategoryDTO> middleCategoryDTOList;
	

}
