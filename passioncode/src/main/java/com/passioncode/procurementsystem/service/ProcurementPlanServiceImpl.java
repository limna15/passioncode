package com.passioncode.procurementsystem.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.passioncode.procurementsystem.dto.ContractDTO;
import com.passioncode.procurementsystem.dto.ProcurementPlanDTO;
import com.passioncode.procurementsystem.entity.Contract;
import com.passioncode.procurementsystem.entity.MRP;
import com.passioncode.procurementsystem.entity.Material;
import com.passioncode.procurementsystem.entity.ProcurementPlan;
import com.passioncode.procurementsystem.repository.ContractRepository;
import com.passioncode.procurementsystem.repository.MRPRepository;
import com.passioncode.procurementsystem.repository.ProcurementPlanRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@RequiredArgsConstructor
@Log4j2
public class ProcurementPlanServiceImpl implements ProcurementPlanService {
	
	private final ProcurementPlanRepository procurementPlanRepository;
	private final MRPRepository mrpRepository;
	private final ContractRepository contractRepository;
	
	
	@Override
	public ProcurementPlan getProcurementPlan(Integer code) {
		return procurementPlanRepository.findById(code).get();
	}

	@Override
	public MRP getMRP(Integer code) {
		return mrpRepository.findById(code).get();
	}
	
	public String ppProgressCheck(ProcurementPlan procurementPlan) {
		String ppProgress = null;
		
		if(procurementPlan.getCompletionDate()==null) {				//조달완료일 존재X
			if(procurementPlan.getDetailPurchaseOrder()==null) {	//발주코드 존재X
				ppProgress="발주 예정";
			}else {													//발주코드 존재O
				ppProgress="조달 진행 중";
			}
		}else {														//조달완료일 존재O
			ppProgress="조달 완료";
		}
		return ppProgress;
	}

	@Override
	public ProcurementPlanDTO mrpEntityToDTO(MRP mrp) {
		//품목코드, 품목명, 소요공정, 소요일, 소요량 
		//협력회사, 품목공급LT
		//조달납기예정일, 최소발주일, 필요수량, 계약상태, 조달계획 등록상태, 조달계획 진행사항
		
		//조달계획코드, 자재소요계획코드(외래키), 계약서번호(외래키),
		//필요수량, 조달납기예정일, 최소발주일, 조달계획 등록일, 조달계획 완료일, 발주코드(외래키)	
		
		ProcurementPlan procurementPlan = procurementPlanRepository.findByMrp(mrp);		
		ProcurementPlanDTO procurementPlanDTO = null;
		
		//procurementPlan2 가 존재할때, 조달계획 등록 완료된 상태
		if(procurementPlan!=null) {
			procurementPlanDTO = ProcurementPlanDTO.builder().materialCode(procurementPlan.getMrp().getMaterial().getCode()).ppcode(procurementPlan.getCode())
								.materialName(procurementPlan.getMrp().getMaterial().getName()).process(procurementPlan.getMrp().getProcess())
								.mrpdate(procurementPlan.getMrp().getDate()).mrpAmount(procurementPlan.getMrp().getAmount())
								.companyName(procurementPlan.getContract().getCompany().getName()).supplyLt(procurementPlan.getContract().getSupplyLt())
								.dueDate(procurementPlan.getDueDate()).minimumOrderDate(procurementPlan.getMinimumOrderDate())
								.ppAmount(procurementPlan.getAmount()).contractStatus(contractRepository.existsByMaterial(procurementPlan.getMrp().getMaterial()))
								.ppRegisterStatus(true).ppProgress(ppProgressCheck(procurementPlan))
								.mrpCode(procurementPlan.getMrp().getCode()).companyNo(procurementPlan.getContract().getCompany().getNo())
								.contractNo(procurementPlan.getContract().getNo()).build();
		}else {  ////procurementPlan2 가 존재X, 조달계획 등록 미완료된 상태
			procurementPlanDTO =  ProcurementPlanDTO.builder().materialCode(mrp.getMaterial().getCode()).materialName(mrp.getMaterial().getName())
									.process(mrp.getProcess()).mrpdate(mrp.getDate()).mrpAmount(mrp.getAmount())
									.contractStatus(contractRepository.existsByMaterial(mrp.getMaterial())).ppRegisterStatus(false)
									.mrpCode(mrp.getCode()).build();
		}		
		return procurementPlanDTO;
	}

