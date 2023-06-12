package com.passioncode.procurementsystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.passioncode.procurementsystem.entity.DetailPurchaseOrder;
import com.passioncode.procurementsystem.entity.MRP;
import com.passioncode.procurementsystem.entity.ProcurementPlan;


public interface ProcurementPlanRepository extends JpaRepository<ProcurementPlan, Integer> {
	
	/**
	 * 세부구매발주서의 발주코드를 이용하여 조달계획가져오기
	 * @param detailPurchaseOrder
	 * @return
	 */
	public ProcurementPlan findByDetailPurchaseOrder(DetailPurchaseOrder detailPurchaseOrder);

	/**
	 * MRP을 이용하여 조달계획등록상태(미완료,완료) 체크하기 <br>
	 * 존재여부=등록상태, False=미완료, True=완료
	 * @param mrp
	 * @return
	 */	
	public Boolean existsByMrp(MRP mrp);
	
	/**
	 * MRP를 이용해서 조달계획 엔티티 찾기
	 * @param mrp
	 * @return
	 */
	public ProcurementPlan findByMrp(MRP mrp);
	
	/**
	 * 조달계획 총개수 세기
	 * count 조회 시 Long으로 리턴 -> 쓰려면 리턴 데이터타입이 Long이어야함
	 * @return
	 */
	Long countBy();
	
}
