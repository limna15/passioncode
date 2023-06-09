package com.passioncode.procurementsystem.repository;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.passioncode.procurementsystem.dto.MaterialInDTO;
import com.passioncode.procurementsystem.entity.DetailPurchaseOrder;
import com.passioncode.procurementsystem.entity.MaterialIn;
import com.passioncode.procurementsystem.entity.ProcurementPlan;

import lombok.extern.log4j.Log4j2;

@SpringBootTest
@Log4j2
public class MaterialInRepositoryTests {

	@Autowired
	MaterialInRepository materialInRepository;
	
	@Autowired
	DetailPurchaseOrderRepository detailPurchaseOrderRepository;
	
	@Autowired
	ProcurementPlanRepository procurementPlanRepository;
	
	@Autowired
	TransactionDetailRepository transactionDetailRepository;
	
	@Test
	public void getList() {
		Optional<MaterialIn> list= materialInRepository.findById(1);
		
		MaterialIn in= list.get();
		
		List<MaterialIn> list2= materialInRepository.findAll();
		
		log.info("이렇게 읽는게 맞는건가....."+in);
		log.info("리스트로 읽을수 있냐구"+list2);
	}
	
//	@Transactional
	@Test
	public void materialInInsertTest() {
		//세부구매발주서 ID로 읽기
		Optional<DetailPurchaseOrder> result= detailPurchaseOrderRepository.findById(3);
		//log.info("세부구매발주서 읽기 get" + result.get());
		//직접적인 외래키(지연로딩된것)를 읽으려면 어노테이션 @Transactional 필요
		//외래키 사용하면 get을 쓰지 않아도 해당 테이블을 가져올 수 있음
		log.info("세부구매발주서 읽기 " + result);
				
		//입고일 현재시간으로 읽기 
		log.info("입고일, localdatetime 현재시간 읽기 : "+LocalDateTime.now());
	
//		//입고상태, 발행상태 기본값이 세팅되어있지만 null이 허용이 안되있어서 null을 넣으면 에러가 남
//		MaterialIn materialIn= MaterialIn.builder().date(LocalDateTime.now()).status(true).transactionStatus(false).detailPurchaseOrder(dpo).build();
//		
//		materialInRepository.save(materialIn);
	}
	
	@Transactional
	@Test
	public void inStatusTest() {
		//발주코드 1-3: 입고코드 O
		//발주코드 4: 입고코드 X
		DetailPurchaseOrder dpo= detailPurchaseOrderRepository.findById(1).get();
		DetailPurchaseOrder dpo2= detailPurchaseOrderRepository.findById(4).get();
		log.info("존재여부 확인해보자! 너는 True가 나오겠지 " + materialInRepository.existsByDetailPurchaseOrder(dpo));
		log.info("존재여부 확인해보자! 너는 False가 나오겠지 " + materialInRepository.existsByDetailPurchaseOrder(dpo2));
	}
	
