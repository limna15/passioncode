package com.passioncode.procurementsystem.service;

import com.passioncode.procurementsystem.dto.MaterialDTO;
import com.passioncode.procurementsystem.entity.Material;

public interface MaterialService {
	
	Material get(String code);
	
	MaterialDTO entityToDTO(Material material);
	
	
//	default MaterialDTO entityToDTO(Material material) {
//		//품목코드, 품목명, 대, 중, 규격, 재질, 제작사양, 도면번호, 도면Image, 공용여부, 계약상태
//		MaterialDTO materialDTO =  MaterialDTO.builder().code(material.getCode()).name(material.getName()).size(material.getSize()).quality(material.getQuality())
//									.spec(material.getSpec()).drawingNo(material.getDrawingNo()).drawingFile(material.getDrawingFile()).shareStatus(material.getShareStatus())
//									.largeCategoryName(material.getMiddleCategory().getLargeCategory().getCategory()).middleCategoryName(material.getMiddleCategory().getCategory())
//									.contractStatus(null)
//	}

}
