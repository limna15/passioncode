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
	public List<ProgressCheckDTO> getDTOList() {
		//List<DetailPurchaseOrder> optionalDpo= detailPurchaseOrderRepository.findAll();
		//ProcurementPlan pp= procurementPlanRepository.findByDetailPurchaseOrder(optionalDpo.get());
		//log.info("세부구매발주서로 procurementPlan 찾기! " + pp.getDueDate());
		
		List<DetailPurchaseOrder> detailList= detailPurchaseOrderRepository.findAll();
		List<ProgressCheckDTO> progressCheckDTOList = new ArrayList<>();
		
		
		for(int i=0; i<detailList.size(); i++) {
			//progressCheckDTOList.add(entityToDTO(detailList.get(i)));
		}
		
		log.info(">> 목록 보여주세요 >>>"+progressCheckDTOList);;
		return progressCheckDTOList;
	}
	
	@Test
	public List<ProgressCheckDTO> getProgressCheckDTOList() {//발주서 목록 갖고오기
		List<DetailPurchaseOrder> detilList = detailPurchaseOrderRepository.findAll();
		
		List<ProgressCheckDTO> progressCheckDTOlList = new ArrayList<>();
		for(int i=0;i<detilList.size();i++) {
			//progressCheckDTOlList.add(entityToDTO(detilList.get(i)));
					}
		return progressCheckDTOlList;
		
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
		//조달계획으로 갖고와서 테스트 하기
		
		//DetailPurchaseOrder detailPurchaseOrder2 = detailPurchaseOrderRepository.findById(1).get();
		//1번 세구부매 발주서로 테스트
		
		//세부구매발주서 -> 조달계획 -> ..
		DetailPurchaseOrder detailPurchaseOrder2= detailPurchaseOrderRepository.findById(1).get();
		ProcurementPlan procurementPlan= procurementPlanRepository.findByDetailPurchaseOrder(detailPurchaseOrder2);
		//MRP mrp = mrpRepository.findBymaterialCode(null);
		//log.info("발주 계획 보기>>"+pp);		
		//만약에로 발주 코드가 널이 아닌것 으로 불러오기
		
		  ProgressCheckDTO progressCheckDTO = ProgressCheckDTO.builder()
		  .companyName(procurementPlan.getContract().getCompany().getName())
		  .purchaseOrderCode(procurementPlan.getDetailPurchaseOrder().getCode())
		  .dueDate(procurementPlan.getDueDate())
		  .materialName(procurementPlan.getContract().getMaterial().getName())
		  .unitPrice(procurementPlan.getContract().getUnitPrice())
		  .diliveryStatus("미완료") .diliveryPercent(20) .inspectionComplete("미완료")
		  .purchaseOrderDeadlineStatus("미완료") .build();
		  
		  log.info(">> 목록 보여주세요 >>>"+progressCheckDTO); 
		 		
		
		//return null;
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
				.amount(pp.getDetailPurchaseOrder().getAmount()).status(true).transactionStatus(false)
				.inDate(mi.getDate()).build();
		
		log.info("DTO 하나는 어케 가져오는거죠 " + materialInDTO);
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
				.stockAmount(procurementPlan.getMrp().getMaterial().getStockAmount())
				.needAmount(procurementPlan.getAmount()).orderAmount((procurementPlan.getAmount())-(procurementPlan.getMrp().getMaterial().getStockAmount()))
				.unitPrice(procurementPlan.getContract().getUnitPrice()).procuremnetPlan(procurementPlan.getCode())
				.supplyPrice((procurementPlan.getAmount())*(procurementPlan.getContract().getUnitPrice())).purchaseOrderStatus(existPurchaseOrder(procurementPlan)).build();
			
			log.info(">>>>>>>>>>>"+purchaseOrderDTO);
	}
	
	public String existPurchaseOrder(ProcurementPlan procurementPlan) {
		String detailStatus = null;
		if (procurementPlan.getDetailPurchaseOrder() == null) {// 발주서 번호 존재X
			detailStatus = "미완료";
			
		} else {
			detailStatus = "완료";
		}
		
		return detailStatus;
	}
	
	public LocalDateTime extistPurchaseOrderDate(ProcurementPlan procurementPlan) {
		//세부 구매발주서에 있는 발주 번호 갖고 오기
		LocalDateTime detailPurchaseOrderDate = null;
		if(procurementPlan.getDetailPurchaseOrder() != null) {
			detailPurchaseOrderDate=procurementPlan.getDetailPurchaseOrder().getDate();
		}

		return detailPurchaseOrderDate;
		
	}
	
	
	
	
}
	