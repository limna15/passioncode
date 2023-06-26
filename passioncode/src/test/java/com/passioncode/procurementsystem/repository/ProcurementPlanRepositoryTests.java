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
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
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
		
		// 소요일 - 납기예정일 해서 Integer 구하기!
		Date mrpdate2 = mrpdate;
		log.info("위에서 받아온 mrp 소요일 : "+mrpdate2);
		Date duedate2 = duedate;
		log.info("위에서 받아온 납기예정일 : "+duedate2);
		
//		Integer test = duedate2.compareTo(mrpdate2);
//		log.info("오오오!! 이렇게 날짜 차이 볼수 있으려나??? : "+test);
		
		long timeDifferent = mrpdate2.getTime() - duedate2.getTime();
		log.info("결과뺀 시간 차이 봐보자 : "+timeDifferent);
		
		TimeUnit time = TimeUnit.DAYS;
		Long different = time.convert(timeDifferent, TimeUnit.MILLISECONDS);
		log.info("이제 날짜 차이 나오려나?? : "+different);
		Integer differentToInteger = different.intValue();
		log.info("Integer로 잘 변환되었나? : "+differentToInteger);
		
		
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
		//품목코드, 품목명, 소요공정, 소요일, 소요량, 협력회사, 품목공급LT, 조달납기예정일, 최소발주일, 필요수량, 계약상태, 조달계획 등록상태, 조달계획 진행사항 
		//조달계획코드, 자재소요계획코드, 사업자등록번호, 계약서번호 // 기본 여유기간
		
		//조달계획코드, 자재소요계획코드(외래키), 계약서번호(외래키),
		//필요수량, 조달납기예정일, 최소발주일, 조달계획 등록일, 조달계획 완료일, 발주코드(외래키)		
		ProcurementPlan procurementPlan=ProcurementPlan.builder().mrp(mrp).contract(contractTest2.get(0)).amount(mrp.getAmount())
										.dueDate(duedate).minimumOrderDate(minimumOrderDate).registerDate(LocalDateTime.now()).build();
