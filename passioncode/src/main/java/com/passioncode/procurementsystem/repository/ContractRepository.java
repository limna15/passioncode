package com.passioncode.procurementsystem.repository;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.passioncode.procurementsystem.entity.Contract;
import com.passioncode.procurementsystem.entity.Material;

public interface ContractRepository extends JpaRepository<Contract, Integer> {
	
	/**
	 * 품목을 이용하여 계약서 찾기
	 * @param material
	 * @return
	 */
	public List<Contract> findByMaterial(Material material);
	
	
	/**
	 * 품목을 이용하여 계약상태(미완료,완료) 체크하기 <br>
	 * 존재여부=계약상태, False=미완료, True=완료
	 * @param material
	 * @return
	 */
	public boolean existsByMaterial(Material material);
	

	
	
}
