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
									.amount(pp.getDetailPurchaseOrder().getAmount()).status(mi.getStatus()).transactionStatus(false)
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
						.status(null).transactionStatus(false).inDate(null).build();
				materialInDTOList.add(materialInDTO);
				//log.info(i + "번 materialIn에 존재 X miDTO 보기 >>> " + materialInDTO);
				}else { //materialIn에 존재할 때 설정
					if(materialInRepository.existsByDetailPurchaseOrder(dpoList.get(i))){ //입고상태 완료
						if(transactionDetailRepository.existsByPurchaseOrder(dpoList.get(i).getPurchaseOrder())) { //발행상태 완료
							materialInDTO= MaterialInDTO.builder().no(dpoList.get(i).getPurchaseOrder().getNo()).code(dpoList.get(i).getCode())
									.dueDate(ppList.get(i).getDueDate()).materialCode(ppList.get(i).getMrp().getMaterial().getCode())
									.materialName(ppList.get(i).getMrp().getMaterial().getName()).amount(ppList.get(i).getDetailPurchaseOrder().getAmount())
									.status(miList.get(i).getStatus()).transactionStatus(true)
									.inDate(miList.get(i).getDate()).build();
							materialInDTOList.add(materialInDTO);
							//log.info(i + "번 입고상태+발행상태 완료 miDTO 보기 >>> " + materialInDTO);
						}else { //발행상태 미완료
							materialInDTO= MaterialInDTO.builder().no(dpoList.get(i).getPurchaseOrder().getNo()).code(dpoList.get(i).getCode())
									.dueDate(ppList.get(i).getDueDate()).materialCode(ppList.get(i).getMrp().getMaterial().getCode())
									.materialName(ppList.get(i).getMrp().getMaterial().getName()).amount(ppList.get(i).getDetailPurchaseOrder().getAmount())
									.status(miList.get(i).getStatus()).transactionStatus(false)
									.inDate(miList.get(i).getDate()).build();
							materialInDTOList.add(materialInDTO);
							//log.info(i + "번 입고 완료 + 발행 미완료 miDTO 보기 >>> " + materialInDTO);
						}
					}else { //입고상태 취소
						materialInDTO=  MaterialInDTO.builder().no(dpoList.get(i).getPurchaseOrder().getNo()).code(dpoList.get(i).getCode())
								.dueDate(ppList.get(i).getDueDate()).materialCode(ppList.get(i).getMrp().getMaterial().getCode())
								.materialName(ppList.get(i).getMrp().getMaterial().getName()).amount(ppList.get(i).getDetailPurchaseOrder().getAmount())
								.status(null).transactionStatus(false).inDate(null).build();
						materialInDTOList.add(materialInDTO);
						//log.info(i + "번 입고상태 취소 miDTO 보기 >>> " + materialInDTO);
					}
				
			} 
			
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
}
