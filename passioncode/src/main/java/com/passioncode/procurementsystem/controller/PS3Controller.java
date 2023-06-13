package com.passioncode.procurementsystem.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.passioncode.procurementsystem.dto.MaterialInDTO;
import com.passioncode.procurementsystem.dto.PurchaseReportDTO;
import com.passioncode.procurementsystem.dto.TransactionDetailDTO;
import com.passioncode.procurementsystem.entity.DetailPurchaseOrder;
import com.passioncode.procurementsystem.service.DetailPurchaseOrderService;
import com.passioncode.procurementsystem.service.MateriallInService;
import com.passioncode.procurementsystem.service.PurchaseReportService;
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
	private final PurchaseReportService purchaseReportService;
	private final DetailPurchaseOrderService detailPurchaseOrderService;
	
	@GetMapping("/materialIn")
	public void materialIn(Model model, MaterialInDTO materialInDTO, String purchaseCode) {
		List<MaterialInDTO> materialInDTOList = materiallInService.getMaterialInDTOLsit();
		//log.info("list............." + materialInDTOList);
		log.info("purchaseCode >>> " + purchaseCode);
	
		List<String> dpoCodeList= new ArrayList<>();
		if(purchaseCode!=null) {
			String[] splitdpoCode= purchaseCode.split(" ");
			for(int i=0; i<splitdpoCode.length; i++) {
				dpoCodeList.add(splitdpoCode[i].trim());
			}
		}
		log.info("dpoCodeList >>> " + dpoCodeList);
		
		//String으로 넘어온 코드 Integer로 변환
		List<Integer> dpoCode= new ArrayList<>();
		for(int i=0; i<dpoCodeList.size(); i++) {
			dpoCode.add(Integer.parseInt(dpoCodeList.get(i)));
		}
		log.info("dpoCode >>> " + dpoCode);
		
		DetailPurchaseOrder detailPurchaseOrder= null;
		TransactionDetailDTO transactionDetailDTO= null;
		List<TransactionDetailDTO> transactionDetailDTOList= new ArrayList<>();
		
		for(int i=0; i<dpoCode.size(); i++) {
			detailPurchaseOrder = detailPurchaseOrderService.get(dpoCode.get(i));
			log.info("detailPurchaseOrder >>> " + detailPurchaseOrder);
			transactionDetailDTO= transactionDetailService.transactionDetailToDTO(detailPurchaseOrder);
			transactionDetailDTOList.add(transactionDetailDTO);
		}
		log.info("transactionDetailDTOList >>> " + transactionDetailDTOList);
		
		model.addAttribute("tdDTOList", transactionDetailDTOList);
		model.addAttribute("DTOList", materialInDTOList);
		model.addAttribute("purchaseCode", purchaseCode);

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
