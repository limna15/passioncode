package com.passioncode.procurementsystem.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.passioncode.procurementsystem.entity.DetailPurchaseOrder;

public interface DetailPurchaseOrderRepository extends JpaRepository<DetailPurchaseOrder, Integer>{
	
	@Query(value="SELECT MAX(CODE)+1 FROM detail_purchase_order",nativeQuery = true)
	public Integer findMaxCode();
	
	@Query(value="SELECT MAX(purchase_order_no)+1 FROM detail_purchase_order",nativeQuery = true)
	public Integer findMaxOrderNo();
	
	@Modifying//select 문이 아님을 나타낸다 
	@Transactional
	@Query(value="UPDATE procurement_plan myp SET myp.detail_purchase_order_code=:detailcode WHERE myp.code=:pcode",nativeQuery = true)
	public void myUpdate(@Param("detailcode")Integer code1,@Param("pcode")Integer code2);
	
}
