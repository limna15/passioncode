package com.passioncode.procurementsystem.dto;

import java.util.Date;
import com.passioncode.procurementsystem.entity.DetailPurchaseOrder;
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
	
	/**
	 * 진척 검수한 날짜
	 */
	private Date thisCheckDate;
	
	/**
	 * 진척 검수 코드
	 */
	private Integer checkCode;
	
	/**
	 * 기타 사항
	 */
	private String etc;
	
	/**
	 * 발주코드, 임시 추가
	 */
	private DetailPurchaseOrder detailCode;
	
	/**
	 * 발주서 번호, 임시 추가
	 */
	private DetailPurchaseOrder detailNo;
	
	/**
	 * 보여지는 발주코드
	 */
	private String showPurchaseOrderCode;
	
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
	 * 발주 수량
	 */
	private Integer orderAmount;
	
	/**
	 * 단가
	 */
	private Integer unitPrice;//단가
	
	/**
	 * 납기현황
	 */
	private String diliveryStatus;
	
	/**
	 *다음 진척 검수 일정
	 */
	private String nextCheckDate;
	
	/**
	 *납기 진도율
	 */
	private String diliveryPercent;//미완료도 있어서 스트링으로
	
	/**
	 *검수완료
	 */
	private String inspectionComplete;
	
	/**
	 *발주서 마감 상태
	 */
	private String purchaseOrderDeadlineStatus;
	
}
