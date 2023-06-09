package com.passioncode.procurementsystem.entity;

import org.hibernate.annotations.ColumnDefault;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 협력회사 테이블을 위한 엔티티 클래스 (10개)<br>
 * 사업자등록번호, 협력회사명, 대표자, 전화번호, 주소, 담당자, 담당자연락처, 담당자Email, 거래가능여부, 기타사항
 * @author KSH
 * 
 */
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class Company {		//협력회사
	
	/**
	 * 사업자등록번호
	 */
	@Id
	@Column(length = 12, columnDefinition = "CHAR(12)")
	private String no;		

	/**
	 * 협력회사명
	 */
	@Column(length = 255, nullable = false) 
	private String name;		
	
	/**
	 * 대표자
	 */
	@Column(length = 255, nullable = false) 
	private String ceo;			
	
	/**
	 * 전화번호
	 */
	@Column(length = 20, nullable = false) 
	private String tel;			
	
	/**
	 * 주소
	 */
	@Column(length = 255, nullable = false) 
	private String address;		
	
	/**
	 * 담당자
	 */
	@Column(length = 255, nullable = false) 
	private String manager;			
	
	/**
	 * 담당자연락처
	 */
	@Column(length = 20, nullable = false) 
	private String managerTel;			
	
	/**
	 * 담당자Email
	 */
	@Column(length = 100, nullable = false) 
	private String managerEmail;		
	
	/**
	 * 거래가능여부,
	 * 기본값 = 1, 0 : No, 1 : Yes
	 */
	@ColumnDefault(value = "1")		//0 : No, 1 : Yes
	@Column(length = 1, columnDefinition = "TINYINT(1)", nullable = false)
	private Integer dealStatus;			
	
	/**
	 * 기타사항
	 */
	@Column(length = 1000)
	private String etc;			

}
