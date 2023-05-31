package com.passioncode.procurementsystem.entity;

import java.time.LocalDateTime;
import java.util.Date;

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

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString(exclude = {"material","company"})
public class Contract {			//계약서
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(length = 8, columnDefinition = "INT(8)")
	private Integer no;			//계약서번호
	
	@Column(length = 10, columnDefinition = "INT(10)", nullable = false)
	private Integer supplyLt;			//품목공급LT
	
	@Column(length = 10, columnDefinition = "INT(10)", nullable = false)
	private Integer unitPrice;			//단가	

	@Column(length = 255, nullable = false) 
	private String dealCondition;			//거래조건
	
	@Column(length = 255, nullable = false) 
	private String contractFile;			//계약서
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false)
	private Material material;			//품목코드
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false)
	private Company company;		//사업자등록번호

}