	@Transactional
	@Test
	public void DTOListTest() {
		//List<DetailPurchaseOrder> optionalDpo= detailPurchaseOrderRepository.findAll();
		//ProcurementPlan pp= procurementPlanRepository.findByDetailPurchaseOrder(optionalDpo.get());
		//log.info("세부구매발주서로 procurementPlan 찾기! " + pp.getDueDate());
		
		List<DetailPurchaseOrder> dpoList= detailPurchaseOrderRepository.findAll();
		//log.info("dpoList 한번 볼게요 " + dpoList);
		
		List<MaterialInDTO> materialInDTOList= new ArrayList<>();
		MaterialInDTO materialInDTO= null;
		List<ProcurementPlan> ppList= new ArrayList<>();
		
		for(int i=0; i<dpoList.size(); i++) {
			ppList.add(procurementPlanRepository.findByDetailPurchaseOrder(dpoList.get(i)));
		}
		
		log.info("pp 리스트 보기 >>> " + ppList);
		log.info("dpo 리스트 보기 >>> " + dpoList);
		log.info("pp date 보기 >>> " + ppList.get(0).getDueDate());
		log.info("dpo 리스트 사이즈 >>> " + dpoList.size());
		log.info("pp 리스트 사이즈 >>> " + ppList.size());
		

		for(int i=0; i<ppList.size(); i++) {
			log.info(i+ "번째 데이트값 " + ppList.get(i).getDueDate());
			if(materialInRepository.existsByDetailPurchaseOrder(dpoList.get(i))){ //입고상태 완료
				if(transactionDetailRepository.existsByPurchaseOrder(dpoList.get(i).getPurchaseOrder())) { //발행상태 완료
					materialInDTO= MaterialInDTO.builder().no(dpoList.get(i).getPurchaseOrder().getNo()).code(dpoList.get(i).getCode())
							.dueDate(ppList.get(i).getDueDate()).materialCode(ppList.get(i).getMrp().getMaterial().getCode())
							.materialName(ppList.get(i).getMrp().getMaterial().getName()).amount(ppList.get(i).getDetailPurchaseOrder().getAmount())
							.status(true).transactionStatus(true).build();
					materialInDTOList.add(materialInDTO);
				}else { //발행상태 미완료
					materialInDTO= MaterialInDTO.builder().no(dpoList.get(i).getPurchaseOrder().getNo()).code(dpoList.get(i).getCode())
							.dueDate(ppList.get(i).getDueDate()).materialCode(ppList.get(i).getMrp().getMaterial().getCode())
							.materialName(ppList.get(i).getMrp().getMaterial().getName()).amount(ppList.get(i).getDetailPurchaseOrder().getAmount())
							.status(true).transactionStatus(false).build();
					materialInDTOList.add(materialInDTO);
				}
			}else { //입고상태 미완료
				materialInDTO=  MaterialInDTO.builder().no(dpoList.get(i).getPurchaseOrder().getNo()).code(dpoList.get(i).getCode())
						.dueDate(ppList.get(i).getDueDate()).materialCode(ppList.get(i).getMrp().getMaterial().getCode())
						.materialName(ppList.get(i).getMrp().getMaterial().getName()).amount(ppList.get(i).getDetailPurchaseOrder().getAmount())
						.status(false).transactionStatus(false).build();
				materialInDTOList.add(materialInDTO);
			}
		}
		log.info("materialDTOList 한번 보자! " + materialInDTOList);
	}

	@Transactional
	@Test
	public void TransactionDetailStatusTest() {
		//발주서 번호 101: 발행상태 완료
		DetailPurchaseOrder dpo= detailPurchaseOrderRepository.findById(1).orElse(null);
		log.info("존재여부 확인해보자! 너는 True가 나오겠지 " + transactionDetailRepository.existsByPurchaseOrder(dpo.getPurchaseOrder()));
		
	}
	
