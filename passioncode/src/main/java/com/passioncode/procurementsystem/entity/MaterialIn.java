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
@ToString(exclude = {"detailPurchaseOrder", "transactionDetail"})
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
	 */
	@ColumnDefault(value="0") //null: 완료/취소 버튼, 0: 미완료, 1: 완료
	@Column(length = 10, columnDefinition = "TINYINT(1)")
	private Integer status;
	
	/**
	 * 입고일
	 */
	@Column(columnDefinition = "DATETIME")
	private LocalDateTime date;
	
	/**
	 * 발행상태
	 */
	@ColumnDefault(value="0") //0: 미완료, 1: 완료
	@Column(length = 10, columnDefinition = "TINYINT(1)", nullable = false)
	private Integer transactionStatus;
	
	/**
	 * 발주코드(외래키)(세부구매발주서)
	 */
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false)
	private DetailPurchaseOrder detailPurchaseOrder;
	
	//거래명세서번호(외래키)(거래명세서)
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false)
	private TransactionDetail transactionDetail;
	//나영아 여기에 연결할게 아니라.. 이거 거래명세서는 구매발주서랑 일대일로 연결하기로 한거 아니야???
	
}
