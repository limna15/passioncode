package com.passioncode.procurementsystem.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.passioncode.procurementsystem.dto.PurchaseOrderDTO;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Controller
public class PS2Controller {
	
	@GetMapping("procurement2/purchaseOrder")
	public String PS2Test(PurchaseOrderDTO purchaseOrderDTO) {
		log.info(">>>>>>>"+purchaseOrderDTO);
		
		return "procurement2/purchaseOrder";
		
	}

}
