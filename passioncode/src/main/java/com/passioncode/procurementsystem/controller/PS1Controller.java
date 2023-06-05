package com.passioncode.procurementsystem.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Controller
@RequiredArgsConstructor
@RequestMapping("procurement1")
@Log4j2
public class PS1Controller {
	
	@GetMapping("/materialList")
	public void PS1List() {
		log.info("품목정보목록 보기>>");
		
	}
	
	@GetMapping("/contractList")
	public void PS1List2() {
		log.info("계약 목록 보기>>");
		
	}

	@GetMapping("/procurementPlanList")
	public void PS1List3() {
		log.info("조달 계획 목록 보기>>");
		
	}

}
