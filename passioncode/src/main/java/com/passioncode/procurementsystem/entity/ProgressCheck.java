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
@ToString(exclude = "purchaseOrder")
public class ProgressCheck {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(length = 5, columnDefinition = "INT(5)")
	private Integer code;	//진척검수코드
	
	@Column(columnDefinition = "DATE", nullable = false)
	private LocalDateTime date;	//진척검수 일정
	
	@ColumnDefault(value = "0")
	@Column(length = 3, columnDefinition = "INT(3)")
	private Integer rate;	//납기 진도율
	
	@Column(length = 1000)
	private String etc;	//기타사항
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false)
	private PurchaseOrder purchaseOrder;	//발주서 번호
	

}
