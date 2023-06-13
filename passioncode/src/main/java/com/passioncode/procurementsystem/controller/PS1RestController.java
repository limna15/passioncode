package com.passioncode.procurementsystem.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.passioncode.procurementsystem.dto.ContractDTO;
import com.passioncode.procurementsystem.entity.Company;
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
	
	
	@PostMapping(value="companySearch")
	public List<ContractDTO> searchCompany(@RequestBody String companyName){	//post메핑이라서 파라미터로 못 읽기 때문에, @RequestBody 바디에 실어서 보내기
		log.info("값은 가져오나 보자 : "+companyName);
		
		List<ContractDTO> searchContractDTOs = new ArrayList<>();
		List<Company> companyList = contractService.searchCompany(companyName);
		log.info("가져온걸로 잘 검색했나~~"+companyList);
		
		for(Company company:companyList) {
			ContractDTO contractDTO = ContractDTO.builder().companyNo(company.getNo()).companyName(company.getName())
															.manager(company.getManager()).managerTel(company.getManagerTel()).build();
			searchContractDTOs.add(contractDTO);
		}
		
		log.info("만들어진 회사리스트 보자 : "+searchContractDTOs);
		
		return searchContractDTOs;
	}
	

}
