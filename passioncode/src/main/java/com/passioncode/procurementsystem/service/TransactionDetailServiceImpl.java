package com.passioncode.procurementsystem.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.passioncode.procurementsystem.dto.TransactionDetailDTO;
import com.passioncode.procurementsystem.entity.Company;
import com.passioncode.procurementsystem.entity.DetailPurchaseOrder;
import com.passioncode.procurementsystem.entity.MaterialIn;
import com.passioncode.procurementsystem.entity.ProcurementPlan;
import com.passioncode.procurementsystem.entity.TransactionDetail;
import com.passioncode.procurementsystem.repository.CompanyRepository;
import com.passioncode.procurementsystem.repository.MaterialInRepository;
import com.passioncode.procurementsystem.repository.ProcurementPlanRepository;
import com.passioncode.procurementsystem.repository.PurchaseOrderRepository;
import com.passioncode.procurementsystem.repository.TransactionDetailRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@RequiredArgsConstructor
@Log4j2
public class TransactionDetailServiceImpl implements TransactionDetailService {
	
	private final MaterialInRepository materialInRepository;
	private final ProcurementPlanRepository procurementPlanRepository;
	private final TransactionDetailRepository transactionDetailRepository;
	private final CompanyRepository companyRepository;
	private final PurchaseOrderRepository purchaseOrderRepository;
	
	@Override
	public TransactionDetailDTO transactionDetailToDTO(DetailPurchaseOrder detailPurchaseOrder) {
		ProcurementPlan pp= procurementPlanRepository.findByDetailPurchaseOrder(detailPurchaseOrder);
		MaterialIn materialIn= materialInRepository.findByDetailPurchaseOrder(detailPurchaseOrder);
		
		Company ourCompany= companyRepository.findById("777-77-77777").get();
		
		TransactionDetail transactionDetail= transactionDetailRepository.findById(1).get();
		TransactionDetailDTO transactionDetailDTO= TransactionDetailDTO.builder().no(transactionDetail.getNo()).company(ourCompany.getName()).purchaseOrderNo(transactionDetail.getPurchaseOrder().getNo())
												.date(materialIn.getDate()).companyNo(pp.getContract().getCompany().getNo())
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

}
