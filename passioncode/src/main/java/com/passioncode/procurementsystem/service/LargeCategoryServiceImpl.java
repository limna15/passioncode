package com.passioncode.procurementsystem.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.passioncode.procurementsystem.dto.LargeCategoryDTO;
import com.passioncode.procurementsystem.entity.LargeCategory;
import com.passioncode.procurementsystem.repository.LargeCategoryRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@RequiredArgsConstructor
@Log4j2
public class LargeCategoryServiceImpl implements LargeCategoryService {
	
	private final LargeCategoryRepository largeCategoryRepository;

	@Override
	public LargeCategory getLargeCategory(String code) {
		return largeCategoryRepository.findById(code).get();
	}

	@Override
	public LargeCategoryDTO entityToDTO(LargeCategory largeCategory) {
		LargeCategoryDTO largeCategoryDTO = LargeCategoryDTO.builder().code(largeCategory.getCode()).category(largeCategory.getCategory()).build();		
		return largeCategoryDTO;
	}

	@Override
	public LargeCategory dtoToEntity(LargeCategoryDTO largeCategoryDTO) {
		LargeCategory largeCategory = LargeCategory.builder().code(largeCategoryDTO.getCode()).category(largeCategoryDTO.getCategory()).build();
		return largeCategory;
	}

	@Override
	public List<LargeCategoryDTO> getDTOList() {
		List<LargeCategory> largeCategoryList = largeCategoryRepository.findAll();
		List<LargeCategoryDTO> largeCategoryDTOList = new ArrayList<>();
		for(int i=0;i<largeCategoryList.size();i++) {
			largeCategoryDTOList.add(entityToDTO(largeCategoryList.get(i)));
		}		
		return largeCategoryDTOList;
	}

}
