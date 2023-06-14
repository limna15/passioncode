package com.passioncode.procurementsystem.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.passioncode.procurementsystem.dto.ProgressCheckDTO;
import com.passioncode.procurementsystem.entity.DetailPurchaseOrder;
import com.passioncode.procurementsystem.entity.ProcurementPlan;
import com.passioncode.procurementsystem.repository.DetailPurchaseOrderRepository;
import com.passioncode.procurementsystem.repository.ProcurementPlanRepository;
import com.passioncode.procurementsystem.repository.ProgressCheckRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@RequiredArgsConstructor
@Log4j2
public class ProgressCheckServiceImpl implements ProgressCheckService {
	
	private final DetailPurchaseOrderRepository detailPurchaseOrderRepository;
	private final ProcurementPlanRepository procurementPlanRepository;
	private final ProgressCheckRepository progressCheckRepository;
	
	@Override
	public List<ProgressCheckDTO> getProgressCheckDTOList() {//발주서 목록 갖고오기
		List<DetailPurchaseOrder> detilList = detailPurchaseOrderRepository.findAll();
		
		List<ProgressCheckDTO> progressCheckDTOlList = new ArrayList<>();
		for(int i=0;i<detilList.size();i++) {
			progressCheckDTOlList.add(entityToDTO(detilList.get(i)));
					}
		return progressCheckDTOlList;
		
	}

	@Override
	public ProgressCheckDTO entityToDTO(DetailPurchaseOrder detailPurchaseOrder) {
		//DetailPurchaseOrder detailPurchaseOrder2= detailPurchaseOrderRepository.findById(1).get();
		ProcurementPlan procurementPlan= procurementPlanRepository.findByDetailPurchaseOrder(detailPurchaseOrder);
		
		  ProgressCheckDTO progressCheckDTO = ProgressCheckDTO.builder()
		  .companyName(procurementPlan.getContract().getCompany().getName())
		  .purchaseOrderCode(procurementPlan.getDetailPurchaseOrder().getCode())
		  .dueDate(procurementPlan.getDueDate())
		  .materialName(procurementPlan.getContract().getMaterial().getName())
		  .unitPrice(procurementPlan.getContract().getUnitPrice())
		  .diliveryStatus("미완료") .diliveryPercent(20) .inspectionComplete("미완료")
		  .purchaseOrderDeadlineStatus("미완료") .build();
		
		
		return progressCheckDTO;
	}
	
}
