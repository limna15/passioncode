package com.passioncode.procurementsystem.repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.passioncode.procurementsystem.dto.DetailPurchaseOrderDTO;
import com.passioncode.procurementsystem.dto.MaterialInDTO;
import com.passioncode.procurementsystem.dto.ProcurementPlanDTO;
import com.passioncode.procurementsystem.dto.ProgressCheckDTO;
import com.passioncode.procurementsystem.dto.PurchaseOrderDTO;
import com.passioncode.procurementsystem.entity.DetailPurchaseOrder;
import com.passioncode.procurementsystem.entity.MRP;
import com.passioncode.procurementsystem.entity.MaterialIn;
import com.passioncode.procurementsystem.entity.ProcurementPlan;
import com.passioncode.procurementsystem.entity.ProgressCheck;
import com.passioncode.procurementsystem.entity.PurchaseOrder;

import lombok.extern.log4j.Log4j2;

@Log4j2
@SpringBootTest
public class ProgressCheckRepositoryTests {

	@Autowired
	ProgressCheckRepository progressCheckRepository;
	
	@Autowired
	PurchaseOrderRepository purchaseOrderRepository;
	
	@Autowired
	DetailPurchaseOrderRepository detailPurchaseOrderRepository;
	
	@Autowired
	ProcurementPlanRepository procurementPlanRepository;
	
	@Autowired
	MaterialInRepository materialInRepository;
	
	@Autowired
	TransactionDetailRepository transactionDetailRepository;
	
	@Autowired
	MRPRepository mrpRepository;
	
	@Transactional
	@Test
	public void detailPurchaseOrderDTOTest() {
		//발주서 목록 화면
		//DTO로 갖고와서 뿌리기
		
		//발주서 목록은 다 뿌려야 한다
		List<DetailPurchaseOrder> detailList= detailPurchaseOrderRepository.findAll();
		
		List<MaterialInDTO> materialInDTOList= new ArrayList<>();
		MaterialInDTO materialInDTO= null;
		List<ProcurementPlan> ppList= new ArrayList<>();
		List<MaterialIn>miList= new ArrayList<>();
		
		DetailPurchaseOrder detailPurchaseOrder = detailPurchaseOrderRepository.findById(1).get();
	
		
	}
	
	@Transactional
	@Test
	public void getDTOList() {
		//List<DetailPurchaseOrder> optionalDpo= detailPurchaseOrderRepository.findAll();
		//ProcurementPlan pp= procurementPlanRepository.findByDetailPurchaseOrder(optionalDpo.get());
		//log.info("세부구매발주서로 procurementPlan 찾기! " + pp.getDueDate());
		
		List<DetailPurchaseOrder> detailList= detailPurchaseOrderRepository.findAll();
		List<ProgressCheckDTO> progressCheckDTOList = new ArrayList<>();
		
		
		for(int i=0; i<detailList.size(); i++) {
			//progressCheckDTOList.add(entityToDTO(detailList.get(i)));
		}
		
		log.info(">> 목록 보여주세요 >>>"+progressCheckDTOList);
	
	}
	
	@Test
	public void getProgressCheckDTOList() {//발주서 목록 갖고오기
		List<DetailPurchaseOrder> detilList = detailPurchaseOrderRepository.findAll();
		
		List<ProgressCheckDTO> progressCheckDTOlList = new ArrayList<>();
		for(int i=0;i<detilList.size();i++) {
			//progressCheckDTOlList.add(entityToDTO(detilList.get(i)));
					}
		
	}
	
	@Test
	public void getList() {
		Optional<DetailPurchaseOrder> list = detailPurchaseOrderRepository.findById(1);
		//ProgressCheck pcheckList = pcheckList.get();
		List<PurchaseOrder> list2 = purchaseOrderRepository.findAll();
		log.info("발주서 가져오기>>"+list2);
		
	}
	
	@Transactional
	@Test
	public void entityToDTO() {//이게 가져와 지는 것
		//발주서 목록 갖고오기
		//ProcurementPlan procurementPlan = procurementPlanRepository.findById(1).get();
		//DetailPurchaseOrder detailPurchaseOrder2 = detailPurchaseOrderRepository.findById(1).get();
		//1번 세구부매 발주서로 테스트
		
		//세부구매발주서 -> 조달계획 -> ..
		DetailPurchaseOrder detailPO= detailPurchaseOrderRepository.findById(1).get();
		ProcurementPlan procurementPlan= procurementPlanRepository.findByDetailPurchaseOrder(detailPO);
		//MRP mrp = mrpRepository.findBymaterialCode(null);
		//log.info("발주 계획 보기>>"+pp);		
		//만약에로 발주 코드가 널이 아닌것 으로 불러오기
		
		  ProgressCheckDTO progressCheckDTO = ProgressCheckDTO.builder()
		.companyName(procurementPlan.getContract().getCompany().getName())
		  .purchaseOrderCode(procurementPlan.getDetailPurchaseOrder().getCode())
		  .orderAmount(procurementPlan.getDetailPurchaseOrder().getAmount())
		  .dueDate(procurementPlan.getDueDate())
		  .materialName(procurementPlan.getContract().getMaterial().getName())
		  .orderAmount(procurementPlan.getAmount())
		  .unitPrice(procurementPlan.getContract().getUnitPrice())
		  .diliveryPercent(20).inspectionComplete("미완료")
		  .purchaseOrderDeadlineStatus(null) 
		  .build();
		  
		  log.info(">> 목록 보여주세요 >>>"+progressCheckDTO); 
		 		
	}
		
	@Transactional
	@Test
	public void DTOTest() {//자재 입고 화면 가져온 것
		DetailPurchaseOrder detailPurchaseOrder2= detailPurchaseOrderRepository.findById(1).get();
		ProcurementPlan pp= procurementPlanRepository.findByDetailPurchaseOrder(detailPurchaseOrder2);
		MaterialIn mi= materialInRepository.findByDetailPurchaseOrder(detailPurchaseOrder2);
		log.info("pp는 어케 찍히나 " + pp);
		
		MaterialInDTO materialInDTO= MaterialInDTO.builder().no(detailPurchaseOrder2.getPurchaseOrder().getNo()).code(detailPurchaseOrder2.getCode())
				.dueDate(pp.getDueDate()).materialCode(pp.getMrp().getMaterial().getCode())
				.materialName(pp.getMrp().getMaterial().getName())
				.amount(pp.getDetailPurchaseOrder().getAmount()).status(true).transactionStatus("발행 예정")
				.inDate(mi.getDate()).build();
		
		log.info("DTO 하나는 어케 가져오는거죠 " + materialInDTO);
	}
	
	
	
//	public String existMInStatus(DetailPurchaseOrder detailPO) {//조달 계획을 가져와야 하기 때문에 리턴이 필요하다. 
//		String detailStatus = null;
//		if (detailPO.getCode() == null) {//입고상태 존재X
//			detailStatus = "미완료";
//			
//		} else {
//			detailStatus = "완료";
//		}
//		
//		return detailStatus;
//	}
	
	public LocalDateTime extistPurchaseOrderDate(ProcurementPlan procurementPlan) {//조달 계획을 가져와야 하기 때문에 리턴이 필요하다. 
		//세부 구매발주서에 있는 발주 번호 갖고 오기
		LocalDateTime detailPurchaseOrderDate = null;
		if(procurementPlan.getDetailPurchaseOrder() != null) {
			detailPurchaseOrderDate=procurementPlan.getDetailPurchaseOrder().getDate();
		}
		
		return detailPurchaseOrderDate;
		
	}
	
	
	
	
}
	