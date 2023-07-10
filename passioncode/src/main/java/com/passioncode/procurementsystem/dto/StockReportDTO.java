package com.passioncode.procurementsystem.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 재고금액 현황리포트를 위한 재고금액DTO 클래스 4+2개  <br>
 * 대분류, 중분류, 재고 금액, 각각의 날짜 <br>
 * 대분류코드, 중분류코드 
 * 
 */ 
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Data
public class StockReportDTO {
	
	/**
	 * 대분류 종류 (대분류)
	 */
	private String largeCategoryName;
	
	/**
	 * 중분류 종류 (중분류)
	 */
	private String middleCategoryName;
	
	/**
	 * 재고 금액 (입고 금액 - 출고 금액)
	 */
	private Integer stockTotalPrice;
		
	/**
	 * 대분류 코드 (대분류)
	 */
	private String largeCategoryCode;
	
	/**
	 * 중분류 코드 (중분류)
	 */
	private String middleCategoryCode;
	
	/**
	 * 계산할때 쓰기위한 각각의 날짜
	 */
	private String eachDateStr;
	
	
}
