package com.passioncode.procurementsystem.entity;


import java.time.LocalDateTime;
import java.util.Date;

import org.hibernate.annotations.ColumnDefault;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
/**
 * 자재입고 테이블을 위한 엔티티 클래스 (5개) <br>
 * 입고코드, 입고상태, 입고일, 발행상태, 발주코드(외래키)(세부구매발주서)
 * @author LNY
 * 
 */
@EntityListeners(value= {AuditingEntityListener.class})
@Entity
@Builder(toBuilder = true)
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
	 * 입고상태 <br>
	 * False(0): 취소, True(1): 완료(기본값)
	 */
	@ColumnDefault(value="1") //0: 미완료, 1: 완료
	@Column(length = 10, columnDefinition = "TINYINT(1)", nullable = false)
	private Boolean status;
	
	/**
	 * 입고일
	 */
	@CreatedDate
	@Column(columnDefinition = "DATETIME", nullable = false, updatable = false)
	private LocalDateTime date;
	
	/**
	 * 거래명세서 발행상태 <br>
	 * 입고 취소: 발행 불가 <br>
	 * 입고 완료: 발행 예정 <br>
	 * 입고 완료 + 거래명세서 발행: 발행 완료
	 */
	@Column(length = 6)
	private String transactionStatus;
	
	/**
	 * 발주코드(외래키)(세부구매발주서)
	 */
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false)
	private DetailPurchaseOrder detailPurchaseOrder;

}
