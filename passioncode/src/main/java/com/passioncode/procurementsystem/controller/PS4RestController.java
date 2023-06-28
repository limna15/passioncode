package com.passioncode.procurementsystem.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.passioncode.procurementsystem.dto.StockResultDTO;
import com.passioncode.procurementsystem.service.ContractService;
import com.passioncode.procurementsystem.service.LargeCategoryService;
import com.passioncode.procurementsystem.service.MaterialInService;
import com.passioncode.procurementsystem.service.MaterialOutService;
import com.passioncode.procurementsystem.service.MaterialService;
import com.passioncode.procurementsystem.service.MiddleCategoryService;
import com.passioncode.procurementsystem.service.ProcurementPlanService;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@RestController
@RequiredArgsConstructor
@RequestMapping("procurement1")
@Log4j2
public class PS4RestController {
	
	private final MaterialService materialService;
	private final LargeCategoryService largeCategoryService;
	private final MiddleCategoryService middleCategoryService;
	private final ContractService contractService;
	private final ProcurementPlanService procurementPlanService; 
	private final MaterialInService materialInService;
	private final MaterialOutService materialOutService;
	
	@PostMapping("stockResult")
	public void stockResult2(StockResultDTO stockResultDTO) {
		log.info("일단 저거 어케 받아오는지 보자 : "+stockResultDTO);
//		@RequestParam("startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate;
//		@RequestParam("endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate;
		
		
	}
}
