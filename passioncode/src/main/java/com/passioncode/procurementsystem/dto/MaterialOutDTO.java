package com.passioncode.procurementsystem.dto;

import java.time.LocalDateTime;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 자재출고 화면을 위한 자재출고DTO 클래스  <br>
 * 발주코드, 소요일, 품목코드, 품목명, 소요공정, 소요량, 출고상태  <br>
 * 출고코드, 자재소요계획코드, 출고일
 * @author KSH
 * 
 */ 
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class MaterialOutDTO {
	/**
	 * 발주코드 (세부구매발주서)
	 */
	private Integer dpoCode;
	
	/**
	 * 발주코드(문자ver.)
	 */
	private String dpoCodeStr;
	
	/**
	 * 소요일 (MRP)
	 */
	private Date mrpDate;
	
	/**
	 * 품목코드 (품목)
	 */
	private String materialCode;
	
	/**
	 * 품목명 (품목)
	 */
	private String materialName;
	
	/**
	 * 소요공정 (MRP)
	 */
	private String process;
	
	/**
	 * 소요량 (MRP)
	 */
	private Integer mrpAmount;
	
	/**
	 * 출고상태 (자재출고) <br>
	 * 완료 버튼만 존재, 0: 버튼, 1: 완료
	 */
	private String outStatus;
	
	/**
	 * 출고코드 (자재출고)
	 */
	private Integer outCode;
	
	/**
	 * 자재소요계획코드 (MRP)
	 */
	private Integer mrpCode;
	
	/**
	 * 출고일 (자재출고)
	 */
	private LocalDateTime outDate;	
}
