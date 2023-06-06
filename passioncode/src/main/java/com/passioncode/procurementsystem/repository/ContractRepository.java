package com.passioncode.procurementsystem.repository;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;

import com.passioncode.procurementsystem.entity.Contract;
import com.passioncode.procurementsystem.entity.Material;

public interface ContractRepository extends JpaRepository<Contract, Integer> {

	public Collection<Contract> findByMaterial(Material material);
}
