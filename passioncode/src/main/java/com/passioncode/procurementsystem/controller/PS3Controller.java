package com.passioncode.procurementsystem.controller;

import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.passioncode.procurementsystem.dto.MaterialInDTO;
import com.passioncode.procurementsystem.dto.PurchaseReportDTO;
import com.passioncode.procurementsystem.dto.TransactionDetailDTO;
import com.passioncode.procurementsystem.entity.DetailPurchaseOrder;
import com.passioncode.procurementsystem.entity.MaterialIn;
import com.passioncode.procurementsystem.service.DetailPurchaseOrderService;
import com.passioncode.procurementsystem.service.MateriallInService;
import com.passioncode.procurementsystem.service.PurchaseReportService;
import com.passioncode.procurementsystem.service.TransactionDetailService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Controller
@RequiredArgsConstructor
@RequestMapping("procurement3")
@Log4j2
public class PS3Controller {
	
	private final MateriallInService materiallInService;
	private final TransactionDetailService transactionDetailService;	
	private final PurchaseReportService purchaseReportService;
	private final DetailPurchaseOrderService detailPurchaseOrderService;
	
	@GetMapping("/materialIn")
	public void materialIn(Model model, HttpServletRequest request, MaterialInDTO materialInDTO, String purchaseCode) {
		List<MaterialInDTO> materialInDTOList = materiallInService.getMaterialInDTOLsit();

		log.info("materialInDTOlist............." + materialInDTOList);

		model.addAttribute("DTOList", materialInDTOList);
	}
	
	@PostMapping(value="materialInRegister")
	public String materialInRegister(MaterialInDTO materialInDTO, RedirectAttributes redirectAttributes, HttpServletRequest request) {
		log.info("잘 보내지나 >>> "+ materialInDTO);
		
		Integer purchaseOrderNo= Integer.parseInt(request.getParameter("no"));
		Integer detailPurchaseOrderCode= Integer.parseInt(request.getParameter("code"));
		String materialCode= request.getParameter("materialCode");
		String materialName= request.getParameter("materialName");
		Integer amount= Integer.parseInt(request.getParameter("amount"));
		String status= request.getParameter("status");
		
//		log.info("js로 만들어 보낸 form 데이터 purchaseOrderNo 잘 받아오나 >>> " + purchaseOrderNo);
//		log.info("js로 만들어 보낸 form 데이터 detailPurchaseOrderCode잘 받아오나 >>> " + detailPurchaseOrderCode);
//		log.info("js로 만들어 보낸 form 데이터 materialCode 잘 받아오나 >>> " + materialCode);
//		log.info("js로 만들어 보낸 form 데이터 materialName 잘 받아오나 >>> " + materialName);
//		log.info("js로 만들어 보낸 form 데이터 amount 잘 받아오나 >>> " + amount);
//		log.info("js로 만들어 보낸 form 데이터 status 잘 받아오나 >>> " + status);
		
		if(status.equals("1")) { //입고 상태가 완료일 때
			materialInDTO.setCode(purchaseOrderNo);
			materialInDTO.setCode(detailPurchaseOrderCode);
			materialInDTO.setMaterialCode(materialCode);
			materialInDTO.setMaterialName(materialName);
			materialInDTO.setAmount(amount);
			materialInDTO.setStatus(true);
			materialInDTO.setTransactionStatus("발행 예정");
			log.info("입고 상태가 완료일 경우, materialDTO 잘 세팅이 되나 >>> " + materialInDTO);
			materiallInService.register(materialInDTO);
		}else { //입고 상태가 취소일 때
			materialInDTO.setCode(purchaseOrderNo);
			materialInDTO.setCode(detailPurchaseOrderCode);
			materialInDTO.setMaterialCode(materialCode);
			materialInDTO.setMaterialName(materialName);
			materialInDTO.setAmount(amount);
			materialInDTO.setStatus(false);
			materialInDTO.setTransactionStatus("발행 불가");
			log.info("입고 상태가 취소일 경우, materialDTO 잘 세팅이 되나 >>> " + materialInDTO);
			materiallInService.register(materialInDTO);
		}
		
		materiallInService.updatePPCompletionDate(materialInDTO.getCode());
		
		//테스트용 맵핑 url
		//return "/procurement3/materialInRegister";
		return "redirect:/procurement3/materialIn";
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
	public void purchaseReport(Model model, PurchaseReportDTO purchaseReportDTO) {
		log.info("발주진행 현황관리");
		
		model.addAttribute("list", purchaseReportService.getCountPurchaseReportDTO());
	}
}
