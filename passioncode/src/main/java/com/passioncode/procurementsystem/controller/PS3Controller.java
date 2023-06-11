package com.passioncode.procurementsystem.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.passioncode.procurementsystem.dto.MaterialInDTO;
import com.passioncode.procurementsystem.dto.TransactionDetailDTO;
import com.passioncode.procurementsystem.service.MateriallInService;
import com.passioncode.procurementsystem.service.TransactionDetailService;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Controller
@RequiredArgsConstructor
@RequestMapping("procurement3")
@Log4j2
public class PS3Controller {
	
	private final MateriallInService materiallInService;
	private final TransactionDetailService transactionDetailService;
	

	@GetMapping("/materialIn")
	public void materialIn(Model model, MaterialInDTO materialInDTO) {
		
		log.info("list............." + materialInDTO);

		model.addAttribute("DTOList",materiallInService.getMaterialInDTOLsit());
	}
	
	@PostMapping("/materialIn")
	public void materialInPost(Model model, MaterialInDTO materialInDTO) {
		
		log.info("list............. 거래명세서 인쇄할 때 화면" + materialInDTO);

		model.addAttribute("DTOList",materiallInService.getMaterialInDTOLsit());
	}
	
	@GetMapping("/transactionList")
	public void transactionList(Model model, TransactionDetailDTO transactionDetailDTO) {
		model.addAttribute("list", transactionDetailService.getTransactionDetailList());
	}
	
	@GetMapping("/transactionPrint")
	public void printTest() {
		log.info("거래명세서 인쇄");		
	}
	
	@GetMapping("/purchaseReport")
	public void purchaseReport() {
		log.info("발주진행 현황관리");		
	}
}
