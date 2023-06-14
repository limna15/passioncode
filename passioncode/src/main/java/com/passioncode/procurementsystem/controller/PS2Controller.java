package com.passioncode.procurementsystem.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.passioncode.procurementsystem.dto.DetailPurchaseOrderDTO;
import com.passioncode.procurementsystem.dto.PurchaseOrderDTO;
import com.passioncode.procurementsystem.repository.DetailPurchaseOrderRepository;
import com.passioncode.procurementsystem.service.DetailPurchaseOrderService;
import com.passioncode.procurementsystem.service.ProgressCheckService;
import com.passioncode.procurementsystem.service.PurchaseOrderService;

import org.springframework.ui.Model;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Controller
@RequiredArgsConstructor
@RequestMapping("procurement2")
@Log4j2
public class PS2Controller {
	
	private final PurchaseOrderService purchaseOrderService;
	private final DetailPurchaseOrderService detailPurchaseOrderService;
	private final ProgressCheckService progressCheckService;
	
	
	@GetMapping("/purchaseOrder")	//발주서 발행
	public void PS2Test(Model model ,PurchaseOrderDTO purchaseOrderDTO, DetailPurchaseOrderDTO detailDTO, Integer checkBox) {
		log.info("내가 원하는 발주서 번호>>"+checkBox);
		
		//model.addAttribute("DetailPurchaseOrderList", detailPurchaseOrderService.getDTOList());
		//model.addAttribute("list",repository.findAll());
		model.addAttribute("purchaseOrderList", purchaseOrderService.getDTOList());
		
		//여서기 서비스 호출 뒤에 리턴 값을 보낸다
		model.addAttribute("myListData",checkBox);
		model.addAttribute("myPublishDTO",detailPurchaseOrderService.detailToDTO(checkBox));
		log.info("제가 보고 싶은 값 2222>>>>>>==="+detailPurchaseOrderService.detailToDTO(checkBox));
		log.info("제가 보고 싶은 값 입니다>>>>>>==="+checkBox);
		
		
		
		
	}
	
	@GetMapping("/progressCheck")
	public void PS2Test2(Model model ,PurchaseOrderDTO purchaseOrderDTO) {
		log.info("progressCheck>>>>>>>"+purchaseOrderDTO);
		
		
		model.addAttribute("pCheckList", progressCheckService.getProgressCheckDTOList());
		model.addAttribute("list",100);
		
		
	}
	
	@GetMapping("/detailPurchaseOrder")
	public void PS2Test22(Model model) {
		model.addAttribute("DetailPurchaseOrderList", detailPurchaseOrderService.getDTOList());
		model.addAttribute("purchaseOrderList", purchaseOrderService.getDTOList());
		log.info("detailPurchaseOrder>>>>>>>"+purchaseOrderService.getDTOList());
		
		
		
	}
	
	@GetMapping("/purchaseOrderPublish")
	public void PS24Test22(Model model, String checkBox) {
		//model.addAttribute("purchaseOrderList", purchaseOrderService.getDTOList());
		//model.addAttribute("DetailPurchaseOrderList", detailPurchaseOrderService.getDTOList());
		//model.addAttribute("myListData",checkBox);
		log.info("purchaseOrderPublish>>>>>>>");
		
		
		
	}
	
	@GetMapping("/print")
	public void PS2TestPrint(DetailPurchaseOrderDTO detailDTO, Model model,PurchaseOrderDTO purchaseOrderDTO, Integer checkBox) {
		model.addAttribute("myPublishDTO",detailPurchaseOrderService.detailToDTO(checkBox));
		log.info("인쇄합니다>>>>>>>");
		
		
		
	}

}
