package com.passioncode.procurementsystem.repository;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.passioncode.procurementsystem.entity.DetailPurchaseOrder;

import lombok.extern.log4j.Log4j2;

@Log4j2
@SpringBootTest
public class DetailPurchaseOrderRepositoryTests {
	
	@Autowired
	DetailPurchaseOrderRepository detailPurchaseOrderRepository;
	
	@Test
	public void getList() {
		Optional<DetailPurchaseOrder> list = detailPurchaseOrderRepository.findById(103);
		
		DetailPurchaseOrder detail = list.get();
		
		List<DetailPurchaseOrder> list2 = detailPurchaseOrderRepository.findAll();
		
		log.info(">>>>>>>>>>"+detail);
		log.info(">>>>>>>>>>"+list);
		log.info(">>>>>>>>>>"+list2);
		
	}
	
	public void testGetList() {
		//detailPurchaseOrderRepository
	}

}
