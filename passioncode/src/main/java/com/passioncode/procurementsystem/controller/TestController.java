package com.passioncode.procurementsystem.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TestController {
	
//	@GetMapping("layout/basic")
//	public void layoutTest() {
//		
//	}
	@GetMapping("layout/basic")
	public String layoutTest() {
		return "/layout/layoutTest";
	}
	
	@GetMapping("PassionCode/ProcurementSystem")
	public String mainTest() {
		return "/layout/procurementSystem";
	}
	
	@GetMapping("PassionCode/ProcurementSystem2")
	public String mainTest2() {
		return "/layout/procurementSystem2";
	}

}
