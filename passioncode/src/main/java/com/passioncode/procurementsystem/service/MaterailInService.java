package com.passioncode.procurementsystem.service;

import java.util.List;

import com.passioncode.procurementsystem.dto.MaterialInMapperDTO;

public interface MaterailInService {
	
	//1. 입고 리스트 보여주기
	List<MaterialInMapperDTO> getList();

}
