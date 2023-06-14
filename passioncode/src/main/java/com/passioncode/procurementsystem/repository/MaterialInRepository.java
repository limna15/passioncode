package com.passioncode.procurementsystem.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

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
}
