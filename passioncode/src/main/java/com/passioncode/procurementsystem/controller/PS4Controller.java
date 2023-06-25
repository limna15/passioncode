package com.passioncode.procurementsystem.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.passioncode.procurementsystem.dto.MaterialOutDTO;
import com.passioncode.procurementsystem.service.MaterialOutService;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Controller
@RequiredArgsConstructor
@RequestMapping("procurement4")
@Log4j2
public class PS4Controller {
	
	private final MaterialOutService materialOutService;
	
	@GetMapping("materialOut")
	public void materialOut(Model model, MaterialOutDTO materialOutDTO) {
		List<MaterialOutDTO> materialOutDTOList = materialOutService.getDTOList();

		log.info("materialOutDTOlist.............");

		model.addAttribute("DTOList", materialOutDTOList);
	}
	
	@GetMapping("stockResult")
	public void stockResult() {
		
	}
	
	@GetMapping("stockReport")
	public void stockReport() {
		
	}

}
