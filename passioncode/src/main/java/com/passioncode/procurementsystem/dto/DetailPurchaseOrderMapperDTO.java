package com.passioncode.procurementsystem.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class DetailPurchaseOrderMapperDTO {
	
	private Integer code;
	private Integer amount;
	private LocalDateTime date;
	private Integer purchase_order_no;

}
