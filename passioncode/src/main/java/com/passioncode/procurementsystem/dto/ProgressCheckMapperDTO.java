package com.passioncode.procurementsystem.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class ProgressCheckMapperDTO {
	
	private Integer code;
	private LocalDateTime date;
	private Integer rate;
	private String ect;
	private Integer purchase_order_no;
	
}
