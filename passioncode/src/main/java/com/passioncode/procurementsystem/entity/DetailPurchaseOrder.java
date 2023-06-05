package com.passioncode.procurementsystem.entity;

import java.time.LocalDateTime;

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
@ToString(exclude = "purchaseOrder")
public class DetailPurchaseOrder {	//세부 구매 발주서
	
	//발주 코드
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(length = 5, columnDefinition = "INT(5)")
	private Integer code;	
	
	//발주 수량
	@Column(length = 10, columnDefinition = "INT(10)", nullable = false)
	private Integer amount;	
	
	//발주일
	@Column(columnDefinition = "DATETIME", nullable = false)
	private LocalDateTime date;	
	
	//발주서 번호(외래키)(구매발주서)
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false)
	private PurchaseOrder purchaseOrder;	

}