	@Override
	public ProcurementPlanDTO ppEntityToDTO(ProcurementPlan procurementPlan) {
		ProcurementPlanDTO procurementPlanDTO =ProcurementPlanDTO.builder().materialCode(procurementPlan.getMrp().getMaterial().getCode()).ppcode(procurementPlan.getCode())
												.materialName(procurementPlan.getMrp().getMaterial().getName()).process(procurementPlan.getMrp().getProcess())
												.mrpdate(procurementPlan.getMrp().getDate()).mrpAmount(procurementPlan.getMrp().getAmount())
												.companyName(procurementPlan.getContract().getCompany().getName()).supplyLt(procurementPlan.getContract().getSupplyLt())
												.dueDate(procurementPlan.getDueDate()).minimumOrderDate(procurementPlan.getMinimumOrderDate())
												.ppAmount(procurementPlan.getAmount()).contractStatus(contractRepository.existsByMaterial(procurementPlan.getMrp().getMaterial()))
												.ppRegisterStatus(true).ppProgress(ppProgressCheck(procurementPlan))
												.mrpCode(procurementPlan.getMrp().getCode()).companyNo(procurementPlan.getContract().getCompany().getNo())
												.contractNo(procurementPlan.getContract().getNo()).build();
		return procurementPlanDTO;
	}

	@Override
	public ProcurementPlan dtoToEntity(ProcurementPlanDTO procurementPlanDTO) {
		//품목코드, 품목명, 소요공정, 소요일, 소요량 
		//협력회사, 품목공급LT
		//조달납기예정일, 최소발주일, 필요수량, 계약상태, 조달계획 등록상태, 조달계획 진행사항
		
		//조달계획코드, 자재소요계획코드(외래키), 계약서번호(외래키),
		//필요수량, 조달납기예정일, 최소발주일, 조달계획 등록일, 조달계획 완료일, 발주코드(외래키)	
		
		ProcurementPlan procurementPlan = ProcurementPlan.builder().code(procurementPlanDTO.getPpcode()).mrp(mrpRepository.findById(procurementPlanDTO.getMrpCode()).get())
											.contract(contractRepository.findById(procurementPlanDTO.getContractNo()).get()).amount(procurementPlanDTO.getPpAmount())
											.dueDate(procurementPlanDTO.getDueDate()).minimumOrderDate(procurementPlanDTO.getMinimumOrderDate())
											.registerDate(procurementPlanRepository.findById(procurementPlanDTO.getPpcode()).get().getRegisterDate())
											.completionDate(procurementPlanRepository.findById(procurementPlanDTO.getPpcode()).get().getCompletionDate())
											.detailPurchaseOrder(procurementPlanRepository.findById(procurementPlanDTO.getPpcode()).get().getDetailPurchaseOrder()).build();		
		return procurementPlan;
	}

	@Override
	public List<ProcurementPlanDTO> getDTOList() {
		List<MRP> mrpList = mrpRepository.findAll();
		
		List<ProcurementPlanDTO> ppDTOList = new ArrayList<>();
		ProcurementPlanDTO procurementPlanDTO = null;		
		
		for(int i=0;i<mrpList.size();i++) {
			ProcurementPlan procurementPlan = procurementPlanRepository.findByMrp(mrpList.get(i));	
			
			//procurementPlan 가 존재할때, 조달계획 등록 완료된 상태
			if(procurementPlan!=null) {
				procurementPlanDTO = ProcurementPlanDTO.builder().materialCode(procurementPlan.getMrp().getMaterial().getCode()).ppcode(procurementPlan.getCode())
										.materialName(procurementPlan.getMrp().getMaterial().getName()).process(procurementPlan.getMrp().getProcess())
										.mrpdate(procurementPlan.getMrp().getDate()).mrpAmount(procurementPlan.getMrp().getAmount())
										.companyName(procurementPlan.getContract().getCompany().getName()).supplyLt(procurementPlan.getContract().getSupplyLt())
										.dueDate(procurementPlan.getDueDate()).minimumOrderDate(procurementPlan.getMinimumOrderDate())
										.ppAmount(procurementPlan.getAmount()).contractStatus(contractRepository.existsByMaterial(procurementPlan.getMrp().getMaterial()))
										.ppRegisterStatus(true).ppProgress(ppProgressCheck(procurementPlan))
										.mrpCode(procurementPlan.getMrp().getCode()).companyNo(procurementPlan.getContract().getCompany().getNo())
										.contractNo(procurementPlan.getContract().getNo()).build();
				ppDTOList.add(procurementPlanDTO);
			}else {  ////procurementPlan 가 존재X, 조달계획 등록 미완료된 상태
				procurementPlanDTO = ProcurementPlanDTO.builder().materialCode(mrpList.get(i).getMaterial().getCode()).materialName(mrpList.get(i).getMaterial().getName())
										.process(mrpList.get(i).getProcess()).mrpdate(mrpList.get(i).getDate()).mrpAmount(mrpList.get(i).getAmount())
										.contractStatus(contractRepository.existsByMaterial(mrpList.get(i).getMaterial())).ppRegisterStatus(false)
										.mrpCode(mrpList.get(i).getCode()).build();	
				ppDTOList.add(procurementPlanDTO);
			}			
		}	
		
		return ppDTOList;
	}

	

	
	
	
}
