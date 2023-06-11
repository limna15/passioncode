package com.passioncode.procurementsystem.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.passioncode.procurementsystem.dto.MaterialDTO;
import com.passioncode.procurementsystem.service.MaterialService;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Controller
@RequiredArgsConstructor
@RequestMapping("procurement1")
@Log4j2
public class PS1Controller {
	
	private final MaterialService materialService;
	
	@GetMapping("materialList")
	public void PS1List(MaterialDTO materialDTO,Model model) {
		log.info("품목정보 목록 보기.....");
		
		model.addAttribute("DTOList",materialService.getDTOList());		
	}
	
	
	@GetMapping("materialRegister")
	public void PS1Register(MaterialDTO materialDTO,Model model) {
		log.info("품목정보 등록 보기.....");
		
		
	}
	
	
	@GetMapping("contractList")
	public void PS1List2() {
		log.info("계약 목록 보기.....");
		
	}

	@GetMapping("procurementPlanList")
	public void PS1List3() {
		log.info("조달 계획 목록 보기.....");
		
	}

}
