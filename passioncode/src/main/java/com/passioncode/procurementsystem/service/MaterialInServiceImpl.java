package com.passioncode.procurementsystem.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
	
	@Override
	public MaterialInDTO materialInToDTO(DetailPurchaseOrder detailPurchaseOrder) {
//		ProcurementPlan pp= procurementPlanRepository.findByDetailPurchaseOrder(detailPurchaseOrder);
//		
//		MaterialInDTO materialInDTO= MaterialInDTO.builder().no(detailPurchaseOrder.getPurchaseOrder().getNo()).code(detailPurchaseOrder.getCode())
//									.dueDate(pp.getDueDate()).materialCode(pp.getMrp().getMaterial().getCode()).materialName(pp.getMrp().getMaterial().getName())
//									.amount(pp.getDetailPurchaseOrder().getAmount()).status(true).transactionStatus(false).build();
//		return materialInDTO;
		return null;
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
		//log.info("dpoList 한번 볼게요 " + dpoList);
		
		List<MaterialInDTO> materialInDTOList= new ArrayList<>();
		MaterialInDTO materialInDTO3= null;
		
		for(int i=0; i<dpoList.size(); i++) {
			List<ProcurementPlan> pp= procurementPlanRepository.findByDetailPurchaseOrder(dpoList.get(i));
			//log.info("pp 리스트 보기 >> " + pp);
			for(int j=0; j<pp.size(); j++) {
				if(materialInRepository.existsByDetailPurchaseOrder(dpoList.get(j))){ //입고상태 완료
					if(transactionDetailRepository.existsByPurchaseOrder(dpoList.get(i).getPurchaseOrder())) { //발행상태 완료
						materialInDTO3= MaterialInDTO.builder().no(dpoList.get(i).getPurchaseOrder().getNo()).code(dpoList.get(i).getCode())
								.dueDate(pp.get(j).getDueDate()).materialCode(pp.get(j).getMrp().getMaterial().getCode())
								.materialName(pp.get(j).getMrp().getMaterial().getName()).amount(pp.get(j).getDetailPurchaseOrder().getAmount())
								.status(true).transactionStatus(true).build();
						materialInDTOList.add(materialInDTO3);
					}else { //발행상태 미완료
						materialInDTO3= MaterialInDTO.builder().no(dpoList.get(i).getPurchaseOrder().getNo()).code(dpoList.get(i).getCode())
								.dueDate(pp.get(j).getDueDate()).materialCode(pp.get(j).getMrp().getMaterial().getCode())
								.materialName(pp.get(j).getMrp().getMaterial().getName()).amount(pp.get(j).getDetailPurchaseOrder().getAmount())
								.status(true).transactionStatus(false).build();
						materialInDTOList.add(materialInDTO3);
					}
				}else { //입고상태 미완료
					materialInDTO3=  MaterialInDTO.builder().no(dpoList.get(i).getPurchaseOrder().getNo()).code(dpoList.get(i).getCode())
							.dueDate(pp.get(j).getDueDate()).materialCode(pp.get(j).getMrp().getMaterial().getCode())
							.materialName(pp.get(j).getMrp().getMaterial().getName()).amount(pp.get(j).getDetailPurchaseOrder().getAmount())
							.status(false).transactionStatus(false).build();
					materialInDTOList.add(materialInDTO3);
				}
			}
		}
		return materialInDTOList;
	}
	
}

//	@Override
//	public MaterialInDTO materialInToDTO(MaterialIn materialIn) {		
//		//발주서번호, 발주코드(세부구매발주서테이블), 조달납기예정일(조달계획테이블), 품목코드, 품목명, 입고상태, 발행상태
//		Optional<DetailPurchaseOrder> dpo= detailPurchaseOrderRepository.findById(materialIn.getDetailPurchaseOrder().getCode());
//		ProcurementPlan pp= procurementPlanRepository.findByDetailPurchaseOrder(dpo.get());
//		
//		MaterialInDTO materialInDTO= MaterialInDTO.builder().no(dpo.get().getPurchaseOrder().getNo()).code(dpo.get().getCode())
//				.dueDate(pp.getDueDate()).materialCode(pp.getMrp().getMaterial().getCode())
//				.materialName(pp.getMrp().getMaterial().getName()).amount(pp.getDetailPurchaseOrder().getAmount())
//				.status(false).transactionStatus(false).build();
//
//		return materialInDTO;
//	}