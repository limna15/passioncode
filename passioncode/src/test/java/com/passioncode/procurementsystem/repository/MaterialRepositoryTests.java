package com.passioncode.procurementsystem.repository;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.passioncode.procurementsystem.entity.Material;
import com.passioncode.procurementsystem.entity.MiddleCategory;

import lombok.extern.log4j.Log4j2;

@SpringBootTest
@Log4j2
public class MaterialRepositoryTests {
	
	@Autowired
	MaterialRepository materialRepository;
	
	@Autowired
	MiddleCategoryRepository middleCategoryRepository;
	
	@Test
	public void InsertTest() {
//		Optional<MiddleCategory> result = middleCategoryRepository.findById("GG0001");		//철, 기어
		Optional<MiddleCategory> result = middleCategoryRepository.findById("SS0001");		//보드, 센서
		
		MiddleCategory middleCategory = result.get();
		
//		Material material = Material.builder().code("CGa0001").name("Gear").size("G1").quality("B").spec("12mm").shareStatus(0)
//							.drawingNo("B23").drawingFile("테스트중").middleCategory(middleCategory).build();
		Material material = Material.builder().code("BSa0001").name("Sensor").size("S1").quality("A").spec("알루미늄").shareStatus(1)
				.drawingNo("N34").drawingFile("테스트중").middleCategory(middleCategory).build();
		
		materialRepository.save(material);
		
	}
	
	
	
	
	

}
