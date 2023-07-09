package com.passioncode.procurementsystem.service;

import java.util.List;
import com.passioncode.procurementsystem.dto.PurchaseReportDTO;

public interface PurchaseReportService {
	
	/**
	 * 조달계획DTO를 이용해 transactionDetailDTO 만들기(procurementPlanDTO) -> purchaseReportDTO)
	 * @param procurementPlanDTO
	 * @return
	 */
	PurchaseReportDTO getCountPurchaseReportDTO();
	
	/**
	 * PurchaseReportDTO 리스트 가져오기
	 * @return
	 */
	List<PurchaseReportDTO> getPurchaseReportDTOList(String[] date);
}
