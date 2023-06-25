package com.passioncode.procurementsystem.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.passioncode.procurementsystem.dto.MaterialOutDTO;
import com.passioncode.procurementsystem.entity.MRP;
import com.passioncode.procurementsystem.entity.ProcurementPlan;
import com.passioncode.procurementsystem.service.MaterialOutService;
import com.passioncode.procurementsystem.service.ProcurementPlanService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Controller
@RequiredArgsConstructor
@RequestMapping("procurement4")
@Log4j2
public class PS4Controller {
	
	private final MaterialOutService materialOutService;
	private final ProcurementPlanService procurementPlanService;
	
	@GetMapping("materialOut")
	public void materialOut(Model model, MaterialOutDTO materialOutDTO) {
		List<MaterialOutDTO> materialOutDTOList = materialOutService.getDTOList();

		log.info("materialOutDTOlist.............");

		model.addAttribute("DTOList", materialOutDTOList);
	}
	
	@PostMapping("materialOutRegister")
	public String materialOutRegister(MaterialOutDTO materialOutDTO, HttpServletRequest request) {
		log.info("materialOutDTO >>> " + materialOutDTO);
		
		Integer detailPurchaseOrderCodeStr= Integer.parseInt(request.getParameter("dpoCode"));
//		String materialCode= request.getParameter("materialCode");
//		String materialName= request.getParameter("materialName");
//		Integer amount= Integer.parseInt(request.getParameter("amount"));
		String status= request.getParameter("status");
		
		log.info("js로 만들어 보낸 form 데이터 detailPurchaseOrderCode잘 받아오나 >>> " + detailPurchaseOrderCodeStr);
//		log.info("js로 만들어 보낸 form 데이터 materialCode 잘 받아오나 >>> " + materialCode);
//		log.info("js로 만들어 보낸 form 데이터 materialName 잘 받아오나 >>> " + materialName);
//		log.info("js로 만들어 보낸 form 데이터 amount 잘 받아오나 >>> " + amount);
		log.info("js로 만들어 보낸 form 데이터 status 잘 받아오나 >>> " + status);
		
		ProcurementPlan pp= procurementPlanService.getPpByDetailPurchaseOrder(detailPurchaseOrderCodeStr);
		Integer mrpCode= pp.getMrp().getCode();
		
		materialOutDTO= MaterialOutDTO.builder().mrpCode(mrpCode).outStatus(status).build();
		materialOutService.register(materialOutDTO);
		
		return "redirect:/procurement4/materialOut";
	}
	
	@GetMapping("stockResult")
	public void stockResult() {
		
	}
	
	@GetMapping("stockReport")
	public void stockReport() {
		
	}

}
