package com.passioncode.procurementsystem.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
/**
 * 중분류 테이블을 위한 엔티티 클래스 (3개) <br>
 * 중분류코드, 종류, 대분류코드(외래키)(대분류)
 * @author KSH
 * 
 */ 
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString(exclude = "largeCategory")
public class MiddleCategory {	//중분류
	
	/**
	 * 중분류코드
	 */
	@Id
	@Column(length = 6, columnDefinition = "CHAR(6)")
	private String code;	
	
	/**
	 * 종류
	 */
	@Column(length = 255, nullable = false) 
	private String category;	
	
	/**
	 * 대분류코드(외래키)(대분류)
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false)
	private LargeCategory largeCategory;	
	
}
