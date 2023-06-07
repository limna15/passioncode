package com.passioncode.procurementsystem.dto;

import lombok.AllArgsConstructor;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
/**
 * 품목정보등록 화면을 위한 품목DTO 클래스, (11개)
 * 품목코드, 품목명, 대, 중, 규격, 재질, 제작사양, 도면번호, 도면Image, 공용여부, 계약상태
 * @author KSH
 * 
 */ 
@Builder
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
	 * 공용여부 (품목), 
	 * 기본값 = 1, 0 : 공용, 1 : 전용
	 */
	private Integer shareStatus;		
	
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
	 * 대분류 종류 (대분류)
	 */
	private String largeCategoryName;
	
	/**
	 * 중분류 종류 (중분류)
	 */
	private String middleCategoryName;
	
	/**
	 * 계약상태 (추가된 변수)
	 * False : 미완료, True : 완료
	 */
	private Boolean contractStatus;
	

}
