package com.passioncode.procurementsystem.service;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.passioncode.procurementsystem.dto.MaterialInDTO;
import com.passioncode.procurementsystem.entity.DetailPurchaseOrder;
import com.passioncode.procurementsystem.entity.MaterialIn;

public interface MaterialInService {

	/**
	 * 세부구매발주서 엔티티를 이용해 materialInDTO 만들기(detailPurchaseOrder -> materialInDTO)
	 * @param detailPurchaseOrder
	 * @return
	 */
	MaterialInDTO materialInToDTO(DetailPurchaseOrder detailPurchaseOrder);
	
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
	
	/**
	 * 코드번호(detailPurchaseOrder)를 이용해 자재입고 테이블이 생길때 조달계획의 조달계획 완료일 업데이트하기
	 * @param code
	 */
	@Transactional
	void updatePPCompletionDate(Integer code);
	
	/**
	 * 세부구매발주서 코드를 이용해서 MaterialIn 엔티티 가져오기
	 * @param code
	 * @return
	 */
	MaterialIn getMeterialInByDetailPurchaseOrder(Integer code);
	
	/**
	 * MaterialInDTO 리스트 가져오기(정렬)
	 * @return
	 */
	List<MaterialInDTO> getSortDTOLsit();
}
