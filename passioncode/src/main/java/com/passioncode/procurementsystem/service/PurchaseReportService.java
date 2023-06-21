package com.passioncode.procurementsystem.service;

import com.passioncode.procurementsystem.dto.PurchaseReportDTO;

public interface PurchaseReportService {
	
	/**
	 * 조달계획DTO를 이용해 transactionDetailDTO 만들기(procurementPlanDTO) -> purchaseReportDTO)
	 * @param procurementPlanDTO
	 * @return
	 */
	PurchaseReportDTO getCountPurchaseReportDTO();
}
