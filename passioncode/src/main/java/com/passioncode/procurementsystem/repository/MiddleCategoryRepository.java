package com.passioncode.procurementsystem.repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.passioncode.procurementsystem.entity.LargeCategory;
import com.passioncode.procurementsystem.entity.MiddleCategory;

public interface MiddleCategoryRepository extends JpaRepository<MiddleCategory, String> {
	
	/**
	 * 중문류 종류(이름)을 이용하여 중분류 찾기 (정확한 종류(이름)으로 찾기)
	 * @param category
	 * @return
	 */
	List<MiddleCategory> findByCategory(String category);	
//	Optional<MiddleCategory> findByCategory(String category);
	
	/**
	 * 대분류코드(외래키) 대분류 엔티티를 이용하여 중분류 찾기
	 * @param largeCategory
	 * @return
	 */
	List<MiddleCategory> findByLargeCategory(LargeCategory largeCategory);
}
