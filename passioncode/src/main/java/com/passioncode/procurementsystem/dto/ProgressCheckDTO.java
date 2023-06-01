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
public class ProgressCheckDTO {
	
	private Integer code;
	private LocalDateTime date;
	private Integer rate;
	private String ect;
	private Integer purchaseOrderNo;
	
}
