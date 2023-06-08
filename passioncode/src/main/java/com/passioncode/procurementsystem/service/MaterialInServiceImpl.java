package com.passioncode.procurementsystem.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.passioncode.procurementsystem.dto.MaterialInDTO;
import com.passioncode.procurementsystem.entity.DetailPurchaseOrder;
import com.passioncode.procurementsystem.entity.MaterialIn;
import com.passioncode.procurementsystem.entity.ProcurementPlan;
import com.passioncode.procurementsystem.repository.DetailPurchaseOrderRepository;
import com.passioncode.procurementsystem.repository.MaterialInRepository;
import com.passioncode.procurementsystem.repository.ProcurementPlanRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@RequiredArgsConstructor
@Log4j2
public class MaterialInServiceImpl implements MaterailInService {
	
	private final MaterialInRepository materialInRepository;
	private final DetailPurchaseOrderRepository detailPurchaseOrderRepository;
	private final ProcurementPlanRepository procurementPlanRepository;
	
	@Override
	public MaterialInDTO materialInToDTO(MaterialIn materialIn) {		
		//발주서번호, 발주코드(세부구매발주서테이블), 조달납기예정일(조달계획테이블), 품목코드, 품목명, 입고상태, 발행상태

//		MaterialInDTO materialInDTO = MaterialInDTO.builder().no(materialIn.getDetailPurchaseOrder().getPurchaseOrder().getNo())
//				.code(materialIn.getDetailPurchaseOrder().getCode()).dueDate()
//				.materialCode().materialName()
//				.amount(materialIn.getDetailPurchaseOrder().getAmount())
//				.status(false).transactionStatus(false).build();
		return null;
	}

	@Override
	public MaterialIn DTOToEntity(MaterialInDTO materialInDTO) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
