package com.passioncode.procurementsystem.repository;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.passioncode.procurementsystem.dto.PurchaseOrderDTO;
import com.passioncode.procurementsystem.entity.DetailPurchaseOrder;
import com.passioncode.procurementsystem.entity.ProcurementPlan;
import com.passioncode.procurementsystem.entity.ProgressCheck;
import com.passioncode.procurementsystem.entity.PurchaseOrder;

import lombok.extern.log4j.Log4j2;

@Log4j2
@SpringBootTest
public class PurchaseOrderRepositoryTests {
	
	@Autowired
	PurchaseOrderRepository purchaseOrderRepository;
	
	@Autowired
	ProcurementPlanRepository procurementPlanRepository;
	
	@Autowired
	DetailPurchaseOrderRepository detailPurchaseOrderRepository;
	
	@Autowired
	ProgressCheckRepository progressCheckRepository;
	
	@Test
	public void myRecord() {//지난 검수 기록 가져오기
		Optional<PurchaseOrder> list = purchaseOrderRepository.findById(1);
		Optional<DetailPurchaseOrder> dlist = detailPurchaseOrderRepository.findById(1);
		ProgressCheck pc = progressCheckRepository.findByDetailPurchaseOrder(dlist.get());
		
		log.info("pc 가져오기>>"+pc);
		
		
	}
	
	@Test
	public void getList() {
		Optional<PurchaseOrder> list = purchaseOrderRepository.findById(1);
		PurchaseOrder detail = list.get();
		List<PurchaseOrder> list2 = purchaseOrderRepository.findAll();
		log.info("글 번호 가져오기>>"+list2);
		
	}
	
	@Test
	public void InsertTest() {	//발주서 번호 생성 
		//잘 만들어진다.
		PurchaseOrder purchaseOrder = new PurchaseOrder(null);
		purchaseOrderRepository.save(purchaseOrder);
		
	}
	
	@Transactional
	@Test
	public void purchaseOrderDTOTest() {//조달 계획 가져오기
		//협력회사, 발주일, 조달납기 예정일, 품목공급LT, 최소 발주일, 품목코드, 품목명
		//, 기존재고수량, 필요수량, 발주수량, 단가, 공급가격, 발주서 발행상태
		//총 13개 DTO
		ProcurementPlan procurementPlan = procurementPlanRepository.findById(1).get();
		PurchaseOrderDTO purchaseOrderDTO = PurchaseOrderDTO.builder().companyName(procurementPlan.getContract().getCompany().getName())
				.purchaseOrderDate(extistPurchaseOrderDate(procurementPlan)).dueDate(procurementPlan.getDueDate()).supplyLT(procurementPlan.getContract().getSupplyLt())
				.minimumOrderDate(procurementPlan.getMinimumOrderDate()).materialCode(procurementPlan.getMrp().getMaterial().getName())
				.materialName(procurementPlan.getContract().getMaterial().getName())
				.needAmount(procurementPlan.getAmount()).orderAmount((procurementPlan.getAmount()))
				.unitPrice(procurementPlan.getContract().getUnitPrice()).procurementPlan(procurementPlan.getCode())
				.supplyPrice((procurementPlan.getAmount())*(procurementPlan.getContract().getUnitPrice())).purchaseOrderStatus(existPurchaseOrder(procurementPlan)).build();
			
			log.info(">>>>>>>>>>>"+purchaseOrderDTO);
	}
	
	
	/**
	 * 조달예정 품목 화면에서 발주서 발행상태를 만들어주는 메소드<br>
	 * 발주서 번확 존재하지 않으면 미완료<br>
	 * 그렇지 않으면 완료<br>
	 * @param procurementPlan
	 * @return
	 */
	public String existPurchaseOrder(ProcurementPlan procurementPlan) {
		String detailStatus = null;
		if (procurementPlan.getDetailPurchaseOrder() == null) {// 발주서 번호 존재X
			detailStatus = "미완료";
			
		} else {
			detailStatus = "완료";
		}
		
		return detailStatus;
	}
	
	/**
	 * 조달예정 품목 화면에서 발주일자를 보여주는 메소드<br>
	 * @param procurementPlan
	 * @return
	 */
	public LocalDateTime extistPurchaseOrderDate(ProcurementPlan procurementPlan) {
		//세부 구매발주서에 있는 발주 번호 갖고 오기
		LocalDateTime detailPurchaseOrderDate = null;
		if(procurementPlan.getDetailPurchaseOrder() != null) {
			detailPurchaseOrderDate=procurementPlan.getDetailPurchaseOrder().getDate();
		}
		
		return detailPurchaseOrderDate;
		
	}
	
}
