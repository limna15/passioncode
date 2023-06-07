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
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 자재출고 테이블을 위한 엔티티 클래스
 * @author KSH
 * 
 */ 
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString(exclude = "mrp")
public class MaterialOut {	
	
	/**
	 * 출고코드
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(length = 5, columnDefinition = "INT(5)")
	private Integer code;
	
	/**
	 * 기존재고수량
	 */
	@Column(length = 10, columnDefinition = "INT(10)")
	private Integer stockAmount;
	
	/**
	 * 출고상태
	 */
	@ColumnDefault(value="1") //null: 완료/취소 버튼, 0: 미완료, 1: 완료
	@Column(length = 10, columnDefinition = "TINYINT(1)")
	private Integer status;
	
	/**
	 * 출고일
	 */
	@Column(columnDefinition = "DATETIME")
	private LocalDateTime date;
	
	/**
	 * 자재소요계획코드(외래키)(MRP)
	 */
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false)
	private MRP mrp;

}
