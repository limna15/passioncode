package com.passioncode.procurementsystem.service;

import org.springframework.stereotype.Service;
import com.passioncode.procurementsystem.dto.MaterialInDTO;
import com.passioncode.procurementsystem.repository.MaterialInRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@RequiredArgsConstructor
@Log4j2
public class MaterialInServiceImpl implements MaterailInService {
	
	private final MaterialInRepository repository;



}
