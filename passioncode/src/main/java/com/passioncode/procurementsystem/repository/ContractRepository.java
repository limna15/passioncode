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
	
	/**
	 * 품목을 이용하여 계약상태(미완료,완료) 체크하는 메소드, 존재여부=계약상태, false=미완료, true=완료
	 * @param material
	 * @return
	 */
	public boolean existsByMaterial(Material material);
}
