package com.passioncode.procurementsystem.entity;

import java.util.Date;

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
@ToString(exclude = "material")
public class MRP {		//자재소요계획
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(length = 5, columnDefinition = "INT(5)")
	private Integer code;	//자재소요계획코드
	
	@Column(length = 255, nullable = false) 
	private String process;		//소요공정
	
	@Column(length = 10, columnDefinition = "INT(10)", nullable = false)
	private Integer amount;		//소요량
	
	@Column(columnDefinition = "DATE", nullable = false)
	private Date date;		//소요일
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false)
	private Material material;		//품목코드

}
