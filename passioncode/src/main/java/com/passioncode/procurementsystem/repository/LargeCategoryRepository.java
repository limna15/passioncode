package com.passioncode.procurementsystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.passioncode.procurementsystem.entity.LargeCategory;

public interface LargeCategoryRepository extends JpaRepository<LargeCategory, String> {
	

}
