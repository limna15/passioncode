package com.passioncode.procurementsystem.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.stereotype.Service;
import com.passioncode.procurementsystem.dto.MaterialDTO;
import com.passioncode.procurementsystem.entity.Material;
import com.passioncode.procurementsystem.entity.MiddleCategory;
import com.passioncode.procurementsystem.repository.ContractRepository;
import com.passioncode.procurementsystem.repository.MaterialRepository;
import com.passioncode.procurementsystem.repository.MiddleCategoryRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@RequiredArgsConstructor
@Log4j2
public class MaterialServiceImpl implements MaterialService {
	
	private final ContractRepository contractRepository;	
	private final MaterialRepository materialRepository;
	private final MiddleCategoryRepository middleCategoryRepository;
	
	
	@Override
	public Material get(String code) {		
		return materialRepository.findById(code).get();
	}

	@Override
	public MaterialDTO entityToDTO(Material material) {
		//품목코드, 품목명, 대, 중, 규격, 재질, 제작사양, 도면번호, 도면Image, 공용여부, 계약상태
		MaterialDTO materialDTO =  MaterialDTO.builder().code(material.getCode()).name(material.getName()).size(material.getSize()).quality(material.getQuality())
									.spec(material.getSpec()).drawingNo(material.getDrawingNo()).drawingFile(material.getDrawingFile()).shareStatus(material.getShareStatus())
									.largeCategoryName(material.getMiddleCategory().getLargeCategory().getCategory()).middleCategoryName(material.getMiddleCategory().getCategory())
									.contractStatus(contractRepository.existsByMaterial(material)).build();									
		return materialDTO;
	}

	@Override
	public Material dtoToEntity(MaterialDTO materialDTO) {
		Collection<MiddleCategory> middleCategoryResult1 = middleCategoryRepository.findByCategory(materialDTO.getMiddleCategoryName());
		ArrayList<MiddleCategory> middleCategoryResult2 = (ArrayList<MiddleCategory>) middleCategoryResult1;
		MiddleCategory middleCategory = middleCategoryResult2.get(0);
		log.info("이거 찾아온 리스트 목록좀 보자 : "+middleCategoryResult2);
		log.info("리스트 사이즈도 봐보자 : "+middleCategoryResult2.size());
		
		//품목코드, 품목명, 공용여부, 규격, 재질, 제작사양, 도면번호, 도면, 중분류
		Material material = Material.builder().code(materialDTO.getCode()).name(materialDTO.getName()).shareStatus(materialDTO.getShareStatus()).size(materialDTO.getSize())
							.quality(materialDTO.getQuality()).spec(materialDTO.getSpec()).drawingNo(materialDTO.getDrawingNo()).drawingFile(materialDTO.getDrawingFile())
							.middleCategory(middleCategory).build();
		return material;
	}

	@Override
	public List<MaterialDTO> getDTOList() {
		
		return null;
	}


}
