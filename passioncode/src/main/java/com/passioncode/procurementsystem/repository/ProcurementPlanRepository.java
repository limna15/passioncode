package com.passioncode.procurementsystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.passioncode.procurementsystem.entity.ProcurementPlan;


public interface ProcurementPlanRepository extends JpaRepository<ProcurementPlan, Integer> {

}
