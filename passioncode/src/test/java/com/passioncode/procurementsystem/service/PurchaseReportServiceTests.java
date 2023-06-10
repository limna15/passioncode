package com.passioncode.procurementsystem.service;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.passioncode.procurementsystem.dto.ProcurementPlanDTO;
import com.passioncode.procurementsystem.dto.PurchaseReportDTO;
import com.passioncode.procurementsystem.repository.ProcurementPlanRepository;

import lombok.extern.log4j.Log4j2;

@SpringBootTest
@Log4j2
public class PurchaseReportServiceTests {

	@Autowired
	ProcurementPlanRepository procurementPlanRepository;
	
	@Autowired
	ProcurementPlanService procurementPlanService;
	
	@Autowired
	PurchaseReportService purchaseReportService;

	@Test
	public void procurementPlanGetCountTest() {
		log.info("조달계획 총개수 >>> " + procurementPlanRepository.countBy());			
	}
	
	@Transactional
	@Test
	public void getCountPRDTOTest() {
		log.info("purchaseReportDTO 한번 보자 >>> " + purchaseReportService.getCountPurchaseReportDTO());	
	}
	
//	@Transactional
//	@Test
//	public void getCountPurchaseReportDTOTest() {
//		List<ProcurementPlanDTO> procurementPlanDTOList= procurementPlanService.getDTOList();
//		
//		//log.info("조달계획DTO 리스트 >>> " + procurementPlanDTOList);
//		//log.info("리스트 크기가 얼마나되나 >>> " + procurementPlanDTOList.size());
//		
//		int beforePurchaseCount= 0;
//		int ingProcurementCount= 0;
//		int doneProcurementCount= 0;
//		
//		for(int i=0; i<procurementPlanDTOList.size(); i++) {
//			String ppProgress= procurementPlanDTOList.get(i).getPpProgress();
//			log.info("조달계획 진행사항 >>> " + ppProgress);
//			if(ppProgress!=null) {
//				if(ppProgress.equals("발주 예정")) {
//					beforePurchaseCount++;
//				}else if(ppProgress.equals("조달 진행 중")){
//					ingProcurementCount++;
//				}else {
//					doneProcurementCount++;
//				}
//			}
//		}
//		
//		log.info("발주 예정 개수 >>> " + beforePurchaseCount);
//		log.info("조달 진행 중 개수 >>> " + ingProcurementCount);
//		log.info("조달 완료 개수 >>> " + doneProcurementCount);
//		
//		PurchaseReportDTO purchaseReportDTO= PurchaseReportDTO.builder().procurementPlanCount(procurementPlanDTOList.size())
//				.procurementPlanCount(procurementPlanDTOList.size())
//				.beforePurchase(beforePurchaseCount).ingProcurement(ingProcurementCount)
//				.doneProcurement(doneProcurementCount).build();
//		
//		log.info("purchaseReportDTO 한번 봅시다 >>> " + purchaseReportDTO);
//	}
}
