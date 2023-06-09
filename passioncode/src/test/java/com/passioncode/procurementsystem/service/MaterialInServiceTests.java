package com.passioncode.procurementsystem.service;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.passioncode.procurementsystem.dto.ContractDTO;
import com.passioncode.procurementsystem.dto.MaterialInDTO;
import com.passioncode.procurementsystem.entity.DetailPurchaseOrder;
import com.passioncode.procurementsystem.entity.MaterialIn;

import lombok.extern.log4j.Log4j2;

@Log4j2
@SpringBootTest
public class MaterialInServiceTests {
	
	@Autowired
	MaterailInService materialInService;
	@Autowired
	DetailPurchaseOrderService dpoService;
	
	@Transactional
	@Test
	public void materialInEntityToDTOTest() {
		DetailPurchaseOrder dpo= dpoService.get(1);
		log.info("세부구매발주서 엔티티로 materialInDTO 가져와지나 " + materialInService.materialInToDTO(dpo));
	}
	
	@Transactional
	@Test
	public void dtoToEntityTest() {
		MaterialInDTO materialInDTO= materialInService.materialInToDTO(dpoService.get(1));
		log.info("materialInDTO -> 자재입고 엔티티로 바꾸기" + materialInService.DTOToEntity(materialInDTO));
	}
	
	@Transactional
	@Test
	public void dtoListTest() {
		log.info("자재입고DTO 리스트 한번 보고싶어요 " + materialInService.getMaterialInDTOLsit());
	}
}
