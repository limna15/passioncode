package com.passioncode.procurementsystem.service;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
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
	@Commit
	@Test
	public void getCount() {
		List<ProcurementPlanDTO> procurementPlanDTOList= procurementPlanService.getDTOList();
		List<DetailPurchaseOrder> dpoList= detailPurchaseOrderService.getDetailList();
		List<ProcurementPlan> ppList= procurementPlanRepository.findAll();
		
		int beforePurchaseCount= 0;
		int ingProcurementCount= 0;
		int doneProcurementCount= 0;
		int ppCount= 0;
		
		log.info("조달계획DTO 사이즈 >>> " + procurementPlanDTOList.size());
		log.info("조달계획 엔티티 사이즈 >>> " + ppList.size());
		log.info("세부구매발주서 사이즈 >>> " + dpoList.size());
		
		List<Date> dpoDate= new ArrayList<>();
		for(int i=0; i<dpoList.size();i++) {
			dpoDate.add(Date.from(dpoList.get(i).getDate().atZone(ZoneId.systemDefault()).toInstant()));			
		}
		
		log.info("세부구매발주서 발주일 Date 형식으로 변환 >>> " + dpoDate);
		
		//조달계획 진행사항이 없는 것만 조달계획이 등록된 것
		for(int i=0; i<procurementPlanDTOList.size(); i++) {
			if(procurementPlanDTOList.get(i).getPpProgress() != null) {
				ppCount++; //총 조달계획 개수
			}
		}
		
		Date date= new Date(); //현재 날짜
		//date= new Date(date.getTime()+1000*60*60*24*-1); //어제 날짜
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String formatDate= formatter.format(date);
		log.info("현재 날짜는 어떻게 찍히죠 >>> " + formatDate);
		
		///////////////////////////////////////////////////////////////////////////
		//해당 날짜의 완료 개수 구하기
		LocalDateTime doneDate= null;
		List<LocalDateTime> doneList= new ArrayList<>();
		
		LocalDateTime ingDate= null;
		List<LocalDateTime> ingList= new ArrayList<>();

		for(int i=0; i<procurementPlanDTOList.size(); i++) {
			doneDate= procurementPlanDTOList.get(i).getCompletionDate();
			doneList.add(doneDate);
			//조달계획 완료일이 존재 + 해당 날짜와 일치하면 카운트
			if(doneList.get(i) != null && doneList.equals(date)) {
				doneProcurementCount++;
			}
			//조달계획에 발주서코드가 존재X + 해당 날짜 전  -> 수정해야함 어떻게 할지 다시 확인해보기
//			if(!purchaseReportRepository.existsByDetailPurchaseOrder(dpoList.get(i)) && date.before(dpoDate.get(i))){
//				beforePurchaseCount++;
//			}
		}
		log.info("doneList? >>> " + doneList + " 사이즈도 >>> " + doneList.size());
		log.info("오늘 기준 완료 개수 제대로 찍히나? >>> " + doneProcurementCount);	
		log.info("오늘 기준 발주 예정 개수 제대로 찍히나? >>> " + beforePurchaseCount);	
	}
}
