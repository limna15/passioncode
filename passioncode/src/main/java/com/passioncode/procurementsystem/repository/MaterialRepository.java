package com.passioncode.procurementsystem.repository;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;

import com.passioncode.procurementsystem.entity.Material;

public interface MaterialRepository extends JpaRepository<Material, String> {
	
	public Collection<Material> findByNameContainingIgnoreCase(String name);

}
