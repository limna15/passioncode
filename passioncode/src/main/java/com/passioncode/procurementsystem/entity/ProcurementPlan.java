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
@ToString(exclude = {"material","purchaseOrder"})
public class ProcurementPlan {		//조달계획
	
	//조달계획코드
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(length = 5, columnDefinition = "INT(5)")
	private Integer code;	
	
	//필요수량
	@Column(length = 10, columnDefinition = "INT(10)", nullable = false)
	private Integer amount;		
	
	//조달납기 예정일
	@Column(columnDefinition = "DATE", nullable = false)
	private Date dueDate;	
	
	//최소발주일
	@Column(columnDefinition = "DATE", nullable = false)
	private Date minimumOrderDate;		
	
	//조달계획 등록일
	@Column(columnDefinition = "DATETIME", nullable = false)
	private LocalDateTime registerDate;			
	
	//조달계획 완료일
	@Column(columnDefinition = "DATETIME", nullable = false)
	private LocalDateTime completionDate;		
	
	//품목코드(외래키)(품목)
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false)
	private Material material;		
	
	//발주서번호(외래키)(구매발주서)
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false)
	private PurchaseOrder purchaseOrder;		

	

}
