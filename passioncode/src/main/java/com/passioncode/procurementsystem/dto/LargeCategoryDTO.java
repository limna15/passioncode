package com.passioncode.procurementsystem.dto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 품목등록 화면을 위한 대분류DTO 클래스 (2개) <br>
 * 대분류코드, 종류 <br>
 * @author KSH
 * 
 */ 
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class LargeCategoryDTO {

	/**
	 * 대분류코드 (대분류)
	 */
	private String code;	
	
	/**
	 * 종류 (대분류)
	 */
	private String category;
}
