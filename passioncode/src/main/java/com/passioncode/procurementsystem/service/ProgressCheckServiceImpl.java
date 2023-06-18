package com.passioncode.procurementsystem.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import com.passioncode.procurementsystem.dto.ProgressCheckDTO;
import com.passioncode.procurementsystem.entity.DetailPurchaseOrder;
import com.passioncode.procurementsystem.entity.MaterialIn;
import com.passioncode.procurementsystem.entity.ProcurementPlan;
import com.passioncode.procurementsystem.entity.ProgressCheck;
import com.passioncode.procurementsystem.repository.DetailPurchaseOrderRepository;
import com.passioncode.procurementsystem.repository.MaterialInRepository;
import com.passioncode.procurementsystem.repository.ProcurementPlanRepository;
import com.passioncode.procurementsystem.repository.ProgressCheckRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@RequiredArgsConstructor
@Log4j2
public class ProgressCheckServiceImpl implements ProgressCheckService {
	
	private final DetailPurchaseOrderRepository detailPurchaseOrderRepository;
	private final ProcurementPlanRepository procurementPlanRepository;
	private final ProgressCheckRepository progressCheckRepository;
	private final MaterialInRepository materialInRepository;
	
	@Override
	public List<ProgressCheckDTO> getProgressCheckDTOList() {//발주서 목록 갖고오기
		List<DetailPurchaseOrder> detilList = detailPurchaseOrderRepository.findAll();
		
		List<ProgressCheckDTO> progressCheckDTOlList = new ArrayList<>();
		for(int i=0;i<detilList.size();i++) {
			progressCheckDTOlList.add(entityToDTO(detilList.get(i)));
					}
		return progressCheckDTOlList;
		
	}

	@Override
	public ProgressCheckDTO entityToDTO(DetailPurchaseOrder detailPurchaseOrder) {
		//DetailPurchaseOrder detailPurchaseOrder2= detailPurchaseOrderRepository.findById(1).get();
		ProcurementPlan procurementPlan= procurementPlanRepository.findByDetailPurchaseOrder(detailPurchaseOrder);
		
		  ProgressCheckDTO progressCheckDTO = ProgressCheckDTO.builder()
		  .companyName(procurementPlan.getContract().getCompany().getName())
		  .purchaseOrderCode(procurementPlan.getDetailPurchaseOrder().getCode())
		  .orderAmount(procurementPlan.getDetailPurchaseOrder().getAmount())
		  .dueDate(procurementPlan.getDueDate())
		  .materialName(procurementPlan.getContract().getMaterial().getName())
		  .unitPrice(procurementPlan.getContract().getUnitPrice())
		  .diliveryPercent(getPercent(procurementPlan.getDetailPurchaseOrder()))
		  .inspectionComplete("미완료")
		  .nextCheckDate(nextCheckDate(procurementPlan.getDetailPurchaseOrder()))
		  .purchaseOrderDeadlineStatus(extistMIn(procurementPlan.getDetailPurchaseOrder())) .build();
		
		
		return progressCheckDTO;
	}
	
	public String extistMIn(DetailPurchaseOrder dp) {//발주서 마감 상태
		String inStatus=null;
		MaterialIn maIn = materialInRepository.findByDetailPurchaseOrder(dp);
		if(maIn!=null) {//입고 상태가 존재할 경우
			
		//발주코드를 통해 입고상태 갖고오기 
		if(maIn.getStatus()==false) {//존재하지 않으면
			inStatus="미완료";
			
		}else {
			inStatus="완료";
		}
		}else {
			inStatus="미완료";//입고 상태도 없을 경우
		}
		
		return inStatus;
		
	}
	//납기 진도율 가져오기
	public String getPercent(DetailPurchaseOrder dp) {
		String myPercent=null;
		ProgressCheck pg = progressCheckRepository.findByDetailPurchaseOrder(dp);
		if(pg!=null) {//납기 진도율이 존재한다면
			myPercent = pg.getRate()+"%";
			
		}else {
			myPercent = "미등록";
		}
		
		
		return myPercent;
		
	}
	//다음 진척검수 일정
	public String nextCheckDate(DetailPurchaseOrder dp) {
		LocalDateTime nextDate=null;
		ProgressCheck pg = progressCheckRepository.findByDetailPurchaseOrder(dp);
		if(pg!=null) {//다음 진척검수 일정이 존재한다면
			nextDate = pg.getDate();
			
		}else {
			nextDate = null;
		}
		
		
		return nextDate+"";
		
	}
}
