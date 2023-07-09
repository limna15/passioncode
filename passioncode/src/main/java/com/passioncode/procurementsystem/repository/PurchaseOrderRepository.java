package com.passioncode.procurementsystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.passioncode.procurementsystem.entity.PurchaseOrder;

public interface PurchaseOrderRepository extends JpaRepository<PurchaseOrder, Integer>{
	
	
	
}
