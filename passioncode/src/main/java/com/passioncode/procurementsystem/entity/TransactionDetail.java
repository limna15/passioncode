package com.passioncode.procurementsystem.entity;

import java.time.LocalDateTime;
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
 * 거래명세서 테이블을 위한 엔티티 클래스 (2개) <br>
 * 거래명세서번호, 발주서번호(외래키)(구매발주서)
 * @author LNY
 * 
 */
@EntityListeners(value= {AuditingEntityListener.class})
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString(exclude = {"purchaseOrder"})
public class TransactionDetail {	//거래명세서
	
	/**
	 * 거래명세서번호
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(length = 8, columnDefinition = "INT(8)")
	private Integer no;
	
	/**
	 * 거래명세서 발행일
	 */
	@CreatedDate
	@Column(columnDefinition = "DATETIME", nullable = false, updatable = false)
	private LocalDateTime date;
	
	/**
	 * 발주서번호(외래키)(구매발주서)
	 */
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false)
	private PurchaseOrder purchaseOrder;	
}
