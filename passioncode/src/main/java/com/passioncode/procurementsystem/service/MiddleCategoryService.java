package com.passioncode.procurementsystem.service;

import java.util.List;
import com.passioncode.procurementsystem.dto.MiddleCategoryDTO;
import com.passioncode.procurementsystem.entity.LargeCategory;
import com.passioncode.procurementsystem.entity.MiddleCategory;

public interface MiddleCategoryService {
	
	/**
	 * 중분류코드를 이용해서 MiddleCategory 중분류 엔티티 가져오기
	 * @param code
	 * @return
	 */
	MiddleCategory getMiddleCategory(String code);
	
	/**
	 * 대분류코드(외래키) 대분류 엔티티를 이용하여 중분류 찾기
	 * @param largeCategory
	 * @return
	 */
	List<MiddleCategory> getMiddleCategoryByLargeCategory(LargeCategory largeCategory);
	
	/**
	 * 대분류코드를 이용해서 LargeCategory 대분류 엔티티 가져오기
	 * @param code
	 * @return
	 */
	LargeCategory getLargeCategory(String code);
	
	/**
	 * 중분류 엔티티를 이용해서 MiddleCategoryDTO로 만들기(MiddleCategory -> MiddleCategoryDTO) 
	 * @param middleCategory
	 * @return
	 */
	MiddleCategoryDTO entityToDTO(MiddleCategory middleCategory);
	
	/**
	 * MiddleCategoryDTO를 이용해서 품목 엔티티 만들기(MiddleCategoryDTO -> MiddleCategory)
	 * @param middleCategoryDTO
	 * @return
	 */
	MiddleCategory dtoToEntity(MiddleCategoryDTO middleCategoryDTO);
	
	/**
	 * MiddleCategoryDTO 리스트 가져오기
	 * @return
	 */
	List<MiddleCategoryDTO> getDTOList();
	

}
