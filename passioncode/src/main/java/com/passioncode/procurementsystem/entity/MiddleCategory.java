package com.passioncode.procurementsystem.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
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
@ToString(exclude = "largeCategory")
public class MiddleCategory {	//중분류
	
	@Id
	@Column(length = 6, columnDefinition = "CHAR(6)")
	private String code;	//중분류코드
	
	@Column(length = 255, nullable = false) 
	private String category;	//종류
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false)
	private LargeCategory largeCategory;	//대분류코드

}
