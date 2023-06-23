package com.passioncode.procurementsystem.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.passioncode.procurementsystem.entity.DetailPurchaseOrder;
import com.passioncode.procurementsystem.entity.MaterialIn;

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
	
	@Query(value= "SELECT po.no, dpo.code, pp.dueDate, m.code, m.name, dpo.amount, mi.status, mi.transactionStatus "
			+ "FROM MaterialIn mi "
			+ "right outer JOIN DetailPurchaseOrder dpo ON dpo =mi.detailPurchaseOrder "
			+ "JOIN PurchaseOrder po ON po=dpo.purchaseOrder "
			+ "JOIN ProcurementPlan pp ON pp.detailPurchaseOrder=dpo "
			+ "JOIN Contract c ON c=pp.contract "
			+ "JOIN material m ON m =c.material "
			+ "ORDER BY COALESCE(mi.status, 2) desc, binary(mi.transactionStatus)")
	public List<Object[]> getOrderByList();
}
