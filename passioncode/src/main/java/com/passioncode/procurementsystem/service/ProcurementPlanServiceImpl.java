package com.passioncode.procurementsystem.service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.passioncode.procurementsystem.dto.ProcurementPlanDTO;
import com.passioncode.procurementsystem.entity.Contract;
import com.passioncode.procurementsystem.entity.DetailPurchaseOrder;
import com.passioncode.procurementsystem.entity.MRP;
import com.passioncode.procurementsystem.entity.Material;
import com.passioncode.procurementsystem.entity.ProcurementPlan;
import com.passioncode.procurementsystem.repository.ContractRepository;
import com.passioncode.procurementsystem.repository.DetailPurchaseOrderRepository;
import com.passioncode.procurementsystem.repository.MRPRepository;
import com.passioncode.procurementsystem.repository.ProcurementPlanRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@RequiredArgsConstructor
@Log4j2
public class ProcurementPlanServiceImpl implements ProcurementPlanService {
	
	private final ProcurementPlanRepository procurementPlanRepository;
	private final MRPRepository mrpRepository;
	private final ContractRepository contractRepository;
	private final DetailPurchaseOrderRepository detailPurchaseOrderRepository;
	
	
	@Override
	public ProcurementPlan getProcurementPlan(Integer code) {
		return procurementPlanRepository.findById(code).get();
	}

	@Override
	public MRP getMRP(Integer code) {
		return mrpRepository.findById(code).get();
	}
	
	@Override
	public Contract getContract(Integer no) {
		return contractRepository.findById(no).get();
	}
	
	@Override
	public List<ProcurementPlan> getPPJoinMRPByMaterialCode(String materialCode) {		
		return procurementPlanRepository.getPPJoinMRPByMaterialCode(materialCode);
	}	
	
