package com.passioncode.procurementsystem.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.passioncode.procurementsystem.dto.ProcurementPlanDTO;
import com.passioncode.procurementsystem.dto.PurchaseReportDTO;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@RequiredArgsConstructor
@Log4j2
public class PurchaseReportServiceImpl implements PurchaseReportService {
	
	private final ProcurementPlanService procurementPlanService;
	
	@Override
	public PurchaseReportDTO getCountPurchaseReportDTO() {
		List<ProcurementPlanDTO> procurementPlanDTOList= procurementPlanService.getDTOList();
		
		int beforePurchaseCount= 0;
		int ingProcurementCount= 0;
		int doneProcurementCount= 0;
		
		for(int i=0; i<procurementPlanDTOList.size(); i++) {
			String ppProgress= procurementPlanDTOList.get(i).getPpProgress();
			log.info("조달계획 진행사항 >>> " + ppProgress);
			if(ppProgress!=null) {
				if(ppProgress.equals("발주 예정")) {
					beforePurchaseCount++;
				}else if(ppProgress.equals("조달 진행 중")){
					ingProcurementCount++;
				}else {
					doneProcurementCount++;
				}
			}
		}
		
		log.info("발주 예정 개수 >>> " + beforePurchaseCount);
		log.info("조달 진행 중 개수 >>> " + ingProcurementCount);
		log.info("조달 완료 개수 >>> " + doneProcurementCount);
		
		PurchaseReportDTO purchaseReportDTO= PurchaseReportDTO.builder().procurementPlanCount(procurementPlanDTOList.size())
				.procurementPlanCount(procurementPlanDTOList.size())
				.beforePurchase(beforePurchaseCount).ingProcurement(ingProcurementCount)
				.doneProcurement(doneProcurementCount).build();
		
		return purchaseReportDTO;
	}

}
