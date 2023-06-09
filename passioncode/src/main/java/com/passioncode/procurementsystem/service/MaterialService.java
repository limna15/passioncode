package com.passioncode.procurementsystem.service;

import java.util.List;

import com.passioncode.procurementsystem.dto.MaterialDTO;
import com.passioncode.procurementsystem.entity.Material;

public interface MaterialService {
	
	/**
	 * 품목코드를 이용해서 Material 품목 엔티티 가져오기
	 * @param code
	 * @return
	 */
	Material getMaterial(String code);
	
	/**
	 * 품목 엔티티를 이용해서 MaterialDTO로 만들기(Material -> MaterialDTO)
	 * @param material
	 * @return
	 */
	MaterialDTO entityToDTO(Material material);
	
	/**
	 * MaterialDTO를 이용해서 품목 엔티티 만들기(MaterialDTO -> Material)
	 * @param materialDTO
	 * @return
	 */
	Material dtoToEntity(MaterialDTO materialDTO);
	
	/**
	 * MaterialDTO 리스트 가져오기
	 * @return
	 */
	List<MaterialDTO> getDTOList();
	
	/**
	 * 품목 엔티티를 이용하여 계약상태 체크하기 <br> 
	 * True : 계약상태 O, False : 계약상태 X
	 * @param material
	 * @return
	 */
	Boolean contractStatusCheck(Material material);
	
	/**
	 * 품목정보 등록 (MaterialDTO 이용해서)
	 * @param materialDTO
	 * @return
	 */
	String register(MaterialDTO materialDTO);
	
	/**
	 * 품목정보 수정 (MaterialDTO 이용해서)
	 * @param materialDTO
	 */
	void modify(MaterialDTO materialDTO);
	
	/**
	 * 품목정보 삭제 (MaterialDTO 이용해서)
	 * @param materialDTO
	 */
	void delete(MaterialDTO materialDTO);
	
	
	
	

}
