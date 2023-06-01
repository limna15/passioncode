package com.passioncode.procurementsystem.entity;


import java.time.LocalDateTime;

import org.hibernate.annotations.ColumnDefault;

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
@ToString(exclude = {"purchaseOrder", "transactionDetail"})
public class MaterialIn {	//입고
	
	//입고코드
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(length = 5, columnDefinition = "INT(5)")
	private Integer code;
	
	//입고상태
	@ColumnDefault(value="0") //0: 미완료, 1: 완료
	@Column(length = 10, columnDefinition = "TINYINT(1)", nullable = false)
	private Integer status;
	
	//입고일
	@Column(columnDefinition = "DATETIME", nullable = false)
	private LocalDateTime date;
	
	//발행상태
	@ColumnDefault(value="0") //0: 미완료, 1: 완료
	@Column(length = 10, columnDefinition = "TINYINT(1)", nullable = false)
	private Integer transactionStatus;
	
	//발주서번호(외래키)(구매발주서)
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false)
	private PurchaseOrder purchaseOrder;
	
	//거래명세서번호(외래키)(거래명세서)
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false)
	private TransactionDetail transactionDetail;
	
}
