package com.passioncode.procurementsystem.mapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import lombok.extern.log4j.Log4j2;

@SpringBootTest
@Log4j2
public class DetailPurchaseOrderMapperTests {
	
	@Autowired
	DetailPurchaseOrderMapper detailPurchaseOrderMapper;
	
	@Test
	public void Test() {
		log.info(detailPurchaseOrderMapper);
		log.info("========");
	}

	@Test
	public void Test2() {
		detailPurchaseOrderMapper.getList().forEach(vo -> log.info(vo));
	}
	
	

}
