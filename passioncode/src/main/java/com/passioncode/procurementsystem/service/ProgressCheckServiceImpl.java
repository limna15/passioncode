package com.passioncode.procurementsystem.service;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import com.passioncode.procurementsystem.dto.DetailProgressCheckListDTO;
import com.passioncode.procurementsystem.dto.ProgressCheckDTO;
import com.passioncode.procurementsystem.entity.DetailPurchaseOrder;
import com.passioncode.procurementsystem.entity.MaterialIn;
import com.passioncode.procurementsystem.entity.ProcurementPlan;
import com.passioncode.procurementsystem.entity.ProgressCheck;
import com.passioncode.procurementsystem.repository.DetailPurchaseOrderRepository;
import com.passioncode.procurementsystem.repository.MaterialInRepository;
import com.passioncode.procurementsystem.repository.ProcurementPlanRepository;
import com.passioncode.procurementsystem.repository.ProgressCheckRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@RequiredArgsConstructor
@Log4j2
public class ProgressCheckServiceImpl implements ProgressCheckService {
	
	private final DetailPurchaseOrderRepository detailPurchaseOrderRepository;
	private final ProcurementPlanRepository procurementPlanRepository;
	private final ProgressCheckRepository progressCheckRepository;
	private final MaterialInRepository materialInRepository;
	
	@Override
	public List<ProgressCheckDTO> getProgressCheckDTOList() {//발주서 목록 갖고오기
		List<DetailPurchaseOrder> detilList = detailPurchaseOrderRepository.findAll();
		
		List<ProgressCheckDTO> progressCheckDTOlList = new ArrayList<>();
		for(int i=0;i<detilList.size();i++) {
			progressCheckDTOlList.add(entityToDTO(detilList.get(i)));
					}
		return progressCheckDTOlList;
		
	}

	@Override
	public ProgressCheckDTO entityToDTO(DetailPurchaseOrder detailPurchaseOrder) {
		//DetailPurchaseOrder detailPurchaseOrder2= detailPurchaseOrderRepository.findById(1).get();
		ProcurementPlan procurementPlan= procurementPlanRepository.findByDetailPurchaseOrder(detailPurchaseOrder);
		
		  ProgressCheckDTO progressCheckDTO = ProgressCheckDTO.builder()
		  .companyName(procurementPlan.getContract().getCompany().getName())
		  .purchaseOrderCode(procurementPlan.getDetailPurchaseOrder().getCode())
		  .orderAmount(procurementPlan.getDetailPurchaseOrder().getAmount())
		  .dueDate(procurementPlan.getDueDate())
		  .materialName(procurementPlan.getContract().getMaterial().getName())
		  .unitPrice(procurementPlan.getContract().getUnitPrice())
		  .diliveryPercent(checkPercent(procurementPlan.getDetailPurchaseOrder().getCode()))
		  .inspectionComplete("미완료")
		  .nextCheckDate(compareDate(procurementPlan.getDetailPurchaseOrder().getCode()))
		  .purchaseOrderDeadlineStatus(extistMIn(procurementPlan.getDetailPurchaseOrder()))
		  .showPurchaseOrderCode(addBlank(procurementPlan.getDetailPurchaseOrder().getCode()))
		  .build();
		
		
		return progressCheckDTO;
	}
	
	public String extistMIn(DetailPurchaseOrder dp) {//발주서 마감 상태
		String inStatus=null;
		MaterialIn maIn = materialInRepository.findByDetailPurchaseOrder(dp);
		if(maIn!=null) {//입고 상태가 존재할 경우
			inStatus="완료";//거짓이여도 존재하는 것
		//발주코드를 통해 입고상태 갖고오기 
		
		}else {
			inStatus="미완료";//입고 상태도 없을 경우
		}
		
		return inStatus;
		
	}
	//납기 진도율 가져오기
	public String getPercent(DetailPurchaseOrder dp) {
		String myPercent=null;
		ProgressCheck pg = progressCheckRepository.findByDetailPurchaseOrder(dp);
		log.info("내가 보고싶은 값"+pg);
		if(pg!=null) {//납기 진도율이 존재한다면
			if(pg.getRate()==null) {//검수 일정은 등록되어 있지만 아직 검사하지 않은 경우
				myPercent = "미등록";
			}else {
				myPercent = pg.getRate()+"%";
				
			}
						
		}else {
			myPercent = "미등록";
		}
		
		
		return myPercent;
		
	}
	
