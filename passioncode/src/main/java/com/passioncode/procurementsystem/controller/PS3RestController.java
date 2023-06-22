package com.passioncode.procurementsystem.controller;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.passioncode.procurementsystem.dto.TransactionDetailDTO;
import com.passioncode.procurementsystem.entity.DetailPurchaseOrder;
import com.passioncode.procurementsystem.entity.MaterialIn;
import com.passioncode.procurementsystem.entity.ProcurementPlan;
import com.passioncode.procurementsystem.entity.PurchaseOrder;
import com.passioncode.procurementsystem.service.DetailPurchaseOrderService;
import com.passioncode.procurementsystem.service.MateriallInService;
import com.passioncode.procurementsystem.service.ProcurementPlanService;
import com.passioncode.procurementsystem.service.PurchaseOrderService;
import com.passioncode.procurementsystem.service.TransactionDetailService;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@RestController
@RequiredArgsConstructor
@RequestMapping("/procurement3")
@Log4j2
public class PS3RestController {
	
	private final MateriallInService materialInService;
	private final ProcurementPlanService ppService;
	private final DetailPurchaseOrderService dpoService;
	private final PurchaseOrderService poService;
	private final TransactionDetailService tdService;
	
	
	@PostMapping(value="reportDate", produces=MediaType.APPLICATION_JSON_VALUE)
	public List<String> getDate(@RequestBody Date[] result){
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		List<String> dates= new ArrayList<>();
		
		for(Date date: result) {
			dates.add(simpleDateFormat.format(date));
		}
		log.info("result 포맷형식 바꿔서 받기 >>> " + dates);	
		
		return dates;
	}
}
