package com.passioncode.procurementsystem.repository;

import java.util.List;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import com.passioncode.procurementsystem.entity.PurchaseOrder;

import lombok.extern.log4j.Log4j2;

@Log4j2
@SpringBootTest
public class PurchaseOrderRepositoryTests {
	
	@Autowired
	PurchaseOrderRepository purchaseOrderRepository;
	
	@Autowired
	ProcurementPlanRepository procurementPlanRepository;
	
	
	@Test
	public void getList() {
		Optional<PurchaseOrder> list = purchaseOrderRepository.findById(101);
		PurchaseOrder detail = list.get();
		List<PurchaseOrder> list2 = purchaseOrderRepository.findAll();
		log.info("글 번호 가져오기>>"+list2);
		
	}
	
	@Test
	public void InsertTest() {	//발주서 번호 생성 
		PurchaseOrder purchaseOrder = new PurchaseOrder(null);
		purchaseOrderRepository.save(purchaseOrder);
		
	}
	
	@Test
	public void purchaseOrderDTOTest() {
//		ProcurementPlan procurementPlan = procurementPlanRepository.findById(1).get();
//		PurchaseOrderDTO purchaseOrderDTO = PurchaseOrderDTO.builder().companyName(procurementPlan.getContract().getCompany().getName())
//				.purchaseOrderDate(procurementPlan.getDueDate()).supplyLT(procurementPlan.getContract().getSupplyLt())
//				.minimumOrderDate(procurementPlan.getMinimumOrderDate()).materialCode(procurementPlan.getContract().getMaterial().getCode())
//				.materialName(procurementPlan.getContract().getMaterial().getName()).stockAmount(procurementPlan.getMrp().get)
//				.needAmount(procurementPlan.getAmount()).orderAmount(procurementPlan.getAmount()).unitPrice(procurementPlan.getMrp().getMaterial().get)
//				;
	}

}
