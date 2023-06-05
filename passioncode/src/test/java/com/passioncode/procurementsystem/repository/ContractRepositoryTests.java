package com.passioncode.procurementsystem.repository;

import java.util.Collection;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.passioncode.procurementsystem.entity.Company;
import com.passioncode.procurementsystem.entity.Contract;

@SpringBootTest
public class ContractRepositoryTests {

	@Autowired
	ContractRepository contractRepository;
	
	@Autowired
	CompanyRepository companyRepository;
	
	@Transactional
	@Test
	public void InsertTest() {
		Collection<Company> test = companyRepository.findByName("길승");
		System.out.println("어디 test 보자 : "+test);
		test.forEach(aa -> System.out.println("모야 나온거야??"+aa));
		
//		Contract contract = Contract.builder().
	}
}
