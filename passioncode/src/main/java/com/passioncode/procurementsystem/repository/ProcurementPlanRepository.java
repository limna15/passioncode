package com.passioncode.procurementsystem.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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
	 * 조달계획 총개수 세기 <br>
	 * count 조회 시 Long으로 리턴 -> 쓰려면 리턴 데이터타입이 Long이어야함
	 * @return
	 */
	public Long countBy();
	
	/**
	 * mrp와 조인하여 조달계획 리스트 가져오기 <br>
	 * 소요일 기준으로 오름차순 정렬 -> 조달계획 완료된 리스트들 <br>
	 */
	@Query(value="SELECT pp.code,pp.mrp_code,pp.contract_no,pp.amount,pp.due_date,pp.minimum_order_date,pp.register_date,pp.completion_date,pp.detail_purchase_order_code "
					+ "FROM procurement_plan pp JOIN mrp m ON pp.mrp_code=m.code ORDER BY m.date", nativeQuery = true)
	public List<ProcurementPlan> getPPJoinMRPWithOrder();
	
	/**
	 * mrp와 조인한 조달계획에서 품목코드에 해당하는 조달계획 리스트 가져오기 <br>
	 * 한번이라도 조달완료, 발주가 들어간 품목은 수정 불가능하게 하기위한, 필요한 리스트 쿼리 
	 * @param materialCode
	 * @return
	 */
	@Query(value="SELECT pp.code,pp.mrp_code,pp.contract_no,pp.amount,pp.due_date,pp.minimum_order_date,pp.register_date,pp.completion_date,pp.detail_purchase_order_code "
			+ "FROM procurement_plan pp JOIN mrp m ON pp.mrp_code=m.code WHERE m.material_code = :materialCode  ", nativeQuery = true)
	public List<ProcurementPlan> getPPJoinMRPByMaterialCode(@Param("materialCode") String materialCode);
}
