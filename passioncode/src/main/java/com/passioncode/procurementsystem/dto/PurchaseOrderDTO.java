package com.passioncode.procurementsystem.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class PurchaseOrderDTO {
	
	//private Integer no;
	
	/**
	 * 협력회사
	 */
	private String companyName;//협력회사
	
	/**
	 * 발주일
	 */
	private Date purchaseOrderDate;//발주일
	
	/**
	 * 조달납기 예정일
	 */
	private Date dueDate;//조달납기 예정일
	
	/**
	 * 품목공급L/T
	 */
	private Integer supplyLT;//품목공급L/T
	
	/**
	 * 최소발주일
	 */
	private Date minimumOrderDate;//최소발주일
	
	/**
	 * 품목 코드
	 */
	private String materialCode;//품목 코드
	
	/**
	 * 품목명
	 */
	private String materialName;//품목명
	
	/**
	 * 기존재고수량
	 */
	private Integer stockAmount;//기존재고수량
	
	/**
	 * 필요수량
	 */
	private Integer needAmount;//필요수량
	
	/**
	 * 발주수량
	 */
	private Integer orderAmount;//발주수량
	
	/**
	 * 단가
	 */
	private Integer unitPrice;//단가
	
	/**
	 * 공급 가격
	 */
	private Integer supplyPrice;//공급 가격
	
	/**
	 * 발주서 발행 상태
	 */
	private Boolean purchaseOrderStatus;//발주서 발행 상태
	

}
