package com.passioncode.procurementsystem.service;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.passioncode.procurementsystem.dto.MaterialDTO;

import lombok.extern.log4j.Log4j2;

@Log4j2
@SpringBootTest
public class MaterialSeviceTests {
	
	@Autowired
	MaterialService materialService;
	
	@Test
	public void materialFindByIdTest() {
		log.info("material 값 보자~ : "+materialService.get("BPa0001"));
	}
	
	
	@Transactional
	@Test
	public void entityToDTOTest() {
		log.info("어디 materialDTO 잘가져오는지 결과좀 봐보자 : "+materialService.entityToDTO(materialService.get("BPa0001")));
		log.info("어디 materialDTO 잘가져오는지 결과좀 봐보자 : "+materialService.entityToDTO(materialService.get("CGa0002")));
	}
	
	@Transactional
	@Test
	public void dtoToEntityTest() {
		MaterialDTO materialDTO = materialService.entityToDTO(materialService.get("BPa0001"));
		log.info("엔티티로 잘 바꾸나 봐보자 : "+materialService.dtoToEntity(materialDTO));
	}
	
	@Transactional
	@Test
	public void materialDTOListTest() {
		log.info("어디 DTO 리스트 봐보자 : "+materialService.getDTOList());
		log.info("리스트 크기 봐보자 : "+materialService.getDTOList().size());
	}

}
