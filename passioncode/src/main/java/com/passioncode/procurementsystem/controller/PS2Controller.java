package com.passioncode.procurementsystem.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.passioncode.procurementsystem.dto.DetailPurchaseOrderDTO;
import com.passioncode.procurementsystem.dto.PurchaseOrderDTO;
import com.passioncode.procurementsystem.entity.DetailPurchaseOrder;
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
	public void PS2Test(Model model ,PurchaseOrderDTO purchaseOrderDTO, DetailPurchaseOrderDTO detailDTO, Integer checkBox, Integer checkBox3) {
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
	
	
	@GetMapping("/publishDetail")//발주서 발행 버튼 누르면
	public void DetailList(Model model, DetailPurchaseOrderDTO deailDTO, RedirectAttributes redirectAttributes) {
		model.addAttribute("list",detailPurchaseOrderService.getDetailList());//미리보기 화면 부분
		
		//발행 버튼을 누르면 발행하도록 하기
		//log.info("잘 저장되는지 보기>>"+detailPurchaseOrderService.detailToDTO(null));
		model.addAttribute("de",detailPurchaseOrderService.dtoToEntity(deailDTO));
		List<DetailPurchaseOrderDTO> detail = new ArrayList<>();
		detail.add(deailDTO);//저장해 주기
		//return "redirect:/procurement2/purchaseOrder";
	}
	
	
	@GetMapping("/progressCheck")
	public void PS2Test2(Model model ,PurchaseOrderDTO purchaseOrderDTO, Integer checkBox) {
		log.info("progressCheck>>>>>>>"+purchaseOrderDTO);
		log.info("progressCheck일정등록 위해>>==="+checkBox);
		
		model.addAttribute("pCheckList", progressCheckService.getProgressCheckDTOList());
		
		
		
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
	
	
	@GetMapping("/purchaseNo")
	public String purchaseNo(DetailPurchaseOrderDTO detailDTO, Model model,PurchaseOrderDTO purchaseOrderDTO, Integer checkBox2) {
		log.info("내가 원하는 발주서 번호>>33333>>"+checkBox2);
		model.addAttribute("myPublishData",detailPurchaseOrderService.dtoToEntity(detailDTO));
		//위에서 같은 방법으로 발주해야 한다. 
		
		//model.addAttribute("myPublishDTO",detailPurchaseOrderService.detailToDTO(checkBox));
		
		
		detailPurchaseOrderService.updataePp(checkBox2);
		log.info("발행 합니다~~>>>>>>>"+checkBox2);
		return "redirect:/procurement2/purchaseOrder";
		
		
	}

}
