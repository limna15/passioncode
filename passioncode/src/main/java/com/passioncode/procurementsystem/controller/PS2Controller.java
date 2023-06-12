package com.passioncode.procurementsystem.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.passioncode.procurementsystem.dto.DetailPurchaseOrderDTO;
import com.passioncode.procurementsystem.dto.PurchaseOrderDTO;
import com.passioncode.procurementsystem.repository.DetailPurchaseOrderRepository;
import com.passioncode.procurementsystem.service.DetailPurchaseOrderService;
import com.passioncode.procurementsystem.service.PurchaseOrderService;

import org.springframework.ui.Model;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Controller
@RequiredArgsConstructor
@RequestMapping("procurement2")
@Log4j2
public class PS2Controller {
	
	private final DetailPurchaseOrderRepository repository;
	//서비스로 받아오는 것으로 변경하기
	
	private final PurchaseOrderService purchaseOrderService;
	
	@GetMapping("/purchaseOrder")	//발주서 발행
	public void PS2Test(Model model ,PurchaseOrderDTO purchaseOrderDTO, DetailPurchaseOrderDTO detailDTO) {
		log.info(">>>>>>>"+purchaseOrderDTO);
		log.info("세부사항 >>>>>>>"+detailDTO);
		
		model.addAttribute("list",repository.findAll());
		model.addAttribute("purchaseOrderList", purchaseOrderService.getDTOList());
		
	}
	
	@GetMapping("/progressCheck")
	public void PS2Test2(Model model ,PurchaseOrderDTO purchaseOrderDTO) {
		log.info(">>>>>>>"+purchaseOrderDTO);
		
		
		model.addAttribute("list",100);
		
		
	}
	
	@GetMapping("/detailPurchaseOrder")
	public void PS2Test22(Model model) {
		model.addAttribute("purchaseOrderList", purchaseOrderService.getDTOList());
		log.info(">>>>>>>");
		
		
		
	}
	
	@GetMapping("/purchaseOrderPublish")
	public void PS24Test22(Model model) {
		model.addAttribute("purchaseOrderList", purchaseOrderService.getDTOList());
		log.info(">>>>>>>");
		
		
		
	}
	
	@GetMapping("/print")
	public void PS2TestPrint(PurchaseOrderDTO purchaseOrderDTO) {
		log.info("인쇄합니다>>>>>>>");
		
		
		
	}

}
