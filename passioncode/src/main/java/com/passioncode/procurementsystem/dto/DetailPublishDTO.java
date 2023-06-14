package com.passioncode.procurementsystem.dto;

import java.util.Date;

import lombok.Data;

@Data
public class DetailPublishDTO {
	
	
	/**
	 * 발주코드
	 */
	private Integer ppcode;
	
	/**
	 * 품목코드
	 */
	private String cname;
	
	/**
	 * 납기 예정일
	 */
	private Date due_date;
	
	/**
	 * 품목명
	 */
	private Integer mcode;
	
	/**
	 * 발주수량
	 */
	private String mname;
	
	/**
	 * 단가
	 */
	private Integer unit_price;

}
