package com.passioncode.procurementsystem.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.passioncode.procurementsystem.dto.MaterialDTO;
import com.passioncode.procurementsystem.service.ContractService;
import com.passioncode.procurementsystem.service.LargeCategoryService;
import com.passioncode.procurementsystem.service.MaterialService;
import com.passioncode.procurementsystem.service.MiddleCategoryService;
import com.passioncode.procurementsystem.service.ProcurementPlanService;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Controller
@RequiredArgsConstructor
@RequestMapping("procurement1")
@Log4j2
public class PS1Controller {
	
	private final MaterialService materialService;
	private final LargeCategoryService largeCategoryService;
	private final MiddleCategoryService middleCategoryService;
	private final ContractService contractService;
	private final ProcurementPlanService procurementPlanService; 
	
	@GetMapping("materialList")
	public void MaterialList(Model model) {
		log.info("품목정보 목록 보기.....");
		
		model.addAttribute("DTOList",materialService.getDTOList());		
	}
	
	
	@GetMapping("materialRegister")
	public void MaterialRegister(Model model) {
		log.info("품목정보 등록 보기.....");
		
		model.addAttribute("LargeCategoryDTOList", largeCategoryService.getDTOList());
		model.addAttribute("MiddleCategoryDTOList", middleCategoryService.getDTOList());
	}
	
	
	@GetMapping("contractList")
	public void ContractList(Model model) {
		log.info("계약 목록 보기.....");
		
		model.addAttribute("DTOList",contractService.getDTOList());	
	}
	
	@GetMapping("contractRegister")
	public void ContractRegister(Model model) {
		log.info("계약 등록 보기.....");
		
	}	

	@GetMapping("procurementPlanList")
	public void ProcurementPlanList(Model model) {
		log.info("조달 계획 목록 보기.....");
		
		model.addAttribute("DTOList",procurementPlanService.getDTOList());
	}
	
	
	@GetMapping("procurementPlanRegister")
	public void ProcurementPlanRegister(Model model) {
		log.info("조달 계획 등록 보기.....");
		
	}

}
