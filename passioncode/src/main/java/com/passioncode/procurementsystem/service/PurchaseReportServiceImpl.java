package com.passioncode.procurementsystem.service;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.passioncode.procurementsystem.dto.ProcurementPlanDTO;
import com.passioncode.procurementsystem.dto.PurchaseReportDTO;
import com.passioncode.procurementsystem.entity.ProcurementPlan;
import com.passioncode.procurementsystem.repository.ProcurementPlanRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@RequiredArgsConstructor
@Log4j2
public class PurchaseReportServiceImpl implements PurchaseReportService {
	
	private final ProcurementPlanService procurementPlanService;
	private final ProcurementPlanRepository procurementPlanRepository;
	
	@Override
	public PurchaseReportDTO getCountPurchaseReportDTO() {
		List<ProcurementPlanDTO> procurementPlanDTOList= procurementPlanService.getDTOList();
		
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
			//log.info("조달계획 진행사항 >>> " + ppProgress);
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
		
		//log.info("발주 예정 개수 >>> " + beforePurchaseCount);
		//log.info("조달 진행 중 개수 >>> " + ingProcurementCount);
		//log.info("조달 완료 개수 >>> " + doneProcurementCount);
		
		PurchaseReportDTO purchaseReportDTO= PurchaseReportDTO.builder()
				.procurementPlanCount(ppCount)
				.beforePurchase(beforePurchaseCount).ingProcurement(ingProcurementCount)
				.doneProcurement(doneProcurementCount).build();
		
		return purchaseReportDTO;
	}

	@Override
	public List<PurchaseReportDTO> getPurchaseReportDTOList(String[] date) {
		List<String> dateList= new ArrayList<>();
		if(date != null) {
			for(String get:date) {
				dateList.add(get);
			}			
		}
		
		Boolean before= null;
		Boolean ing= null;
		Boolean done= null;
		  
		List<PurchaseReportDTO> prDTOList= new ArrayList<>();
		PurchaseReportDTO prDTO= null;
		
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
				//log.info("등록일 비교 찍기 >>> " + i + "번째 " + registerDateCompare);
				if(ppList.get(i).getCompletionDate() != null) {
					completionDateCompare= ppList.get(i).getCompletionDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")).compareTo(dateList.get(j));			
					//log.info("완료일 비교 찍기 >>> " + i + "번째 " + completionDateCompare);
				}
				if(ppList.get(i).getDetailPurchaseOrder() != null) {
					purchaseDateCompare= ppList.get(i).getDetailPurchaseOrder().getDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")).compareTo(dateList.get(j));	
					//log.info("발주일 비교 찍기 >>> " + i + "번째 " + purchaseDateCompare);
				}
			
				if(procurementPlanRepository.existsById(i+1)) {
					//발주 예정 -> 완료일(입고일)X + 구매발주서(발주일) X + 등록일 O
					if(ppList.get(i).getCompletionDate() == null && ppList.get(i).getDetailPurchaseOrder() == null  && registerDateCompare <= 0) {
						//log.info("발주예정 >>> " + ppList.get(i));
						before= true;
						ing= false;
						done= false;
					//조달 진행 중 -> 완료일(입고일)X + 구매발주서(발주일) O + 등록일 O
					}else if(ppList.get(i).getCompletionDate() == null && ppList.get(i).getDetailPurchaseOrder() != null && (registerDateCompare == -1 || registerDateCompare == 0)){
						//log.info("조달 진행 중 >>> " + ppList.get(i));
						before= false;
						ing= true;
						done= false;
					//조달 완료 -> 완료일(입고일)O + 구매발주서(발주일) O + 등록일 O
					}else if(ppList.get(i).getCompletionDate() != null && completionDateCompare == 0){
						//log.info("조달 완료 >>> " + ppList.get(i));
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
			prDTO= PurchaseReportDTO.builder().date(dateList.get(j)).beforePurchase(beforeCount).ingProcurement(ingCount)
					.doneProcurement(doneCount).build();
			prDTOList.add(prDTO);
			
			beforeCount=0;
			ingCount=0;
			doneCount=0;
		}//for문(dateList.size()) 끝
		return prDTOList;
	}

}
