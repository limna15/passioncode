package com.passioncode.procurementsystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.passioncode.procurementsystem.entity.LargeCategory;
import com.passioncode.procurementsystem.entity.TransactionDetail;

public interface TransactionDetailRepository extends JpaRepository<TransactionDetail, Integer> {
	

}
