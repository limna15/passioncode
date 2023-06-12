package com.passioncode.procurementsystem.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.passioncode.procurementsystem.dto.MaterialDTO;
import com.passioncode.procurementsystem.service.ContractService;
import com.passioncode.procurementsystem.service.LargeCategoryService;
import com.passioncode.procurementsystem.service.MaterialService;
import com.passioncode.procurementsystem.service.MiddleCategoryService;
import com.passioncode.procurementsystem.service.ProcurementPlanService;

import jakarta.servlet.http.HttpServletRequest;
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
		log.info("품목정보 목록 화면 보기.....");
		
		model.addAttribute("DTOList",materialService.getDTOList());		
	}
	
	
	@GetMapping("materialRegister")
	public void MaterialRegister(Model model) {
		log.info("품목정보 등록 화면 보기.....");
		
		model.addAttribute("LargeCategoryDTOList", largeCategoryService.getDTOList());
		model.addAttribute("MiddleCategoryDTOList", middleCategoryService.getDTOList());
	}
	
	@PostMapping("materialRegister")
	public String MaterialRegister2(MaterialDTO materialDTO,RedirectAttributes redirectAttributes,HttpServletRequest request) {
		//품목코드, 품목명, 대, 중, 규격, 재질, 제작사양, 도면번호, 도면Image, 공용여부, 계약상태 <br>
		//대분류코드, 중분류코드, 기존재고수량
		
		log.info("품목정보 등록 처리.....");
		
		log.info("화면에서 보낸 MaterialDTO 가져오나 보자 : "+materialDTO);
		//같은 이름으로 input 오는거 배열로 넘어옴 -> 각각을 배열로 받아서 하나씩 원하는대로 세팅해줘야함
		
		List<MaterialDTO> materialDTOList = new ArrayList<>();
				
		//품목코드 배열
		String[] code = request.getParameterValues("code");	
		//품목명 배열
		String[] name = request.getParameterValues("name");
		//대분류코드 배열
		String[] largeCategoryCode = request.getParameterValues("largeCategoryCode");
		//중분류코드 배열
		String[] middleCategoryCode = request.getParameterValues("middleCategoryCode");
		//규격 배열
		String[] size = request.getParameterValues("size");
		//재질 배열
		String[] quality = request.getParameterValues("quality");
		//제작사양 배열
		String[] spec = request.getParameterValues("spec");
		//도면번호 배열
		String[] drawingNo = request.getParameterValues("drawingNo");
		//도면 배열
		String[] drawingFile = request.getParameterValues("drawingFile");
		//공용여부 배열
		String[] shareStatus =  request.getParameterValues("shareStatus");
		
		//품목코드는 null일수 없으니까, 품목코드 배열 기준으로, 각각의 DTO에 넣어주고, 그값 DTO리스트에 넣기
		for(int i=0; i<code.length; i++) {
			MaterialDTO materialDTO2 = new MaterialDTO();
			materialDTO2.setCode(code[i]);
			materialDTO2.setName(name[i]);
			materialDTO2.setLargeCategoryCode(largeCategoryCode[i]);
			materialDTO2.setLargeCategoryName(middleCategoryService.getLargeCategory(largeCategoryCode[i]).getCategory());
			materialDTO2.setMiddleCategoryCode(middleCategoryCode[i]);
			materialDTO2.setMiddleCategoryName(middleCategoryService.getMiddleCategory(middleCategoryCode[i]).getCategory());
			materialDTO2.setShareStatus(shareStatus[i]);
			materialDTO2.setContractStatus("미완료");
			materialDTO2.setStockAmount(0);
			if(size != null) {										//받아온 size가 존재 O
				if(!size[i].equals("")) {							//"" 빈값으로 받아올때
					materialDTO2.setSize(size[i]);					
				}else {												//빈값이 아닐때
					materialDTO2.setSize(null);
				}
			}else {													//받아온 size가 존재 X
				materialDTO2.setSize(null);	
			}
			
			if(quality != null) {									//받아온 quality가 존재 O
				if(!quality[i].equals("")) {						//"" 빈값으로 받아올때
					materialDTO2.setQuality(quality[i]);			
				}else {												//빈값이 아닐때
					materialDTO2.setQuality(null);					
				}
			}else {													//받아온 quality가 존재 X
				materialDTO2.setQuality(null);				
			}
			
			if(spec != null) {										//받아온 spec가 존재 O
				if(!spec[i].equals("")) {							//"" 빈값으로 받아올때
					materialDTO2.setSpec(spec[i]);								
				}else {												//빈값이 아닐때
					materialDTO2.setSpec(null);
				}
			}else {													//받아온 spec가 존재 X
				materialDTO2.setSpec(null);
			}
			
			if(drawingNo != null) {									//받아온 drawingNo가 존재 O
				if(!drawingNo[i].equals("")) {						//"" 빈값으로 받아올때
					materialDTO2.setDrawingNo(drawingNo[i]);		
				}else {												//빈값이 아닐때
					materialDTO2.setDrawingNo(null);
				}				
			}else {													//받아온 drawingNo가 존재 X
				materialDTO2.setDrawingNo(null);
			}
			
			if(drawingFile != null) {								//받아온 drawingFile가 존재 O
				if(!drawingFile[i].equals("")) {					//"" 빈값으로 받아올때
					materialDTO2.setDrawingFile(drawingFile[i]);	
				}else {												//빈값이 아닐때
					materialDTO2.setDrawingFile(null);
				}				
			}else {													//받아온 drawingFile가 존재 X
				materialDTO2.setDrawingFile(null);
			}			
			materialDTOList.add(materialDTO2);
		}
		
		log.info("만든 materialDTOList 보자 : "+materialDTOList);
		
		//등록된 품목코드 리스트
		List<String> registerList = new ArrayList<>();
		//받아온 DTO리스트 각각 DB에 저장하기
		for(MaterialDTO dto : materialDTOList) {
			registerList.add(materialService.register(dto));
		}
		
		redirectAttributes.addFlashAttribute("registerList",registerList);
		
		return "redirect:/procurement1/materialList";
	}
	
	
	@GetMapping("contractList")
	public void ContractList(Model model) {
		log.info("계약 목록 화면 보기.....");
		
		model.addAttribute("DTOList",contractService.getDTOList());	
	}
	
	@GetMapping("contractRegister")
	public void ContractRegister(Model model) {
		log.info("계약 등록 화면 보기.....");
		
	}	

	@GetMapping("procurementPlanList")
	public void ProcurementPlanList(Model model) {
		log.info("조달 계획 목록 화면 보기.....");
		
		model.addAttribute("DTOList",procurementPlanService.getDTOList());
	}
	
	
	@GetMapping("procurementPlanRegister")
	public void ProcurementPlanRegister(Model model) {
		log.info("조달 계획 등록 화면 보기.....");
		
	}

}
