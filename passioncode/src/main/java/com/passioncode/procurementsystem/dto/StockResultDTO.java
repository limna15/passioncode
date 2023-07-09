package com.passioncode.procurementsystem.dto;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 자재산출 화면을 위한 자재산출DTO 클래스  <br>
 * 대분류, 중분류, 품목코드, 품목명, 입고 수량(= 발주 수량), 품목 단가, 입고 금액, 출고 수량(=필요 수량), 출고 금액, 재고 수량, 재고 단가, 재고 금액  <br>
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
	 * 품목 단가 (계약서)
	 */
	private Integer unitPrice;
	
	/**
	 * 입고 금액 (입고 수량 * 품목 단가)
	 */
	private Integer inPrice;
	
	/**
	 * 출고 수량(=필요 수량) (조달계획)
	 */
	private Integer outAmount;
	
	/**
	 * 출고 금액 (출고 수량 * 품목 단가)
	 */
	private Integer outPrice;
	
	/**
	 * 재고 수량 (입고 수량 - 출고 수량)
	 */
	private Integer stockAmount;
	
	/**
	 * 재고 단가 (재고 수량/재고 금액)
	 */
	private Integer stockUnitPrice;
	
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
	private String dateForCalculate;
	
	
	
}
