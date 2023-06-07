package com.passioncode.procurementsystem.service;

import java.util.List;

import com.passioncode.procurementsystem.dto.ContractDTO;
import com.passioncode.procurementsystem.dto.MaterialDTO;
import com.passioncode.procurementsystem.entity.Contract;
import com.passioncode.procurementsystem.entity.Material;

public interface ContractService {
	
	/**
	 * 계약서번호를 이용해서 Contract 계약서 엔티티 가져오기
	 * @param no
	 * @return
	 */
	Contract get(Integer no);
	
	/**
	 * 품목 엔티티를 이용해서 List<ContractDTO>로 만들기(Material -> List<ContractDTO>), 
	 * 품목에 해당하는 계약이 여러개일수도 있어 리스트로 나옴, 
	 * 품목 기준으로 ContractDTO를 뽑는 거라서, 계약상태가 미완료인 것도 나옴
	 * @param material
	 * @return
	 */
	List<ContractDTO> materialEntityToDTO(Material material);
	
	/**
	 * 계약서 엔티티를 이용해서 ContractDTO로 만들기(Contract -> ContractDTO)
	 * 계약서 기준으로 ContractDTO 뽑는 거라서, 계약상태가 완료인 것만 나옴
	 * @param contract
	 * @return
	 */
	ContractDTO contractEntityToDTO(Contract contract);
	
	/**
	 * contractDTO를 이용해서 계약서 엔티티 만들기(contractDTO -> contract)
	 * @param contractDTO
	 * @return
	 */
	Contract dtoToEntity(ContractDTO contractDTO);
	
	/**
	 * ContractDTO 리스트 가져오기 (계약상태 완료,미완료 전부)
	 * @return
	 */
	List<ContractDTO> getDTOList();

}