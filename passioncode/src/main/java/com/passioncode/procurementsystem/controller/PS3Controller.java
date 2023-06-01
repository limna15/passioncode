package com.passioncode.procurementsystem.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.passioncode.procurementsystem.repository.MaterialInRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Controller
@RequiredArgsConstructor
@RequestMapping("procurement3")
@Log4j2
public class PS3Controller {
	private final MaterialInRepository repository;

	@GetMapping("/materialIn")
	public void materialIn(Model model) {
		model.addAttribute("list",repository.findAll());
		
	}
	
	@GetMapping("/transactionList")
	public void transactionList() {
		
	}
}
