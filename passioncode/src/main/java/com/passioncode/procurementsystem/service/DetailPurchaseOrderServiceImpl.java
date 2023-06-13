package com.passioncode.procurementsystem.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;
import com.passioncode.procurementsystem.dto.DetailPurchaseOrderDTO;
import com.passioncode.procurementsystem.dto.PurchaseOrderDTO;
import com.passioncode.procurementsystem.entity.DetailPurchaseOrder;
import com.passioncode.procurementsystem.entity.MaterialOut;
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
public class DetailPurchaseOrderServiceImpl implements DetailPurchaseOrderService {

	private final DetailPurchaseOrderRepository detailPurchaseOrderRepository;
	private final ProcurementPlanRepository procurementPlanRepository;
	
	@Override
	public Integer register(DetailPurchaseOrderDTO detailPurchaseOrderDTO) {
		DetailPurchaseOrder detailPurchaseOrder = dtoToEntity(detailPurchaseOrderDTO);
		return null;
	}
	
	@Override
	public DetailPurchaseOrder dtoToEntity(DetailPurchaseOrderDTO detailPurchaseOrderDTO) {
		DetailPurchaseOrder detailPurchaseOrder = null;
		//DetailPurchaseOrder = detailPurchaseOrderDTO.builder()
		
		return null;
		
	}
	
	@Override
	public DetailPurchaseOrderDTO read(Integer no) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public List<DetailPurchaseOrderDTO> getDTOList(){
		List<ProcurementPlan> procurmentPlanList = procurementPlanRepository.findAll();
		
		List<DetailPurchaseOrderDTO> detailDTOList = new ArrayList<>();
		
		for(int i=0;i<procurmentPlanList.size();i++) {
			detailDTOList.add(entityToDTO(procurmentPlanList.get(i)));
		}
		System.out.println("보여줘요 디테일>>>>>>>"+detailDTOList);
		return detailDTOList;
		
	}
	
	@Override
	public DetailPurchaseOrderDTO entityToDTO(ProcurementPlan procurementPlan) {
		// 세부 구매 발주서 발행 화면
		//발주서 번호, 협력 회사, 발주 일자, 납기 예정일
		//발주 코드, 품목 코드, 발주 수량, 단가, 공급 가격, 조달계획코드(총 10개)
		DetailPurchaseOrderDTO detailPurchaseOrderDTO = DetailPurchaseOrderDTO.builder()
				.purchaseOrderNo(detailPurchaseOrderRepository.findMaxOrderNo())
				.companyName(procurementPlan.getContract().getCompany().getName())
				.materialName(procurementPlan.getMrp().getMaterial().getName())
				.purchaseOrderDate(LocalDateTime.now()).dueDate(procurementPlan.getDueDate())
				.purchaseOrderCode(detailPurchaseOrderRepository.findMaxCode())
				.materialCode(procurementPlan.getMrp().getMaterial().getCode())
				.purchaseOrderAmount((procurementPlan.getAmount())-(procurementPlan.getMrp().getMaterial().getStockAmount()))
				.unitPrice(procurementPlan.getContract().getUnitPrice())
				.suppluPrice((procurementPlan.getAmount())*(procurementPlan.getContract().getUnitPrice()))
				.procurementPlan(procurementPlan.getCode()).build();
				
		log.info("세부 구매 발주서 발행DTO"+detailPurchaseOrderDTO);
		return detailPurchaseOrderDTO;
	}

	@Override
	public DetailPurchaseOrder get(Integer code) {
		return detailPurchaseOrderRepository.findById(code).get();
	}

	
	
	

}
