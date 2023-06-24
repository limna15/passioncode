package com.passioncode.procurementsystem.dto;

import java.time.LocalDateTime;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 
 * @author Soojin
 * 2개 이상의 진척검수 
 */

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class DetailProgressCheckListDTO {
	//pccode, pcdate, pcectc, pcdetail, pcrate
	
	/**
	 *  진척검수 코드
	 */
	private Integer pccode;
	
	/**
	 * 진척검수일
	 */
	private Date pcdate;
	
	/**
	 *  진척검수 기타사항
	 */
	private String pcectc;
	
	/**
	 *  발주 코드
	 */
	private Integer pcdetail;
	
	/**
	 * 납기 진도율
	 */
	private Integer pcrate;
	
	/**
	 * 오늘 날짜
	 */
	private LocalDateTime todaydate;
	
	/**
	 * 열 번호
	 */
	private Integer countno=1;
}
