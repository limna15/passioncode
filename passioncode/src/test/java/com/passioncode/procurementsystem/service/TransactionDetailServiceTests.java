package com.passioncode.procurementsystem.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.passioncode.procurementsystem.dto.MaterialInDTO;
import com.passioncode.procurementsystem.dto.TransactionDetailDTO;
import com.passioncode.procurementsystem.entity.DetailPurchaseOrder;

import lombok.extern.log4j.Log4j2;

@Log4j2
@SpringBootTest
public class TransactionDetailServiceTests {
	
	@Autowired
	TransactionDetailService transactionDetailService;
	@Autowired
	DetailPurchaseOrderService dpoService;
	
	@Transactional
	@Test
	public void TransactionDetailEntityToDTOTest() {
		DetailPurchaseOrder dpo= dpoService.get(1);
		log.info("세부구매발주서 엔티티로 materialInDTO 가져와지나 " + transactionDetailService.transactionDetailToDTO(dpo));
	}
	
	@Transactional
	@Test
	public void dtoToEntityTest() {
		TransactionDetailDTO transactionDetailDTO= transactionDetailService.transactionDetailToDTO(dpoService.get(1));
		log.info("materialInDTO -> 자재입고 엔티티로 바꾸기" + transactionDetailService.DTOToEntity(transactionDetailDTO));
	}

}
