package com.passioncode.procurementsystem.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.passioncode.procurementsystem.dto.ContractDTO;

import lombok.extern.log4j.Log4j2;

@Log4j2
@SpringBootTest
public class ContractServiceTests {
	
	@Autowired
	ContractService contractService;
	
	@Autowired
	MaterialService materialService;
	
	@Transactional
	@Test
	public void materialEntityToDTOTest() {
		//CGa0002 계약 미완료 품목
		//CGa0001 계약 완료 품목
		log.info("품목엔티티 이용해서 contractDTO 가져오나 보자(리스트) :"+contractService.materialEntityToDTO(materialService.get("CGa0002")));
		log.info("품목엔티티 이용해서 contractDTO 가져오나 보자(리스트) :"+contractService.materialEntityToDTO(materialService.get("CGa0001")));		
	}
	
	@Transactional
	@Test
	public void contractEntityToDTOTest() {
		log.info("계약서 엔티티 이용해서 contractDTO 가져오기 : "+contractService.contractEntityToDTO(contractService.get(1)));
	}
	
	@Transactional
	@Test
	public void dtoToEntityTest() {
		ContractDTO contractDTO = contractService.contractEntityToDTO(contractService.get(1));
		log.info("contractDTO -> 계약서 엔티티로 바꾸기 : "+contractService.dtoToEntity(contractDTO));
	}
	
	@Transactional
	@Test
	public void getDTOListTest() {
		log.info("모든 contractDTO 리스트 보기 : "+contractService.getDTOList());
		log.info("리스트 크기 봐보자 : "+contractService.getDTOList().size());
	}
	

}
