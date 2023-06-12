package com.passioncode.procurementsystem.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.passioncode.procurementsystem.entity.DetailPurchaseOrder;

public interface DetailPurchaseOrderRepository extends JpaRepository<DetailPurchaseOrder, Integer>{
	
	@Query(value="SELECT MAX(CODE)+1 FROM detail_purchase_order",nativeQuery = true)
	public Integer findMaxCode();
	
}
