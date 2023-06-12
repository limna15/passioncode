package com.passioncode.procurementsystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.passioncode.procurementsystem.entity.PurchaseOrder;

public interface PurchaseOrderRepository extends JpaRepository<PurchaseOrder, Integer>{
	
	/**
	 * 
	 * @param purchaseOrder
	 * @return
	 */
	//public PurchaseOrder findPurchaseOrder(PurchaseOrder purchaseOrder);
	
	/**
	 * 발주서 발행 상태 확인
	 * 발주번호가 존재하면 발행
	 */
	//public Boolean ;
	
}
