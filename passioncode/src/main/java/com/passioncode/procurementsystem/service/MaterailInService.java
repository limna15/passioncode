package com.passioncode.procurementsystem.service;

import com.passioncode.procurementsystem.dto.MaterialInDTO;
import com.passioncode.procurementsystem.entity.MaterialIn;

public interface MaterailInService {

	/**
	 * 자재입고 엔티티를 이용해 materialInDTO 만들기(materialIn -> materialInDTO)
	 * @param materialIn
	 * @return
	 */
	MaterialInDTO materialInToDTO(MaterialIn materialIn);
	
	/**
	 * materialInDTO 만들기(materialIn -> materialInDTO)
	 * @param materialIn
	 * @return
	 */
	MaterialIn DTOToEntity(MaterialInDTO materialInDTO);

}
