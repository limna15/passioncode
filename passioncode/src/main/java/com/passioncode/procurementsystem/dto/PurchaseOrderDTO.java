package com.passioncode.procurementsystem.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class PurchaseOrderDTO {
	
	private Integer no;
	private String name;//협력회사
	private Date date;//발주일
	private Date date2;//조달납기 예정일
	private Integer supply;//품목공급L/T
	private Date date3;//최소발주일
	private String code;//품목 코드
	private String name2;//품목명
	private Integer amount;//기존재고수량
	private Integer amount2;//필요수량
	private Integer amount3;//발주수량
	private Integer price;//단가
	private Integer price2;//공급 가격
	private Integer status;//발주서 발행 상태
	

}
