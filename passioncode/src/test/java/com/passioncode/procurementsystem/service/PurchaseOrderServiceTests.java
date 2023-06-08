package com.passioncode.procurementsystem.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.passioncode.procurementsystem.entity.ProcurementPlan;

import lombok.extern.log4j.Log4j2;

@Log4j2
@SpringBootTest
public class PurchaseOrderServiceTests {
	
	@Autowired
	PurchaseOrderService purchaseOrderService;
	
	@Transactional
	@Test
	public void entityToDTOTest() {
		ProcurementPlan procurementPlan = purchaseOrderService.get(1);
		log.info("DTO되나 보자 : "+ purchaseOrderService.entityToDTO(procurementPlan));
	}

}
