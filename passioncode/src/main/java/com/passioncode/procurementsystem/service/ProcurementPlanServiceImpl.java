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
import com.passioncode.procurementsystem.entity.MRP;
import com.passioncode.procurementsystem.entity.Material;
import com.passioncode.procurementsystem.entity.ProcurementPlan;
import com.passioncode.procurementsystem.repository.ContractRepository;
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
			procurementPlanDTO = ProcurementPlanDTO.builder().materialCode(procurementPlan.getMrp().getMaterial().getCode()).ppcode(procurementPlan.getCode())
															.materialName(procurementPlan.getMrp().getMaterial().getName()).process(procurementPlan.getMrp().getProcess())
															.mrpdate(procurementPlan.getMrp().getDate()).mrpAmount(procurementPlan.getMrp().getAmount())
															.freePeriod(makefreePeriod(procurementPlan.getMrp().getDate(),procurementPlan.getDueDate()))
															.companyName(procurementPlan.getContract().getCompany().getName())
															.supplyLt(procurementPlan.getContract().getSupplyLt())
															.dueDate(procurementPlan.getDueDate()).minimumOrderDate(procurementPlan.getMinimumOrderDate())
															.ppAmount(procurementPlan.getAmount())
															.contractStatus(contractStatusCheck(procurementPlan.getMrp().getMaterial()))
															.ppRegisterStatus("완료").ppProgress(ppProgressCheck(procurementPlan))
															.mrpCode(procurementPlan.getMrp().getCode()).companyNo(procurementPlan.getContract().getCompany().getNo())
															.contractNo(procurementPlan.getContract().getNo()).build();
		}else {  ////procurementPlan 가 존재X, 조달계획 등록 미완료된 상태
			procurementPlanDTO =  ProcurementPlanDTO.builder().materialCode(mrp.getMaterial().getCode()).materialName(mrp.getMaterial().getName())
																.process(mrp.getProcess()).mrpdate(mrp.getDate()).mrpAmount(mrp.getAmount())
																.contractStatus(contractStatusCheck(mrp.getMaterial())).ppRegisterStatus("미완료")
																.mrpCode(mrp.getCode()).build();
		}		
		return procurementPlanDTO;
	}

	@Override
	public ProcurementPlanDTO ppEntityToDTO(ProcurementPlan procurementPlan) {
		ProcurementPlanDTO procurementPlanDTO =ProcurementPlanDTO.builder().materialCode(procurementPlan.getMrp().getMaterial().getCode()).ppcode(procurementPlan.getCode())
																			.materialName(procurementPlan.getMrp().getMaterial().getName())
																			.process(procurementPlan.getMrp().getProcess())
																			.mrpdate(procurementPlan.getMrp().getDate()).mrpAmount(procurementPlan.getMrp().getAmount())
																			.freePeriod(makefreePeriod(procurementPlan.getMrp().getDate(),procurementPlan.getDueDate()))
																			.companyName(procurementPlan.getContract().getCompany().getName())
																			.supplyLt(procurementPlan.getContract().getSupplyLt())
																			.dueDate(procurementPlan.getDueDate()).minimumOrderDate(procurementPlan.getMinimumOrderDate())
																			.ppAmount(procurementPlan.getAmount())
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
		
		if(procurementPlanDTO.getPpcode()!=null) {  //조달계획코드가 존재O -> 기존에 있던 DTO 값들을 엔티티로 바꿀때!
			procurementPlan = ProcurementPlan.builder().code(procurementPlanDTO.getPpcode()).mrp(mrpRepository.findById(procurementPlanDTO.getMrpCode()).get())
													.contract(contractRepository.findById(procurementPlanDTO.getContractNo()).get())
													.amount(procurementPlanDTO.getPpAmount())
													.dueDate(procurementPlanDTO.getDueDate()).minimumOrderDate(procurementPlanDTO.getMinimumOrderDate())
													.registerDate(procurementPlanRepository.findById(procurementPlanDTO.getPpcode()).orElse(null).getRegisterDate())
													.completionDate(procurementPlanRepository.findById(procurementPlanDTO.getPpcode()).orElse(null).getCompletionDate())
													.detailPurchaseOrder(procurementPlanRepository.findById(procurementPlanDTO.getPpcode()).orElse(null).getDetailPurchaseOrder())
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
		List<MRP> mrpList = mrpRepository.findAll();
		
		List<ProcurementPlanDTO> ppDTOList = new ArrayList<>();
		ProcurementPlanDTO procurementPlanDTO = null;		
		
		for(int i=0;i<mrpList.size();i++) {
			ProcurementPlan procurementPlan = procurementPlanRepository.findByMrp(mrpList.get(i));	
			
			//procurementPlan 가 존재할때, 조달계획 등록 완료된 상태
			if(procurementPlan!=null) {
				procurementPlanDTO =ProcurementPlanDTO.builder().materialCode(procurementPlan.getMrp().getMaterial().getCode()).ppcode(procurementPlan.getCode())
																.materialName(procurementPlan.getMrp().getMaterial().getName()).process(procurementPlan.getMrp().getProcess())
																.mrpdate(procurementPlan.getMrp().getDate()).mrpAmount(procurementPlan.getMrp().getAmount())
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
																.process(mrpList.get(i).getProcess()).mrpdate(mrpList.get(i).getDate()).mrpAmount(mrpList.get(i).getAmount())
																.contractStatus(contractStatusCheck(mrpList.get(i).getMaterial())).ppRegisterStatus("미완료")
																.mrpCode(mrpList.get(i).getCode()).build();	
				ppDTOList.add(procurementPlanDTO);
			}			
		}	
		
		return ppDTOList;
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
				procurementPlanDTO =ProcurementPlanDTO.builder().materialCode(procurementPlan.getMrp().getMaterial().getCode()).ppcode(procurementPlan.getCode())
																.materialName(procurementPlan.getMrp().getMaterial().getName()).process(procurementPlan.getMrp().getProcess())
																.mrpdate(procurementPlan.getMrp().getDate()).mrpAmount(procurementPlan.getMrp().getAmount())
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
																.process(mrpList.get(i).getProcess()).mrpdate(mrpList.get(i).getDate()).mrpAmount(mrpList.get(i).getAmount())
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
		procurementPlanRepository.deleteById(procurementPlanDTO.getPpcode());
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

	@Override
	public ProcurementPlanDTO getProcurementPlanCalculate(ProcurementPlanDTO procurementPlanDTO) {
		//화면에서 받을 procurementPlanDTO 정보 => 소요일, 기본여유기간, 품목공급LT
		//소요일 -> 등록화면 셋팅된 값에서 얻기
		//기본여유기간, 품목공급LT -> 계약서 찾기 모달창에서 얻기
		
		//조달납기 예정일 구하기 -> procurementPlanDTO 에 값 셋팅해주기
		Date dueDate = makeDuedate(procurementPlanDTO.getMrpdate(), procurementPlanDTO.getFreePeriod());
		procurementPlanDTO.setDueDate(dueDate);
		
		//최소발주일 구하기 -> procurementPlanDTO 에 값 셋팅해주기
		Date minimumOrderDate=makeMinimumOrderDate(dueDate,procurementPlanDTO.getSupplyLt());
		procurementPlanDTO.setMinimumOrderDate(minimumOrderDate);
				
		return procurementPlanDTO;
	}



	

	
	
	
}
