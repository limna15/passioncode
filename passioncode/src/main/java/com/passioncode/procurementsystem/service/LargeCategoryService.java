package com.passioncode.procurementsystem.service;

import java.util.List;

import com.passioncode.procurementsystem.dto.LargeCategoryDTO;
import com.passioncode.procurementsystem.entity.LargeCategory;

public interface LargeCategoryService {
	
	/**
	 * 대분류코드를 이용해서 LargeCategory 대분류 엔티티 가져오기
	 * @param code
	 * @return
	 */
	LargeCategory getLargeCategory(String code);
	
	/**
	 * 대분류 엔티티를 이용해서 LargeCategoryDTO로 만들기(LargeCategory -> LargeCategoryDTO) 
	 * @param largeCategory
	 * @return
	 */
	LargeCategoryDTO entityToDTO(LargeCategory largeCategory);
	
	/**
	 * LargeCategoryDTO를 이용해서 품목 엔티티 만들기(LargeCategoryDTO -> LargeCategory)
	 * @param largeCategoryDTO
	 * @return
	 */
	LargeCategory dtoToEntity(LargeCategoryDTO largeCategoryDTO);
	
	/**
	 * LargeCategoryDTO 리스트 가져오기
	 * @return
	 */
	List<LargeCategoryDTO> getDTOList();
	
}
