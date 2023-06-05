package com.passioncode.procurementsystem.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class MaterialInMapperDTO {

	private Integer no;
	private Integer code;
	private LocalDateTime due_date;
	private String material_code;
	private String name;
	private Integer amount;
	private Integer status;
	private Integer transaction_status;
}
