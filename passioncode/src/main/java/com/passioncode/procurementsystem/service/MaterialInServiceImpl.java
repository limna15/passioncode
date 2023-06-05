package com.passioncode.procurementsystem.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.passioncode.procurementsystem.dto.MaterialInMapperDTO;
import com.passioncode.procurementsystem.mapper.MaterialInMapper;

import io.micrometer.common.lang.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@RequiredArgsConstructor
@Log4j2
public class MaterialInServiceImpl implements MaterailInService {

	@Autowired
	MaterialInMapper mapper;

	@Override
	public List<MaterialInMapperDTO> getList() {
		log.info("Service getList......");
		return mapper.getList();
	}



}
