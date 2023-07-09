package com.passioncode.procurementsystem.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import lombok.extern.log4j.Log4j2;

@SpringBootTest
@Log4j2
public class StockReportServiceTests {

	@Autowired
	StockReportService stockReportService;
	
	@Test
	public void getReportListTest() {
		log.info("어디 리포트 리스트 보자 : "+stockReportService.getStockReportForLCList());
	}
	
	
}
