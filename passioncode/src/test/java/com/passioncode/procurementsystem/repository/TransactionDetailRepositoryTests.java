package com.passioncode.procurementsystem.repository;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import com.passioncode.procurementsystem.dto.DetailPurchaseOrderDTO;
import com.passioncode.procurementsystem.dto.MaterialDTO;
import com.passioncode.procurementsystem.dto.TransactionDetailDTO;
import com.passioncode.procurementsystem.entity.Company;
import com.passioncode.procurementsystem.entity.DetailPurchaseOrder;
import com.passioncode.procurementsystem.entity.Material;
import com.passioncode.procurementsystem.entity.MaterialIn;
import com.passioncode.procurementsystem.entity.ProcurementPlan;
import com.passioncode.procurementsystem.entity.PurchaseOrder;
import com.passioncode.procurementsystem.entity.TransactionDetail;

import lombok.extern.log4j.Log4j2;

@SpringBootTest
@Log4j2
public class TransactionDetailRepositoryTests {
	
	@Autowired
	TransactionDetailRepository transactionDetailRepository;
	
	@Autowired
	ProcurementPlanRepository procurementPlanRepository;
	
	@Autowired
	DetailPurchaseOrderRepository detailPurchaseOrderRepository;
	
	@Autowired
	MaterialInRepository materialInRepository;
	
	@Autowired
	CompanyRepository companyRepository;
	
	@Autowired
	PurchaseOrderRepository purchaseOrderRepository;
	
	@Transactional
	@Test
	public void DTOTest() {
		//거래명서세번호, 발주회사(=우리회사), 발주서번호, 납기일자(=입고일), 사업자등록번호, 상호, CEO, 주소, 담당자, 연락처, 품목코드, 품목명, 수량, 단가
		DetailPurchaseOrder detailPurchaseOrder= detailPurchaseOrderRepository.findById(1).get();
		ProcurementPlan pp= procurementPlanRepository.findByDetailPurchaseOrder(detailPurchaseOrder);
		
		Company ourCompany= companyRepository.findById("777-77-77777").get();
		
		TransactionDetailDTO transactionDetailDTO= TransactionDetailDTO.builder().company(ourCompany.getName()).purchaseOrderNo(detailPurchaseOrder.getPurchaseOrder().getNo())
												.detailPurchaseOrderCode(detailPurchaseOrder.getCode())
												.companyNo(pp.getContract().getCompany().getNo())
												.companyName(pp.getContract().getCompany().getName()).CEO(pp.getContract().getCompany().getCeo())
												.companyAddress(pp.getContract().getCompany().getAddress()).manager(pp.getContract().getCompany().getManager())
												.managerTel(pp.getContract().getCompany().getManagerTel())
												.materialCode(pp.getContract().getMaterial().getCode()).materialName(pp.getContract().getMaterial().getName())
												.amount(pp.getDetailPurchaseOrder().getAmount()).unitPrice(pp.getContract().getUnitPrice()).build();
		
		log.info("transactionDetailDTO 어떻게 가지고 오는지 보자>>> " + transactionDetailDTO);
	}

	@Transactional
	@Commit
	@Test
	public void InsertByDTOTest() {
		Company ourCompany= companyRepository.findById("777-77-77777").get();
		
		TransactionDetailDTO transactionDetailDTO= TransactionDetailDTO.builder().no(2).company(ourCompany.getName()).purchaseOrderNo(2).date(LocalDateTime.now())
				.companyNo("403-81-80895").companyName("(유)길승산업").CEO("김태리").companyAddress("서울시 동작구").manager("박서준").managerTel("010-1111-1111")
				.materialCode("CGa0001").materialName("Gear1").amount(150).unitPrice(500).build();

		log.info("transactionDetailDTO 어떻게 가지고 오는지 보자>>> " + transactionDetailDTO);
	
		TransactionDetail transactionDetail= TransactionDetail.builder().no(transactionDetailDTO.getNo())
											.purchaseOrder(purchaseOrderRepository.findById(transactionDetailDTO.getPurchaseOrderNo()).get()).build();
		log.info("transactionDetail 어떻게 가지고 오는지 보자>>> " + transactionDetail);
	
	}
	
	@Transactional
	@Commit
	@Test
	public void getDTOList() {
		List<DetailPurchaseOrder> dpoList= detailPurchaseOrderRepository.findAll();
		List<ProcurementPlan> ppList= new ArrayList<>();
		List<MaterialIn> miList= new ArrayList<>();
		
		for(int i=0; i<dpoList.size();i++) {
			ppList.add(procurementPlanRepository.findByDetailPurchaseOrder(dpoList.get(i)));
			miList.add(materialInRepository.findByDetailPurchaseOrder(dpoList.get(i)));		
		}
		
		log.info("ppList >>> " + ppList + ppList.size());
		log.info("miList >>> " + miList + miList.size());
		//ProcurementPlan pp= procurementPlanRepository.findByDetailPurchaseOrder(detailPurchaseOrder);
		//MaterialIn materialIn= materialInRepository.findByDetailPurchaseOrder(detailPurchaseOrder);
		
		TransactionDetailDTO transactionDetailDTO= null;
		List<TransactionDetailDTO> transactionDetailDTOList= new ArrayList<>();
		for(int i=0; i<miList.size();i++) {
			Company ourCompany= companyRepository.findById("777-77-77777").get();
			
			transactionDetailDTO= TransactionDetailDTO.builder().company(ourCompany.getName()).purchaseOrderNo(dpoList.get(i).getPurchaseOrder().getNo())
													.detailPurchaseOrderCode(miList.get(i).getDetailPurchaseOrder().getCode())
													.companyNo(ppList.get(i).getContract().getCompany().getNo())
													.companyName(ppList.get(i).getContract().getCompany().getName()).CEO(ppList.get(i).getContract().getCompany().getCeo())
													.companyAddress(ppList.get(i).getContract().getCompany().getAddress()).manager(ppList.get(i).getContract().getCompany().getManager())
													.managerTel(ppList.get(i).getContract().getCompany().getManagerTel())
													.materialCode(ppList.get(i).getContract().getMaterial().getCode()).materialName(ppList.get(i).getContract().getMaterial().getName())
													.amount(ppList.get(i).getDetailPurchaseOrder().getAmount()).unitPrice(ppList.get(i).getContract().getUnitPrice()).build();
			transactionDetailDTOList.add(transactionDetailDTO);
		}
		log.info("transactionDetailDTOList 어떻게 가지고 오는지 보자>>> " + transactionDetailDTOList);
	}
	
	
	
}