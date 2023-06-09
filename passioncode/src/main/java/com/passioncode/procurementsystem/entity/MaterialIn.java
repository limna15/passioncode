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
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
/**
 * 자재입고 테이블을 위한 엔티티 클래스
 * @author LNY
 * 
 */ 
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString(exclude = {"detailPurchaseOrder"})
public class MaterialIn {	//입고
	
	/**
	 * 입고코드
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(length = 5, columnDefinition = "INT(5)")
	private Integer code;
	
	/**
	 * 입고상태
	 * False(0): 미완료, True(1): 완료(기본값)
	 */
	@ColumnDefault(value="1") //0: 미완료, 1: 완료
	@Column(length = 10, columnDefinition = "TINYINT(1)", nullable = false)
	private Boolean status;
	
	/**
	 * 입고일
	 */
	@Column(columnDefinition = "DATETIME", nullable = false)
	private LocalDateTime date;
	
	/**
	 * 발행상태
	 */
	@ColumnDefault(value="0") //0: 미완료, 1: 완료
	@Column(length = 10, columnDefinition = "TINYINT(1)", nullable = false)
	private Boolean transactionStatus;
	
	/**
	 * 거래명세서 발행상태
	 * False(0): 미완료(기본값), True(1): 완료
	 */
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false)
	private DetailPurchaseOrder detailPurchaseOrder;

}
