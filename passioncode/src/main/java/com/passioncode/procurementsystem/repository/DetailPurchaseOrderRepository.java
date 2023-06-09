package com.passioncode.procurementsystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.passioncode.procurementsystem.entity.DetailPurchaseOrder;
import com.passioncode.procurementsystem.entity.MaterialIn;

public interface DetailPurchaseOrderRepository extends JpaRepository<DetailPurchaseOrder, Integer>{


}
