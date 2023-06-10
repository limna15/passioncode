package com.passioncode.procurementsystem.service;

import java.util.Date;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import com.passioncode.procurementsystem.dto.ContractDTO;
import com.passioncode.procurementsystem.dto.MaterialDTO;
import com.passioncode.procurementsystem.dto.MaterialInDTO;
import com.passioncode.procurementsystem.entity.DetailPurchaseOrder;
import com.passioncode.procurementsystem.entity.MaterialIn;

import lombok.extern.log4j.Log4j2;

@Log4j2
@SpringBootTest
public class MaterialInServiceTests {
	
	@Autowired
	MateriallInService materialInService;
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
	
//	트랜잭션 처리는 테스트에서 쓰면 자동 롤백된다. 그래서 지연로딩부분때문에 하는거 아니면 쓰면 삽입이 안됨
//	트랙젹션 어노테이션 쓰려면 쓰고, 커밋 어노테이션 추가해줘야지 제대로 커밋됨
	@Transactional
	@Commit
	@Test
	public void registerTest() {
		Date date= new Date();
		date.setDate(2023-06-16);
		
		MaterialInDTO materialInDTO= MaterialInDTO.builder().no(2).code(5)
				.dueDate(date).materialCode("BSa0001").materialName("Sensor")
				.amount(200).status(true).transactionStatus(false).build();
		log.info("만들어진 materialInDTO 보자 >>> "+materialInDTO);

		log.info("등록이 되었는지 보자(등록된 세부구매발주서코드) >>> "+materialInService.register(materialInDTO));
	}
}
