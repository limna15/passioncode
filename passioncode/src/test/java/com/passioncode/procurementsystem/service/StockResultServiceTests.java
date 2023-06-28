package com.passioncode.procurementsystem.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.log4j.Log4j2;

@SpringBootTest
@Log4j2
public class StockResultServiceTests {
	
	@Autowired
	StockResultService stockResultService;
	
	@Transactional
	@Test
	public void getDtoTest() {
		log.info("DTO 테스트 어디 보자!!! : "+stockResultService.getStockResultDTOList());
	}

}
