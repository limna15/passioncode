package com.passioncode.procurementsystem.dto;

import java.util.Date;

import com.passioncode.procurementsystem.entity.ProcurementPlan;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 조달계획 등록 화면을 위한 조달계획DTO 클래스, (13개)
 * 품목코드, 품목명, 소요공정, 소요일, 소요량, 협력회사, 품목공급LT, 조달납기예정일, 최소발주일, 필요수량, 계약상태, 조달계획 등록상태, 조달계획 진행사항
 * @author KSH
 * 
 */ 
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ProcurementPlanDTO {
	
	/**
	 * 품목코드 (품목, MRP)
	 */
	private String materialCode;	
	
	/**
	 * 품목명 (품목, MRP)
	 */
	private String materialName;
	
	/**
	 * 소요공정(MRP)
	 */
	private String process;		
	
	/**
	 * 소요량 (MRP)
	 */
	private Integer mrpAmount;		
	
	/**
	 * 소요일 (MRP)
	 */
	private Date mrpdate;	
	
	/**
	 * 협력회사명 (협력회사, 계약서)
	 */
	private String companyName;
	
	/**
	 * 품목공급LT (계약서)
	 */
	private Integer supplyLt;	

	/**
	 * 필요수량 (조달계획)
	 */
	private Integer ppAmount;		
	
	/**
	 * 조달납기 예정일 (조달계획)
	 */
	private Date dueDate;	
	
	/**
	 * 최소발주일 (조달계획)
	 */
	private Date minimumOrderDate;		
	
	/**
	 * 계약상태 (추가된 변수)
	 * False : 미완료, True : 완료
	 */
	private Boolean contractStatus;
	
	/**
	 * 조달계획 등록상태 (추가된 변수)
	 * False : 미완료, True : 완료
	 */
	private Boolean ppRegisterStatus;
	
	/**
	 * 조달계획 진행사항 (추가된 변수)
	 * 등록 후 발주코드 존재X : 발주 예정, 
	 * 등록 후 발주코드 존재O, 조달완료일 존재X : 조달 진행 중, 
	 * 등록 후 조달완료일 존재O : 조달 완료 
	 */
	private String ppProgress;
	
	
}
