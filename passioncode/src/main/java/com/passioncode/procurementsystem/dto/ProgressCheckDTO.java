package com.passioncode.procurementsystem.dto;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 진척검수처리 화면을 위한 진척검수처리DTO 클래스
 * @author Soojin
 *
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ProgressCheckDTO {
	//총 10개(외래키 1개)
	//발주코드, 협력회사, 조달납기 예정일, 품목명, 발주수량
	//, 납기 진도율, 검수완료, 발주서 마감상태, 
	//외래키 갖고 오기==> 기타(외래키)
	
	//외래키로 사용할 것들 추가하기
	
	//진척 검수 코드.. 생각 중
	/**
	 * 기타 사항
	 */
	//private String etc;
	
	/**
	 * 발주코드, 외래키
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
	
	//납기 현황
	
	/**
	 * 단가
	 */
	private Integer unitPrice;//단가
	
	/**
	 * 납기현황
	 */
	private Integer diliveryStatus;
	
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
	
	/**
	 *발주서 마감 상태
	 */
	private String purchaseOrderDeadlineStatus;
	
}