	/**
	 * 조달계획코드 문자버전으로 바꾸기 <br>
	 * 조달계획코드 1 -> PP00001 로 바꿔주기
	 * @param contractNo
	 * @return
	 */
	public String makePPCodeStr(Integer ppCode) {
		//조달계획코드 1 -> PP00001 로 바꿔주기
		String ppCodeStr = String.format("%05d",ppCode);
		ppCodeStr = "PP" + ppCodeStr;
		//log.info("바꾼 조달계획코드 좀 보자 : ",ppCodeStr);
		return ppCodeStr;
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
	 * 품목 엔티티를 이용하여 계약상태 체크하기 <br> 
	 * 완료 : 계약상태 O, 미완료 : 계약상태 X
	 * @param material
	 * @return
	 */
	public String contractStatusCheck(Material material) {		
		//완료 : 계약상태 O, 미완료 : 계약상태 X
		return contractRepository.existsByMaterial(material) ? "완료" : "미완료";
	}
	
	@Override
	public ProcurementPlanDTO mrpEntityToDTO(MRP mrp) {
		//품목코드, 품목명, 소요공정, 소요일, 소요량, 협력회사, 품목공급LT, 조달납기예정일, 최소발주일, 필요수량, 계약상태, 조달계획 등록상태, 조달계획 진행사항 
		//조달계획코드, 자재소요계획코드, 사업자등록번호, 계약서번호 // 기본 여유기간
		
		//조달계획코드, 자재소요계획코드(외래키), 계약서번호(외래키),
		//필요수량, 조달납기예정일, 최소발주일, 조달계획 등록일, 조달계획 완료일, 발주코드(외래키)	
		
		ProcurementPlan procurementPlan = procurementPlanRepository.findByMrp(mrp);		
		ProcurementPlanDTO procurementPlanDTO = null;
		
		//procurementPlan 가 존재할때, 조달계획 등록 완료된 상태
		if(procurementPlan!=null) {
			procurementPlanDTO = ProcurementPlanDTO.builder().materialCode(procurementPlan.getMrp().getMaterial().getCode()).ppCode(procurementPlan.getCode())
															.ppCodeStr(makePPCodeStr(procurementPlan.getCode()))
															.materialName(procurementPlan.getMrp().getMaterial().getName()).process(procurementPlan.getMrp().getProcess())
															.mrpDate(procurementPlan.getMrp().getDate()).mrpAmount(procurementPlan.getMrp().getAmount())
															.freePeriod(makefreePeriod(procurementPlan.getMrp().getDate(),procurementPlan.getDueDate()))
															.companyName(procurementPlan.getContract().getCompany().getName())
															.supplyLt(procurementPlan.getContract().getSupplyLt())
															.dueDate(procurementPlan.getDueDate()).minimumOrderDate(procurementPlan.getMinimumOrderDate())
															.ppAmount(procurementPlan.getAmount()).completionDate(procurementPlan.getCompletionDate())
															.contractStatus(contractStatusCheck(procurementPlan.getMrp().getMaterial()))
															.ppRegisterStatus("완료").ppProgress(ppProgressCheck(procurementPlan))
															.mrpCode(procurementPlan.getMrp().getCode()).companyNo(procurementPlan.getContract().getCompany().getNo())
															.contractNo(procurementPlan.getContract().getNo()).build();
		}else {  ////procurementPlan 가 존재X, 조달계획 등록 미완료된 상태
			procurementPlanDTO =  ProcurementPlanDTO.builder().materialCode(mrp.getMaterial().getCode()).materialName(mrp.getMaterial().getName())
																.process(mrp.getProcess()).mrpDate(mrp.getDate()).mrpAmount(mrp.getAmount())
																.contractStatus(contractStatusCheck(mrp.getMaterial())).ppRegisterStatus("미완료")
																.mrpCode(mrp.getCode()).build();
		}		
		return procurementPlanDTO;
	}

	@Override
	public ProcurementPlanDTO ppEntityToDTO(ProcurementPlan procurementPlan) {
		ProcurementPlanDTO procurementPlanDTO =ProcurementPlanDTO.builder().materialCode(procurementPlan.getMrp().getMaterial().getCode()).ppCode(procurementPlan.getCode())
																			.materialName(procurementPlan.getMrp().getMaterial().getName())
																			.process(procurementPlan.getMrp().getProcess()).ppCodeStr(makePPCodeStr(procurementPlan.getCode()))
																			.mrpDate(procurementPlan.getMrp().getDate()).mrpAmount(procurementPlan.getMrp().getAmount())
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
		return procurementPlanDTO;
	}

	@Override
	public ProcurementPlan dtoToEntity(ProcurementPlanDTO procurementPlanDTO) {
		//품목코드, 품목명, 소요공정, 소요일, 소요량, 협력회사, 품목공급LT, 조달납기예정일, 최소발주일, 필요수량, 계약상태, 조달계획 등록상태, 조달계획 진행사항 
		//조달계획코드, 자재소요계획코드, 사업자등록번호, 계약서번호 // 기본 여유기간
		
		//조달계획코드, 자재소요계획코드(외래키), 계약서번호(외래키),
		//필요수량, 조달납기예정일, 최소발주일, 조달계획 등록일, 조달계획 완료일, 발주코드(외래키)	
		ProcurementPlan procurementPlan = null;
		
		if(procurementPlanDTO.getPpCode()!=null) {  //조달계획코드가 존재O -> 기존에 있던 DTO 값들을 엔티티로 바꿀때!
			procurementPlan = ProcurementPlan.builder().code(procurementPlanDTO.getPpCode()).mrp(mrpRepository.findById(procurementPlanDTO.getMrpCode()).get())
													.contract(contractRepository.findById(procurementPlanDTO.getContractNo()).get())
													.amount(procurementPlanDTO.getPpAmount())
													.dueDate(procurementPlanDTO.getDueDate()).minimumOrderDate(procurementPlanDTO.getMinimumOrderDate())
													.registerDate(procurementPlanRepository.findById(procurementPlanDTO.getPpCode()).orElse(null).getRegisterDate())
													.completionDate(procurementPlanDTO.getCompletionDate())
													.detailPurchaseOrder(procurementPlanRepository.findById(procurementPlanDTO.getPpCode()).orElse(null).getDetailPurchaseOrder())
													.build();		
		}else {		//조달계획코드가 존재X -> 새로운 DTO로 엔티티로 바꾸어서 등록할때!
			procurementPlan = ProcurementPlan.builder().mrp(mrpRepository.findById(procurementPlanDTO.getMrpCode()).get())
														.contract(contractRepository.findById(procurementPlanDTO.getContractNo()).get())
														.amount(procurementPlanDTO.getPpAmount()).dueDate(procurementPlanDTO.getDueDate())
														.minimumOrderDate(procurementPlanDTO.getMinimumOrderDate()).build();
			
		}
		return procurementPlan;
	}
	
	@Override
	public List<ProcurementPlanDTO> getDTOList() {
		// 조달계획등록 상태가 완료된거, 미완료 된거 나눠서 정리!
		
		//1. 조달계획등록 미완료인 mrp 가져오기
		// -> 조달계획등록 미완료, 계약완료 미완료, 랜덤으로 섞여서 위에 존재하는 상태 
		List<MRP> mrpList = mrpRepository.getMRPJoinPPWithNotCompletePP();
		
		//일단 받아온 mrp 리스트 -> ProcurementPlanDTO 리스트로 변경
		List<ProcurementPlanDTO> mrpListToDTOList = new ArrayList<>();
		for(MRP mrp : mrpList) {
			mrpListToDTOList.add(mrpEntityToDTO(mrp));
		}
		
		//최종적으로 만들 DTO 리스트 셋팅해주기
		List<ProcurementPlanDTO> finalDTOList = new ArrayList<>();
		
		//1번 리스트에서 계약상태 완료 리스트 만들기
		//1번 리스트에서 계약상태 미완료 리스트 만들기
		List<ProcurementPlanDTO> notPPByMrpListCompleteContract = new ArrayList<>();
		List<ProcurementPlanDTO> notPPByMrpListNonContract = new ArrayList<>();
		for(ProcurementPlanDTO dto : mrpListToDTOList) {
			if(dto.getContractStatus().equals("완료")) {
				notPPByMrpListCompleteContract.add(dto);
			}else {
				notPPByMrpListNonContract.add(dto);
			}			
		}
		
		//1번 리스트에서 계약상태 완료 먼저 최종 리스트에 넣어주고, 
		for(ProcurementPlanDTO dto : notPPByMrpListCompleteContract) {
			finalDTOList.add(dto);
		}
		//1번 리스트에서 계약상태 미완료인것은 그다음에 최종리스트에 넣어주기
		for(ProcurementPlanDTO dto : notPPByMrpListNonContract) {
			finalDTOList.add(dto);
		}
		
		//2. 조달계획등록 완료인 조달계획 가져오기 
		//조달계획진행사항을  발주 예정 -> 조달 진행 중 -> 조달 완료  순서로 셋팅해주기!
		List<ProcurementPlan> ppList = procurementPlanRepository.getPPJoinMRPWithOrder();
		//log.info("조달계획에서 가져온 전체 조달계획 리스트 한번 보자 : "+ppList);
		//log.info("조달계획에서 가져온 전체 조달계획 리스트크기 : "+ppList.size());
		
		//받아온 리스트, 일단 DTO로 바꿔주기
		List<ProcurementPlanDTO> ppListToDTOList = new ArrayList<>();		
		for(ProcurementPlan pp : ppList) {
			ppListToDTOList.add(ppEntityToDTO(pp));
		}
		
		//받아온 리스트 -> 발주예정 만 모을 리스트에 넣어주기
		List<ProcurementPlanDTO> ppListFirstStep = new ArrayList<>();
		for(ProcurementPlanDTO dto : ppListToDTOList) {
			if(dto.getPpProgress().equals("발주 예정")) {
				ppListFirstStep.add(dto);
			}
		}
		//log.info("발주 예정인것만 체크해보자"+ppListFirstStep);
		
		//받아온 리스트 -> 조달 진행 중 만 모을 리스트에 넣어주기
		List<ProcurementPlanDTO> ppListSecondStep = new ArrayList<>();
		for(ProcurementPlanDTO dto : ppListToDTOList) {
			if(dto.getPpProgress().equals("조달 진행 중")) {
				ppListSecondStep.add(dto);
			}
		}
		//log.info(" 조달 진행 중 인것만 체크해보자"+ppListSecondStep);
		
		//받아온 리스트 -> 조달 완료 만 모을 리스트에 넣어주기
		List<ProcurementPlanDTO> ppListThirdStep = new ArrayList<>();
		for(ProcurementPlanDTO dto : ppListToDTOList) {
			if(dto.getPpProgress().equals("조달 완료")) {
				ppListThirdStep.add(dto);
			}
		}
		//log.info("조달 완료 인것만 체크해보자"+ppListThirdStep);
		
		//최종리스트에 발주예정 -> 조달진행중 -> 조달완료 순으로 넣어주기
		for(ProcurementPlanDTO dto : ppListFirstStep) {
			finalDTOList.add(dto);
		}
		
		for(ProcurementPlanDTO dto : ppListSecondStep) {
			finalDTOList.add(dto);
		}
		
		for(ProcurementPlanDTO dto : ppListThirdStep) {
			finalDTOList.add(dto);
		}	
		
		//log.info("만들어진 정렬된 ProcurementPlanDTO 리스트 보기 : "+finalDTOList);
		
		return finalDTOList;
	}
	
	/**
	 * 정렬생각 안하고 처음에 짠 ProcurementPlanDTO리스트 불러오기
	 * @return
	 */
	public List<ProcurementPlanDTO> getDTOList2() {
		List<MRP> mrpList = mrpRepository.findAll();
		
		List<ProcurementPlanDTO> ppDTOList = new ArrayList<>();
		ProcurementPlanDTO procurementPlanDTO = null;		
		
		for(int i=0;i<mrpList.size();i++) {
			ProcurementPlan procurementPlan = procurementPlanRepository.findByMrp(mrpList.get(i));	
			
			//procurementPlan 가 존재할때, 조달계획 등록 완료된 상태
			if(procurementPlan!=null) {
				procurementPlanDTO =ProcurementPlanDTO.builder().materialCode(procurementPlan.getMrp().getMaterial().getCode()).ppCode(procurementPlan.getCode())
																.materialName(procurementPlan.getMrp().getMaterial().getName()).process(procurementPlan.getMrp().getProcess())
																.mrpDate(procurementPlan.getMrp().getDate()).mrpAmount(procurementPlan.getMrp().getAmount())
																.freePeriod(makefreePeriod(procurementPlan.getMrp().getDate(),procurementPlan.getDueDate()))
																.companyName(procurementPlan.getContract().getCompany().getName())
																.supplyLt(procurementPlan.getContract().getSupplyLt())
																.dueDate(procurementPlan.getDueDate()).minimumOrderDate(procurementPlan.getMinimumOrderDate())
																.ppAmount(procurementPlan.getAmount())
																.contractStatus(contractStatusCheck(procurementPlan.getMrp().getMaterial()))
																.ppRegisterStatus("완료").ppProgress(ppProgressCheck(procurementPlan))
																.mrpCode(procurementPlan.getMrp().getCode()).companyNo(procurementPlan.getContract().getCompany().getNo())
																.contractNo(procurementPlan.getContract().getNo()).build();
				ppDTOList.add(procurementPlanDTO);
			}else {  ////procurementPlan 가 존재X, 조달계획 등록 미완료된 상태
				procurementPlanDTO = ProcurementPlanDTO.builder().materialCode(mrpList.get(i).getMaterial().getCode()).materialName(mrpList.get(i).getMaterial().getName())
																.process(mrpList.get(i).getProcess()).mrpDate(mrpList.get(i).getDate()).mrpAmount(mrpList.get(i).getAmount())
																.contractStatus(contractStatusCheck(mrpList.get(i).getMaterial())).ppRegisterStatus("미완료")
																.mrpCode(mrpList.get(i).getCode()).build();	
				ppDTOList.add(procurementPlanDTO);
			}			
		}			
		return ppDTOList;
	}
	
	
	@Override
	public Integer register(ProcurementPlanDTO procurementPlanDTO) {
		//조달계획을 등록한다는건! 계약상태 = 완료, 조달계획 등록상태 = 완료, 조달계획 진행사항 =  발주 예정
		//화면에서 계약상태, 조달계획 등록상태 숨겨서 true로 보내주고, 조달계획 진행사항 발주 예정 으로 숨겨서 보내주자!
		
		ProcurementPlan procurementPlan = dtoToEntity(procurementPlanDTO);
		procurementPlanRepository.save(procurementPlan);
		log.info("저장된 조달계획(procurementPlan) 정보 : "+procurementPlan);
		
		return procurementPlan.getCode();
	}
	
	@Transactional
	@Override
	public void modify(ProcurementPlanDTO procurementPlanDTO) {
		ProcurementPlan procurementPlan = dtoToEntity(procurementPlanDTO);
		procurementPlanRepository.save(procurementPlan);
		log.info("수정된 조달계획(procurementPlan) 정보 : "+procurementPlan);		
	}

	@Override
	public void delete(ProcurementPlanDTO procurementPlanDTO) {
		log.info("삭제된 조달계획(procurementPlan) 정보 : "+dtoToEntity(procurementPlanDTO));
		procurementPlanRepository.deleteById(procurementPlanDTO.getPpCode());
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
		//log.info("캘린더 셋팅된 조달납기예정일 : "+simpleDateFormat.format(duedate));
		
		//조달납기예정일 - 품목공급LT => 최소발주일 만들기
		cal.add(Calendar.DATE,-supplyLt);
		
		Date minimumOrderDate=cal.getTime();
		//log.info("제대로 뺀, 최소발주일 : "+simpleDateFormat.format(minimumOrderDate));
		
		return minimumOrderDate;
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
		//log.info("캘린더 셋팅된 mrp 소요일 : "+simpleDateFormat.format(mrpDate));
		
		//소요일 - 기본 여유기간 => 납기예정일 만들기
		cal.add(Calendar.DATE,-freePeriod);
		
		Date duedate=cal.getTime();
		//log.info("제대로 뺀, 납기예정일 : "+simpleDateFormat.format(duedate));
		
		return duedate;
	}

	@Override
	public ProcurementPlanDTO getProcurementPlanCalculate(ProcurementPlanDTO procurementPlanDTO) {
		//화면에서 받을 procurementPlanDTO 정보 => 소요일, 기본여유기간, 품목공급LT
		//소요일 -> 등록화면 셋팅된 값에서 얻기
		//기본여유기간, 품목공급LT -> 계약서 찾기 모달창에서 얻기
		
		//조달납기 예정일 구하기 -> procurementPlanDTO 에 값 셋팅해주기
		Date dueDate = makeDuedate(procurementPlanDTO.getMrpDate(), procurementPlanDTO.getFreePeriod());
		procurementPlanDTO.setDueDate(dueDate);
		
		//최소발주일 구하기 -> procurementPlanDTO 에 값 셋팅해주기
		Date minimumOrderDate=makeMinimumOrderDate(dueDate,procurementPlanDTO.getSupplyLt());
		procurementPlanDTO.setMinimumOrderDate(minimumOrderDate);
				
		return procurementPlanDTO;
	}

	@Override
	public ProcurementPlan getPpByDetailPurchaseOrder(Integer code) {
		DetailPurchaseOrder dpo= detailPurchaseOrderRepository.findById(code).get();
		return	procurementPlanRepository.findByDetailPurchaseOrder(dpo);
	}

	@Override
	public Boolean ppExistsByContract(Integer contractNo) {
		
		return procurementPlanRepository.existsByContract(contractRepository.findById(contractNo).get());
	}

	
}
