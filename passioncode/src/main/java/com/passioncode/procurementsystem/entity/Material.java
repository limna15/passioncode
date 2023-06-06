package com.passioncode.procurementsystem.entity;

import org.hibernate.annotations.ColumnDefault;

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
 * 품목 테이블을 위한 엔티티 클래스
 * @author KSH
 * 
 */ 
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString(exclude = "middleCategory")
public class Material {		//품목
	
	/**
	 * 품목코드
	 */
	@Id
	@Column(length = 7, columnDefinition = "CHAR(7)")
	private String code;	
	
	/**
	 * 품목명
	 */
	@Column(length = 255,nullable = false) 
	private String name;	
	
	/**
	 * 공용여부,
	 * 기본값 = 1, 0 : 공용, 1 : 전용
	 */
	@ColumnDefault(value = "1")  //0 : 공용, 1 : 전용
	@Column(length = 1, columnDefinition = "TINYINT(1)", nullable = false)
	private Integer shareStatus;	
	
	/**
	 * 규격
	 */
	@Column(length = 255)
	private String size;	
	
	/**
	 * 재질
	 */
	@Column(length = 255)
	private String quality;		
	
	/**
	 * 제작사양
	 */
	@Column(length = 255)
	private String spec;	
	
	/**
	 * 도면번호
	 */
	@Column(length = 255)
	private String drawingNo;	
	
	/**
	 * 도면
	 */
	@Column(length = 255)
	private String drawingFile; 	
	
	/**
	 * 중분류코드(외래키)(중분류)
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false)
	private MiddleCategory middleCategory;	
	

}
