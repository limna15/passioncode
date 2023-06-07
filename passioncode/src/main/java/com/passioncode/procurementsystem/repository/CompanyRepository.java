package com.passioncode.procurementsystem.repository;

import java.util.Collection;


import org.springframework.data.jpa.repository.JpaRepository;

import com.passioncode.procurementsystem.entity.Company;

public interface CompanyRepository extends JpaRepository<Company, String> {
	
	/**
	 * 협력회사명을 포함한 name을 이용하여 협력회사 찾기
	 * @param name
	 * @return
	 */
	public Collection<Company> findByNameContaining(String name);

}
