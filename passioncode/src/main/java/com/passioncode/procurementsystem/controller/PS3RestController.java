package com.passioncode.procurementsystem.controller;


import java.util.ArrayList;
import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.passioncode.procurementsystem.dto.TransactionDetailDTO;
import com.passioncode.procurementsystem.entity.DetailPurchaseOrder;
import com.passioncode.procurementsystem.entity.MaterialIn;
import com.passioncode.procurementsystem.entity.ProcurementPlan;
import com.passioncode.procurementsystem.entity.PurchaseOrder;
import com.passioncode.procurementsystem.service.DetailPurchaseOrderService;
import com.passioncode.procurementsystem.service.MateriallInService;
import com.passioncode.procurementsystem.service.ProcurementPlanService;
import com.passioncode.procurementsystem.service.PurchaseOrderService;
import com.passioncode.procurementsystem.service.TransactionDetailService;

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
	private final TransactionDetailService tdService;
	
	//발주서 번호에 코드가 한개일 경우
	@PostMapping(value= "getTransactionDetail")
	public TransactionDetailDTO getDetail(@RequestBody String[] purchaseNo) {
		log.info("purchaseNo 잘 보내졌나 >>> " + purchaseNo);
		// 거래명세서DTO: 거래명서세번호, 발주회사(=우리회사), 발주서번호, 발주코드, 납기일자(=입고일), 사업자등록번호, 상호, CEO, 주소, 담당자, 연락처, 품목코드, 품목명, 수량, 단가
		
		Integer no= Integer.parseInt(purchaseNo[0]);
		//log.info("형변환이 문제인가 >>> " + no);
		
		DetailPurchaseOrder dpo= dpoService.getDetailByPurchaseNo(poService.getPurchaseOrder(no)).get(0);
		//log.info("발주서 번호에 맞는 dpo가 오나? >>> " + dpo);
		ProcurementPlan pp= ppService.getPpByDetailPurchaseOrder(dpo.getCode());
		log.info("발주서 번호에 맞는 조달 계획을 가지고 오나? >>> " + pp);
		MaterialIn mi= materialInService.getMeterialInByDetailPurchaseOrder(dpo.getCode());
		
		TransactionDetailDTO transactionDetailDTO= TransactionDetailDTO.builder().company("패션코드")
								.purchaseOrderNo(no).detailPurchaseOrderCode(dpo.getCode()).date(mi.getDate())
								.companyNo(pp.getContract().getCompany().getNo()).companyName(pp.getContract().getCompany().getName())
								.CEO(pp.getContract().getCompany().getCeo()).companyAddress(pp.getContract().getCompany().getAddress())
								.manager(pp.getContract().getCompany().getManager()).managerTel(pp.getContract().getCompany().getManagerTel())
								.materialCode(pp.getContract().getMaterial().getCode()).materialName(pp.getContract().getMaterial().getName())
								.amount(dpo.getAmount()).unitPrice(pp.getContract().getUnitPrice()).build();
		log.info("발주서 번호에 맞는 거래명세서DTO를 가지고 오나? >>> " + transactionDetailDTO);
		
		tdService.register(transactionDetailDTO);
		materialInService.updateTransactionStatus(dpo.getCode());
		
		return transactionDetailDTO;
	}
	
	//발주서 번호에 코드가 여러개일 경우
	@PostMapping(value= "getTransactionDetail2", produces=MediaType.APPLICATION_JSON_VALUE)
	public List<TransactionDetailDTO> getDetail2(@RequestBody String noList) {
		log.info("noList 잘 보내 >>> " + noList.getClass().getName());
		
		String noListReplace= noList.replaceAll("\"", "");
		Integer purchaseNo= Integer.parseInt(noListReplace);
		log.info("쌍따옴표를 지워야 형변환이 가능해짐 >>> " + purchaseNo);
		
		PurchaseOrder po= poService.getPurchaseOrder(purchaseNo);
		
		List<DetailPurchaseOrder> dpoList= dpoService.getDetailByPurchaseNo(po);
		log.info("po로 가져온 dpoList는 어떻게 찍히지 >>> " + dpoList);
		
		List<ProcurementPlan> ppList= new ArrayList<>();
		List<MaterialIn> miList= new ArrayList<>();
		for(int i=0; i<dpoList.size(); i++) {
			ppList.add(ppService.getPpByDetailPurchaseOrder(dpoList.get(i).getCode()));
			miList.add(materialInService.getMeterialInByDetailPurchaseOrder(dpoList.get(i).getCode()));
		}
		log.info("ppList는 어떻게 찍히지 >>> " + ppList);
		log.info("miList는 어떻게 찍히지 >>> " + miList);
		
		List<TransactionDetailDTO> transactionDetailDTOList= new ArrayList<>();
		TransactionDetailDTO transactionDetailDTO= null;
		
		for(int i=0; i<miList.size(); i++) {
			if(miList.get(i).getStatus()) { //입고상태가 true(1=완료)일때만
				transactionDetailDTO= TransactionDetailDTO.builder().company("패션코드")
						.purchaseOrderNo(purchaseNo).detailPurchaseOrderCode(dpoList.get(i).getCode()).date(miList.get(i).getDate())
						.companyNo(ppList.get(i).getContract().getCompany().getNo()).companyName(ppList.get(i).getContract().getCompany().getName())
						.CEO(ppList.get(i).getContract().getCompany().getCeo()).companyAddress(ppList.get(i).getContract().getCompany().getAddress())
						.manager(ppList.get(i).getContract().getCompany().getManager()).managerTel(ppList.get(i).getContract().getCompany().getManagerTel())
						.materialCode(ppList.get(i).getContract().getMaterial().getCode()).materialName(ppList.get(i).getContract().getMaterial().getName())
						.amount(dpoList.get(i).getAmount()).unitPrice(ppList.get(i).getContract().getUnitPrice()).build();
				transactionDetailDTOList.add(transactionDetailDTO);
			}
		}		
		tdService.register(transactionDetailDTO);
		for(int i=0; i<miList.size(); i++) {
			materialInService.updateTransactionStatus(dpoList.get(i).getCode());
		}
		log.info("거래명세서DTOList >>> " + transactionDetailDTOList);
		
		return transactionDetailDTOList;
	}

}
