package com.passioncode.procurementsystem.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 자재입고 화면을 위한 입고DTO 클래스(8개)
 * 발주서 번호, 발주코드, 조달납기 예정일, 품목코드, 품목명, 발주수량, 입고상태, 거래명세서 발행상태
 * @author LNY
 *
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class MaterialInDTO {

	/**
	 * 발주서 번호
	 */
	private Integer no;
	
	/**
	 * 발주코드
	 */
	private Integer code;
	
	/**
	 * 조달납기예정일
	 */
	private LocalDateTime dueDate;
	
	/**
	 * 품목코드
	 */
	private Integer materialCode;

	/**
	 * 품목명
	 */
	private Integer materialName;
	
	/**
	 * 발주수량
	 */
	private Integer amount;
	
	/**
	 * 입고상태
	 * False(0): 미완료, True(1): 완료(기본값)
	 */
	private Boolean status;
	
	/**
	 * 거래명세서 발행상태
	 * False(0): 미완료(기본값), True(1): 완료
	 */
	private Boolean transactionStatus;

}
