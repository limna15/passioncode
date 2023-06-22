package com.passioncode.procurementsystem.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import com.passioncode.procurementsystem.dto.ProcurementPlanDTO;
import com.passioncode.procurementsystem.dto.PurchaseReportDTO;
import com.passioncode.procurementsystem.entity.DetailPurchaseOrder;
import com.passioncode.procurementsystem.entity.ProcurementPlan;
import com.passioncode.procurementsystem.entity.PurchaseOrder;
import com.passioncode.procurementsystem.repository.ProcurementPlanRepository;
import com.passioncode.procurementsystem.repository.PurchaseReportRepository;

import lombok.extern.log4j.Log4j2;

@SpringBootTest
@Log4j2
public class PurchaseReportServiceTests {

	@Autowired
	ProcurementPlanRepository procurementPlanRepository;
	
	@Autowired
	PurchaseReportRepository purchaseReportRepository;
	
	@Autowired
	ProcurementPlanService procurementPlanService;
	
	@Autowired
	PurchaseReportService purchaseReportService;

	@Autowired
	DetailPurchaseOrderService detailPurchaseOrderService;

	@Test
	public void procurementPlanGetCountTest() {
		log.info("조달계획 총개수 >>> " + procurementPlanRepository.countBy());			
	}
	
	@Transactional
	@Test
	public void getCountPRDTOTest() {
		log.info("purchaseReportDTO 한번 보자 >>> " + purchaseReportService.getCountPurchaseReportDTO());	
	}
	
	//조달계획 전체 개수를 가져옴
	@Transactional
	@Test
	public void getCountPurchaseReportDTOTest() {
		List<ProcurementPlanDTO> procurementPlanDTOList= procurementPlanService.getDTOList();
		
		//log.info("조달계획DTO 리스트 >>> " + procurementPlanDTOList);
		//log.info("리스트 크기가 얼마나되나 >>> " + procurementPlanDTOList.size());
		
		int beforePurchaseCount= 0;
		int ingProcurementCount= 0;
		int doneProcurementCount= 0;
		int ppCount= 0;
		
		for(int i=0; i<procurementPlanDTOList.size(); i++) {
			if(procurementPlanDTOList.get(i).getPpProgress() != null){			
				ppCount++;
			}
		}
		
				
		for(int i=0; i<procurementPlanDTOList.size(); i++) {
			String ppProgress= procurementPlanDTOList.get(i).getPpProgress();
			log.info("조달계획 진행사항 >>> " + ppProgress);
			if(ppProgress!=null) {
				if(ppProgress.equals("발주 예정")) {
					beforePurchaseCount++;
				}else if(ppProgress.equals("조달 진행 중")){
					ingProcurementCount++;
				}else {
					doneProcurementCount++;
				}
			}
		}
		
		log.info("발주 예정 개수 >>> " + beforePurchaseCount);
		log.info("조달 진행 중 개수 >>> " + ingProcurementCount);
		log.info("조달 완료 개수 >>> " + doneProcurementCount);
		
		PurchaseReportDTO purchaseReportDTO= PurchaseReportDTO.builder()
				.procurementPlanCount(ppCount)
				.beforePurchase(beforePurchaseCount).ingProcurement(ingProcurementCount)
				.doneProcurement(doneProcurementCount).build();
		
		log.info("purchaseReportDTO 한번 봅시다 >>> " + purchaseReportDTO);
	}
	
	//기간별로 개수 가져오기 ~ing
	@Transactional
	@Test
	public void getCount() throws ParseException {
		List<String> dateList= new ArrayList<>();
		  dateList.add("2023-06-09"); dateList.add("2023-06-10"); dateList.add("2023-06-11");
		  
		//log.info("날짜가 찍히나 >>> " + dateList);
		//log.info("날짜 첫번째 값 >>> " + dateList.get(0));

		//각각의 조달 코드마다 진행 상태를 체크해주기 위한 boolean값
		Boolean before= null;
		Boolean ing= null;
		Boolean done= null;
		  
		int beforeCount= 0;
		int ingCount= 0;
		int doneCount= 0;
		
		//등록된 조달계획 리스트
		List<ProcurementPlan> ppList= procurementPlanRepository.findAll();
		//log.info("ppList 읽기 >>> " + ppList + "ppList 사이즈는? >>> " + ppList.size());
		int registerDateCompare= 0;
		int completionDateCompare= 0;
		int purchaseDateCompare= 0;
		
		//ex> 특정 날짜에 해당하는 발주 예정 개수
		for(int j=0; j<dateList.size();j++) {
		for(int i=0; i<ppList.size(); i++) {
			//compareTo
			//Date타입일 경우: 비교하는 날짜가 이전이면 -1, 동일하면 0, 이후이면 1 반환
			//String타입일 경우: 날짜 사이값을 반환
			registerDateCompare= ppList.get(i).getRegisterDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")).compareTo(dateList.get(j));
			log.info("등록일 비교 찍기 >>> " + i + "번째 " + registerDateCompare);
			if(ppList.get(i).getCompletionDate() != null) {
				completionDateCompare= ppList.get(i).getCompletionDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")).compareTo(dateList.get(j));			
				log.info("완료일 비교 찍기 >>> " + i + "번째 " + completionDateCompare);
			}
			if(ppList.get(i).getDetailPurchaseOrder() != null) {
				purchaseDateCompare= ppList.get(i).getDetailPurchaseOrder().getDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")).compareTo(dateList.get(j));	
				log.info("발주일 비교 찍기 >>> " + i + "번째 " + purchaseDateCompare);
			}
		
			if(procurementPlanRepository.existsById(i+1)) {
				//발주 예정 -> 완료일(입고일)X + 구매발주서(발주일) X + 등록일 O
				if(ppList.get(i).getCompletionDate() == null && ppList.get(i).getDetailPurchaseOrder() == null  && registerDateCompare <= 0) {
					log.info("발주예정 >>> " + ppList.get(i));
					before= true;
					ing= false;
					done= false;
				//조달 진행 중 -> 완료일(입고일)X + 구매발주서(발주일) O + 등록일 O
				}else if(ppList.get(i).getCompletionDate() == null && ppList.get(i).getDetailPurchaseOrder() != null && (registerDateCompare == -1 || registerDateCompare == 0)){
					log.info("조달 진행 중 >>> " + ppList.get(i));
					before= false;
					ing= true;
					done= false;
				//조달 완료 -> 완료일(입고일)O + 구매발주서(발주일) O + 등록일 O
				}else if(ppList.get(i).getCompletionDate() != null && completionDateCompare == 0){
					log.info("조달 완료 >>> " + ppList.get(i));
					before= false;
					ing= false;
					done= true;
				}else {
					before= null;
					ing= null;
					done= null;
				}
				
				if(before != null && before == true) {
					beforeCount++;
				}else if(ing != null && ing == true) {
					ingCount++;
				}else if(done != null && done == true) {
					doneCount++;
				}
			}//if문(existsBy) 끝
		}//for문(ppList.size()) 끝
		}//for문(dateList.size()) 끝
		log.info("카운트 세기 >>> " + beforeCount +", "+ ingCount + ", " + doneCount);	
		//log.info("dateList.get(0) 어떻게 나오지 >>> " + dateList.get(0));
		//log.info("dateList 날짜 어떻게 나오지 >>> " + ppList.get(1));
	}
}
