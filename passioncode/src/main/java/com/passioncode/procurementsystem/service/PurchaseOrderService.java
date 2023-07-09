package com.passioncode.procurementsystem.service;

import java.util.List;
import com.passioncode.procurementsystem.dto.PurchaseOrderDTO;
import com.passioncode.procurementsystem.entity.ProcurementPlan;
import com.passioncode.procurementsystem.entity.PurchaseOrder;

public interface PurchaseOrderService {
	
	ProcurementPlan get(Integer code);
	
	
	
	/**
	 * 조달계획 엔티티를 이용해 PurchaseOrderDTO로 만들기(ProcurementPlan -> PurchaseOrderDTO)
	 * @param procurementPlan
	 * @return
	 */
	PurchaseOrderDTO entityToDTO(ProcurementPlan procurementPlan);
	
	
	
	/**
	 * PurchaseOrderDTO 리스트 가져오기
	 * @return
	 */
	List<PurchaseOrderDTO> getDTOList();
	
	/**
	 * 구매발주서 번호로 구매발주서 엔티티 가져오기
	 * @param procurementPlan
	 * @return
	 */
	PurchaseOrder getPurchaseOrder(Integer no);


	/**
	 * 여러개의 발주서 발행
	 * @param num1
	 */
	void makePoCode(Integer[] num1);

}
