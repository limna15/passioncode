package com.passioncode.procurementsystem.service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;
import com.passioncode.procurementsystem.dto.ProcurementPlanDTO;
import com.passioncode.procurementsystem.entity.Contract;
import com.passioncode.procurementsystem.entity.MRP;
import com.passioncode.procurementsystem.entity.ProcurementPlan;
import lombok.extern.log4j.Log4j2;

@Log4j2
@SpringBootTest
public class ProcurementPlanServiceTests {
	
	@Autowired
	ProcurementPlanService procurementPlanService;
	
	@Transactional
	@Test
	public void mrpEntityToDTOTest() {
		//mrp 1번 조달계획 등록 완료된 mrp
		//mrp 17번 조달계획 등록 미완료된 mrp
		log.info("mrp를 이용해서 PPDTO 만들어보자 : "+procurementPlanService.mrpEntityToDTO(procurementPlanService.getMRP(1)));
		log.info("mrp를 이용해서 PPDTO 만들어보자 : "+procurementPlanService.mrpEntityToDTO(procurementPlanService.getMRP(17)));		
	}
	
	@Transactional
	@Test
	public void ppEntityToDTOTest() {
		log.info("조달계획 엔티티를 이용해서 PPDTO 만들기 : "+procurementPlanService.ppEntityToDTO(procurementPlanService.getProcurementPlan(1)));
	}
	
	@Transactional
	@Test
	public void dtoToEntityTest() {
		ProcurementPlanDTO procurementPlanDTO = procurementPlanService.ppEntityToDTO(procurementPlanService.getProcurementPlan(2));
		log.info("조달계획DTO를 이용해서 조달계획 엔티티 만들기 : "+procurementPlanService.dtoToEntity(procurementPlanDTO));
	}
	
	@Transactional
	@Test
	public void getDTOListTest() {
		log.info("모든 procurementPlanDTO 리스트 보기 : "+procurementPlanService.getDTOList());
		log.info("리스트 크기 봐보자 : "+procurementPlanService.getDTOList().size());
	}
	
	/**
	 * 기본 여유기간 계산해주기 <br>
	 * 소요일 - 조달납기예정일
	 * @param mrpDate
	 * @param duedate
	 * @return
	 */
	public Integer makefreePeriod(Date mrpDate,Date duedate) {
		Long timeDifferent = mrpDate.getTime() - duedate.getTime();
//		log.info("결과뺀 시간 차이 봐보자 : "+timeDifferent);		
		TimeUnit time = TimeUnit.DAYS;
		Long different = time.convert(timeDifferent, TimeUnit.MILLISECONDS);
//		log.info("이제 날짜 차이 나오려나?? : "+different);
		Integer differentToInteger = different.intValue();
//		log.info("Integer로 잘 변환되었나? : "+differentToInteger);
		return differentToInteger;
	}	
	
	/**
	 * 조달납기예정일 계산해주기 <br>
	 * 소요일 - 기본 여유기간
	 * @param mrpDate
	 * @param freePeriod
	 * @return
	 */
	public Date makeDuedate(Date mrpDate,Integer freePeriod) {
		DateFormat simpleDateFormat = new SimpleDateFormat("yyyy년 MM월 dd일"); 		
		Calendar cal = Calendar.getInstance();
		cal.setTime(mrpDate);
		mrpDate=cal.getTime();
//		log.info("캘린더 셋팅된 mrp 소요일 : "+simpleDateFormat.format(mrpDate));
		
		//소요일 - 기본 여유기간 => 납기예정일 만들기
		cal.add(Calendar.DATE,-freePeriod);
		
		Date duedate=cal.getTime();
//		log.info("제대로 뺀, 납기예정일 : "+simpleDateFormat.format(duedate));
		
		return duedate;
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
//		log.info("캘린더 셋팅된 조달납기예정일 : "+simpleDateFormat.format(duedate));
		
		//조달납기예정일 - 품목공급LT => 최소발주일 만들기
		cal.add(Calendar.DATE,-supplyLt);
		
		Date minimumOrderDate=cal.getTime();
//		log.info("제대로 뺀, 최소발주일 : "+simpleDateFormat.format(minimumOrderDate));
		
		return minimumOrderDate;
	}
	
	
	@Transactional
	@Commit
	@Test
	public void registerTest() {
		//품목코드, 품목명, 소요공정, 소요일, 소요량, 협력회사, 품목공급LT, 조달납기예정일, 최소발주일, 필요수량, 계약상태, 조달계획 등록상태, 조달계획 진행사항
		//조달계획코드, 자재소요계획코드, 사업자등록번호, 계약서번호  // 기본 여유기간
		MRP mrp = procurementPlanService.getMRP(17); 
		Contract contract = procurementPlanService.getContract(9);
		
		//기본여유기간 화면에 숨겨뒀다가 값 받아오자!
		Integer freePeriod = 2;
		
		//조달계획을 등록한다는건! 계약상태 = 완료, 조달계획 등록상태 = 완료, 조달계획 진행사항 =  발주 예정
		//화면에서 계약상태, 조달계획 등록상태 숨겨서 완료 로 보내주고, 조달계획 진행사항 발주 예정 으로 숨겨서 보내주자!
		
		//조달계획 등록하는 상황이라 조달계획코드는 모른다! 없이 만들고 엔티티로 만들어주면, save에서 자동으로 만들어준다!
		ProcurementPlanDTO procurementPlanDTO = ProcurementPlanDTO.builder().materialCode(mrp.getMaterial().getCode()).materialName(mrp.getMaterial().getName())
																			.process(mrp.getProcess()).mrpDate(mrp.getDate()).mrpAmount(mrp.getAmount())
																			.companyName(contract.getCompany().getName()).supplyLt(contract.getSupplyLt())
																			.dueDate(makeDuedate(mrp.getDate(), freePeriod))
																			.minimumOrderDate(makeMinimumOrderDate(makeDuedate(mrp.getDate(), freePeriod), contract.getSupplyLt()))
																			.ppAmount(mrp.getAmount()).contractStatus("완료").ppRegisterStatus("완료").ppProgress("발주 예정")
																			.mrpCode(mrp.getCode()).companyNo(contract.getCompany().getNo()).contractNo(contract.getNo())
																			.freePeriod(freePeriod).build();
		log.info("제대로 만들어 진건가? : "+procurementPlanDTO);
		
		//조달계획코드, 자재소요계획코드(외래키), 계약서번호(외래키),
		//필요수량, 조달납기예정일, 최소발주일, 조달계획 등록일, 조달계획 완료일, 발주코드(외래키)	
//		ProcurementPlan procurementPlan = ProcurementPlan.builder().mrp(procurementPlanService.getMRP(procurementPlanDTO.getMrpCode()))
//																	.contract(procurementPlanService.getContract(procurementPlanDTO.getContractNo()))
//																	.amount(procurementPlanDTO.getPpAmount()).dueDate(procurementPlanDTO.getDueDate())
//																	.minimumOrderDate(procurementPlanDTO.getMinimumOrderDate()).build();
//		log.info("제대로 만들어 진건가? : "+procurementPlan);
		log.info("제대로 만들어 진건가? : "+procurementPlanService.dtoToEntity(procurementPlanDTO));
		
		log.info("등론된거 봐보자 : "+procurementPlanService.register(procurementPlanDTO));		
	}
	
