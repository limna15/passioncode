package com.passioncode.procurementsystem.service;

import java.time.LocalDateTime;
import java.util.Date;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import com.passioncode.procurementsystem.dto.MaterialInDTO;
import com.passioncode.procurementsystem.dto.TransactionDetailDTO;
import com.passioncode.procurementsystem.entity.Company;
import com.passioncode.procurementsystem.entity.DetailPurchaseOrder;
import com.passioncode.procurementsystem.entity.MaterialIn;
import com.passioncode.procurementsystem.entity.TransactionDetail;

import lombok.extern.log4j.Log4j2;

@Log4j2
@SpringBootTest
public class TransactionDetailServiceTests {
	
	@Autowired
	TransactionDetailService transactionDetailService;
	@Autowired
	DetailPurchaseOrderService dpoService;
	@Autowired
	PurchaseOrderService purchaseOrderService;
	
	@Transactional
	@Test
	public void TransactionDetailEntityToDTOTest() {
		DetailPurchaseOrder dpo= dpoService.get(1);
		log.info("세부구매발주서 엔티티로 transactionDetailDTO 가져와지나 " + transactionDetailService.transactionDetailToDTO(dpo));
	}
	
	@Transactional
	@Test
	public void dtoToEntityTest() {
		TransactionDetailDTO transactionDetailDTO= transactionDetailService.transactionDetailToDTO(dpoService.get(1));
		log.info("transactionDetailDTO -> 거래명세서내용 엔티티로 바꾸기" + transactionDetailService.DTOToEntity(transactionDetailDTO));
	}

	@Transactional
	@Commit
	@Test
	public void insertByDTOTest() {
	
		TransactionDetailDTO transactionDetailDTO= TransactionDetailDTO.builder().no(2).company("패션코드").purchaseOrderNo(2).date(LocalDateTime.now())
				.companyNo("403-81-80895").companyName("(유)길승산업").CEO("김태리").companyAddress("서울시 동작구").manager("박서준").managerTel("010-1111-1111")
				.materialCode("CGa0001").materialName("Gear1").amount(150).unitPrice(500).build();

		log.info("transactionDetailDTO 어떻게 만들어지는지 보자 >>> " + transactionDetailDTO);
		
		log.info("거래명세서 등록됐나 보자 >>> " + transactionDetailService.register(transactionDetailDTO));
	}
}
