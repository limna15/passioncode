package com.passioncode.procurementsystem.controller;


import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.passioncode.procurementsystem.dto.TransactionDetailDTO;
import com.passioncode.procurementsystem.entity.DetailPurchaseOrder;
import com.passioncode.procurementsystem.entity.PurchaseOrder;
import com.passioncode.procurementsystem.service.DetailPurchaseOrderService;
import com.passioncode.procurementsystem.service.MateriallInService;
import com.passioncode.procurementsystem.service.ProcurementPlanService;
import com.passioncode.procurementsystem.service.PurchaseOrderService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@RestController
@RequiredArgsConstructor
@RequestMapping("/procurement3")
@Log4j2
public class PS3RestController {
	
	private final MateriallInService materialInService;
	private final ProcurementPlanService ppService;
	private final DetailPurchaseOrderService dpoService;
	private final PurchaseOrderService poService;
	
	@PostMapping(value= "getTransactionDetail")
	public List<TransactionDetailDTO> getDetail(@RequestBody String purchaseNo, RedirectAttributes redirectAttributes, HttpServletRequest request) {
		log.info("purchaseNo 잘 보내졌나 >>> " + purchaseNo);
		// 거래명세서DTO: 거래명서세번호, 발주회사(=우리회사), 발주서번호, 발주코드, 납기일자(=입고일), 사업자등록번호, 상호, CEO, 주소, 담당자, 연락처, 품목코드, 품목명, 수량, 단가
		List<TransactionDetailDTO> detailList= new ArrayList<>();
		
		Integer no= Integer.parseInt(purchaseNo);
		PurchaseOrder purchaseOrder= poService.getPurchaseOrder(no);
		//지금은 하나 선택했을 때 넘어오는 data이기 때문에 0을 넣어줌
		DetailPurchaseOrder detailPurchaseOrder= dpoService.getDetailByPurchaseNo(purchaseOrder).get(0);

		return detailList;
	}
}
