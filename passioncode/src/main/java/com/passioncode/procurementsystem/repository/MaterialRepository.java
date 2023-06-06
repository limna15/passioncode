package com.passioncode.procurementsystem.repository;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;

import com.passioncode.procurementsystem.entity.Material;

public interface MaterialRepository extends JpaRepository<Material, String> {
	
	/**
	 * 품목명을 포한한 name을 이용하여 품목 찾는 메소드 (대소문자 무시하고 찾음)
	 * @param name
	 * @return
	 */
	public Collection<Material> findByNameContainingIgnoreCase(String name);

}
