package com.passioncode.procurementsystem.service;

import java.util.List;

import com.passioncode.procurementsystem.dto.MaterialInDTO;
import com.passioncode.procurementsystem.entity.DetailPurchaseOrder;
import com.passioncode.procurementsystem.entity.Material;
import com.passioncode.procurementsystem.entity.MaterialIn;

public interface MateriallInService {

	/**
	 * 세부구매발주서 엔티티를 이용해 materialInDTO 만들기(detailPurchaseOrder -> materialInDTO)
	 * @param detailPurchaseOrder
	 * @return
	 */
	MaterialInDTO materialInToDTO(DetailPurchaseOrder detailPurchaseOrder);
	//MaterialInDTO materialInToDTO(MaterialIn materialIn);
	
	/**
	 * materialInDTO를 이용해 자재입고 엔티티 만들기(materialInDTO -> materialIn)
	 * @param materialDTO
	 * @return
	 */
	MaterialIn DTOToEntity(MaterialInDTO materialInDTO);

	/**
	 * 입고코드를 이용해서 자재입고 엔티티 가져오기
	 * @param no
	 * @return
	 */
	MaterialIn get(Integer no);
	
	/**
	 * MaterialInDTO 리스트 가져오기
	 * @return
	 */
	List<MaterialInDTO> getMaterialInDTOLsit();
	
	/**
	 * MaterialIn 리스트 가져오기
	 * @return
	 */
	List<MaterialIn> getMaterialInLsit();
	
	/**
	 * MaterialInDTO를 이용해서 자재입고 등록
	 * @param MaterialInDTO
	 */
	Integer register(MaterialInDTO materialInDTO);

	
}
