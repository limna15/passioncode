package com.passioncode.procurementsystem.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.passioncode.procurementsystem.dto.ProcurementPlanDTO;

import lombok.extern.log4j.Log4j2;

@Log4j2
@SpringBootTest
public class ProcurementPlanServiceTests {
	
	@Autowired
	ProcurementPlanService procurementPlanService;
	
	@Transactional
	@Test
	public void mrpEntityToDTOTest() {
		//mrp 1번 조달계획 등록 완료된 mrp
		//mrp 2번 조달계획 등록 미완료된 mrp
		log.info("mrp를 이용해서 PPDTO 만들어보자 : "+procurementPlanService.mrpEntityToDTO(procurementPlanService.getMRP(1)));
		log.info("mrp를 이용해서 PPDTO 만들어보자 : "+procurementPlanService.mrpEntityToDTO(procurementPlanService.getMRP(2)));		
	}
	
	@Transactional
	@Test
	public void ppEntityToDTOTest() {
		log.info("조달계획 엔티티를 이용해서 PPDTO 만들기 : "+procurementPlanService.ppEntityToDTO(procurementPlanService.getProcurementPlan(1)));
	}
	
	@Transactional
	@Test
	public void dtoToEntityTest() {
		ProcurementPlanDTO procurementPlanDTO = procurementPlanService.ppEntityToDTO(procurementPlanService.getProcurementPlan(1));
		log.info("조달계획DTO를 이용해서 조달계획 엔티티 만들기 : "+procurementPlanService.dtoToEntity(procurementPlanDTO));
	}
	
	@Transactional
	@Test
	public void getDTOListTest() {
		log.info("모든 procurementPlanDTO 리스트 보기 : "+procurementPlanService.getDTOList());
		log.info("리스트 크기 봐보자 : "+procurementPlanService.getDTOList().size());
	}
	
	

}
