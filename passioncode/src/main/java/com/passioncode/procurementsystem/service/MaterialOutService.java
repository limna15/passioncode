package com.passioncode.procurementsystem.service;

import java.util.List;

import com.passioncode.procurementsystem.dto.MaterialOutDTO;
import com.passioncode.procurementsystem.entity.MRP;
import com.passioncode.procurementsystem.entity.MaterialOut;

public interface MaterialOutService {
	
	/**
	 * mrp 엔티티를 이용해 materialOutDTO 만들기(mrp -> materialOutDTO)
	 * @param mrp
	 */
	MaterialOutDTO materialOutToDTO(MRP mrp);
	
	/**
	 * materialOutDTO를 이용해 자재출고 엔티티 만들기(materialOutDTO -> materialOut)
	 * @param materialOutDTO
	 */
	MaterialOut DTOtoEntity(MaterialOutDTO materialOutDTO);

	/**
	 * MaterialOutDTO 리스트 가져오기
	 * @return
	 */
	List<MaterialOutDTO> getDTOList();
	
	/**
	 * MaterialOutDTO를 이용해서 자재입고 등록
	 * @param MaterialOutDTO
	 */
	Integer register(MaterialOutDTO materialOutDTO);
}