	//다음 진척검수 일정
	public String nextCheckDate(DetailPurchaseOrder dp) {
		LocalDateTime nextDate=null;
		ProgressCheck pg = progressCheckRepository.findByDetailPurchaseOrder(dp);
		if(pg!=null) {//다음 진척검수 일정이 존재한다면
			nextDate = pg.getDate();
			
		}else {
			nextDate = null;
		}
		
		
		return nextDate+"";
		
	}
	
	//발주 계획 등록
	@Override
	public void nextCheckDate(LocalDateTime date, Integer code) {
		//날짜와 조달 계획을 넣어서 빌드 저장
		DetailPurchaseOrder dp = detailPurchaseOrderRepository.findById(code).get();
		ProgressCheck pc = ProgressCheck.builder().date(date).detailPurchaseOrder(dp).build();
		progressCheckRepository.save(pc);
		
		
	}
	
	@Override
	public void addAvg(Integer num1, String etc, Integer num2) {
		DetailPurchaseOrder detailPO = detailPurchaseOrderRepository.findById(num2).get();
		ProgressCheck pc = progressCheckRepository.findByDetailPurchaseOrder(detailPO);
		log.info("업데이트 할 평가: " + pc);
		ProgressCheck pc2 = ProgressCheck.builder().code(pc.getCode()).date(pc.getDate())
				.detailPurchaseOrder(pc.getDetailPurchaseOrder()).etc(etc).rate(num1).build();
		log.info("업데이트된 내용: " + pc2);
		log.info("num2의 값: " + num2);
		
		 progressCheckRepository.save(pc2);
	}
	
	//발주코드에 문자를 넣어서 보내기
	public String addBlank(Integer num1) {
		String pNum = String.format("%05d", num1);
		pNum = "DPO"+pNum;
		log.info("잘 찍어 보낼 문자"+pNum);
		
		return pNum;
		
	}
	
