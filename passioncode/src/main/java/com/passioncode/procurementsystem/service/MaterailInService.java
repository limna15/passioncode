package com.passioncode.procurementsystem.service;

import com.passioncode.procurementsystem.dto.MaterialInDTO;
import com.passioncode.procurementsystem.entity.MaterialIn;

public interface MaterailInService {

	/**
	 * materialInDTO 이용해 자재입고 엔티티 만들기(materialInDTO -> materialIn)
	 * @param materialInDTO
	 * @return
	 */
	MaterialInDTO materialInToDTO(MaterialIn materialIn);

}
