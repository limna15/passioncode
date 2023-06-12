package com.passioncode.procurementsystem.dto;

import java.time.LocalDateTime;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 구매발주서 발행화면을 위한 DTO 클래스
 * @author soojin
 *
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class DetailPurchaseOrderDTO {
	
	/**
	 * 발주서 번호
	 */
	private Integer purchaseOrderNo;
	
	/**
	 * 협력 회사
	 */
	private String companyName;
	
	/**
	 * 발주 일자
	 */
	private LocalDateTime purchaseOrderDate;
	
	/**
	 * 납기 예정일
	 */
	private Date dueDate;
	
	/**
	 * 발주코드
	 */
	private Integer purchaseOrderCode;
	
	/**
	 * 품목코드
	 */
	private String materialCode;
	
	/**
	 * 품목명
	 */
	private String materialName;
	
	/**
	 * 발주수량
	 */
	private Integer purchaseOrderAmount;
	
	/**
	 * 단가
	 */
	private Integer unitPrice;
	
	/**
	 * 공급가격
	 */
	private Integer suppluPrice;
	
	/**
	 * 조달계획코드
	 */
	private Integer procurementPlan;//세부 구매발주서를 위한 조달계획코드
	
}
