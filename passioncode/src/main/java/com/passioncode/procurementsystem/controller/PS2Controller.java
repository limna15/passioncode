package com.passioncode.procurementsystem.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.passioncode.procurementsystem.dto.PurchaseOrderDTO;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Controller
@RequiredArgsConstructor
@RequestMapping("procurement2")
@Log4j2
public class PS2Controller {
	
	@GetMapping("/purchaseOrder")
	public String PS2Test(PurchaseOrderDTO purchaseOrderDTO) {
		log.info(">>>>>>>"+purchaseOrderDTO);
		return "/procurement2/purchaseOrder";
		
		
	}
	
	@GetMapping("/progressCheck")
	public String PS2Test2(PurchaseOrderDTO purchaseOrderDTO) {
		log.info(">>>>>>>"+purchaseOrderDTO);
		return "/procurement2/progressCheck";
		
		
	}

}
