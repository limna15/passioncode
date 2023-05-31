package com.passioncode.procurementsystem.entity;

import java.time.LocalDateTime;
import java.util.Date;

import org.hibernate.annotations.ColumnDefault;

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
public class Company {		//협력회사
	
	@Id
	@Column(length = 12, columnDefinition = "CHAR(12)")
	private String no;		//사업자등록번호
	
	@Column(length = 255, nullable = false) 
	private String name;		//협력회사명
	
	@Column(length = 255, nullable = false) 
	private String ceo;			//대표자
	
	@Column(length = 20, nullable = false) 
	private String tel;			//전화번호
	
	@Column(length = 255, nullable = false) 
	private String address;			//주소
	
	@Column(length = 255, nullable = false) 
	private String manager;			//담당자
	
	@Column(length = 20, nullable = false) 
	private String managerTel;			//담당자연락처
	
	@Column(length = 100, nullable = false) 
	private String managerEmail;		//담당자Email
	
	@ColumnDefault(value = "1")		//0 : No, 1 : Yes
	@Column(length = 1, columnDefinition = "TINYINT(1)", nullable = false)
	private Integer dealStatus;			//거래가능여부
	
	@Column(length = 1000)
	private String etc;			//기타사항

}
