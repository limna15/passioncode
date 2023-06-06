package com.passioncode.procurementsystem.repository;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;

import com.passioncode.procurementsystem.entity.Contract;
import com.passioncode.procurementsystem.entity.Material;

public interface ContractRepository extends JpaRepository<Contract, Integer> {
	
	/**
	 * 품목을 이용하여 계약서 찾는 메소드
	 * @param material
	 * @return
	 */
	public Collection<Contract> findByMaterial(Material material);
}
