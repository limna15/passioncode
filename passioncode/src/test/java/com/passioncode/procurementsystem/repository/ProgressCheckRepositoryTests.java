package com.passioncode.procurementsystem.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import lombok.extern.log4j.Log4j2;

@Log4j2
@SpringBootTest
public class ProgressCheckRepositoryTests {

	
	@Autowired
	PurchaseOrderRepository purchaseOrderRepository;
	
	@Test
	public void progressCheckDTOTest() {
		
		log.info(">>>>");
	}
}
