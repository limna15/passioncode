package com.passioncode.procurementsystem.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 
 * @author Soojin
 * 발주서 발행용 
 */

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class DetailPublishDTO {
	
	
	/**
	 * 발주코드
	 */
	private Integer ppcode;
	
	/**
	 * 회사이름
	 */
	private String cname;
	
	/**
	 * 납기 예정일
	 */
	private Date due_date;
	
	/**
	 * 품목코드
	 */
	private String mcode;
	
	/**
	 * 품목이름
	 */
	private String mname;
	
	/**
	 * 단가
	 */
	private Integer unit_price;

}
