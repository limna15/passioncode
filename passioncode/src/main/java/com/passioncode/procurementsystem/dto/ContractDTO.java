package com.passioncode.procurementsystem.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 계약등록 화면을 위한 계약DTO 클래스 (11개+1개) <br>
 * 계약서번호, 품목코드, 품목명, 협력회사, 담당자, 담당자연락처, 품목공급LT, 단가, 거래조건, 계약서, 계약상태 <br>
 * 사업자등록번호
 * @author KSH
 * 
 */ 
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ContractDTO {
	
	/**
	 * 계약서번호 (계약서)
	 */
	private Integer contractNo;
	
	/**
	 * 계약서번호(문자.ver) (계약서)
	 */
	private String contractNoStr;
	
	/**
	 * 품목코드 (품목)
	 */
	private String materialCode;
	
	/**
	 * 품목명 (품목)
	 */
	private String materialName;
	
	/**
	 * 사업자등록번호 (협력회사)
	 */
	private String companyNo;
	
	/**
	 * 협력회사명 (협력회사)
	 */
	private String companyName;
	
	/**
	 * 담당자 (협력회사)
	 */
	private String manager;
	
	/**
	 * 담당자연락처 (협력회사)
	 */
	private String managerTel;
	
	/**
	 * 품목공급LT (계약서)
	 */
	private Integer supplyLt;
	
	/**
	 * 단가 (계약서)
	 */
	private Integer unitPrice;				
	
	/**
	 * 거래조건 (계약서)
	 */
	private String dealCondition;			
	
	/**
	 * 계약서 (계약서)
	 */
	private String contractFile;	
	
	/**
	 * 계약상태 (추가된 변수) <br>
	 * 완료 : 계약상태 O, 미완료 : 계약상태 X 
	 */
	private String contractStatus;
	
}
