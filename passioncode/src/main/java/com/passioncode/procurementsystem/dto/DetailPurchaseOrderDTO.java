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
public class DetailPurchaseOrderDTO {
	
	private Integer code;
	private Integer amount;
	private LocalDateTime date;
	private Integer purchaseOrderNo;

}
