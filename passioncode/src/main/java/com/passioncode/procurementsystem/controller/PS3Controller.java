package com.passioncode.procurementsystem.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.passioncode.procurementsystem.dto.MaterialInDTO;
import com.passioncode.procurementsystem.service.MateriallInService;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Controller
@RequiredArgsConstructor
@RequestMapping("procurement3")
@Log4j2
public class PS3Controller {
	
	private final MateriallInService materiallInService;
	

	@GetMapping("/materialIn")
	public void materialIn(Model model, MaterialInDTO materialInDTO) {
		
		log.info("list............." + materialInDTO);

		model.addAttribute("DTOList",materiallInService.getMaterialInDTOLsit());
	}
	
	@GetMapping("/transactionList")
	public void transactionList() {
		
	}
	
	@GetMapping("/transactionPrint")
	public void printTest() {
		log.info("거래명세서 인쇄");		
	}
}
