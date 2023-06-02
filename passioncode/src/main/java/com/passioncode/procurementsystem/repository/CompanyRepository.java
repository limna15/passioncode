package com.passioncode.procurementsystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.passioncode.procurementsystem.entity.Company;

public interface CompanyRepository extends JpaRepository<Company, String> {

}
