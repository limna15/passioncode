package com.passioncode.procurementsystem.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.passioncode.procurementsystem.dto.MaterialInDTO;
import com.passioncode.procurementsystem.dto.TransactionDetailDTO;
import com.passioncode.procurementsystem.entity.Company;
import com.passioncode.procurementsystem.entity.DetailPurchaseOrder;
import com.passioncode.procurementsystem.entity.MaterialIn;
import com.passioncode.procurementsystem.entity.ProcurementPlan;
import com.passioncode.procurementsystem.entity.TransactionDetail;
import com.passioncode.procurementsystem.repository.CompanyRepository;
import com.passioncode.procurementsystem.repository.DetailPurchaseOrderRepository;
import com.passioncode.procurementsystem.repository.MaterialInRepository;
import com.passioncode.procurementsystem.repository.ProcurementPlanRepository;
import com.passioncode.procurementsystem.repository.PurchaseOrderRepository;
import com.passioncode.procurementsystem.repository.TransactionDetailRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TransactionDetailServiceImpl implements TransactionDetailService {
	
	private final MaterialInRepository materialInRepository;
	private final ProcurementPlanRepository procurementPlanRepository;
	private final TransactionDetailRepository transactionDetailRepository;
	private final CompanyRepository companyRepository;
	private final PurchaseOrderRepository purchaseOrderRepository;
	private final DetailPurchaseOrderRepository detailPurchaseOrderRepository;
	
	@Override
	public TransactionDetailDTO transactionDetailToDTO(DetailPurchaseOrder detailPurchaseOrder) {
		ProcurementPlan pp= procurementPlanRepository.findByDetailPurchaseOrder(detailPurchaseOrder);
		MaterialIn mi= materialInRepository.findByDetailPurchaseOrder(detailPurchaseOrder);
		
		Company ourCompany= companyRepository.findById("777-77-77777").get();
		
		TransactionDetailDTO transactionDetailDTO= TransactionDetailDTO.builder().company(ourCompany.getName()).purchaseOrderNo(detailPurchaseOrder.getPurchaseOrder().getNo())
				.detailPurchaseOrderCode(detailPurchaseOrder.getCode())
				.date(mi.getDate())
				.companyNo(pp.getContract().getCompany().getNo())
				.companyName(pp.getContract().getCompany().getName()).CEO(pp.getContract().getCompany().getCeo())
				.companyAddress(pp.getContract().getCompany().getAddress()).manager(pp.getContract().getCompany().getManager())
				.managerTel(pp.getContract().getCompany().getManagerTel())
				.materialCode(pp.getContract().getMaterial().getCode()).materialName(pp.getContract().getMaterial().getName())
				.amount(pp.getDetailPurchaseOrder().getAmount()).unitPrice(pp.getContract().getUnitPrice()).build();
		return transactionDetailDTO;
	}

	@Override
	public TransactionDetail DTOToEntity(TransactionDetailDTO transactionDetailDTO) {
		TransactionDetail transactionDetail= TransactionDetail.builder().no(transactionDetailDTO.getNo())
				.purchaseOrder(purchaseOrderRepository.findById(transactionDetailDTO.getPurchaseOrderNo()).get()).build();
		return transactionDetail;
	}

	@Override
	public Integer register(TransactionDetailDTO transactionDetailDTO) {
		TransactionDetail transactionDetail=DTOToEntity(transactionDetailDTO);
		transactionDetailRepository.save(transactionDetail);
		
		return transactionDetail.getNo();
	}

	@Override
	public List<TransactionDetail> getTransactionDetailList() {
		List<TransactionDetail> transactionDetailList= transactionDetailRepository.findAll();
		return transactionDetailList;
	}

	@Override
	public List<TransactionDetailDTO> getTransactionDetailDTOLsit() {
		List<DetailPurchaseOrder> dpoList= detailPurchaseOrderRepository.findAll();
		List<ProcurementPlan> ppList= new ArrayList<>();
		List<MaterialIn> miList= new ArrayList<>();
		
		for(int i=0; i<dpoList.size();i++) {
			ppList.add(procurementPlanRepository.findByDetailPurchaseOrder(dpoList.get(i)));
			if(materialInRepository.findByDetailPurchaseOrder(dpoList.get(i))!=null) {
				miList.add(materialInRepository.findByDetailPurchaseOrder(dpoList.get(i)));		
			}
		}

		List<MaterialInDTO> miDTOList= new ArrayList<>();
		MaterialInDTO materialInDTO= null;
		
		for(int i=0; i<miList.size(); i++) {
			materialInDTO= MaterialInDTO.builder().code(miList.get(i).getCode()).inDate(miList.get(i).getDate()).build();
			miDTOList.add(materialInDTO);
		}

		TransactionDetailDTO transactionDetailDTO= null;
		List<TransactionDetailDTO> transactionDetailDTOList= new ArrayList<>();
		
		for(int i=0; i<miList.size();i++) {
			Company ourCompany= companyRepository.findById("777-77-77777").get();

			transactionDetailDTO= TransactionDetailDTO.builder().company(ourCompany.getName()).purchaseOrderNo(dpoList.get(i).getPurchaseOrder().getNo())
								.detailPurchaseOrderCode(ppList.get(i).getDetailPurchaseOrder().getCode())
								.date(miDTOList.get(i).getInDate())
								.companyNo(ppList.get(i).getContract().getCompany().getNo())
								.companyName(ppList.get(i).getContract().getCompany().getName()).CEO(ppList.get(i).getContract().getCompany().getCeo())
								.companyAddress(ppList.get(i).getContract().getCompany().getAddress()).manager(ppList.get(i).getContract().getCompany().getManager())
								.managerTel(ppList.get(i).getContract().getCompany().getManagerTel())
								.materialCode(ppList.get(i).getContract().getMaterial().getCode()).materialName(ppList.get(i).getContract().getMaterial().getName())
								.amount(ppList.get(i).getDetailPurchaseOrder().getAmount()).unitPrice(ppList.get(i).getContract().getUnitPrice()).build();
			
			transactionDetailDTOList.add(transactionDetailDTO);
		}			
		return transactionDetailDTOList;
	}

}
