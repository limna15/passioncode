package com.passioncode.procurementsystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.passioncode.procurementsystem.entity.Contract;

public interface ContractRepository extends JpaRepository<Contract, Integer> {

}
