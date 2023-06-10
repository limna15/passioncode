package com.passioncode.procurementsystem.repository;

import java.util.List;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.passioncode.procurementsystem.dto.PurchaseOrderDTO;
import com.passioncode.procurementsystem.entity.ProcurementPlan;
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
	
	@Transactional
	@Test
	public void purchaseOrderDTOTest() {//조달 계획 가져오기
		ProcurementPlan procurementPlan = procurementPlanRepository.findById(1).get();
		PurchaseOrderDTO purchaseOrderDTO = PurchaseOrderDTO.builder().companyName(procurementPlan.getContract().getCompany().getName())
				.purchaseOrderDate(procurementPlan.getDueDate()).dueDate(procurementPlan.getDueDate()).supplyLT(procurementPlan.getContract().getSupplyLt())
				.minimumOrderDate(procurementPlan.getMinimumOrderDate()).materialCode(procurementPlan.getMrp().getMaterial().getName())
				.materialName(procurementPlan.getContract().getMaterial().getName()).stockAmount(procurementPlan.getMrp().getMaterial().getStockAmount())
				.needAmount(procurementPlan.getAmount()).orderAmount((procurementPlan.getAmount())-(procurementPlan.getMrp().getMaterial().getStockAmount()))
				.unitPrice(procurementPlan.getContract().getUnitPrice())
				.supplyPrice((procurementPlan.getAmount())*(procurementPlan.getContract().getUnitPrice())).purchaseOrderStatus(true).build();
				//purchaseOrderDate발주일 고치기
				//발주서 발행 상태 고치기
				
				log.info(">>>>>>>>>>>"+purchaseOrderDTO);
	}

}
