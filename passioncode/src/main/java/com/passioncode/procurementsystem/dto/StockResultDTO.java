package com.passioncode.procurementsystem.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 자재산출 화면을 위한 자재산출DTO 클래스 8+2개<br>
 * 대분류, 중분류, 품목코드, 품목명, 입고 수량(= 발주 수량), 출고 수량(=필요 수량), 재고 수량, 재고 단가   <br>
 * 대분류코드, 중분류코드 
 * 
 */ 
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Data
public class StockResultDTO {
	
	/**
	 * 대분류 종류 (대분류)
	 */
	private String largeCategoryName;
	
	/**
	 * 중분류 종류 (중분류)
	 */
	private String middleCategoryName;
	
	/**
	 * 품목코드 (품목)
	 */
	private String materialCode;
	
	/**
	 * 품목명 (품목)
	 */
	private String materialName;
	
	/**
	 * 입고 수량(=발주 수량) (세부구매발주서)
	 */
	private Integer inAmount;
	
	/**
	 * 출고 수량(=필요 수량) (조달계획)
	 */
	private Integer outAmount;
	
	/**
	 * 재고 수량 (입고 수량 - 출고 수량)
	 */
	private Integer stockAmount;
	
	/**
	 * 재고 단가 (재고 수량/재고 금액)
	 */
	private Integer stockUnitPrice;
	
	/**
	 * 대분류 코드 (대분류)
	 */
	private String largeCategoryCode;
	
	/**
	 * 중분류 코드 (중분류)
	 */
	private String middleCategoryCode;
	
}
