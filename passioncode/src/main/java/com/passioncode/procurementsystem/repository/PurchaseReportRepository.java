package com.passioncode.procurementsystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.passioncode.procurementsystem.entity.DetailPurchaseOrder;
import com.passioncode.procurementsystem.entity.ProcurementPlan;

public interface PurchaseReportRepository extends JpaRepository<ProcurementPlan, Integer> {
	
	/**
	 * 조달계획 <br>
	 * count 조회 시 Long으로 리턴 -> 쓰려면 리턴 데이터타입이 Long이어야함
	 * @return
	 */
	public Long countBy();
	
	public Boolean existsByDetailPurchaseOrder(DetailPurchaseOrder detailPurchaseOrder);

	public boolean existsById(Integer code);
}
