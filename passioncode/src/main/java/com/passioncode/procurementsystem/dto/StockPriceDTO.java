package com.passioncode.procurementsystem.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

//재고산출이랑 겹칠 것 같아 같은 재고산출DTO쓸 예정

/**
 * 현황관리 리포트 화면을 위한 현황관리 리포트DTO 클래스  <br>
 * 대분류, 중분류, 품목코드, 품목명, 입고 수량(= 발주 수량), 출고 수량(=필요 수량), 재고 수량, 재고 단가  <br>
 * 대분류코드, 중분류코드
 * 
 */ 
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class StockPriceDTO {
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
	 * 대분류 코드 (대분류)
	 */
	private String largeCategoryCode;
	
	/**
	 * 중분류 코드 (중분류)
	 */
	private String middleCategoryCode;

}
