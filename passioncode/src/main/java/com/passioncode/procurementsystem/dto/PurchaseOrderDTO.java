package com.passioncode.procurementsystem.dto;

import java.time.LocalDateTime;
import java.util.Date;

import com.passioncode.procurementsystem.entity.DetailPurchaseOrder;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 구매발주서 등록 화면을 위한 구매발주서DTO 클래스, (13개+) 
 * 협력회사, 발주일, 조달납기 예정일, 품목공급LT, 최소발주일, 품목코드, 품목명, 기존재고수량, 필요수량, 발주수량, 단가, 공급가격, 발주서 발행상태
 * 조달계획코드, 자재소요계획코드, 출고코드 , 사업자등록번호, 계약서번호, 발주코드
 * @author Soojin
 *
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class PurchaseOrderDTO {
	
	/**
	 * 발주코드, 임시 추가
	 */
	private Integer detailCode;
	
	/**
	 * 발주서 번호, 임시 추가
	 */
	private Integer detailNo;
	
	/**
	 * 협력회사
	 */
	private String companyName;//협력회사
	
	/**
	 * 발주일
	 */
	private LocalDateTime purchaseOrderDate;//발주일
	
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
	 * 기존재고수량 <- 사용하지 않기로함
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
	private String purchaseOrderStatus;//발주서 발행 상태
	
	/**
	 * 조달계획코드
	 */
	private Integer procurementPlan;//세부 구매발주서를 위한 조달계획코드 //이전 스펠링 : procuremnetPlan

}
