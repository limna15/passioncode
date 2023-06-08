package com.passioncode.procurementsystem.repository;

import java.text.DateFormat;

import java.text.ParseException;
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

import com.passioncode.procurementsystem.dto.ProcurementPlanDTO;
import com.passioncode.procurementsystem.entity.Contract;
import com.passioncode.procurementsystem.entity.MRP;
import com.passioncode.procurementsystem.entity.Material;
import com.passioncode.procurementsystem.entity.ProcurementPlan;

import lombok.extern.log4j.Log4j2;

@SpringBootTest
@Log4j2
public class ProcurementPlanRepositoryTests {
	
	@Autowired
	ProcurementPlanRepository procurementPlanRepository;
	
	@Autowired
	MRPRepository mrpRepository;	
	
	@Autowired
	MaterialRepository materialRepository;
	
	@Autowired
	ContractRepository contractRepository;
	
//	@Transactional
	@Test
	public void InsertTest() {
		//폼목명으로 품목테이블의 품목코드 찾는 법
		Collection<Material> materialTest = materialRepository.findByNameContainingIgnoreCase("gear");
		log.info("어디 대소문자 구별보자 : "+materialTest);
		ArrayList<Material> materialTest2=(ArrayList<Material>) materialTest;
		log.info("이렇게 보면 되나?? : "+materialTest2.get(0).getCode());
		
		//품목코드로 mrp테이블 찾는법
		Collection<MRP> mrpTest = mrpRepository.findBymaterialCode(materialTest2.get(0).getCode());
		ArrayList<MRP> mrpTest2 = (ArrayList<MRP>) mrpTest;
		log.info("어디한번 보자!!!!! : "+ mrpTest2);
		
		//mrp 코드로 mrp테이블 찾는법
		Optional<MRP> result= mrpRepository.findById(1);
		MRP mrp=result.get();
		log.info("mrp코드로 찾은 mrp 봐보자 : "+mrp);
//		log.info("mrp의 material 보자! : "+ mrp.getMaterial());  이런식의 직접적인 외래키를 읽으려면(지연로딩된거! 그러면 어노테이션 @Transactional 필요!
		//하지만 그 외래키를 이용해서 쓰는거에는 어노테이션 필요 X
		
		//mrpdate 소요일 불러오기
		Date mrpdate=mrp.getDate();
		
		DateFormat simpleDateFormat = new SimpleDateFormat("yyyy년 MM월 dd일"); 		
		Calendar cal = Calendar.getInstance();
		cal.setTime(mrpdate);
		mrpdate=cal.getTime();
		log.info("캘린더 셋팅된 mrp 소요일 : "+simpleDateFormat.format(mrpdate));
		
		//소요일에서 기본값 2일 빼서, 납기예정일 만들기
		cal.add(Calendar.DATE,-2);
		
		Date duedate=cal.getTime();
		log.info("제대로 뺀, 납기예정일 : "+simpleDateFormat.format(duedate));
		
		//mrp의 품목코드를 이용해서 해당 계약서 찾기
		log.info("material이용해서 찾아본건데... 계약서 결과 보자 : "+ contractRepository.findByMaterial(materialTest2.get(0)));
//		log.info("mrp의 material 보자! : "+ mrp.getMaterial());  이런식의 직접적인 외래키를 읽으려면(지연로딩된거! 그러면 어노테이션 @Transactional 필요!
		log.info("mrp의 material 이용해서 계약서 결과 보자 : "+ contractRepository.findByMaterial(mrp.getMaterial()));  //하지만 이런식의 외래키 이용해서 쓸때는 어노테이션 필요X
		Collection<Contract> contractTest = contractRepository.findByMaterial(mrp.getMaterial());
		ArrayList<Contract> contractTest2 =(ArrayList<Contract>) contractTest;
		//[Contract(no=4, supplyLt=5, unitPrice=100, dealCondition=null, contractFile=테스트중), 
		// Contract(no=8, supplyLt=13, unitPrice=20000, dealCondition=null, contractFile=쿼리로 테스트중)]
		log.info("2개의 계약서중 첫번째꺼로 읽자! : "+contractTest2.get(0));
		
		//mrp의 소요량 읽어오기 => 필요수량
		Integer amount = mrp.getAmount();
		log.info("읽어온 소요량=필요수량 읽어보자 : "+amount);
		
		//계약서의 품목공급LT를 이용해서 조달납기예정일-품목공급LT => 최소발주일 만들기
		log.info("아직 -2일 한상태 그대로인지 봐보자 : "+simpleDateFormat.format(cal.getTime()));
		cal.add(Calendar.DATE, -contractTest2.get(0).getSupplyLt());
		Date minimumOrderDate=cal.getTime();
		log.info("최소발주일 계산 된거 봐보자 : "+simpleDateFormat.format(minimumOrderDate));
	
		//조달계획 등록일 
		log.info("조달계획 등록일, localdatetime으로 현재시간 읽어보자 : "+LocalDateTime.now());
		
		
		//화면
		//품목코드, 품목명, 소요공정, 소요일, 소요량 
		//협력회사, 품목공급LT
		//조달납기예정일, 최소발주일, 필요수량, 계약상태, 조달계획 등록상태, 조달계획 진행사항
		
		//조달계획코드, 자재소요계획코드(외래키), 계약서번호(외래키),
		//필요수량, 조달납기예정일, 최소발주일, 조달계획 등록일, 조달계획 완료일, 발주코드(외래키)		
		ProcurementPlan procurementPlan=ProcurementPlan.builder().mrp(mrp).contract(contractTest2.get(0)).amount(mrp.getAmount())
										.dueDate(duedate).minimumOrderDate(minimumOrderDate).registerDate(LocalDateTime.now()).build();
//		log.info("몬데... 왜 안돼...?? "+procurementPlan);
		procurementPlanRepository.save(procurementPlan);	
	}
	
