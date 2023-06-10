package com.passioncode.procurementsystem.service;

import java.util.List;
import org.springframework.stereotype.Service;
import com.passioncode.procurementsystem.dto.DetailPurchaseOrderDTO;
import com.passioncode.procurementsystem.dto.PurchaseOrderDTO;
import com.passioncode.procurementsystem.entity.DetailPurchaseOrder;
import com.passioncode.procurementsystem.entity.ProcurementPlan;
import com.passioncode.procurementsystem.entity.PurchaseOrder;
import com.passioncode.procurementsystem.repository.DetailPurchaseOrderRepository;
import com.passioncode.procurementsystem.repository.ProcurementPlanRepository;
import com.passioncode.procurementsystem.repository.PurchaseOrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@RequiredArgsConstructor
@Log4j2
public class PurchaseOrderServiceImpl implements PurchaseOrderService {
	
	private final PurchaseOrderRepository purchaseOrderRepository;
	private final DetailPurchaseOrderRepository detailPurchaseOrderRepository;
	private final ProcurementPlanRepository procurementPlanRepository;
	
	@Override
	public ProcurementPlan get(Integer code) {
		return procurementPlanRepository.findById(code).get();
	}
	
	@Override
	public List<PurchaseOrderDTO> getList(ProcurementPlan procurementPlan) {
		//
		
		
		return null;
	}

	@Override
	public PurchaseOrderDTO entityToDTO(ProcurementPlan procurementPlan) {
		//조달 계획 가져오기
				PurchaseOrderDTO purchaseOrderDTO = PurchaseOrderDTO.builder().companyName(procurementPlan.getContract().getCompany().getName())
						.purchaseOrderDate(procurementPlan.getDueDate()).supplyLT(procurementPlan.getContract().getSupplyLt())
						.minimumOrderDate(procurementPlan.getMinimumOrderDate()).materialCode(procurementPlan.getContract().getMaterial().getCode())
						.materialName(procurementPlan.getContract().getMaterial().getName()).stockAmount(10)
						.needAmount(procurementPlan.getAmount()).orderAmount(procurementPlan.getAmount()-10).unitPrice(procurementPlan.getContract().getUnitPrice())
						.supplyPrice((procurementPlan.getAmount()-100)*(procurementPlan.getContract().getUnitPrice())).purchaseOrderStatus(true).build();
		return purchaseOrderDTO;
	}

	@Override
	public List<PurchaseOrderDTO> getList() {
		// TODO Auto-generated method stub
		return null;
	}
}
