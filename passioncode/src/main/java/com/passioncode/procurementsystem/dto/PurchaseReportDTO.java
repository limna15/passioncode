package com.passioncode.procurementsystem.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 발주현황 리포트 화면을 위한 발주리포트DTO 클래스(5개) <br>
 * 조달계획개수, 발주예정, 발주서발행, 조달 진행 중, 조달완료
 * @author LNY
 *
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class PurchaseReportDTO {
	
	/**
	 * 날짜(선택한 기간 넣어주기 위한 변수)
	 */
	private String date;
	
	/**
	 * 발주예정
	 */
	private Integer beforePurchase;

	/**
	 * 조달 진행 중
	 */
	private Integer ingProcurement;
	
	/**
	 * 조달완료
	 */
	private Integer doneProcurement;
	
	/**
	 * 조달계획개수
	 */
	private Integer procurementPlanCount;
}