	@Transactional
	@Test
	public void DTOTest() {
		DetailPurchaseOrder detailPurchaseOrder= detailPurchaseOrderRepository.findById(1).get();
		ProcurementPlan pp= procurementPlanRepository.findByDetailPurchaseOrder(detailPurchaseOrder);
		log.info("pp는 어케 찍히나 " + pp);
		
		MaterialInDTO materialInDTO= MaterialInDTO.builder().no(detailPurchaseOrder.getPurchaseOrder().getNo()).code(detailPurchaseOrder.getCode())
									.dueDate(pp.getDueDate()).materialCode(pp.getMrp().getMaterial().getCode())
									.materialName(pp.getMrp().getMaterial().getName())
									.amount(pp.getDetailPurchaseOrder().getAmount()).status(true).transactionStatus(false).build();
		
		log.info("DTO 하나는 어케 가져오는거죠 " + materialInDTO);
	}

//	@Transactional
//	@Test
//	public void materialInDTOTest() {
//		
//		//읽어와야하는 내용
//		//발주서번호, 발주코드(세부구매발주서테이블), 조달납기예정일(조달계획테이블), 품목코드, 품목명, 입고상태, 발행상태
//		MaterialIn materialIn= materialInRepository.findById(1).get();
//		
//		Optional<DetailPurchaseOrder> optionalDpo= detailPurchaseOrderRepository.findById(materialIn.getDetailPurchaseOrder().getCode());
//		ProcurementPlan pp= procurementPlanRepository.findByDetailPurchaseOrder(optionalDpo.get());
//		log.info("세부구매발주서로 procurementPlan 찾기! " + pp.getDueDate());
//		
//		//입고상태가 완료인 자재입고DTO가 됨 + 발행상태 미완료
//		MaterialInDTO materialInDTO= MaterialInDTO.builder().no(optionalDpo.get().getPurchaseOrder().getNo()).code(optionalDpo.get().getCode())
//				.dueDate(pp.getDueDate()).materialCode(pp.getMrp().getMaterial().getCode())
//				.materialName(pp.getMrp().getMaterial().getName()).amount(pp.getDetailPurchaseOrder().getAmount())
//				.status(true).transactionStatus(false).build();
//		
//		log.info("materialInDTO 어떻게 찍히니 " + materialInDTO);
//		
//		List<MaterialIn> miList= materialInRepository.findAll();
//		List<DetailPurchaseOrder> dpoList= detailPurchaseOrderRepository.findAll();
//		
//		
//		//입고상태가 미완료인 자재입고DTO 만들기
//		DetailPurchaseOrder dpo2= detailPurchaseOrderRepository.findById(4).get();
//		MaterialInDTO materialInDTO2= MaterialInDTO.builder().no(dpo2.getPurchaseOrder().getNo()).code(dpo2.getCode())
//				.dueDate(pp.getDueDate()).materialCode(pp.getMrp().getMaterial().getCode())
//				.materialName(pp.getMrp().getMaterial().getName()).amount(pp.getDetailPurchaseOrder().getAmount())
//				.status(false).transactionStatus(false).build();
//		log.info("materialInDTO2는 입고상태가 미완료인 경우, 어떻게 찍히니 " + materialInDTO2);	
//
//		List<MaterialInDTO> miDTOList= new ArrayList<>();
//		MaterialInDTO materialInDTO3= null;
//		log.info("miList 한번보자 " + miList);
//		
//		for(int i=0; i<dpoList.size(); i++) {
//			if(materialInRepository.existsByDetailPurchaseOrder(dpoList.get(i))){ //입고상태 완료
//				if(transactionDetailRepository.existsByPurchaseOrder(dpoList.get(i).getPurchaseOrder())) { //발행상태 완료
//					materialInDTO3= MaterialInDTO.builder().no(optionalDpo.get().getPurchaseOrder().getNo()).code(optionalDpo.get().getCode())
//									.dueDate(pp.getDueDate()).materialCode(pp.getMrp().getMaterial().getCode())
//									.materialName(pp.getMrp().getMaterial().getName()).amount(pp.getDetailPurchaseOrder().getAmount())
//									.status(true).transactionStatus(true).build();
//					miDTOList.add(materialInDTO3);
//				}else { //발행상태 미완료
//					materialInDTO3= MaterialInDTO.builder().no(optionalDpo.get().getPurchaseOrder().getNo()).code(optionalDpo.get().getCode())
//							.dueDate(pp.getDueDate()).materialCode(pp.getMrp().getMaterial().getCode())
//							.materialName(pp.getMrp().getMaterial().getName()).amount(pp.getDetailPurchaseOrder().getAmount())
//							.status(true).transactionStatus(false).build();
//			miDTOList.add(materialInDTO3);
//				}
//			}else { //입고상태 미완료
//				materialInDTO3=  MaterialInDTO.builder().no(optionalDpo.get().getPurchaseOrder().getNo()).code(optionalDpo.get().getCode())
//						.dueDate(pp.getDueDate()).materialCode(pp.getMrp().getMaterial().getCode())
//						.materialName(pp.getMrp().getMaterial().getName()).amount(pp.getDetailPurchaseOrder().getAmount())
//						.status(false).transactionStatus(false).build();
//				miDTOList.add(materialInDTO3);
//			}
//		}
//		
//		log.info("miDTOList 한번 보자! " + miDTOList);
//				
//	}
	
}
