package com.passioncode.procurementsystem.dto;

import java.time.LocalDateTime;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 
 * @author Soojin
 * 발주서 발행용 
 */

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class DetailPublishDTO {
	
	/**
	 * 발주서 번호
	 */
	private Integer pono;
	
	/**
	 * 발주 코드
	 */
	private Integer pocode;
	
	/**
	 * 조달계획 코드
	 */
	private Integer ppcode;
	
	/**
	 * 회사이름
	 */
	private String cname;
	
	/**
	 * 납기 예정일
	 */
	private Date due_date;
	
	/**
	 * 품목코드
	 */
	private String mcode;
	
	/**
	 * 품목이름
	 */
	private String mname;
	
	/**
	 * 단가
	 */
	private Integer unit_price;
	
	/**
	 * 공급 가격
	 */
	private Integer supply_price;
	
	/**
	 * 발주일
	 */
	private LocalDateTime purchaseOrderDate;
	
	/**
	 * 기존재고수량 -> 사용하지 않음
	 */
	private Integer mamount;

	/**
	 * 필요수량
	 */
	private Integer ppamount;
}
