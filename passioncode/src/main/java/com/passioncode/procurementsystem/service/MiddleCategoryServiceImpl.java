package com.passioncode.procurementsystem.service;

import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;
import com.passioncode.procurementsystem.dto.MiddleCategoryDTO;
import com.passioncode.procurementsystem.entity.LargeCategory;
import com.passioncode.procurementsystem.entity.MiddleCategory;
import com.passioncode.procurementsystem.repository.LargeCategoryRepository;
import com.passioncode.procurementsystem.repository.MiddleCategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@RequiredArgsConstructor
@Log4j2
public class MiddleCategoryServiceImpl implements MiddleCategoryService {
	
	private final LargeCategoryRepository largeCategoryRepository;
	private final MiddleCategoryRepository middleCategoryRepository;

	@Override
	public MiddleCategory getMiddleCategory(String code) {
		return middleCategoryRepository.findById(code).get();
	}

	@Override
	public List<MiddleCategory> getMiddleCategoryByLargeCategory(LargeCategory largeCategory) {		
		return middleCategoryRepository.findByLargeCategory(largeCategory);
	}
	
	@Override
	public LargeCategory getLargeCategory(String code) {
		return largeCategoryRepository.findById(code).get();
	}

	@Override
	public MiddleCategoryDTO entityToDTO(MiddleCategory middleCategory) {
		MiddleCategoryDTO middleCategoryDTO = MiddleCategoryDTO.builder().middleCode(middleCategory.getCode()).middleCategory(middleCategory.getCategory())
																		.largeCode(middleCategory.getLargeCategory().getCode())
																		.largeCategory(middleCategory.getLargeCategory().getCategory()).build();
		return middleCategoryDTO;
	}

	@Override
	public MiddleCategory dtoToEntity(MiddleCategoryDTO middleCategoryDTO) {
		MiddleCategory middleCategory = MiddleCategory.builder().code(middleCategoryDTO.getMiddleCode()).category(middleCategoryDTO.getMiddleCategory())
																.largeCategory(largeCategoryRepository.findById(middleCategoryDTO.getLargeCode()).get()).build();
		return middleCategory;
	}

	@Override
	public List<MiddleCategoryDTO> getDTOList() {
		List<MiddleCategory> middleCategoryList = middleCategoryRepository.findAll();
		List<MiddleCategoryDTO> middleCategoryDTOList = new ArrayList<>();
		for(int i=0;i<middleCategoryList.size();i++) {
			middleCategoryDTOList.add(entityToDTO(middleCategoryList.get(i)));
		}		
		return middleCategoryDTOList;
	}

	

}
