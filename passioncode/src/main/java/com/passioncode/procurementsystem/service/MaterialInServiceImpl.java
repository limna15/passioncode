package com.passioncode.procurementsystem.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.passioncode.procurementsystem.dto.MaterialInDTO;
import com.passioncode.procurementsystem.dto.MaterialOutDTO;
import com.passioncode.procurementsystem.entity.DetailPurchaseOrder;
import com.passioncode.procurementsystem.entity.MaterialIn;
import com.passioncode.procurementsystem.entity.MaterialOut;
import com.passioncode.procurementsystem.entity.ProcurementPlan;
import com.passioncode.procurementsystem.repository.DetailPurchaseOrderRepository;
import com.passioncode.procurementsystem.repository.MRPRepository;
import com.passioncode.procurementsystem.repository.MaterialInRepository;
import com.passioncode.procurementsystem.repository.MaterialOutRepository;
import com.passioncode.procurementsystem.repository.ProcurementPlanRepository;
import com.passioncode.procurementsystem.repository.TransactionDetailRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@RequiredArgsConstructor
@Log4j2
public class MaterialInServiceImpl implements MaterialInService {
	
	private final MaterialInRepository materialInRepository;
	private final DetailPurchaseOrderRepository detailPurchaseOrderRepository;
	private final ProcurementPlanRepository procurementPlanRepository;
	private final TransactionDetailRepository transactionDetailRepository;
	private final MaterialOutRepository materialOutRepository;
	private final MRPRepository mrpRepository;
	
	//발주코드에 문자를 넣어서 보내기
	public String codeStr(Integer num1) {
		String pNum = String.format("%05d", num1);
		pNum = "DPO"+pNum;
		
		return pNum;
	}
	
	//발주서번호에 문자를 넣어서 보내기
	public String noStr(Integer num1) {
		String pNum = String.format("%08d", num1);
		pNum = "PO"+pNum;
		
		return pNum;
	}
	
	@Override
	public MaterialInDTO materialInToDTO(DetailPurchaseOrder detailPurchaseOrder) {
		ProcurementPlan pp= procurementPlanRepository.findByDetailPurchaseOrder(detailPurchaseOrder);		
		MaterialIn mi= materialInRepository.findByDetailPurchaseOrder(detailPurchaseOrder);

		MaterialInDTO materialInDTO= MaterialInDTO.builder().no(detailPurchaseOrder.getPurchaseOrder().getNo()).code(detailPurchaseOrder.getCode())
									.dueDate(pp.getDueDate()).materialCode(pp.getMrp().getMaterial().getCode())
									.materialName(pp.getMrp().getMaterial().getName())
									.amount(pp.getDetailPurchaseOrder().getAmount()).status(mi.getStatus()).transactionStatus(null)
									.inDate(mi.getDate()).build();
		return materialInDTO;
	}


	@Override
	public MaterialIn DTOToEntity(MaterialInDTO materialInDTO) {
		MaterialIn materialIn= MaterialIn.builder().status(materialInDTO.getStatus()).date(LocalDateTime.now()).transactionStatus(materialInDTO.getTransactionStatus())
							.detailPurchaseOrder(detailPurchaseOrderRepository.findById(materialInDTO.getCode()).get()).build();
		return materialIn;
	}

	@Override
	public MaterialIn get(Integer no) {
		return materialInRepository.findById(no).get();
	}


