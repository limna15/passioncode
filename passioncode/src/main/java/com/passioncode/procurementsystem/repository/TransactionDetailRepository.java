package com.passioncode.procurementsystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.passioncode.procurementsystem.entity.PurchaseOrder;
import com.passioncode.procurementsystem.entity.TransactionDetail;

public interface TransactionDetailRepository extends JpaRepository<TransactionDetail, Integer> {
	
	/**
	 * 구매발주서 번호를 이용해 발행상태(미완료,완료) 체크하기<br>
	 * False=미완료, True=완료
	 * @param purchaseOrder
	 * @return
	 */	
	public Boolean existsByPurchaseOrder(PurchaseOrder purchaseOrder);
	
	/**
	 * 구매발주서 번호를 이용해 거래명세서 번호찾기<br>
	 * @param purchaseOrder
	 * @return
	 */	
	public TransactionDetail findByPurchaseOrder(PurchaseOrder purchaseOrder);
}
