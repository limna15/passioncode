package com.passioncode.procurementsystem.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import lombok.extern.log4j.Log4j2;

@Log4j2
@SpringBootTest
public class LargeCategoryServiceTests {
	
	@Autowired
	LargeCategoryService largeCategoryService;
	
	@Test
	public void getDTOListTest() {
		log.info("DTO리스트 봐보자 : "+largeCategoryService.getDTOList());
	}
}
