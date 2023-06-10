package com.passioncode.procurementsystem.dto;

import java.time.LocalDateTime;
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
	//협력회사, 조달납기예정일, 품목, 발주수량, 단가, 납기현황, 다음 진척 검수 일정, 진척 검수 일정, 진척 평가
	//, 납기 진도율, 검수완료, 발주서 마감 상태
	
	
	private Integer code;
	private LocalDateTime date;
	private Integer rate;
	private String ect;
	private Integer purchaseOrderNo;
	
}
