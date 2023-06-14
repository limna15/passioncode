package com.passioncode.procurementsystem.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.passioncode.procurementsystem.dto.ContractDTO;
import com.passioncode.procurementsystem.dto.ProcurementPlanDTO;
import com.passioncode.procurementsystem.entity.Company;
import com.passioncode.procurementsystem.entity.Contract;
import com.passioncode.procurementsystem.service.ContractService;
import com.passioncode.procurementsystem.service.LargeCategoryService;
import com.passioncode.procurementsystem.service.MaterialService;
import com.passioncode.procurementsystem.service.MiddleCategoryService;
import com.passioncode.procurementsystem.service.ProcurementPlanService;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@RestController
@RequiredArgsConstructor
@RequestMapping("procurement1")
@Log4j2
public class PS1RestController {
	
	private final MaterialService materialService;
	private final LargeCategoryService largeCategoryService;
	private final MiddleCategoryService middleCategoryService;
	private final ContractService contractService;
	private final ProcurementPlanService procurementPlanService; 
	
	/**
	 * 계약 등록 화면에서, 회사이름을 통해서 협력회사 찾기
	 * @param companyName
	 * @return
	 */
	@PostMapping(value="companySearch")
	public List<ContractDTO> searchCompany(@RequestBody String companyName){	//post메핑이라서 파라미터로 못 읽기 때문에, @RequestBody 바디에 실어서 보내기
		log.info("값은 가져오나 보자 : "+companyName);
		
		List<ContractDTO> searchContractDTOs = new ArrayList<>();
		List<Company> companyList = contractService.searchCompanyWithDeal(companyName);
		log.info("가져온걸로 잘 검색했나~~"+companyList);
		
		for(Company company:companyList) {
			ContractDTO contractDTO = ContractDTO.builder().companyNo(company.getNo()).companyName(company.getName())
															.manager(company.getManager()).managerTel(company.getManagerTel()).build();
			searchContractDTOs.add(contractDTO);
		}
		
		log.info("만들어진 회사리스트 보자 : "+searchContractDTOs);
		
		return searchContractDTOs;
	}
	/**
	 * 조달계획 등록 화면에서, 회사이름을 통해 계약서 찾기 <br>
	 * 이때, 해당 품목코드를 읽어와서, 그 해당되는 계약서롤 찾기
	 * @param companyName
	 * @return
	 */
	@PostMapping(value="contractSearch",produces=MediaType.APPLICATION_JSON_VALUE)
	public List<ContractDTO> contractSearch(@RequestBody ContractDTO contractDTO){
		//계약서번호, 품목코드, 품목명, 협력회사, 담당자, 담당자연락처, 품목공급LT, 단가, 거래조건, 계약서, 계약상태 <br>
		//사업자등록번호
		
		//contractDTO로 받아도 보낸 변수는 companyName, materialCode 2개!
		log.info("화면에서 보낸 회사이름,품목코드 json데이터 값 보자 : "+contractDTO); 
				
		List<ContractDTO> searchContractDTOs = new ArrayList<>();
		
		//화면에서 이미 품목코드 값이 셋팅되서 넘어오는데, 회사이름을 입력안할시엔 null이 아닌 빈값으로 받아오기때문에, 자꾸 값이 넘어간다!
		//따라서 받은 회사이름이 빈값이 아닐때만 값을 리스트에 담아서 보내주자!
		if(contractDTO.getCompanyName()==null || contractDTO.getCompanyName().equals("")) {
			searchContractDTOs=null;
		}else {
			List<Contract> contractList = contractService.searchContractByCompanyNameAndMaterialCode(contractDTO.getCompanyName(), contractDTO.getMaterialCode());
			log.info("잘 검색해서 왔나 계약서 보자 : "+contractList);
			
			//거래조건은 null일경우 null 이라고 화면에 그대로 찍히기 때문에, 거래조건이 null일때 "" 빈값으로 셋팅 바꿔주자
			for(Contract contract:contractList) {
				ContractDTO contractDTO2 = contractService.contractEntityToDTO(contract);
				if(contractDTO2.getDealCondition()==null) {
					contractDTO2.setDealCondition("");
				}
				searchContractDTOs.add(contractDTO2);
			}				
		}		
		
		log.info("만들어진 계약서 리스트 보자 : "+searchContractDTOs);
		
		return searchContractDTOs;		
	}
	
	/**
	 * 조달계획 등록 화면에서, 조달납기예정일, 최소발주일을 계산해주기 <br>
	 * 소요일, 기본여유기간, 품목공급LT 이용 <br>
	 * 소요일, 기본여유기간, 품목공급LT -> 조달납기예정일, 최소발주일
	 * @param procurementPlanDTO
	 * @return
	 */
	@PostMapping(value="procurementPlanCalculate",produces=MediaType.APPLICATION_JSON_VALUE)
	public ProcurementPlanDTO procurementPlanCalculate(@RequestBody ProcurementPlanDTO procurementPlanDTO){		
		//화면에서 받을 procurementPlanDTO 정보 => 소요일, 기본여유기간, 품목공급LT
		//소요일 -> 등록화면 셋팅된 값에서 얻기
		//기본여유기간, 품목공급LT -> 계약서 찾기 모달창에서 얻기		
		//위의 값으로 조달납기예정일, 최소발주일 값 계산해주기
		log.info("소요일, 기본여유기간, 품목공급LT 객체로 보낸 json 타입 보자 : "+procurementPlanDTO); 		
		
		ProcurementPlanDTO result = procurementPlanService.getProcurementPlanCalculate(procurementPlanDTO);
		
		log.info("계산된 값 확인해 보자 : "+result);
		
		return result;
	}
	
	

}
