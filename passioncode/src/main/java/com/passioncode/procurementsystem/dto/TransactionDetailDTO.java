package com.passioncode.procurementsystem.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 거래명세서 화면을 위한 거래명세서디테일DTO 클래스(14개) <br>
 * 거래명서세번호, 발주회사(=우리회사), 발주서번호, 발주코드, 납기일자(=입고일), 사업자등록번호, 상호, CEO, 주소, 담당자, 연락처, 품목코드, 품목명, 수량, 단가
 * @author LNY
 *
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class TransactionDetailDTO {
	
	/**
	 * 거래명세서 번호 (거래명세서)
	 */
	private Integer no;
	
	/**
	 * 발주회사(=우리회사) (협력회사)
	 */
	private String company;
	
	/**
	 * 발주서번호 (구매발주서)
	 */
	private Integer purchaseOrderNo;
	
	/**
	 * 발주코드 (세부구매발주서)
	 */
	private Integer detailPurchaseOrderCode;
	
	/**
	 * 납기일자(=입고일) (자재입고)
	 */
	private LocalDateTime date;
	
	/**
	 * 사업자등록번호 (협력회사)
	 */
	private String companyNo;
	
	/**
	 * 상호 (협력회사)
	 */
	private String companyName;
	
	/**
	 * CEO (협력회사)
	 */
	private String CEO;
	
	/**
	 * 주소 (협력회사)
	 */
	private String companyAddress;
	
	/**
	 * 담당자 (협력회사)
	 */
	private String manager;
	
	/**
	 * 담당자연락처 (협력회사)
	 */
	private String managerTel;
	
	/**
	 * 품목코드 (품목)
	 */
	private String materialCode;
	
	/**
	 * 품목명 (품목)
	 */
	private String materialName;
	
	/**
	 * 수량 (세부구매발주서)
	 */
	private Integer amount;
	
	/**
	 * 단가 (계약서)
	 */
	private Integer unitPrice;
}
