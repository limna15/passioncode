package com.passioncode.procurementsystem.repository;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.passioncode.procurementsystem.entity.PurchaseOrder;

import lombok.extern.log4j.Log4j2;

@Log4j2
@SpringBootTest
public class PurchaseOrderRepositoryTests {
	
	@Autowired
	PurchaseOrderRepository purchaseOrderRepository;
	
	@Test
	public void getList() {
		Optional<PurchaseOrder> list = purchaseOrderRepository.findById(101);
		PurchaseOrder detail = list.get();
		List<PurchaseOrder> list2 = purchaseOrderRepository.findAll();
		log.info("글 번호 가져오기>>"+list2);
		
	}

}
