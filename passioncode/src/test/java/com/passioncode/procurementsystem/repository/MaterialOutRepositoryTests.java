package com.passioncode.procurementsystem.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.passioncode.procurementsystem.entity.MRP;

import lombok.extern.log4j.Log4j2;

@Log4j2
@SpringBootTest
public class MaterialOutRepositoryTests {
	
	@Autowired
	MaterialOutRepository materialOutRepository;
	
	@Autowired
	MRPRepository mrpRepository;
	
	@Transactional
	@Test
	public void findByMRPTest() {
		MRP mrp = mrpRepository.findById(1).get();
		log.info("출고 찍히는거 보자 : "+materialOutRepository.findByMrp(mrp));
	}
	
}
