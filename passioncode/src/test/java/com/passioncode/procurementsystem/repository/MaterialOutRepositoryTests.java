package com.passioncode.procurementsystem.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.passioncode.procurementsystem.entity.MRP;
import com.passioncode.procurementsystem.entity.MaterialOut;

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
	
	@Test
	public void InsertTest() {
		//출고코드, 출고상태, 출고일, 자재소요계획코드(외래키)(MRP)
		MRP mrp = mrpRepository.findById(1).get();
		
		MaterialOut materialOut = MaterialOut.builder().status(1).mrp(mrp).build();
		
		materialOutRepository.save(materialOut);
		
	}
	
}
