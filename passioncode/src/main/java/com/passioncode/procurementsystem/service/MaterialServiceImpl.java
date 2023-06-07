package com.passioncode.procurementsystem.service;

import org.springframework.stereotype.Service;
import com.passioncode.procurementsystem.dto.MaterialDTO;
import com.passioncode.procurementsystem.entity.Material;
import com.passioncode.procurementsystem.repository.ContractRepository;
import com.passioncode.procurementsystem.repository.MaterialRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@RequiredArgsConstructor

@Log4j2
public class MaterialServiceImpl implements MaterialService {
	
	ContractRepository contractRepository;	
	MaterialRepository materialRepository;
	
	
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


}
