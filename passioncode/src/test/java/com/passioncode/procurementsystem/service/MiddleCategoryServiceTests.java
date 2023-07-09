package com.passioncode.procurementsystem.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import com.passioncode.procurementsystem.dto.MiddleCategoryDTO;
import com.passioncode.procurementsystem.entity.LargeCategory;
import com.passioncode.procurementsystem.entity.MiddleCategory;
import lombok.extern.log4j.Log4j2;

@Log4j2
@SpringBootTest
public class MiddleCategoryServiceTests {
	
	@Autowired
	MiddleCategoryService middleCategoryService;
	
	@Transactional
	@Test
	public void getMiddleCategoryByLargeCategoryTest() {
		LargeCategory largeCategory = middleCategoryService.getLargeCategory("CC0001");
		log.info("대분류 이용해서 중분류 잘가져오나 보자 : "+middleCategoryService.getMiddleCategoryByLargeCategory(largeCategory));	
		
		MiddleCategory middleCategory = middleCategoryService.getMiddleCategoryByLargeCategory(largeCategory).get(1);
		log.info("엔티티 -> DTO 보자 : "+middleCategoryService.entityToDTO(middleCategory));
		MiddleCategoryDTO middleCategoryDTO = middleCategoryService.entityToDTO(middleCategory);
		log.info("DTO -> 엔티티 보자 : "+middleCategoryService.dtoToEntity(middleCategoryDTO));
	}
	
	@Transactional
	@Test
	public void getDTOListTest() {
		log.info("DTO 리스트 잘가져오나 보자 : "+middleCategoryService.getDTOList());
	}
	
}