	@Override
	public List<MaterialInDTO> getMaterialInDTOLsit() {
		
		List<DetailPurchaseOrder> dpoList= detailPurchaseOrderRepository.findAll();
		
		List<MaterialInDTO> materialInDTOList= new ArrayList<>();
		MaterialInDTO materialInDTO= null;
		List<ProcurementPlan> ppList= new ArrayList<>();
		List<MaterialIn>miList= new ArrayList<>();
		
		for(int i=0; i<dpoList.size(); i++) {
			ppList.add(procurementPlanRepository.findByDetailPurchaseOrder(dpoList.get(i)));
	
			if(materialInRepository.findByDetailPurchaseOrder(dpoList.get(i)) != null) {
				miList.add(materialInRepository.findByDetailPurchaseOrder(dpoList.get(i)));
			}
		}
		
		//입고상태가 취소가 되면 출고 테이블에 출고 상태가 0으로 등록되어야함
		MaterialOutDTO materialOutDTO= null;
		
		for(int i=0; i<ppList.size(); i++) {
			if(!materialInRepository.existsById(i+1)) { //materialIn에 존재 X 때 설정
				materialInDTO=  MaterialInDTO.builder().noStr(noStr(dpoList.get(i).getPurchaseOrder().getNo())).codeStr(codeStr(dpoList.get(i).getCode()))
						.dueDate(ppList.get(i).getDueDate()).materialCode(ppList.get(i).getMrp().getMaterial().getCode())
						.materialName(ppList.get(i).getMrp().getMaterial().getName()).amount(ppList.get(i).getDetailPurchaseOrder().getAmount())
						.status(null).transactionStatus(null).inDate(null).build();
				materialInDTOList.add(materialInDTO);
				//log.info(i + "번 materialIn에 존재 X miDTO 보기 >>> " + materialInDTO);
				}else { //materialIn에 존재할 때 설정
					if(materialInRepository.existsByDetailPurchaseOrder(dpoList.get(i))){ //입고상태가 null이 아닐 때
						if(transactionDetailRepository.existsByPurchaseOrder(dpoList.get(i).getPurchaseOrder())) { //입고상태 완료 + 발행상태 완료
							materialInDTO= MaterialInDTO.builder().noStr(noStr(dpoList.get(i).getPurchaseOrder().getNo())).codeStr(codeStr(dpoList.get(i).getCode()))
									.dueDate(ppList.get(i).getDueDate()).materialCode(ppList.get(i).getMrp().getMaterial().getCode())
									.materialName(ppList.get(i).getMrp().getMaterial().getName()).amount(ppList.get(i).getDetailPurchaseOrder().getAmount())
									.status(miList.get(i).getStatus()).transactionStatus("발행 완료")
									.inDate(miList.get(i).getDate()).build();
							materialInDTOList.add(materialInDTO);
							//log.info(i + "번 입고상태+발행상태 완료 miDTO 보기 >>> " + materialInDTO);
						}else { //발행상태 미완료
							if(miList.get(i).getStatus()) { //입고상태 완료 + 발행상태 미완료
								materialInDTO= MaterialInDTO.builder().noStr(noStr(dpoList.get(i).getPurchaseOrder().getNo())).codeStr(codeStr(dpoList.get(i).getCode()))
										.dueDate(ppList.get(i).getDueDate()).materialCode(ppList.get(i).getMrp().getMaterial().getCode())
										.materialName(ppList.get(i).getMrp().getMaterial().getName()).amount(ppList.get(i).getDetailPurchaseOrder().getAmount())
										.status(miList.get(i).getStatus()).transactionStatus("발행 예정")
										.inDate(miList.get(i).getDate()).build();
								materialInDTOList.add(materialInDTO);
								//log.info(i + "번 입고 완료 + 발행 미완료 miDTO 보기 >>> " + materialInDTO);
							
							}else { //입고상태 취소 + 발행상태 "발행 불가" -> 출고 테이블에 취소 등록
								materialInDTO=  MaterialInDTO.builder().noStr(noStr(dpoList.get(i).getPurchaseOrder().getNo())).codeStr(codeStr(dpoList.get(i).getCode()))
										.dueDate(ppList.get(i).getDueDate()).materialCode(ppList.get(i).getMrp().getMaterial().getCode())
										.materialName(ppList.get(i).getMrp().getMaterial().getName()).amount(ppList.get(i).getDetailPurchaseOrder().getAmount())
										.status(miList.get(i).getStatus()).transactionStatus("발행 불가").inDate(null).build();
								materialInDTOList.add(materialInDTO);
								//log.info(i + "번 입고상태 취소 miDTO 보기 >>> " + materialInDTO);					
							}
						} //두번째 else 끝(발행상태 미완료)
					} //두번째 if문 끝 (입고상태가 null이 아닐 때)
			} //else 끝(materialIn에 존재할 때) 
		} //for문 끝
		return materialInDTOList;
	}

	public MaterialOut DTOtoEntity(MaterialOutDTO materialOutDTO) {
		MaterialOut materialOut= MaterialOut.builder().status(1).mrp(mrpRepository.findById(materialOutDTO.getMrpCode()).get()).build();		
		return materialOut;
	}

	@Override
	public Integer register(MaterialInDTO materialInDTO) {
		MaterialIn materialIn= DTOToEntity(materialInDTO);
		materialInRepository.save(materialIn);
		
		return materialIn.getCode();
	}


	@Override
	public List<MaterialIn> getMaterialInLsit() {
		List<MaterialIn> materialInList= materialInRepository.findAll();
		return materialInList;
	}

	@Override
	public void updatePPCompletionDate(Integer code) {
		DetailPurchaseOrder detailPurchaseOrder= detailPurchaseOrderRepository.findById(code).get();
		ProcurementPlan procurementPlan= procurementPlanRepository.findByDetailPurchaseOrder(detailPurchaseOrder);
		MaterialIn materialIn= materialInRepository.findByDetailPurchaseOrder(detailPurchaseOrder);
		
		//log.info("materialIn 어떻게 읽히나 >>>  " + materialIn);

		procurementPlan= ProcurementPlan.builder().code(procurementPlan.getCode())
				.mrp(procurementPlan.getMrp()).contract(procurementPlan.getContract()).amount(procurementPlan.getAmount())
				.dueDate(procurementPlan.getDueDate()).minimumOrderDate(procurementPlan.getMinimumOrderDate())
				.registerDate(procurementPlan.getRegisterDate()).completionDate(materialIn.getDate())
				.detailPurchaseOrder(detailPurchaseOrder).build();
		
		procurementPlanRepository.save(procurementPlan);
	}