	public String compareDate(Integer num1) {//리턴 스트링으로 하기?(date1이 null이라면 미래가 없는 것) 고민중, 검수일정 없음을 위해
		//날짜 비교하기
		//오늘과 가까운 미래 날짜 가져오기
		DetailPurchaseOrder detailPO = detailPurchaseOrderRepository.findById(num1).get();
		// 아래 리스트에 더하기
		List<DetailProgressCheckListDTO> list = new ArrayList<>();
		List<Object[]> pcList = progressCheckRepository.findByDetailPurchaseOrderList(detailPO.getCode());
		//Date futureDate=null;//더 미래인 날을 여기에 저장하기
		Date date1=null;
		Date date2=null;
		Date today = new Date();//오늘 날짜 보는 방법
		
		for (Object[] arr : pcList) {
			DetailProgressCheckListDTO pdDTO = new DetailProgressCheckListDTO();
			pdDTO.setPccode((Integer) arr[0]);
			pdDTO.setPcdate((Date) arr[1]);
			//1. 오늘보다 미래에 있기, 2. 미래에서 가장 가까운 날짜
			
			if(date1!=null) {//비교하기 위한 값이 있다면
				date2 = ((Date) arr[1]);//다음에 들어온 날을 넣어준다.
				log.info("두 번째 수"+date2);
				int comparison2 = date1.compareTo(date2);// 날짜 비교
				//오늘이랑 먼저 비교하고
				//(미래)오늘 뒤에 있는 날만 보여주기
				//date1이 date2보다 오늘과 가까운 미래이면 date1에 저장하기
				//다음 진척검수일이 과거인 것은 다른 곳에서 비교해서 없음 표시하기
				
				if (comparison2 > 0) {//이게 더 빠른 날짜
					date1 = date2;//date2에 있음으로 date1으로 바꿔주기, 그래야 계속 비교가능
		            System.out.println("date1이 date2보다 뒤에 있습니다.");
		        } else if (comparison2 < 0) {//이 경우가 가까운 날의 진척 검수
		            System.out.println("date1이 date2보다 앞에 있습니다.");
		            System.out.println("다음 진척 검수일정"+date1);
		        } else {//이런 경우는 처음부터 없도록 하기
		            System.out.println("두 날짜는 같습니다.");
		        }
			}else {//처음에 date1이 null인 경우
				date1 = ((Date) arr[1]);//무조건 담는다
				log.info("처음에만 있는 수"+date1);
				int comparison = date1.compareTo(today);//오늘과 날짜 비교
				if (comparison > 0) {//이게 더 빠른 날짜
		            System.out.println("date1이 today보다 뒤에 있습니다.");
		        } else if (comparison < 0) {//오늘보다 앞에 있으면 null로 보내기
		        	date1 = null;
		            System.out.println("date1이 today보다 앞에 있습니다.");
		        }
			}
			
			list.add(pdDTO);
		

		}
		String dateString;
		if(date1 == null) {
			dateString = "미등록";
		}else {
			log.info("날짜: "+date1);//마지막 한개만 나옴
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
			dateString = dateFormat.format(date1);
			
		}
		return dateString;
		
	}
	
	
	public String checkPercent(Integer num1) {//없으면 미등록, 날짜 중 가까운 과거의 데이터 가져오기
		DetailPurchaseOrder detailPO = detailPurchaseOrderRepository.findById(num1).get();
		List<DetailProgressCheckListDTO> list = new ArrayList<>();
		List<Object[]> pcList = progressCheckRepository.findByDetailPurchaseOrderList(detailPO.getCode());
		
		//오늘과 가까운 과거 날짜 가져오기
		Date date1=null;
		Date date2=null;
		Date today = new Date();//오늘 날짜 보는 방법
		Integer rate =null;
				
		for (Object[] arr : pcList) {
			DetailProgressCheckListDTO pdDTO = new DetailProgressCheckListDTO();
			pdDTO.setPccode((Integer) arr[0]);
			pdDTO.setPcdate((Date) arr[1]);
			pdDTO.setPcrate((Integer) arr[3]);//저장할 납기 진도율
			//int comparison = date1.compareTo(today);// 날짜 비교, 미래 비교는 마지막에 하기
			//1. 오늘보다 과거, 2. 가장 최근 과거
			if(date1!=null) {//비교하기 위한 값이 있다면
				date2 = ((Date) arr[1]);//다음에 들어온 날을 넣어준다.
				log.info("두 번째 수"+date2);
				int comparison2 = date1.compareTo(date2);// 날짜 비교
					
				//(미래)오늘 뒤에 있는 날만 보여주기
				//date1이 date2보다 오늘과 가까운 미래이면 date1에 저장하기
				//다음 진척검수일이 과거인 것은 다른 곳에서 비교해서 없음 표시하기
				
				if (comparison2 > 0) {//이게 더 빠른 날짜
					date1 = date2;//date2에 있음으로 date1으로 바꿔주기, 그래야 계속 비교가능
					rate = ((Integer) arr[3]);//자기 자신 더해주기//고치기
					System.out.println("date1이 date2보다 뒤에 있습니다."+rate+"%"+date1);
					//여기에 있는게 원하는 rate
				} else if (comparison2 < 0) {//이 경우가 가까운 날의 진척 검수
					System.out.println("date1이 date2보다 앞에 있습니다."+rate+"%"+date1);
					//System.out.println("다음 진척 검수일정"+date1);
				} else {//이런 경우는 처음부터 없도록 하기
					System.out.println("두 날짜는 같습니다.");
				}
				//오늘이랑 먼저 비교하고
			}else {//처음에 date1이 null인 경우(처음 비교하는 경우, 한개의 일정)
				date1 = ((Date) arr[1]);	//무조건 담는다
				//rate = ((Integer) arr[3]);	//처음 진도율 담기
				log.info("처음에만 있는 수"+date1);
				int comparison = date1.compareTo(today);// 날짜 비교
				if (comparison > 0) {//이게 더 빠른 날짜
					//미래의 것은 필요 없음
					date1 = null;
					System.out.println("date1이 today보다 뒤에 있습니다."+rate+"%");
				} else if (comparison < 0) {
					if(((Integer) arr[3]) == null) {
						System.out.println(rate+"진척 검수가 기록되지 않은 경우 이전 데이터를 가져감");
					}else {//값이 있는 경우만 최근 데이터 넣어줌
						rate = ((Integer) arr[3]);//과거의 평가가 존재함으로 저장
					}
					date1 = null;
					System.out.println("date1이 today보다 앞에 있습니다."+rate+"%");
				}
			}
			
			list.add(pdDTO);
		}
		String lastRate;
		if(rate==null) {
			lastRate = "미등록";
		}else
			lastRate = rate+"%";
		log.info("납기 진도율: "+lastRate);//마지막 한개만 나옴
		return lastRate;
				
				
	}
	
	
	
}
