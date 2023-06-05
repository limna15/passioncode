package com.passioncode.procurementsystem.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.passioncode.procurementsystem.dto.DetailPurchaseOrderDTO;
import com.passioncode.procurementsystem.dto.PurchaseOrderDTO;
import com.passioncode.procurementsystem.repository.DetailPurchaseOrderRepository;
import com.passioncode.procurementsystem.service.DetailPurchaseOrderService;

import org.springframework.ui.Model;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Controller
@RequiredArgsConstructor
@RequestMapping("procurement2")
@Log4j2
public class PS2Controller {
	
	private final DetailPurchaseOrderRepository repository;
	
	@GetMapping("/purchaseOrder")
	public void PS2Test(Model model ,PurchaseOrderDTO purchaseOrderDTO, DetailPurchaseOrderDTO detailDTO) {
		log.info(">>>>>>>"+purchaseOrderDTO);
		log.info("세부사항 >>>>>>>"+detailDTO);
		
		model.addAttribute("list",repository.findAll());
		
	}
	
	@GetMapping("/progressCheck")
	public String PS2Test2(PurchaseOrderDTO purchaseOrderDTO) {
		log.info(">>>>>>>"+purchaseOrderDTO);
		return "/procurement2/progressCheck";
		
		
	}
	
	@GetMapping("/detailPurchaseOrder")
	public void PS2Test22(PurchaseOrderDTO purchaseOrderDTO) {
		log.info(">>>>>>>"+purchaseOrderDTO);
		
		
		
	}

}
