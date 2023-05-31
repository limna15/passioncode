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

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString(exclude = "middleCategory")
public class Material {		//품목
	
	@Id
	@Column(length = 7, columnDefinition = "CHAR(7)")
	private String code;	//품목코드
	
	@Column(length = 255,nullable = false) 
	private String name;	//품목명
	
	@ColumnDefault(value = "1")  //0 : 공용, 1 : 전용
	@Column(length = 1, columnDefinition = "TINYINT(1)", nullable = false)
	private Integer shareStatus;	//공용여부
	
	@Column(length = 255)
	private String size;	//규격
	
	@Column(length = 255)
	private String quality;		//재질
	
	@Column(length = 255)
	private String spec;	//제작사양
	
	@Column(length = 255)
	private String drawingNo;	//도면번호
	
	@Column(length = 255)
	private String drawingFile; 	//도면
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false)
	private MiddleCategory middleCategory;	//중분류코드
	

}