	@Transactional
	@Commit
	@Test
	public void modifyTest() {
		//품목코드, 품목명, 소요공정, 소요일, 소요량, 협력회사, 품목공급LT, 조달납기예정일, 최소발주일, 필요수량, 계약상태, 조달계획 등록상태, 조달계획 진행사항
		//조달계획코드, 자재소요계획코드, 사업자등록번호, 계약서번호  // 기본 여유기간
		MRP mrp = procurementPlanService.getMRP(17); 
		Contract contract = procurementPlanService.getContract(9);
		
		//기본여유기간 화면에 숨겨뒀다가 값 받아오자!
		Integer freePeriod = 3;
		
		//수정을 발주예정인것만 !! 가능하게!!!
		
		//조달계획을 수정한다는건! 계약상태 = 완료, 조달계획 등록상태 = 완료, 조달계획 진행사항 = "발주 예정"
		//화면에서 계약상태, 조달계획 등록상태 숨겨서 완료 로 보내주자, 조달계획 진행사항 발주 예정 으로 보내주자
		
		//조달계획 수정하는 상황이라, 조달계획 코드값이 들어간다!
		ProcurementPlanDTO procurementPlanDTO = ProcurementPlanDTO.builder().ppCode(18).materialCode(mrp.getMaterial().getCode()).materialName(mrp.getMaterial().getName())
																			.process(mrp.getProcess()).mrpDate(mrp.getDate()).mrpAmount(mrp.getAmount())
																			.companyName(contract.getCompany().getName()).supplyLt(contract.getSupplyLt())
																			.dueDate(makeDuedate(mrp.getDate(), freePeriod))
																			.minimumOrderDate(makeMinimumOrderDate(makeDuedate(mrp.getDate(), freePeriod), contract.getSupplyLt()))
																			.ppAmount(mrp.getAmount()).contractStatus("완료").ppRegisterStatus("완료").ppProgress("발주 예정")
																			.mrpCode(mrp.getCode()).companyNo(contract.getCompany().getNo()).contractNo(contract.getNo())
																			.freePeriod(freePeriod).build();
		log.info("제대로 만들어 진건가? : "+procurementPlanDTO);
		
		procurementPlanService.modify(procurementPlanDTO);			
		
	}
	
	@Transactional
	@Commit
	@Test
	public void deleteTest() {
		ProcurementPlan procurementPlan = procurementPlanService.getProcurementPlan(18);
		ProcurementPlanDTO procurementPlanDTO = procurementPlanService.ppEntityToDTO(procurementPlan); 	//이때 DTO로 만들면서 외래키 지연로딩 쓰기 때문에 @Transactional 필요
		procurementPlanService.delete(procurementPlanDTO);												//하지만 테스트환경에서는 @Transactional 쓰면 자동롤백 되기때문에, 삭제 테스틀르 위해 @Commit 필요
	}
	
	

}
