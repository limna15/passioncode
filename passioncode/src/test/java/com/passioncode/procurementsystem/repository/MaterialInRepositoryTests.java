package com.passioncode.procurementsystem.repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.hibernate.mapping.Array;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
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
	
	@Transactional
	@Commit
	@Test
	public void materialInInsertTest() {
		//log.info("세부구매발주서 읽기 get" + result.get());
		//직접적인 외래키(지연로딩된것)를 읽으려면 어노테이션 @Transactional 필요
		//외래키 사용하면 get을 쓰지 않아도 해당 테이블을 가져올 수 있음

		//세부구매발주서 ID로 읽기
		DetailPurchaseOrder detailPurchaseOrder= detailPurchaseOrderRepository.findById(4).get();

		//입고일 현재시간으로 읽기 
		//log.info("입고일, localdatetime 현재시간 읽기 : "+LocalDateTime.now());
	
		//입고상태 기본값이 세팅되어있지만 null이 허용이 안되있어서 null을 넣으면 에러가 남
		MaterialIn materialIn= MaterialIn.builder().date(LocalDateTime.now()).status(true).transactionStatus("발행 예정").detailPurchaseOrder(detailPurchaseOrder).build();
		
		materialInRepository.save(materialIn);
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
		
		log.info("pp 리스트 보기 >>> " + ppList);
		log.info("dpo 리스트 보기 >>> " + dpoList);
		log.info("mi 리스트 보기 >>> " + miList + "   mi 리스트 사이즈 보기 >>> " + miList.size());
		log.info("dpo 리스트 사이즈 >>> " + dpoList.size());
		log.info("pp 리스트 사이즈 >>> " + ppList.size());
		
		for(int i=0; i<ppList.size(); i++) {
			if(!materialInRepository.existsById(i+1)) { //materialIn에 존재 X 때 설정
				materialInDTO=  MaterialInDTO.builder().no(dpoList.get(i).getPurchaseOrder().getNo()).code(dpoList.get(i).getCode())
						.dueDate(ppList.get(i).getDueDate()).materialCode(ppList.get(i).getMrp().getMaterial().getCode())
						.materialName(ppList.get(i).getMrp().getMaterial().getName()).amount(ppList.get(i).getDetailPurchaseOrder().getAmount())
						.status(null).transactionStatus(null).inDate(null).build();
				materialInDTOList.add(materialInDTO);
				//log.info(i + "번 materialIn에 존재 X miDTO 보기 >>> " + materialInDTO);
				}else { //materialIn에 존재할 때 설정
					if(materialInRepository.existsByDetailPurchaseOrder(dpoList.get(i))){ //입고상태 완료
						if(transactionDetailRepository.existsByPurchaseOrder(dpoList.get(i).getPurchaseOrder())) { //발행상태 완료
							materialInDTO= MaterialInDTO.builder().no(dpoList.get(i).getPurchaseOrder().getNo()).code(dpoList.get(i).getCode())
									.dueDate(ppList.get(i).getDueDate()).materialCode(ppList.get(i).getMrp().getMaterial().getCode())
									.materialName(ppList.get(i).getMrp().getMaterial().getName()).amount(ppList.get(i).getDetailPurchaseOrder().getAmount())
									.status(miList.get(i).getStatus()).transactionStatus("발행 완료")
									.inDate(miList.get(i).getDate()).build();
							materialInDTOList.add(materialInDTO);
							//log.info(i + "번 입고상태+발행상태 완료 miDTO 보기 >>> " + materialInDTO);
						}else { //발행상태 미완료
							materialInDTO= MaterialInDTO.builder().no(dpoList.get(i).getPurchaseOrder().getNo()).code(dpoList.get(i).getCode())
									.dueDate(ppList.get(i).getDueDate()).materialCode(ppList.get(i).getMrp().getMaterial().getCode())
									.materialName(ppList.get(i).getMrp().getMaterial().getName()).amount(ppList.get(i).getDetailPurchaseOrder().getAmount())
									.status(miList.get(i).getStatus()).transactionStatus("발행 예정")
									.inDate(miList.get(i).getDate()).build();
							materialInDTOList.add(materialInDTO);
							//log.info(i + "번 입고 완료 + 발행 미완료 miDTO 보기 >>> " + materialInDTO);
						}
					}else { //입고상태 취소
						materialInDTO=  MaterialInDTO.builder().no(dpoList.get(i).getPurchaseOrder().getNo()).code(dpoList.get(i).getCode())
								.dueDate(ppList.get(i).getDueDate()).materialCode(ppList.get(i).getMrp().getMaterial().getCode())
								.materialName(ppList.get(i).getMrp().getMaterial().getName()).amount(ppList.get(i).getDetailPurchaseOrder().getAmount())
								.status(null).transactionStatus("발행 불가").inDate(null).build();
						materialInDTOList.add(materialInDTO);
						//log.info(i + "번 입고상태 취소 miDTO 보기 >>> " + materialInDTO);
					}
				
			} 
			
		} //for문 끝
		log.info("materialDTOList 한번 보자! " + materialInDTOList + "materialDTOList 사이즈 한번 보자! " + materialInDTOList.size());
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
		MaterialIn mi= materialInRepository.findByDetailPurchaseOrder(detailPurchaseOrder);

		log.info("pp는 어케 찍히나 " + pp);
		
		MaterialInDTO materialInDTO= MaterialInDTO.builder().no(detailPurchaseOrder.getPurchaseOrder().getNo()).code(detailPurchaseOrder.getCode())
									.dueDate(pp.getDueDate()).materialCode(pp.getMrp().getMaterial().getCode())
									.materialName(pp.getMrp().getMaterial().getName())
									.amount(pp.getDetailPurchaseOrder().getAmount()).status(mi.getStatus()).transactionStatus(mi.getTransactionStatus())
									.inDate(mi.getDate()).build();
		
		log.info("DTO 하나는 어케 가져오는거죠 " + materialInDTO);
	}
	
	@Transactional
	@Commit
	@Test
	public void insertByDTOTest() {
		Date date= new Date();
		date.setDate(2023-06-16);
		
		MaterialInDTO materialInDTO= MaterialInDTO.builder().no(7).code(7)
				.dueDate(date).materialCode("BSa0001").materialName("Sensor")
				.amount(200).status(null).transactionStatus(null).build();
		
		MaterialIn materialIn= MaterialIn.builder().date(LocalDateTime.now()).status(true).transactionStatus("발행 예정")
				.detailPurchaseOrder(detailPurchaseOrderRepository.findById(materialInDTO.getCode()).get()).build();
		
		log.info("DTO를 이용해서 엔티티에 넣어지는걸까 " + materialIn);
		
		materialInRepository.save(materialIn);	
	}
	
	@Test
	public void miListTest() {
		List<MaterialIn> materialInList= materialInRepository.findAll();
		log.info("List >>> " + materialInList);
	}
	
}
