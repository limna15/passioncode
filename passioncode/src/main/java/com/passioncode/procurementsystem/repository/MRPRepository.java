package com.passioncode.procurementsystem.repository;

import java.util.Collection;


import org.springframework.data.jpa.repository.JpaRepository;

import com.passioncode.procurementsystem.entity.MRP;

public interface MRPRepository extends JpaRepository<MRP, Integer> {
	
	/**
	 * 품목코드를 이용하여 MRP를 찾기 (정확한 품목코드 이용)
	 * @param materialCode
	 * @return
	 */
	public Collection<MRP> findBymaterialCode(String materialCode);

}
