package com.passioncode.procurementsystem.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
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
@ToString
public class LargeCategory {	//대분류
	
	//대분류코드
	@Id
	@Column(length = 6, columnDefinition = "CHAR(6)")
	private String code;	
	
	//종류
	@Column(length = 255, nullable = false) 
	private String category;	
	
	
	
	
}
