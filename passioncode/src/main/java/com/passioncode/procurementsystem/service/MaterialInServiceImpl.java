package com.passioncode.procurementsystem.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.passioncode.procurementsystem.dto.MaterialInDTO;
import com.passioncode.procurementsystem.entity.MaterialIn;
import com.passioncode.procurementsystem.repository.DetailPurchaseOrderRepository;
import com.passioncode.procurementsystem.repository.MaterialInRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@RequiredArgsConstructor
@Log4j2
public class MaterialInServiceImpl implements MaterailInService {
	
	private final MaterialInRepository materialInRepository;
	private final DetailPurchaseOrderRepository detailPurchaseOrderRepository;
	
	@Override
	public MaterialInDTO materialInToDTO(MaterialIn materialIn) {
		
		MaterialInDTO materialInDTO = MaterialInDTO.builder().no(materialIn.getDetailPurchaseOrder().getPurchaseOrder().getNo()).build();
		
		return null;
	}
	
}
