package com.passioncode.procurementsystem.service;

import java.util.List;
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
	 * mrp와 조인한 조달계획에서 품목코드에 해당하는 조달계획 리스트 가져오기 <br>
	 * 한번이라도 조달완료, 발주가 들어간 품목은 수정 불가능하게 하기위한, 필요한 리스트 
	 * @param materialCode
	 * @return
	 */
	List<ProcurementPlan> getPPJoinMRPByMaterialCode(String materialCode);
		
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
	
	/**
	 * 소요일, 기본여유기간, 품목공급LT -> 조달납기예정일, 최소발주일 <br>
	 * 조달계획 등록 화면에서, 조달납기예정일, 최소발주일을 계산해주기 <br>
	 * @param procurementPlanDTO
	 * @return
	 */
	ProcurementPlanDTO getProcurementPlanCalculate(ProcurementPlanDTO procurementPlanDTO);
	
	/**
	 * 세부구매발주서 코드를 이용해서 ProcurementPlan 조달계획 엔티티 가져오기
	 * @param code
	 * @return
	 */
	ProcurementPlan getPpByDetailPurchaseOrder(Integer code);
	
	/**
	 * 계약서번호를 이용하여, 조달계획이 존재하는지 체크하기 <br>
	 * 계약서 수정 화면 진입을 위한, contractInPPCheck <br>
	 * 조달계획 존재 O -> 계약 수정 불가능 <br>
	 * 조달계획 존재 X -> 계약 수정 가능
	 * @param contractNo
	 * @return
	 */
	Boolean ppExistsByContract(Integer contractNo);

}
