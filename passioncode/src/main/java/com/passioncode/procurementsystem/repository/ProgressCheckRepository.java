package com.passioncode.procurementsystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.passioncode.procurementsystem.entity.DetailPurchaseOrder;
import com.passioncode.procurementsystem.entity.ProgressCheck;

public interface ProgressCheckRepository extends JpaRepository<ProgressCheck, Integer> {
	
	public ProgressCheck findByDetailPurchaseOrder(DetailPurchaseOrder detailPurchaseOrder);
	
	
}