	@Transactional
	@Test
	public void ppRegisterStatusTest() {
		//1번 mrp = 조달계획 등록 완료된 mrp
		//2번 mrp = 조달계획 등록 미완료된 mrp
		MRP mrp = mrpRepository.findById(1).get(); 
		log.info("MRP로 존재여부 체크해보자! : "+procurementPlanRepository.existsByMrp(mrp));
		MRP mrp2 = mrpRepository.findById(2).get(); 
		log.info("MRP로 존재여부 체크해보자! : "+procurementPlanRepository.existsByMrp(mrp2));
	}
	
	
	@Test
	public void ppProgressTest() {
		/*
		 * 등록 후 발주코드 존재X : 발주 예정, 
		 * 등록 후 발주코드 존재O, 조달완료일 존재X : 조달 진행 중, 
		 * 등록 후 조달완료일 존재O : 조달 완료 
		 */
		
//		ProcurementPlan procurementPlan = procurementPlanRepository.findById(1).get();	//발주예정
//		ProcurementPlan procurementPlan = procurementPlanRepository.findById(2).get();	//조달 완료
		ProcurementPlan procurementPlan = procurementPlanRepository.findById(3).get();	//조달 진행 중
		String ppProgress = null;
		
		if(procurementPlan.getCompletionDate()==null) {				//조달완료일 존재X
			if(procurementPlan.getDetailPurchaseOrder()==null) {	//발주코드 존재X
				ppProgress="발주 예정";
			}else {													//발주코드 존재O
				ppProgress="조달 진행 중";
			}
		}else {														//조달완료일 존재O
			ppProgress="조달 완료";
		}
		
		log.info("조달계획 진행사항 좀 봐보자 : "+ppProgress);
	}
	
//	@Test
//	public void ppProgressCheckTest() {
//		ProcurementPlan procurementPlan = procurementPlanRepository.findById(3).get();	//조달 진행 중
//		ProcurementPlanDTO procurementPlanDTO = new ProcurementPlanDTO();
//		log.info("이렇게 테스트하면 되겠지? 조달계획 진행사항 보자! : "+procurementPlanDTO.ppProgressCheck(procurementPlan));		
//	}
	
	public String ppProgressCheck(ProcurementPlan procurementPlan) {
		String ppProgress = null;
		
		if(procurementPlan.getCompletionDate()==null) {				//조달완료일 존재X
			if(procurementPlan.getDetailPurchaseOrder()==null) {	//발주코드 존재X
				ppProgress="발주 예정";
			}else {													//발주코드 존재O
				ppProgress="조달 진행 중";
			}
		}else {														//조달완료일 존재O
			ppProgress="조달 완료";
		}
		return ppProgress;
	}
	
	@Test
	public void ppProgressCheckTest2() {
		ProcurementPlan procurementPlan = procurementPlanRepository.findById(3).get();	//조달 진행 중
		log.info("조달계획 진행사항 결과 보자 : "+ppProgressCheck(procurementPlan));	
			}
	
