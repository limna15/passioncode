package com.passioncode.procurementsystem.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;
import com.passioncode.procurementsystem.dto.PurchaseOrderDTO;
import com.passioncode.procurementsystem.entity.DetailPurchaseOrder;
import com.passioncode.procurementsystem.entity.ProcurementPlan;
import com.passioncode.procurementsystem.entity.PurchaseOrder;
import com.passioncode.procurementsystem.repository.DetailPurchaseOrderRepository;
import com.passioncode.procurementsystem.repository.ProcurementPlanRepository;
import com.passioncode.procurementsystem.repository.PurchaseOrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@RequiredArgsConstructor
@Log4j2
public class PurchaseOrderServiceImpl implements PurchaseOrderService {

	private final PurchaseOrderRepository purchaseOrderRepository;
	private final DetailPurchaseOrderRepository detailPurchaseOrderRepository;
	private final ProcurementPlanRepository procurementPlanRepository;

	//1. 발주서 번호만 만드는 곳
	public PurchaseOrder makePONo() {
		PurchaseOrder po = PurchaseOrder.builder().build();
		purchaseOrderRepository.save(po);//발주서 번호 생성 후 저장
		return po;
	}
	
	//2. 발주 코드 만드는 곳(1개 이상의)
	@Override
	public void makePoCode(Integer[] num1) {
		//여기서 반복해서 만들기
		PurchaseOrder poNo = makePONo();//발주서 번호
		//log.info("발주서 번호: "+poNo);
		DetailPurchaseOrder detailPurchaseOrder;
		//Integer[] num2 = new Integer[2];
		//num2[0] = 17;
		//num2[1] = 19;
		ProcurementPlan pp= null; //= procurementPlanRepository.findById(num1).get();
		if(num1.length>0) {//1개 이상의 조달계획 번호가 오면
			for(int i=0;i<num1.length;i++) {
				pp = procurementPlanRepository.findById(num1[i]).get();
				detailPurchaseOrder = DetailPurchaseOrder.builder()
						.amount(pp.getAmount()).date(LocalDateTime.now())
						.purchaseOrder(poNo)//발주서 번호 생성하는 곳
						.build();
				detailPurchaseOrderRepository.save(detailPurchaseOrder);
				//총 9개
				ProcurementPlan pp2 = ProcurementPlan.builder()
						.amount(pp.getAmount())
						.code(pp.getCode())
						.completionDate(pp.getCompletionDate())
						.contract(pp.getContract())
						.detailPurchaseOrder(detailPurchaseOrder)
						.dueDate(pp.getDueDate())
						.minimumOrderDate(pp.getMinimumOrderDate())
						.mrp(pp.getMrp())
						.registerDate(pp.getRegisterDate()).build();
				procurementPlanRepository.save(pp2);
				//log.info("저장하는 조달계획번호  ~~~>>" + pp2);
			}
			
		}
		
	}
	
	@Override
	public ProcurementPlan get(Integer code) {
		return procurementPlanRepository.findById(code).get();
	}
	
	@Override
	public List<PurchaseOrderDTO> getDTOList() {
		List<ProcurementPlan> procurementPlanList = procurementPlanRepository.findAll();
		
		List<PurchaseOrderDTO> purchaseOrderDTOList = new ArrayList<>();
		
		for(int i=0;i<procurementPlanList.size();i++) {
			purchaseOrderDTOList.add(entityToDTO(procurementPlanList.get(i)));

		}
		
		return purchaseOrderDTOList;
	}
	
	@Override
	public PurchaseOrderDTO entityToDTO(ProcurementPlan procurementPlan) {
		// 조달 계획 가져오기
		// 협력회사, 발주일, 조달납기 예정일, 품목공급LT, 최소 발주일, 품목코드, 품목명
		// , 기존재고수량, 필요수량, 발주수량, 단가, 공급가격, 발주서 발행상태
		// 총 13개 DTO
		PurchaseOrderDTO purchaseOrderDTO = PurchaseOrderDTO.builder().detailNo(detailNo(procurementPlan.getCode()))
				.detailCode(detailCode(procurementPlan.getCode()))
				.companyName(procurementPlan.getContract().getCompany().getName())
				.purchaseOrderDate(extistPurchaseOrderDate(procurementPlan)).dueDate(procurementPlan.getDueDate()).supplyLT(procurementPlan.getContract().getSupplyLt())
				.minimumOrderDate(procurementPlan.getMinimumOrderDate()).materialCode(procurementPlan.getMrp().getMaterial().getCode())
				.materialName(procurementPlan.getContract().getMaterial().getName())
				.needAmount(procurementPlan.getAmount()).orderAmount((procurementPlan.getAmount()))
				.unitPrice(procurementPlan.getContract().getUnitPrice()).procurementPlan(procurementPlan.getCode())
				.supplyPrice((procurementPlan.getAmount())*(procurementPlan.getContract().getUnitPrice())).purchaseOrderStatus(existPurchaseOrder(procurementPlan)).build();
			
		
		return purchaseOrderDTO;
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

	@Override
	public PurchaseOrder getPurchaseOrder(Integer no) {
	
		return purchaseOrderRepository.findById(no).get();
	}
	
	public Integer detailCode(Integer num1) {//발주코드 구하기
		ProcurementPlan procurementPlan = procurementPlanRepository.findById(num1).get();
		//PurchaseOrderDTO purchaseOrderDTO = 
		Integer mycode = 0;
		if(procurementPlan.getDetailPurchaseOrder()==null) {
			mycode = 0;//발주되지 않은 것
		}else {
			mycode = procurementPlan.getDetailPurchaseOrder().getCode();
		}
		//log.info(mycode+"<<코드번호");
		
		return mycode;
	}
	
	public Integer detailNo(Integer num1) {//발주서 번호 구하기
		ProcurementPlan procurementPlan = procurementPlanRepository.findById(num1).get();
		//PurchaseOrderDTO purchaseOrderDTO = 
		Integer myno = 0;
		if(procurementPlan.getDetailPurchaseOrder()==null) {
			myno = 0;//발주되지 않은 것
		}else {
			myno = procurementPlan.getDetailPurchaseOrder().getPurchaseOrder().getNo();
		}
		//log.info(myno+"<<발주서 번호");
		return myno;
	}
		
}
