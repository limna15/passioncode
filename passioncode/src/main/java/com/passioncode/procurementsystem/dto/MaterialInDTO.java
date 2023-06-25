package com.passioncode.procurementsystem.dto;

import java.time.LocalDateTime;
import java.util.Date;

import com.passioncode.procurementsystem.entity.Contract;
import com.passioncode.procurementsystem.entity.DetailPurchaseOrder;
import com.passioncode.procurementsystem.entity.MRP;
import com.passioncode.procurementsystem.entity.ProcurementPlan;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 자재입고 화면을 위한 입고DTO 클래스(8개 + 1개(거래명세서에 쓸 납기일자=입고일)) <br>
 * 발주서 번호, 발주코드, 조달납기 예정일, 품목코드, 품목명, 발주수량, 입고상태, 거래명세서 발행상태
 * @author LNY
 *
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class MaterialInDTO {

	/**
	 * 발주서 번호 (구매발주서)
	 */
	private Integer no;
	
	/**
	 * 발주서 번호(문자ver.)
	 */
	private String noStr;
	
	/**
	 * 발주코드 (세부구매발주서)
	 */
	private Integer code;
	
	/**
	 * 발주코드(문자ver.)
	 */
	private String codeStr;
	
	/**
	 * 조달납기예정일 (조달계획)
	 */
	private Date dueDate;
	
	/**
	 * 품목코드 (품목)
	 */
	private String materialCode;

	/**
	 * 품목명 (품목)
	 */
	private String materialName;
	
	/**
	 * 발주수량 (세부구매발주서)
	 */
	private Integer amount;
	
	/**
	 * 입고일(=납기일자)
	 */
	private LocalDateTime inDate;
	
	/**
	 * 입고상태 <br>
	 * False(0): 취소, True(1): 완료
	 */
	private Boolean status;
	
	/**
	 * 거래명세서 발행상태 <br>
	 * 입고 취소: 발행 불가 <br>
	 * 입고 완료: 발행 예정 <br>
	 * 입고 완료 + 거래명세서 발행: 발행 완료
	 */
	private String transactionStatus;
}
