package com.passioncode.procurementsystem.dto;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 진척검수일정등록, 진척 평가 화면을 위한 진척검수일정등록, 진척 평가DTO 클래스
 * @author Soojin
 *
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class DetailProgressCheckDTO {
	//발주코드, 협력회사, 조달납기예정일, 품목명, 납기현황, 다음 진척 검수 일정
	//, 납기 진도율, 검수완료, 발주서 마감 상태
	
	//외래키로 사용할 것들 추가하기
	
	/**
	 * 발주코드
	 */
	private Integer purchaseOrderCode;
	
	/**
	 * 협력회사
	 */
	private String companyName;//협력회사
	
	/**
	 * 조달납기예정일
	 */
	private Date dueDate;//조달납기 예정일
	
	/**
	 * 품목명
	 */
	private String materialName;//품목명
	
	/**
	 *다음 진척 검수 일정
	 */
	private Date nextCheckDate;
	
	/**
	 *납기 진도율
	 */
	private Integer diliveryPercent;
	
	/**
	 *검수완료
	 */
	private Integer inspectionComplete;
	
}
