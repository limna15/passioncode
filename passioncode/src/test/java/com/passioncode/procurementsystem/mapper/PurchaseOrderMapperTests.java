package com.passioncode.procurementsystem.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import lombok.extern.log4j.Log4j2;

@SpringBootTest
@Log4j2
public class PurchaseOrderMapperTests {
	
	@Autowired
	PurchaseOrderMapper purchaseOrderMapper;
	
	@org.junit.jupiter.api.Test
	public void Test() {
		log.info(purchaseOrderMapper);
		log.info("=====");
		
	}

}
