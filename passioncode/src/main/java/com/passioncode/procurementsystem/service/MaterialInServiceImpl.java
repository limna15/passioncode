package com.passioncode.procurementsystem.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.passioncode.procurementsystem.dto.MaterialInDTO;
import com.passioncode.procurementsystem.entity.DetailPurchaseOrder;
import com.passioncode.procurementsystem.entity.MaterialIn;
import com.passioncode.procurementsystem.entity.ProcurementPlan;
import com.passioncode.procurementsystem.repository.DetailPurchaseOrderRepository;
import com.passioncode.procurementsystem.repository.MaterialInRepository;
import com.passioncode.procurementsystem.repository.ProcurementPlanRepository;
import com.passioncode.procurementsystem.repository.TransactionDetailRepository;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@RequiredArgsConstructor
@Log4j2
public class MaterialInServiceImpl implements MateriallInService {
	
	private final MaterialInRepository materialInRepository;
	private final DetailPurchaseOrderRepository detailPurchaseOrderRepository;
	private final ProcurementPlanRepository procurementPlanRepository;
	private final TransactionDetailRepository transactionDetailRepository;
	private final EntityManager entityManager;
	
	
	public Integer boolInInt(Boolean status) {
		
		return status ? 1 : 0;
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
		
		for(int i=0; i<ppList.size(); i++) {
			if(!materialInRepository.existsById(i+1)) { //materialIn에 존재 X 때 설정
				materialInDTO=  MaterialInDTO.builder().no(dpoList.get(i).getPurchaseOrder().getNo()).code(dpoList.get(i).getCode())
						.dueDate(ppList.get(i).getDueDate()).materialCode(ppList.get(i).getMrp().getMaterial().getCode())
						.materialName(ppList.get(i).getMrp().getMaterial().getName()).amount(ppList.get(i).getDetailPurchaseOrder().getAmount())
						.status(null).transactionStatus(null).inDate(null).build();
				materialInDTOList.add(materialInDTO);
				//log.info(i + "번 materialIn에 존재 X miDTO 보기 >>> " + materialInDTO);
				}else { //materialIn에 존재할 때 설정
					if(materialInRepository.existsByDetailPurchaseOrder(dpoList.get(i))){ //입고상태가 null이 아닐 때
						if(transactionDetailRepository.existsByPurchaseOrder(dpoList.get(i).getPurchaseOrder())) { //입고상태 완료 + 발행상태 완료
							materialInDTO= MaterialInDTO.builder().no(dpoList.get(i).getPurchaseOrder().getNo()).code(dpoList.get(i).getCode())
									.dueDate(ppList.get(i).getDueDate()).materialCode(ppList.get(i).getMrp().getMaterial().getCode())
									.materialName(ppList.get(i).getMrp().getMaterial().getName()).amount(ppList.get(i).getDetailPurchaseOrder().getAmount())
									.status(miList.get(i).getStatus()).transactionStatus("발행 완료")
									.inDate(miList.get(i).getDate()).build();
							materialInDTOList.add(materialInDTO);
							//log.info(i + "번 입고상태+발행상태 완료 miDTO 보기 >>> " + materialInDTO);
						}else { //발행상태 미완료
							if(miList.get(i).getStatus()) { //입고상태 완료 + 발행상태 미완료
								materialInDTO= MaterialInDTO.builder().no(dpoList.get(i).getPurchaseOrder().getNo()).code(dpoList.get(i).getCode())
										.dueDate(ppList.get(i).getDueDate()).materialCode(ppList.get(i).getMrp().getMaterial().getCode())
										.materialName(ppList.get(i).getMrp().getMaterial().getName()).amount(ppList.get(i).getDetailPurchaseOrder().getAmount())
										.status(miList.get(i).getStatus()).transactionStatus("발행 예정")
										.inDate(miList.get(i).getDate()).build();
								materialInDTOList.add(materialInDTO);
								//log.info(i + "번 입고 완료 + 발행 미완료 miDTO 보기 >>> " + materialInDTO);
							
							}else { //입고상태 취소 + 발행상태 "발행 불가"
								materialInDTO=  MaterialInDTO.builder().no(dpoList.get(i).getPurchaseOrder().getNo()).code(dpoList.get(i).getCode())
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
		
		log.info("materialIn 어떻게 읽히나 >>>  " + materialIn);

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

}
