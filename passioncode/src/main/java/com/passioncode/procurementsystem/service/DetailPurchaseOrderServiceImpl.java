package com.passioncode.procurementsystem.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.passioncode.procurementsystem.dto.DetailPurchaseOrderDTO;
import com.passioncode.procurementsystem.dto.PurchaseOrderDTO;
import com.passioncode.procurementsystem.entity.DetailPurchaseOrder;
import com.passioncode.procurementsystem.entity.MaterialOut;
import com.passioncode.procurementsystem.entity.ProcurementPlan;
import com.passioncode.procurementsystem.entity.PurchaseOrder;
import com.passioncode.procurementsystem.repository.DetailPurchaseOrderRepository;
import com.passioncode.procurementsystem.repository.PurchaseOrderRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@RequiredArgsConstructor
@Log4j2
public class DetailPurchaseOrderServiceImpl implements DetailPurchaseOrderService {

	private final DetailPurchaseOrderRepository detailPurchaseOrderRepository;
	
	@Override
	public DetailPurchaseOrderDTO read(Integer no) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DetailPurchaseOrderDTO entityToDTO(DetailPurchaseOrder detailPurchaseOrder, MaterialOut materialOut) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DetailPurchaseOrder get(Integer code) {
		return detailPurchaseOrderRepository.findById(code).get();
	}
	

}
