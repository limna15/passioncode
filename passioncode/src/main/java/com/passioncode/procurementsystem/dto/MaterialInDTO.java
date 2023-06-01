package com.passioncode.procurementsystem.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class MaterialInDTO {

	private Integer code;
	private Integer amount;
	private Integer status;
	private LocalDateTime date;
	private Integer transactionStatus;
	private Integer purchaseOrderNo;
	private Integer transactionDetailNo;

}
