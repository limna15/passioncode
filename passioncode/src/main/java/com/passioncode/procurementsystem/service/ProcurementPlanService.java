package com.passioncode.procurementsystem.service;

import java.util.List;

import com.passioncode.procurementsystem.dto.ContractDTO;
import com.passioncode.procurementsystem.dto.ProcurementPlanDTO;
import com.passioncode.procurementsystem.entity.Contract;
import com.passioncode.procurementsystem.entity.MRP;
import com.passioncode.procurementsystem.entity.ProcurementPlan;

public interface ProcurementPlanService {
	
	/**
	 * 조달계획코드를 이용해서 ProcurementPlan 조달계획 엔티티 가져오기
	 * @param code
	 * @return
	 */
	ProcurementPlan getProcurementPlan(Integer code);
	
	/**
	 * 자재소요계획코드를 이용해서 MRP 엔티티 가져오기 
	 * @param code
	 * @return
	 */
	MRP getMRP(Integer code);	
	
	/**
	 * 계약서번호를 이용해서 Contract 엔티티 가져오기
	 * @param no
	 * @return
	 */
	Contract getContract(Integer no);
		
	/**
	 * MRP 엔티티를 이용해서 ProcurementPlanDTO로 만들기(ProcurementPlan -> ProcurementPlanDTO) <br> 
	 * MRP와 조달계획이 일대일 관계라서 한개만 나옴 <br>
	 * MRP 기준으로 ProcurementPlanDTO를 뽑는 거라서, 조달계획 등록상태가 미완료인 것도 나옴
	 * @param mrp
	 * @return
	 */
	ProcurementPlanDTO mrpEntityToDTO(MRP mrp);
	
	/**
	 * 조달계획 엔티티를 이용해서 ProcurementPlanDTO로 만들기(ProcurementPlan -> ProcurementPlanDTO) <br>
	 * 조달계획 기준으로 ProcurementPlanDTO를 뽑는 거라서, 조달계획 등록상태가 완료인 것만 나옴
	 * @param procurementPlan
	 * @return
	 */
	ProcurementPlanDTO ppEntityToDTO(ProcurementPlan procurementPlan);
	
	/**
	 * procurementPlanDTO를 이용해서 조달계획 엔티티 만들기(procurementPlanDTO -> procurementPlan)
	 * @param procurementPlanDTO
	 * @return
	 */
	ProcurementPlan dtoToEntity(ProcurementPlanDTO procurementPlanDTO);
	
	/**
	 * ProcurementPlanDTO 리스트 가져오기 (조달계획 등록상태 완료,미완료 전부)
	 * @return
	 */
	List<ProcurementPlanDTO> getDTOList();
		
	/**
	 * 조달계획 등록 (ProcurementPlanDTO 이용해서)
	 * @param procurementPlanDTO
	 * @return
	 */
	Integer register(ProcurementPlanDTO procurementPlanDTO);
	
	/**
	 * 조달계획 수정 (ProcurementPlanDTO 이용해서)
	 * @param procurementPlanDTO
	 */
	void modify(ProcurementPlanDTO procurementPlanDTO);
	
	/**
	 * 조달계획 삭제 (ProcurementPlanDTO 이용해서)
	 * @param procurementPlanDTO
	 */
	void delete(ProcurementPlanDTO procurementPlanDTO);
	

}
