package com.passioncode.procurementsystem.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.passioncode.procurementsystem.dto.DetailPurchaseOrderDTO;
import com.passioncode.procurementsystem.entity.DetailPurchaseOrder;
import com.passioncode.procurementsystem.entity.MRP;
import com.passioncode.procurementsystem.entity.ProcurementPlan;
import com.passioncode.procurementsystem.entity.PurchaseOrder;

import lombok.extern.log4j.Log4j2;

@Log4j2
@SpringBootTest
public class DetailPurchaseOrderRepositoryTests {
	
	@Autowired
	PurchaseOrderRepository purchaseOrderRepository;
	
	@Autowired
	DetailPurchaseOrderRepository detailPurchaseOrderRepository;
	
	@Autowired
	ProcurementPlanRepository procurementPlanRepository;
	
	@Test
	public void getList() {
		Optional<DetailPurchaseOrder> list = detailPurchaseOrderRepository.findById(1);
		DetailPurchaseOrder detail = list.get();
		List<DetailPurchaseOrder> list2 = detailPurchaseOrderRepository.findAll();
		
		log.info(">>>>>>>>>>"+detail);
		log.info(">>>>>>>>>>"+list);
		log.info("여러개>>>>>>>>>>"+list2);
		
	}
	
	@Test
	public void InsertTest() {	//발주 코드 생성 테스트
		PurchaseOrder purchaseOrder = new PurchaseOrder(101);
		//Integer code, Integer amount, LocalDateTime date, Integer purchaseOrderNo		
		
		DetailPurchaseOrder detailPurchaseOrder = new DetailPurchaseOrder(null, 450, LocalDateTime.now(), purchaseOrder);
		detailPurchaseOrderRepository.save(detailPurchaseOrder);
		
	}
	
	@Transactional
	@Test
	public void detailPurchaseOrderDTOTest() {//발주서 번호 생성 전 보기
		ProcurementPlan procurementPlan = procurementPlanRepository.findById(1).get();
		//발주서 번호, 협력회사, 발주일자, 납기 예정일
		//발주 번호, 품목코드, 품목, 발주수량, 단가, 공급 가격,조달계획코드
		//총 11개(+ 외래키 조달계획코드로 갖고오기)
		
		//공급가격만 구매발주서에서 가져옴
		DetailPurchaseOrderDTO detailPurchaseOrderDTO = DetailPurchaseOrderDTO.builder().purchaseOrderNo(null)
				.company(procurementPlan.getContract().getCompany().getName())
				.purchaseOrderDate(LocalDateTime.now()).dueDate(procurementPlan.getDueDate())
				.purchaseOrderCode(detailPurchaseOrderRepository.findMaxCode())
				.materialCode(procurementPlan.getMrp().getMaterial().getName())
				.purchaseOrderAmount((procurementPlan.getAmount())-(procurementPlan.getMrp().getMaterial().getStockAmount()))
				.unitPrice(procurementPlan.getContract().getUnitPrice())
				.suppluPrice((procurementPlan.getAmount())*(procurementPlan.getContract().getUnitPrice()))
				.procuremnetPlan(procurementPlan.getCode()).build();
				
		
		log.info(detailPurchaseOrderDTO);
		
	}
	
	@Test
	public void findMax() {
		detailPurchaseOrderRepository.findMaxCode();
		
		log.info(">>>>>>>>>>"+detailPurchaseOrderRepository.findMaxCode());
	}
	
}
