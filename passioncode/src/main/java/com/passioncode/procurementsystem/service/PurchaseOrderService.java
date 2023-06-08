package com.passioncode.procurementsystem.service;

import java.util.List;
import com.passioncode.procurementsystem.dto.PurchaseOrderDTO;
import com.passioncode.procurementsystem.entity.ProcurementPlan;

public interface PurchaseOrderService {
	
	ProcurementPlan get(Integer code);
	
	/**
	 * 조달계획 엔티티를 이용해 PurchaseOrderDTO로 만들기(ProcurementPlan -> PurchaseOrderDTO)
	 * @param PurchaseOrder
	 * @return
	 */
	PurchaseOrderDTO entityToDTO(ProcurementPlan procurementPlan);

	
	
	/**
	 * PurchaseOrderDTO 리스트 가져오기
	 * @return
	 */
	List<PurchaseOrderDTO> getList();
	
	List<PurchaseOrderDTO> getList(ProcurementPlan procurementPlan);

	
	
}