//		log.info("몬데... 왜 안돼...?? "+procurementPlan);
		procurementPlanRepository.save(procurementPlan);	
	}
	
	/**
	 * 최소발주일 계산해주기 <br>
	 * 조달납기예정일 - 품목공급LT
	 * @param duedate
	 * @param supplyLt
	 * @return
	 */
	public Date makeMinimumOrderDate(Date duedate,Integer supplyLt) {
		DateFormat simpleDateFormat = new SimpleDateFormat("yyyy년 MM월 dd일"); 		
		Calendar cal = Calendar.getInstance();
		cal.setTime(duedate);
		duedate=cal.getTime();
		log.info("캘린더 셋팅된 조달납기예정일 : "+simpleDateFormat.format(duedate));
		
		//조달납기예정일 - 품목공급LT => 최소발주일 만들기
		cal.add(Calendar.DATE,-supplyLt);
		
		Date minimumOrderDate=cal.getTime();
		log.info("제대로 뺀, 최소발주일 : "+simpleDateFormat.format(minimumOrderDate));
		
		return minimumOrderDate;
	}
	
	/**
	 * 조달납기예정일 계산해주기 <br>
	 * 소요일 - 기본 여유기간
	 * @param mrpdate
	 * @param freePeriod
	 * @return
	 */
	public Date makeDuedate(Date mrpdate,Integer freePeriod) {
		DateFormat simpleDateFormat = new SimpleDateFormat("yyyy년 MM월 dd일"); 		
		Calendar cal = Calendar.getInstance();
		cal.setTime(mrpdate);
		mrpdate=cal.getTime();
		log.info("캘린더 셋팅된 mrp 소요일 : "+simpleDateFormat.format(mrpdate));
		
		//소요일 - 기본 여유기간 => 납기예정일 만들기
		cal.add(Calendar.DATE,-freePeriod);
		
		Date duedate=cal.getTime();
		log.info("제대로 뺀, 납기예정일 : "+simpleDateFormat.format(duedate));
		
		return duedate;
	}
	
	/**
	 * 기본 여유기간 계산해주기 <br>
	 * 소요일 - 조달납기예정일
	 * @param mrpdate
	 * @param duedate
	 * @return
	 */
	public Integer makefreePeriod(Date mrpdate,Date duedate) {
		Long timeDifferent = mrpdate.getTime() - duedate.getTime();
//		log.info("결과뺀 시간 차이 봐보자 : "+timeDifferent);		
		TimeUnit time = TimeUnit.DAYS;
		Long different = time.convert(timeDifferent, TimeUnit.MILLISECONDS);
//		log.info("이제 날짜 차이 나오려나?? : "+different);
		Integer differentToInteger = different.intValue();
//		log.info("Integer로 잘 변환되었나? : "+differentToInteger);
		return differentToInteger;
	}
	
	/**
	 * 품목 엔티티를 이용하여 계약상태 체크하기 <br> 
	 * 완료 : 계약상태 O, 미완료 : 계약상태 X
	 * @param material
	 * @return
	 */
	public String contractStatusCheck(Material material) {		
		//완료 : 계약상태 O, 미완료 : 계약상태 X
		return contractRepository.existsByMaterial(material) ? "완료" : "미완료";
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
		 * 등록 후 발주코드 존재X, 조달완료일 존재X : 발주 예정, 
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
		
	/**
	 * 조달계획 진행사항 만들어주는 메소드 (조달계획을 이용해서) <br>
	 * 등록 후 발주코드 존재X, 조달완료일 존재X : 발주 예정  <br>
	 * 등록 후 발주코드 존재O, 조달완료일 존재X : 조달 진행 중 <br> 
	 * 등록 후 조달완료일 존재O : 조달 완료  <br>
	 * @param procurementPlan
	 * @return
	 */
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
	public void ppProgressCheckTest() {
		ProcurementPlan procurementPlan = procurementPlanRepository.findById(3).get();	//조달 진행 중
		log.info("조달계획 진행사항 결과 보자 : "+ppProgressCheck(procurementPlan));	
	}
	
	@Transactional
	@Test
	public void ProcurementPlanDTOTest() {
		//화면
		//품목코드, 품목명, 소요공정, 소요일, 소요량, 협력회사, 품목공급LT, 조달납기예정일, 최소발주일, 필요수량, 계약상태, 조달계획 등록상태, 조달계획 진행사항 
		//조달계획코드, 자재소요계획코드, 사업자등록번호, 계약서번호 // 기본 여유기간
		
		//조달계획코드, 자재소요계획코드(외래키), 계약서번호(외래키),
		//필요수량, 조달납기예정일, 최소발주일, 조달계획 등록일, 조달계획 완료일, 발주코드(외래키)	
		
		//조달계획 등록 완료인 조달계획DTO 만들기 (사실상 조달계획 엔티티를 이용해서 만들면 등록완료된 DTO)
		ProcurementPlan procurementPlan = procurementPlanRepository.findById(1).get();		
		ProcurementPlanDTO procurementPlanDTO = ProcurementPlanDTO.builder().materialCode(procurementPlan.getMrp().getMaterial().getCode()).ppcode(procurementPlan.getCode())
																			.materialName(procurementPlan.getMrp().getMaterial().getName())
																			.process(procurementPlan.getMrp().getProcess())
																			.mrpdate(procurementPlan.getMrp().getDate()).mrpAmount(procurementPlan.getMrp().getAmount())
																			.freePeriod(makefreePeriod(procurementPlan.getMrp().getDate(),procurementPlan.getDueDate()))
																			.companyName(procurementPlan.getContract().getCompany().getName())
																			.supplyLt(procurementPlan.getContract().getSupplyLt())
																			.dueDate(procurementPlan.getDueDate()).minimumOrderDate(procurementPlan.getMinimumOrderDate())
																			.ppAmount(procurementPlan.getAmount()).completionDate(procurementPlan.getCompletionDate())
																			.contractStatus(contractStatusCheck(procurementPlan.getMrp().getMaterial()))
																			.ppRegisterStatus("완료").ppProgress(ppProgressCheck(procurementPlan))
																			.mrpCode(procurementPlan.getMrp().getCode())
																			.companyNo(procurementPlan.getContract().getCompany().getNo())
																			.contractNo(procurementPlan.getContract().getNo()).build();
		
		log.info("조달계획 DTO 결과 값 : "+procurementPlanDTO);
		
		//조달계획 등록 미완료인 조달계획DTO 만들기 (조달계획 테이블에 없는건 다 null로 해서 만들기)
		//mrp 2번 조달계획 등록 안된 상태
		MRP mrp = mrpRepository.findById(2).get();
		
		ProcurementPlanDTO procurementPlanDTO2 = ProcurementPlanDTO.builder().materialCode(mrp.getMaterial().getCode()).materialName(mrp.getMaterial().getName())
													.process(mrp.getProcess()).mrpdate(mrp.getDate()).mrpAmount(mrp.getAmount())
													.contractStatus(contractStatusCheck(mrp.getMaterial())).ppRegisterStatus("미완료")
													.mrpCode(mrp.getCode()).build();				

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
			procurementPlanDTO3 = ProcurementPlanDTO.builder().materialCode(procurementPlan2.getMrp().getMaterial().getCode()).ppcode(procurementPlan2.getCode())
																.materialName(procurementPlan2.getMrp().getMaterial().getName()).process(procurementPlan2.getMrp().getProcess())
																.mrpdate(procurementPlan2.getMrp().getDate()).mrpAmount(procurementPlan2.getMrp().getAmount())
																.freePeriod(makefreePeriod(procurementPlan2.getMrp().getDate(),procurementPlan2.getDueDate()))
																.companyName(procurementPlan2.getContract().getCompany().getName())
																.supplyLt(procurementPlan2.getContract().getSupplyLt())
																.dueDate(procurementPlan2.getDueDate()).minimumOrderDate(procurementPlan2.getMinimumOrderDate())
																.ppAmount(procurementPlan2.getAmount()).completionDate(procurementPlan2.getCompletionDate())
																.contractStatus(contractStatusCheck(procurementPlan2.getMrp().getMaterial()))
																.ppRegisterStatus("완료").ppProgress(ppProgressCheck(procurementPlan2))
																.mrpCode(procurementPlan2.getMrp().getCode()).companyNo(procurementPlan2.getContract().getCompany().getNo())
																.contractNo(procurementPlan2.getContract().getNo()).build();
			ppDTOList.add(procurementPlanDTO3);
		}else {  ////procurementPlan2 가 존재X, 조달계획 등록 미완료된 상태
			procurementPlanDTO3 = ProcurementPlanDTO.builder().materialCode(mrp.getMaterial().getCode()).materialName(mrp.getMaterial().getName())
									.process(mrp.getProcess()).mrpdate(mrp.getDate()).mrpAmount(mrp.getAmount())
									.contractStatus(contractStatusCheck(mrp.getMaterial())).ppRegisterStatus("미완료")
									.mrpCode(mrp.getCode()).build();	
			ppDTOList.add(procurementPlanDTO3);
		}
		
		log.info("리스트까지 이건 볼필요 없겠다! 바로 값 보자 : "+procurementPlanDTO3);
		log.info("리스트까지 그래도 봐보자 : "+ppDTOList);		
	}
	
	@Transactional
	@Commit
	@Test
	public void InsertByDTOTest() {
		//품목코드, 품목명, 소요공정, 소요일, 소요량, 협력회사, 품목공급LT, 조달납기예정일, 최소발주일, 필요수량, 계약상태, 조달계획 등록상태, 조달계획 진행사항
		//조달계획코드, 자재소요계획코드, 사업자등록번호, 계약서번호  // 기본 여유기간
		MRP mrp = mrpRepository.findById(16).get();
		Contract contract = contractRepository.findById(5).get();
		
		//기본여유기간 화면에 숨겨뒀다가 값 받아오자!
		Integer freePeriod = 2;
		
//		Date duedate = makeDuedate(mrp.getDate(), freePeriod);
//		log.info("제대로 최소발주일 계산 되는건가? : "+makeMinimumOrderDate(duedate, contract.getSupplyLt()));
		
		//조달계획을 등록한다는건! 계약상태 = 완료, 조달계획 등록상태 = 완료, 조달계획 진행사항 =  발주 예정
		//화면에서 계약상태, 조달계획 등록상태 숨겨서 완료 로 보내주고, 조달계획 진행사항 발주 예정 으로 숨겨서 보내주자!
		
		//조달계획 등록하는 상황이라 조달계획코드는 모른다! 없이 만들고 엔티티로 만들어주면, save에서 자동으로 만들어준다!
		ProcurementPlanDTO procurementPlanDTO = ProcurementPlanDTO.builder().materialCode(mrp.getMaterial().getCode()).materialName(mrp.getMaterial().getName())
																			.process(mrp.getProcess()).mrpdate(mrp.getDate()).mrpAmount(mrp.getAmount())
																			.companyName(contract.getCompany().getName()).supplyLt(contract.getSupplyLt())
																			.dueDate(makeDuedate(mrp.getDate(), freePeriod))
																			.minimumOrderDate(makeMinimumOrderDate(makeDuedate(mrp.getDate(), freePeriod), contract.getSupplyLt()))
																			.ppAmount(mrp.getAmount()).contractStatus("완료").ppRegisterStatus("완료").ppProgress("발주 예정")
																			.mrpCode(mrp.getCode()).companyNo(contract.getCompany().getNo()).contractNo(contract.getNo())
																			.freePeriod(freePeriod).build();
		log.info("제대로 만들어 진건가? : "+procurementPlanDTO);
		
		//조달계획코드, 자재소요계획코드(외래키), 계약서번호(외래키),
		//필요수량, 조달납기예정일, 최소발주일, 조달계획 등록일, 조달계획 완료일, 발주코드(외래키)	
//		ProcurementPlan procurementPlan = ProcurementPlan.builder().mrp(mrpRepository.findById(procurementPlanDTO.getMrpCode()).get())
//																	.contract(contractRepository.findById(procurementPlanDTO.getContractNo()).get())
//																	.amount(procurementPlanDTO.getPpAmount()).dueDate(procurementPlanDTO.getDueDate())
//																	.minimumOrderDate(procurementPlanDTO.getMinimumOrderDate()).build();
//		log.info("제대로 만들어 진건가? : "+procurementPlan);
		
//		ProcurementPlan procurementPlan = ProcurementPlan.builder().code(procurementPlanDTO.getPpcode()).mrp(mrpRepository.findById(procurementPlanDTO.getMrpCode()).get())
//															.contract(contractRepository.findById(procurementPlanDTO.getContractNo()).get())
//															.amount(procurementPlanDTO.getPpAmount())
//															.dueDate(procurementPlanDTO.getDueDate()).minimumOrderDate(procurementPlanDTO.getMinimumOrderDate())
//															.registerDate(procurementPlanRepository.findById(procurementPlanDTO.getPpcode()).orElse(null).getRegisterDate())
//															.completionDate(procurementPlanRepository.findById(procurementPlanDTO.getPpcode()).orElse(null).getCompletionDate())
//															.detailPurchaseOrder(procurementPlanRepository.findById(procurementPlanDTO.getPpcode()).orElse(null).getDetailPurchaseOrder())
//															.build();	
//		
//		log.info("제대로 만들어 진건가? : "+procurementPlan);
		
		log.info("값들 한번 찍어 보자 : "+procurementPlanDTO.getPpcode());
		log.info("값들 한번 찍어 보자 : "+procurementPlanRepository.findById(procurementPlanDTO.getPpcode()).orElse(null));
//		log.info("값들 한번 찍어 보자 : "+procurementPlanRepository.findById(procurementPlanDTO.getPpcode()).orElse(null).getRegisterDate());
//		log.info("값들 한번 찍어 보자 : "+procurementPlanRepository.findById(procurementPlanDTO.getPpcode()).orElse(null).getCompletionDate());
//		log.info("값들 한번 찍어 보자 : "+procurementPlanRepository.findById(procurementPlanDTO.getPpcode()).orElse(null).getDetailPurchaseOrder());
//		
//		procurementPlanRepository.save(procurementPlan);		
		
	}
	
	@Transactional
	@Commit
	@Test
	public void ModifyByDTOTest() {
		//품목코드, 품목명, 소요공정, 소요일, 소요량, 협력회사, 품목공급LT, 조달납기예정일, 최소발주일, 필요수량, 계약상태, 조달계획 등록상태, 조달계획 진행사항
		//조달계획코드, 자재소요계획코드, 사업자등록번호, 계약서번호  // 기본 여유기간
		MRP mrp = mrpRepository.findById(16).get();
		Contract contract = contractRepository.findById(5).get();
		
		//기본여유기간 화면에 숨겨뒀다가 값 받아오자!
		Integer freePeriod = 3;
		
		//수정을 발주예정인것만 !! 가능하게!!!
		
		//조달계획을 수정한다는건! 계약상태 = 완료, 조달계획 등록상태 = 완료, 조달계획 진행사항 = "발주 예정"
		//화면에서 계약상태, 조달계획 등록상태 숨겨서 완료 로 보내주자, 조달계획 진행사항 발주 예정 으로 보내주자
		
		//조달계획 수정하는 상황이라, 조달계획 코드값이 들어간다!
		ProcurementPlanDTO procurementPlanDTO = ProcurementPlanDTO.builder().ppcode(15).materialCode(mrp.getMaterial().getCode()).materialName(mrp.getMaterial().getName())
																			.process(mrp.getProcess()).mrpdate(mrp.getDate()).mrpAmount(mrp.getAmount())
																			.companyName(contract.getCompany().getName()).supplyLt(contract.getSupplyLt())
																			.dueDate(makeDuedate(mrp.getDate(), freePeriod))
																			.minimumOrderDate(makeMinimumOrderDate(makeDuedate(mrp.getDate(), freePeriod), contract.getSupplyLt()))
																			.ppAmount(mrp.getAmount()).contractStatus("완료").ppRegisterStatus("완료").ppProgress("발주 예정")
																			.mrpCode(mrp.getCode()).companyNo(contract.getCompany().getNo()).contractNo(contract.getNo())
																			.freePeriod(freePeriod).build();
		log.info("제대로 만들어 진건가? : "+procurementPlanDTO);
		
		ProcurementPlan procurementPlan = ProcurementPlan.builder().code(procurementPlanDTO.getPpcode())
																	.mrp(mrpRepository.findById(procurementPlanDTO.getMrpCode()).get())
																	.contract(contractRepository.findById(procurementPlanDTO.getContractNo()).get())
																	.amount(procurementPlanDTO.getPpAmount()).dueDate(procurementPlanDTO.getDueDate())
																	.minimumOrderDate(procurementPlanDTO.getMinimumOrderDate()).build();
		log.info("제대로 만들어 진건가? : "+procurementPlan);		
		
		procurementPlanRepository.save(procurementPlan);		
	}
	
	@Test
	public void deleteByDTOTest() {
		ProcurementPlanDTO procurementPlanDTO = ProcurementPlanDTO.builder().ppcode(15).build();		
		
		procurementPlanRepository.deleteById(procurementPlanDTO.getPpcode());
	}
	
	@Test
	public void getMRPJoinPPWithOrderTest() {
		log.info("어디 조인한 mrp리스트 잘가져오나 보자 : "+mrpRepository.getMRPJoinPPWithNotCompletePP());
//		log.info("조달계획 총 개수 보자 : "+procurementPlanRepository.countBy());
//		log.info("mrp 총 개수 보자 : "+mrpRepository.countBy());
//		log.info("조달계획 리스트 보자 : "+procurementPlanRepository.getPPJoinMRPWithOrder());
	}
	
	@Test
	public void  tobuilderTest() {
		ProcurementPlan procurementPlan = procurementPlanRepository.findById(1).get();
		log.info("현재 값 보자 : "+procurementPlan);
		LocalDateTime localDateTime = LocalDateTime.now();
		log.info("시간 보자 : "+localDateTime);
		procurementPlan = procurementPlan.toBuilder().completionDate(localDateTime).build();
		log.info("나중 값 보자 : "+procurementPlan);
	}
	
	
	@Test
	public void getPPJoinMRPByMaterialCodeTest() {
//		List<ProcurementPlan> list = procurementPlanRepository.getPPJoinMRPByMaterialCode("BP0001");
		List<ProcurementPlan> list = procurementPlanRepository.getPPJoinMRPByMaterialCode("BP0002");
		log.info("어디 제대로 리스트 읽나 보자 : "+list);
		if(list == null) {
			log.info("이게 그럼 null 인건가???");
		}
		if(list.size()==0) {
			log.info("아니네 null이 아니고 앞에서 선언해서 받아왔으니 있고, 길이가 0인거 겠지! 빈값인거");
		}
		
	}
	
	
	
	
	
}
