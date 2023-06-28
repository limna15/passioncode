package com.passioncode.procurementsystem.dto;

import java.time.LocalDateTime;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.RequestParam;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 자재산출 화면을 위한 자재산출DTO 클래스  <br>
 * 대분류, 중분류, 품목코드, 품목명, 입고 수량(= 발주 수량), 출고 수량(=필요 수량), 재고 수량, 재고 단가  <br>
 * 대분류코드, 중분류코드, 출고코드, 세부구매발주서코드, 조달계획코드
 * 
 */ 
@Builder
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
	 * 재고 수량
	 */
	private Integer stockAmount;
	
	/**
	 * 재고 단가
	 */
	private Integer stockPrice;
	
	/**
	 * 품목 단가 (계약서)
	 */
	private Integer unitPrice;
		
	/**
	 * 대분류 코드 (대분류)
	 */
	private String largeCategoryCode;
	
	/**
	 * 중분류 코드 (중분류)
	 */
	private String middleCategoryCode;
	
	/**
	 * 출고코드 (자재출고)
	 */
	private Integer outCode;
	
	/**
	 * 세부구매발주서코드 (세부구매발주서)
	 */
	private Integer dpoCode;
	
	/**
	 * 조달계획 코드 (조달계획)
	 */
	private Integer ppCode;	
	
	
	
}
