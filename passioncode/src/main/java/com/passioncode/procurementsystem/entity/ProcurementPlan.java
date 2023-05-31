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
@ToString(exclude = {"material","purchaceOrder"})
public class ProcurementPlan {		//조달계획
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(length = 5, columnDefinition = "INT(5)")
	private Integer code;	//조달계획코드
	
	@Column(length = 10, columnDefinition = "INT(10)", nullable = false)
	private Integer amount;		//필요수량
	
	@Column(columnDefinition = "DATE", nullable = false)
	private Date dueDate;		//조달납기 예정일
	
	@Column(columnDefinition = "DATE", nullable = false)
	private Date minimumOrderDate;		//최소발주일
	
	@Column(columnDefinition = "DATETIME", nullable = false)
	private LocalDateTime registerDate;			//조달계획 등록일
	
	@Column(columnDefinition = "DATETIME", nullable = false)
	private LocalDateTime completionDate;		//조달계획 완료일
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false)
	private Material material;		//품목코드
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false)
	private PurchaceOrder purchaceOrder;		//발주서번호

	

}
