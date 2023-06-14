package com.passioncode.procurementsystem.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.passioncode.procurementsystem.dto.ContractDTO;
import com.passioncode.procurementsystem.entity.Company;
import com.passioncode.procurementsystem.entity.Material;

import lombok.extern.log4j.Log4j2;

@SpringBootTest
@Log4j2
public class CompanyRepositoryTests {
	
	@Autowired
	CompanyRepository companyRepository;
	
	@Test
	public void InsertTest() {
//		Company company = Company.builder().no("777-77-77777").address("경기 수원시 영통구 영통로217번길 6 3층").ceo("민수진").dealStatus(1).manager("임나영")
//							.managerEmail("skskdudu15@gmail.com").managerTel("010-7777-7777").name("패션코드").tel("031-777-7777").build();
//		Company company = Company.builder().no("403-81-80895").address("서울시 동작구").ceo("김태리").dealStatus(1).manager("박서준")
//							.managerEmail("parkseojun@gmail.com").managerTel("010-1111-1111").name("(유)길승산업").tel("02-111-1111").build();
//		Company company = Company.builder().no("312-81-40374").address("경상북도 포항시").ceo("이민정").dealStatus(1).manager("박보검")
//							.managerEmail("bokeom@gmail.com").managerTel("010-4444-4444").name("(주)경도전자").tel("054-444-4444").build();
//		Company company = Company.builder().no("114-86-35933").address("인천광역시 연수구").ceo("신지").dealStatus(1).manager("이영지")
//				.managerEmail("yeongJi@gmail.com").managerTel("010-5555-5555").name("(주)넥스큐브").tel("032-555-5555").build();
//		Company company = Company.builder().no("615-81-61720").address("전라북도 전주시").ceo("이정신").dealStatus(0).manager("이전주")
//				.managerEmail("JeonJu@gmail.com").managerTel("010-6666-6666").name("(주)경신이티엠").tel("063-666-6666").build();
		Company company = Company.builder().no("124-87-31644").address("서울시 강북구").ceo("김신영").dealStatus(0).manager("브로니")
				.managerEmail("browny@gmail.com").managerTel("010-9999-9999").name("(주)브로우맥스").tel("02-999-9999").build();
		
		companyRepository.save(company);		
	};
	
	
	@Test
	public void getCompnayByNameWithDealTest() {
		log.info("되나아?? : "+companyRepository.findByNameContainingWithDeal("길승"));
		log.info("되나아?? : "+companyRepository.findByNameContainingWithDeal("미쥬"));
		
	}
	
//	@Test
//	public void getCompnayByNameWithDeal2Test() {
//		log.info("되나아?? : "+companyRepository.getCompnayByNameWithDeal2("길승"));
//		log.info("되나아?? : "+companyRepository.getCompnayByNameWithDeal("미쥬"));
//		
//	}

}
