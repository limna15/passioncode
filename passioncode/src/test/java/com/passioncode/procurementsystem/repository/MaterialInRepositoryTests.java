package com.passioncode.procurementsystem.repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;
import com.passioncode.procurementsystem.dto.MaterialInDTO;
import com.passioncode.procurementsystem.entity.DetailPurchaseOrder;
import com.passioncode.procurementsystem.entity.MaterialIn;
import com.passioncode.procurementsystem.entity.MaterialOut;
import com.passioncode.procurementsystem.entity.ProcurementPlan;
import com.passioncode.procurementsystem.entity.PurchaseOrder;
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
	
	@Autowired
	ContractRepository contractRepository;
	
	@Autowired
	MRPRepository mrpRepository;
	
	@Autowired
	PurchaseOrderRepository purchaseOrderRepository;
	
	@Autowired
	MaterialOutRepository materialOutRepository;
	
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
				log.info(i + "번 materialIn에 존재 X miDTO 보기 >>> " + materialInDTO);
				}else { //materialIn에 존재할 때 설정
					if(materialInRepository.existsByDetailPurchaseOrder(dpoList.get(i))){ //입고상태가 null이 아닐 때
						if(transactionDetailRepository.existsByPurchaseOrder(dpoList.get(i).getPurchaseOrder())) { //입고상태 완료 + 발행상태 완료
							materialInDTO= MaterialInDTO.builder().no(dpoList.get(i).getPurchaseOrder().getNo()).code(dpoList.get(i).getCode())
									.dueDate(ppList.get(i).getDueDate()).materialCode(ppList.get(i).getMrp().getMaterial().getCode())
									.materialName(ppList.get(i).getMrp().getMaterial().getName()).amount(ppList.get(i).getDetailPurchaseOrder().getAmount())
									.status(miList.get(i).getStatus()).transactionStatus("발행 완료")
									.inDate(miList.get(i).getDate()).build();
							materialInDTOList.add(materialInDTO);
							log.info(i + "번 입고상태+발행상태 완료 miDTO 보기 >>> " + materialInDTO);
						}else { //발행상태 미완료
							if(miList.get(i).getStatus()) { //입고상태 완료 + 발행상태 미완료
								materialInDTO= MaterialInDTO.builder().no(dpoList.get(i).getPurchaseOrder().getNo()).code(dpoList.get(i).getCode())
										.dueDate(ppList.get(i).getDueDate()).materialCode(ppList.get(i).getMrp().getMaterial().getCode())
										.materialName(ppList.get(i).getMrp().getMaterial().getName()).amount(ppList.get(i).getDetailPurchaseOrder().getAmount())
										.status(miList.get(i).getStatus()).transactionStatus("발행 예정")
										.inDate(miList.get(i).getDate()).build();
								materialInDTOList.add(materialInDTO);
								log.info(i + "번 입고 완료 + 발행 미완료 miDTO 보기 >>> " + materialInDTO);
							
							}else { //입고상태 취소 + 발행상태 "발행 불가"
								materialInDTO=  MaterialInDTO.builder().no(dpoList.get(i).getPurchaseOrder().getNo()).code(dpoList.get(i).getCode())
										.dueDate(ppList.get(i).getDueDate()).materialCode(ppList.get(i).getMrp().getMaterial().getCode())
										.materialName(ppList.get(i).getMrp().getMaterial().getName()).amount(ppList.get(i).getDetailPurchaseOrder().getAmount())
										.status(miList.get(i).getStatus()).transactionStatus("발행 불가").inDate(null).build();
								materialInDTOList.add(materialInDTO);
								log.info(i + "번 입고상태 취소 miDTO 보기 >>> " + materialInDTO);					
							}
						} //두번째 else 끝(발행상태 미완료)
					} //두번째 if문 끝 (입고상태가 null이 아닐 때)
			} //else 끝(materialIn에 존재할 때) 
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
	
	//입고상태가 존재 = 조달계획 완료 -> 완료일 업데이트 시켜야함
	@Transactional
	@Commit
	@Test
	public void updatePPCompletionDate() {
		DetailPurchaseOrder detailPurchaseOrder= detailPurchaseOrderRepository.findById(3).get();
		ProcurementPlan procurementPlan= procurementPlanRepository.findByDetailPurchaseOrder(detailPurchaseOrder);
		MaterialIn materialIn= materialInRepository.findByDetailPurchaseOrder(detailPurchaseOrder);
		
		log.info("materialIn 어떻게 읽히나 >>>  " + materialIn);

		procurementPlan= ProcurementPlan.builder().code(procurementPlan.getCode())
				.mrp(procurementPlan.getMrp()).contract(procurementPlan.getContract()).amount(procurementPlan.getAmount())
				.dueDate(procurementPlan.getDueDate()).minimumOrderDate(procurementPlan.getMinimumOrderDate())
				.registerDate(procurementPlan.getRegisterDate()).completionDate(materialIn.getDate())
				.detailPurchaseOrder(detailPurchaseOrder).build();
		
		procurementPlanRepository.save(procurementPlan);
		
		log.info("procurementPlan 어떻게 읽히죠 >>> " + procurementPlan);
	}
	
	//발주서 번호로 세부발주서 내용 가져오기
	@Test
	public void getDetail() {
		PurchaseOrder po= purchaseOrderRepository.findById(1).get();
		log.info("세부구매발주서 내용 가져와보자 >>> " + detailPurchaseOrderRepository.findByPurchaseOrder(po));
	}
	
	@Transactional
	@Test
	public void getSortDTOListTest() {
		//자재입고 테이블에 등록된 자재입고DTO 만들기
		MaterialIn mi= materialInRepository.findById(1).get();
		ProcurementPlan pp= procurementPlanRepository.findByDetailPurchaseOrder(mi.getDetailPurchaseOrder());

		MaterialInDTO materialInDTO= MaterialInDTO.builder()
				.no(mi.getDetailPurchaseOrder().getPurchaseOrder().getNo()).code(mi.getDetailPurchaseOrder().getCode())
				.materialCode(pp.getMrp().getMaterial().getCode()).materialName(pp.getMrp().getMaterial().getName())
				.amount(pp.getDetailPurchaseOrder().getAmount()).dueDate(pp.getDueDate())
				.status(mi.getStatus()).transactionStatus(mi.getTransactionStatus()).build();
		
		log.info("자재입고 DTO 결과 보기 >>> " + materialInDTO);
		
		//자재입고 테이블에 등록X -> 세부구매발주서기준으로 입고 찾아서 DTO 만들기(입고상태, 발행상태 null로 만들기)
		DetailPurchaseOrder dpo= detailPurchaseOrderRepository.findById(4).get();
		ProcurementPlan pp2= procurementPlanRepository.findByDetailPurchaseOrder(dpo);
		
		MaterialInDTO materialInDTO2= MaterialInDTO.builder()
				.no(dpo.getPurchaseOrder().getNo()).code(dpo.getCode())
				.materialCode(pp2.getMrp().getMaterial().getCode()).materialName(pp2.getMrp().getMaterial().getName())
				.amount(pp2.getDetailPurchaseOrder().getAmount()).dueDate(pp2.getDueDate()).build();
		
		log.info("자재입고 DTO2 결과 보기 >>> " + materialInDTO2);
	}
	
	@Transactional
	@Test
	public void getJoin() {
		//log.info("조인이 되나 >>> " + materialInRepository.getJoinDpo());
		
		//입고테이블에 등록된 List(입고상태, 발행상태 기준으로 정렬)
		//List<MaterialIn> miList= materialInRepository.getJoinDpo();
		
		
		//세부구매발주서 전체List
		List<DetailPurchaseOrder> dpoList= detailPurchaseOrderRepository.findAll();
		
		List<MaterialInDTO> MaterialInDTOList= new ArrayList<>();
		List<MaterialInDTO> nullMaterialInDTOList= new ArrayList<>();
		List<MaterialInDTO> notNullMaterialInDTOList= new ArrayList<>();
		MaterialInDTO materialInDTO= null;
		
		List<ProcurementPlan> ppList= new ArrayList<>();
		List<ProcurementPlan> ppList2= new ArrayList<>();
		
		List<MaterialIn> miList= new ArrayList<>();
		
		//세부구매발주서목록으로 조달계획List가져오기
		for(int i=0; i<dpoList.size(); i++) {
			ppList.add(procurementPlanRepository.findByDetailPurchaseOrder(dpoList.get(i)));
	
			//세부구매발주서코드가 자재입고 테이블에 존재하면 join해서 miList에 넣기
			if(materialInRepository.findByDetailPurchaseOrder(dpoList.get(i)) != null) {
				 miList= materialInRepository.getJoinDpo();
			}
		}
		
		//miList에 존재하는 세부구매발주서코드로 조달계획list2 만들기
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
			log.info(i + "번 materialIn에 존재 O miDTO 보기 >>> " + materialInDTO);		
		}
		
		log.info("ppList >>> " + ppList + ", 사이즈 >>> " + ppList.size()); //4
		log.info("4번 존재 안함 >>> " + materialInRepository.existsById(4)); //false
		
		for(int i=0; i<dpoList.size(); i++) { //0~3
			log.info("dpoList >>> " + dpoList);
			
			//materialIn에 존재 X 때 설정
			if(!materialInRepository.existsByDetailPurchaseOrder(dpoList.get(i))){ //1~4 현재 dpocode 2,4가 나와야하는데 3,4가 나옴
				materialInDTO=  MaterialInDTO.builder().noStr(noStr(dpoList.get(i).getPurchaseOrder().getNo())).codeStr(codeStr(dpoList.get(i).getCode()))
						.dueDate(ppList.get(i).getDueDate()).materialCode(ppList.get(i).getMrp().getMaterial().getCode())
						.materialName(ppList.get(i).getMrp().getMaterial().getName()).amount(ppList.get(i).getDetailPurchaseOrder().getAmount())
						.status(null).transactionStatus(null).inDate(null).build();
				nullMaterialInDTOList.add(materialInDTO);
				log.info(i + "번 materialIn에 존재 X miDTO 보기 >>> " + materialInDTO);		
			}	
		}
		log.info("nullMaterialInDTOList 보기 >>> " + nullMaterialInDTOList);
		log.info("notNullMaterialInDTOList 보기 >>> " + notNullMaterialInDTOList);
		
		//입고상태 null값 -> 정렬된 List 순으로 넣기
		for(MaterialInDTO dto : nullMaterialInDTOList) {
			MaterialInDTOList.add(dto);
		}
		for(MaterialInDTO dto : notNullMaterialInDTOList) {
			MaterialInDTOList.add(dto);
		}
		
		log.info("materialInDTOList 보기 >>> " + MaterialInDTOList + ", 몇개 나오지 >>> " + MaterialInDTOList.size());
	}
	
	//입고를 취소 했을 때 출고테이블에 등록이 되어야함
	@Transactional
	@Commit
	@Test
	public void materialInCancleTest() {
		//입고 취소된 입고코드
		MaterialIn mi= materialInRepository.findById(7).get();
		
		if(mi.getStatus() == false) {
			DetailPurchaseOrder dpo= mi.getDetailPurchaseOrder();
			ProcurementPlan pp= procurementPlanRepository.findByDetailPurchaseOrder(dpo);
			
			MaterialOut materialOut= MaterialOut.builder().status(0).mrp(pp.getMrp()).build();
			
			materialOutRepository.save(materialOut);			
		}
	}
	
	@Transactional
	@Test
	public void getLastTest() {
		//마지막행 가져오기
		log.info("마지막행 가져오기 >>> " + materialInRepository.getLastCode());
	}
	
}