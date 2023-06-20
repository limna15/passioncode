package com.passioncode.procurementsystem.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 품목등록 화면을 위한 중분류DTO 클래스 (4개) <br>
 * 중분류코드, 종류, / 대분류코드, 종류 <br>
 * @author KSH
 * 
 */ 
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class MiddleCategoryDTO {
	
	/**
	 * 중분류코드 (중분류)
	 */
	private String middleCode;	
	
	/**
	 * 종류 (중분류)
	 */
	private String middleCategory;	
	
	/**
	 * 대분류코드 (대분류)
	 */
	private String largeCode;	
	
	/**
	 * 종류 (대분류)
	 */
	private String largeCategory;

}
