package com.passioncode.procurementsystem.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.passioncode.procurementsystem.entity.DetailPurchaseOrder;
import com.passioncode.procurementsystem.entity.MaterialIn;
import com.passioncode.procurementsystem.entity.ProcurementPlan;

public interface MaterialInRepository extends JpaRepository<MaterialIn, Integer> {
	
	/**
	 * 세부구매발주서 코드를 이용해 입고상태(미완료,완료) 체크하기 <br>
	 * False=미완료, True=완료
	 * @param detailPurchaseOrder
	 * @return
	 */	
	public Boolean existsByDetailPurchaseOrder(DetailPurchaseOrder detailPurchaseOrder);
	
	/**
	 * 세부구매발주서의 발주코드를 이용하여 입고일 가져오기
	 * @param detailPurchaseOrder
	 * @return
	 */
	public MaterialIn findByDetailPurchaseOrder(DetailPurchaseOrder detailPurchaseOrder);
	
	/**
	 * 세부구매발주서와 조인해서 자재입고 등록된 리스트 가져오기
	 * 입고상태(내림차순) - 발행상태(오름차순) 으로 정렬
	 * @return
	 */
	@Query(value="SELECT mi.code, mi.date, mi.status, mi.transaction_status, mi.detail_purchase_order_code "
			+ "FROM material_in mi JOIN detail_purchase_order dpo ON mi.detail_purchase_order_code=dpo.code "
			+ "ORDER BY STATUS DESC, transaction_status", nativeQuery = true)
	public List<MaterialIn> getJoinDpo();
}
