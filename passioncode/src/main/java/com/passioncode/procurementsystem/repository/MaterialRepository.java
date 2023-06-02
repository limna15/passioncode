package com.passioncode.procurementsystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.passioncode.procurementsystem.entity.Material;

public interface MaterialRepository extends JpaRepository<Material, String> {

}
