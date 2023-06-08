package com.passioncode.procurementsystem.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.passioncode.procurementsystem.entity.DetailPurchaseOrder;
import com.passioncode.procurementsystem.entity.ProcurementPlan;


public interface ProcurementPlanRepository extends JpaRepository<ProcurementPlan, Integer> {
	
	/**
	 * 세부구매발주서의 발주코드를 이용하여 조달계획가져오기
	 * @param detailPurchaseOrder
	 * @return
	 */
	public ProcurementPlan findByDetailPurchaseOrder(DetailPurchaseOrder detailPurchaseOrder);
}
