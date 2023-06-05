package com.passioncode.procurementsystem.repository;

import java.util.ArrayList;
import java.util.Collection;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.passioncode.procurementsystem.entity.Company;
import com.passioncode.procurementsystem.entity.Contract;
import com.passioncode.procurementsystem.entity.Material;

import lombok.extern.log4j.Log4j2;

@SpringBootTest
@Log4j2
public class ContractRepositoryTests {

	@Autowired
	ContractRepository contractRepository;
	
	@Autowired
	CompanyRepository companyRepository;
	
	@Autowired
	MaterialRepository materialRepository;
	
//	@Transactional
	@Test
	public void InsertTest() {
		Collection<Company> companyTest = companyRepository.findByNameContaining("길승산업");
		log.info("어디 test 보자 : "+companyTest);
//		companyTest.forEach(aa -> log.info("모야 나온거야?? : "+aa.getName()));
		ArrayList<Company> companyTest2 = (ArrayList<Company>) companyTest;
		log.info("모야 된거 아니야??? : "+companyTest2.get(0));
		
		Collection<Material> materialTest = materialRepository.findByNameContainingIgnoreCase("sensor");
		log.info("어디 대소문자 구별보자 : "+materialTest);
		ArrayList<Material> materialTest2=(ArrayList<Material>) materialTest;
		log.info("이렇게 보면 되나?? : "+materialTest2.get(0));
		
		
		Contract contract = Contract.builder().supplyLt(13).unitPrice(20000).contractFile("쿼리로 테스트중").material(materialTest2.get(0)).company(companyTest2.get(0)).build();
		
		contractRepository.save(contract);
		
	}
}
