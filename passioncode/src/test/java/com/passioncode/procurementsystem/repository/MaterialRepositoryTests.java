package com.passioncode.procurementsystem.repository;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.passioncode.procurementsystem.dto.MaterialDTO;
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
	
	@Autowired
	ContractRepository contractRepository;
	
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
	
	@Transactional
	@Test
	public void readCategoryTest() {
		Optional<Material> result = materialRepository.findById("BPa0001");
		Material material = result.get();
		log.info("읽어온 material 한번 보자 : "+material);
		log.info("모야모야 바로 읽을 수 있는건가??? : "+materialRepository.findById("BPa0001").get());
		log.info("품목에서 외래키 중분류 보자 : "+material.getMiddleCategory());
		log.info("품목에서 외래키 중분류타고 대분류도 봐보자 : "+material.getMiddleCategory().getLargeCategory());
		
	}
	
	@Transactional
	@Test
	public void materialDTOTest() {
		Material material = materialRepository.findById("BPa0001").get();
		MaterialDTO materialDTO =  MaterialDTO.builder().code(material.getCode()).name(material.getName()).size(material.getSize()).quality(material.getQuality())
				.spec(material.getSpec()).drawingNo(material.getDrawingNo()).drawingFile(material.getDrawingFile()).shareStatus(material.getShareStatus())
				.largeCategoryName(material.getMiddleCategory().getLargeCategory().getCategory()).middleCategoryName(material.getMiddleCategory().getCategory())
				.contractStatus(contractRepository.existsByMaterial(material)).build();
		log.info("DTO 테스트 결과 확인해보자 : "+materialDTO);
	}
	
	
	
	
	

}
