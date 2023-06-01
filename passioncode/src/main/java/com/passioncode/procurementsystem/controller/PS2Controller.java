package com.passioncode.procurementsystem.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PS2Controller {
	
	@GetMapping("procurement2/PurchaseOrder")
	public String PS2Test() {
		return "procurement2/PurchaseOrder";
		
	}

}
