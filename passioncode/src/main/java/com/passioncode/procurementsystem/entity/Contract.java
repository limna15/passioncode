package com.passioncode.procurementsystem.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 계약서 테이블을 위한 엔티티 클래스
 * @author KSH
 * 
 */ 
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString(exclude = {"material","company"})
public class Contract {			//계약서
	
	/**
	 * 계약서번호
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(length = 8, columnDefinition = "INT(8)")
	private Integer no;			
	
	/**
	 * 품목공급LT
	 */
	@Column(length = 10, columnDefinition = "INT(10)", nullable = false)
	private Integer supplyLt;			
	
	/**
	 * 단가
	 */
	@Column(length = 10, columnDefinition = "INT(10)", nullable = false)
	private Integer unitPrice;				
	
	/**
	 * 거래조건
	 */
	@Column(length = 255) 
	private String dealCondition;			
	
	/**
	 * 계약서	
	 */
	@Column(length = 255, nullable = false) 
	private String contractFile;		
	
	/**
	 * 품목코드(외래키)(품목)
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false)
	private Material material;			
	
	/**
	 * 사업자등록번호(외래키)(협력회사)
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false)
	private Company company;		

}
