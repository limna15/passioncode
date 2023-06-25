package com.passioncode.procurementsystem.service;

import java.util.List;

import com.passioncode.procurementsystem.dto.MaterialOutDTO;

public interface MaterialOutService {

	/**
	 * MaterialOutDTO 리스트 가져오기
	 * @return
	 */
	List<MaterialOutDTO> getDTOList();
}