	@Override
	public MaterialIn getMeterialInByDetailPurchaseOrder(Integer code) {
		DetailPurchaseOrder dpo= detailPurchaseOrderRepository.findById(code).get();
		return	materialInRepository.findByDetailPurchaseOrder(dpo);
	}

	//자재입고 테이블에 등록된 것과 등록안된것(입고상태가 null인것) 구분한 List
	@Override
	public List<MaterialInDTO> getSortDTOLsit() {
		
		List<DetailPurchaseOrder> dpoList= detailPurchaseOrderRepository.findAll();
		
		List<MaterialInDTO> MaterialInDTOList= new ArrayList<>();
		List<MaterialInDTO> nullMaterialInDTOList= new ArrayList<>();
		List<MaterialInDTO> notNullMaterialInDTOList= new ArrayList<>();
		MaterialInDTO materialInDTO= null;
		
		List<ProcurementPlan> ppList= new ArrayList<>();
		List<ProcurementPlan> ppList2= new ArrayList<>();
		
		List<MaterialIn> miList= new ArrayList<>();
		
		for(int i=0; i<dpoList.size(); i++) {
			ppList.add(procurementPlanRepository.findByDetailPurchaseOrder(dpoList.get(i)));
	
			if(materialInRepository.findByDetailPurchaseOrder(dpoList.get(i)) != null) {
				//세부구매발주서와 조인한 자재입고 List 가져오기
				miList= materialInRepository.getJoinDpo();
			}
		}
		
		for(int i=0; i<miList.size(); i++) {
			ppList2.add(procurementPlanRepository.findByDetailPurchaseOrder(miList.get(i).getDetailPurchaseOrder()));
		}	
		
		//정렬된 자재입고List DTO로 바꾸기
		for(int i=0; i<miList.size(); i++) {
			materialInDTO= MaterialInDTO.builder()
					.noStr(noStr(miList.get(i).getDetailPurchaseOrder().getPurchaseOrder().getNo())).codeStr(codeStr(miList.get(i).getDetailPurchaseOrder().getCode()))
					.dueDate(ppList2.get(i).getDueDate())
					.materialCode(ppList2.get(i).getMrp().getMaterial().getCode()).materialName(ppList2.get(i).getMrp().getMaterial().getName())
					.amount(miList.get(i).getDetailPurchaseOrder().getAmount())
					.status(miList.get(i).getStatus()).transactionStatus(miList.get(i).getTransactionStatus()).build();
					
			notNullMaterialInDTOList.add(materialInDTO);	
		}	
		
		for(int i=0; i<ppList.size(); i++) {
			//materialIn에 존재 X 때 설정
			if(!materialInRepository.existsById(i+1)) {
				materialInDTO=  MaterialInDTO.builder().noStr(noStr(ppList.get(i).getDetailPurchaseOrder().getPurchaseOrder().getNo())).codeStr(codeStr(ppList.get(i).getDetailPurchaseOrder().getCode()))
						.dueDate(ppList.get(i).getDueDate()).materialCode(ppList.get(i).getMrp().getMaterial().getCode())
						.materialName(ppList.get(i).getMrp().getMaterial().getName()).amount(ppList.get(i).getDetailPurchaseOrder().getAmount())
						.status(null).transactionStatus(null).inDate(null).build();
				nullMaterialInDTOList.add(materialInDTO);
				//log.info(i + "번 materialIn에 존재 X miDTO 보기 >>> " + materialInDTO);
				}
		}
		//log.info("nullMaterialInDTOList 보기 >>> " + nullMaterialInDTOList);
		
		//입고상태 null값 -> 정렬된 List 순으로 넣기
		for(MaterialInDTO dto : nullMaterialInDTOList) {
			MaterialInDTOList.add(dto);
		}
		for(MaterialInDTO dto : notNullMaterialInDTOList) {
			MaterialInDTOList.add(dto);
		}
		
		return MaterialInDTOList;
	}

	@Override
	public void registerMaterialOut(Integer code) {
		MaterialIn mi= materialInRepository.findById(code).get();
		
		if(mi.getStatus() == false) {
			DetailPurchaseOrder dpo= mi.getDetailPurchaseOrder();
			ProcurementPlan pp= procurementPlanRepository.findByDetailPurchaseOrder(dpo);
			
			MaterialOut materialOut= MaterialOut.builder().status(0).mrp(pp.getMrp()).build();
			
			materialOutRepository.save(materialOut);			
		}
	}

	@Override
	public Integer getLastCode() {
		return materialInRepository.getLastCode();
	}
}