	@Transactional
	@Test
	public void ProcurementPlanDTOTest() {
		//화면
		//품목코드, 품목명, 소요공정, 소요일, 소요량 
		//협력회사, 품목공급LT
		//조달납기예정일, 최소발주일, 필요수량, 계약상태, 조달계획 등록상태, 조달계획 진행사항
		
		//조달계획코드, 자재소요계획코드(외래키), 계약서번호(외래키),
		//필요수량, 조달납기예정일, 최소발주일, 조달계획 등록일, 조달계획 완료일, 발주코드(외래키)	
		
		//조달계획 등록 완료인 조달계획DTO 만들기 (사실상 조달계획 엔티티를 이용해서 만들면 등록완료된 DTO)
		ProcurementPlan procurementPlan = procurementPlanRepository.findById(1).get();		
		ProcurementPlanDTO procurementPlanDTO = ProcurementPlanDTO.builder().materialCode(procurementPlan.getMrp().getMaterial().getCode())
												.materialName(procurementPlan.getMrp().getMaterial().getName()).process(procurementPlan.getMrp().getProcess())
												.mrpdate(procurementPlan.getMrp().getDate()).mrpAmount(procurementPlan.getMrp().getAmount())
												.companyName(procurementPlan.getContract().getCompany().getName()).supplyLt(procurementPlan.getContract().getSupplyLt())
												.dueDate(procurementPlan.getDueDate()).minimumOrderDate(procurementPlan.getMinimumOrderDate())
												.ppAmount(procurementPlan.getAmount()).contractStatus(contractRepository.existsByMaterial(procurementPlan.getMrp().getMaterial()))
												.ppRegisterStatus(true).ppProgress(ppProgressCheck(procurementPlan)).build();
		
		log.info("조달계획 DTO 결과 값 : "+procurementPlanDTO);
		
		//조달계획 등록 미완료인 조달계획DTO 만들기 (조달계획 테이블에 없는건 다 null로 해서 만들기)
		//mrp 2번 조달계획 등록 안된 상태
		MRP mrp = mrpRepository.findById(2).get();
		
		ProcurementPlanDTO procurementPlanDTO2 = ProcurementPlanDTO.builder().materialCode(mrp.getMaterial().getCode()).materialName(mrp.getMaterial().getName())
													.process(mrp.getProcess()).mrpdate(mrp.getDate()).mrpAmount(mrp.getAmount())
													.contractStatus(contractRepository.existsByMaterial(mrp.getMaterial())).ppRegisterStatus(false).build();				

		log.info("조달계획 DTO 결과 값 : "+procurementPlanDTO2);
		
		/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		
		//MRP 기준으로 조달계획을 찾아서 DTO 만들기
//		MRP mrp2 = mrpRepository.findById(1).get();	//조달계획 등록 완료인 mrp
		MRP mrp2 = mrpRepository.findById(2).get();	//조달계획 등록 미완료인 mrp
		ProcurementPlan procurementPlan2 = procurementPlanRepository.findByMrp(mrp2);
		
		List<ProcurementPlanDTO> ppDTOList = new ArrayList<>();
		ProcurementPlanDTO procurementPlanDTO3 = null;
		
		//procurementPlan2 가 존재할때, 조달계획 등록 완료된 상태
		if(procurementPlan2!=null) {
			procurementPlanDTO3 = ProcurementPlanDTO.builder().materialCode(procurementPlan2.getMrp().getMaterial().getCode())
									.materialName(procurementPlan2.getMrp().getMaterial().getName()).process(procurementPlan2.getMrp().getProcess())
									.mrpdate(procurementPlan2.getMrp().getDate()).mrpAmount(procurementPlan2.getMrp().getAmount())
									.companyName(procurementPlan2.getContract().getCompany().getName()).supplyLt(procurementPlan2.getContract().getSupplyLt())
									.dueDate(procurementPlan2.getDueDate()).minimumOrderDate(procurementPlan2.getMinimumOrderDate())
									.ppAmount(procurementPlan2.getAmount()).contractStatus(contractRepository.existsByMaterial(procurementPlan2.getMrp().getMaterial()))
									.ppRegisterStatus(true).ppProgress(ppProgressCheck(procurementPlan2)).build();
			ppDTOList.add(procurementPlanDTO3);
		}else {  ////procurementPlan2 가 존재X, 조달계획 등록 미완료된 상태
			procurementPlanDTO3 = ProcurementPlanDTO.builder().materialCode(mrp.getMaterial().getCode()).materialName(mrp.getMaterial().getName())
									.process(mrp.getProcess()).mrpdate(mrp.getDate()).mrpAmount(mrp.getAmount())
									.contractStatus(contractRepository.existsByMaterial(mrp.getMaterial())).ppRegisterStatus(false).build();	
			ppDTOList.add(procurementPlanDTO3);
		}
		
		log.info("리스트까지 이건 볼필요 없겠다! 바로 값 보자 : "+procurementPlanDTO3);
		log.info("리스트까지 그래도 봐보자 : "+ppDTOList);
		
		
	}
	
	
	
	

}
