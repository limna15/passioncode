package com.passioncode.procurementsystem.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.passioncode.procurementsystem.dto.ContractDTO;
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
	
	/**
	 * 품목정보 목록 화면 보기 <br>	  
	 * @param model
	 */
	@GetMapping("materialList")
	public void MaterialList(Model model) {
		log.info("품목정보 목록 화면 보기.....");
		
		model.addAttribute("DTOList",materialService.getDTOList());		
	}
	
	/**
	 * 품목정보 등록 화면 보기
	 * @param model
	 */
	@GetMapping("materialRegister")
	public void MaterialRegister(Model model) {
		log.info("품목정보 등록 화면 보기.....");
		
		model.addAttribute("LargeCategoryDTOList", largeCategoryService.getDTOList());
		model.addAttribute("MiddleCategoryDTOList", middleCategoryService.getDTOList());
	}
	
	/**
	 * 품목정보 등록 처리 
	 * @param materialDTO
	 * @param redirectAttributes
	 * @param request
	 * @return
	 */
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
				if(!size[i].equals("")) {							//빈값이 아닐때
					materialDTO2.setSize(size[i]);					
				}else {												//"" 빈값으로 받아올때
					materialDTO2.setSize(null);
				}
			}else {													//받아온 size가 존재 X
				materialDTO2.setSize(null);	
			}
			
			if(quality != null) {									//받아온 quality가 존재 O
				if(!quality[i].equals("")) {						//빈값이 아닐때
					materialDTO2.setQuality(quality[i]);			
				}else {												//"" 빈값으로 받아올때
					materialDTO2.setQuality(null);					
				}
			}else {													//받아온 quality가 존재 X
				materialDTO2.setQuality(null);				
			}
			
			if(spec != null) {										//받아온 spec가 존재 O
				if(!spec[i].equals("")) {							//빈값이 아닐때
					materialDTO2.setSpec(spec[i]);								
				}else {												//"" 빈값으로 받아올때
					materialDTO2.setSpec(null);
				}
			}else {													//받아온 spec가 존재 X
				materialDTO2.setSpec(null);
			}
			
			if(drawingNo != null) {									//받아온 drawingNo가 존재 O
				if(!drawingNo[i].equals("")) {						//빈값이 아닐때
					materialDTO2.setDrawingNo(drawingNo[i]);		
				}else {												//"" 빈값으로 받아올때
					materialDTO2.setDrawingNo(null);
				}				
			}else {													//받아온 drawingNo가 존재 X
				materialDTO2.setDrawingNo(null);
			}
			
			if(drawingFile != null) {								//받아온 drawingFile가 존재 O
				if(!drawingFile[i].equals("")) {					//빈값이 아닐때
					materialDTO2.setDrawingFile(drawingFile[i]);	
				}else {												//"" 빈값으로 받아올때
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
	
	/**
	 * 계약 목록 화면 보기
	 * @param model
	 */
	@GetMapping("contractList")
	public void ContractList(Model model) {
		log.info("계약 목록 화면 보기.....");
		
		model.addAttribute("DTOList",contractService.getDTOList());	
	}
	
	/**
	 * 계약 등록 화면 보기
	 * @param materialCodeList
	 * @param model
	 */
	@GetMapping("contractRegister")
	public void ContractRegister(String[] materialCodeList, Model model) {
		log.info("계약 등록 화면 보기.....");
		List<ContractDTO> contractDTOList = new ArrayList<>();
		
		for(int i=0;i<materialCodeList.length;i++) {
			ContractDTO contractDTO = ContractDTO.builder().materialCode(materialCodeList[i]).materialName(materialService.getMaterial(materialCodeList[i]).getName()).build();
			contractDTOList.add(contractDTO);
		}
		
		model.addAttribute("contractDTOList", contractDTOList);
		
	}	
	
	/**
	 * 계약 등록 처리
	 * @param contractDTO
	 * @param redirectAttributes
	 * @param request
	 */
	@PostMapping("contractRegister")
	public void ContractRegister2(ContractDTO contractDTO,RedirectAttributes redirectAttributes,HttpServletRequest request) {
		//계약서번호, 품목코드, 품목명, 협력회사, 담당자, 담당자연락처, 품목공급LT, 단가, 거래조건, 계약서, 계약상태 <br>
		//사업자등록번호
		
		log.info("계약 등록 처리.....");
		
		log.info("화면에서 보낸 ContractDTO 가져오나 보자 : "+contractDTO);
		//같은 이름으로 input 오는거 배열로 넘어옴 -> 각각을 배열로 받아서 하나씩 원하는대로 세팅해줘야함
		
		List<ContractDTO> contractDTOList = new ArrayList<>();
		
		//계약서 번호 -> 등록하는 거라서 null 값, 애초에 보내지도 않음
		
		//품목코드 배열
		String[] materialCode = request.getParameterValues("materialCode");	
		//품목명 배열
		String[] materialName = request.getParameterValues("materialName");
		//협력회사명 배열
		String[] companyName = request.getParameterValues("companyName");
		//담당자 배열
		String[] manager = request.getParameterValues("manager");
		//담당자연락처 배열
		String[] managerTel = request.getParameterValues("managerTel");
		//품목공급LT 배열  -> Integer로 바꿔줘야 함
		String[] supplyLt = request.getParameterValues("supplyLt");
		//단가 배열  -> Integer로 바꿔줘야 함
		String[] unitPrice = request.getParameterValues("unitPrice");
		//거래조건 배열
		String[] dealCondition = request.getParameterValues("dealCondition");
		//계약서 배열
		String[] contractFile = request.getParameterValues("contractFile");
		//사업자등록번호 배열
		String[] companyNo =  request.getParameterValues("companyNo");
		//계약상태 -> 이제 등록하는거니까 완료로 해서 보냄
		
		//품목코드는 null일수 없으니까, 품목코드 배열 기준으로, 각각의 DTO에 넣어주고, 그값 DTO리스트에 넣기
		for(int i=0; i<materialCode.length; i++) {
			ContractDTO contractDTO2 = new ContractDTO();
			//계약서 번호 null
			contractDTO2.setContractStatus("완료");			
			contractDTO2.setMaterialCode(materialCode[i]);
			contractDTO2.setMaterialName(materialName[i]);
			contractDTO2.setCompanyName(companyName[i]);;
			contractDTO2.setManager(manager[i]);
			contractDTO2.setManagerTel(managerTel[i]);
			contractDTO2.setSupplyLt(Integer.parseInt(supplyLt[i]));
			contractDTO2.setUnitPrice(Integer.parseInt(unitPrice[i]));
			//계약서 업로드 null 허용이 아니라서, 일단은 "화면DB테스트 문구"라고 보내자
//			contractDTO2.setContractFile(contractFile[i]);
			contractDTO2.setContractFile("화면DB 테스트중");
			contractDTO2.setCompanyNo(companyNo[i]);
			
			if(dealCondition != null) {									//받아온 dealCondition가 존재 O
				if(!dealCondition[i].equals("")) {						//빈값이 아닐때
					contractDTO2.setDealCondition(dealCondition[i]);				
				}else {													//"" 빈값으로 받아올때
					contractDTO2.setDealCondition(null);
				}
			}else {														//받아온 dealCondition가 존재 X
				contractDTO2.setDealCondition(null);
			}
			
			contractDTOList.add(contractDTO2);
		}
		
		log.info("만든 contractDTOList 보자 : "+contractDTOList);
	
		//등록된 계약서 번호 리스트
		List<Integer> registerList = new ArrayList<>();
		//받아온 DTO리스트 각각 DB에 저장하기
		for(ContractDTO dto : contractDTOList) {
			registerList.add(contractService.register(dto));
//			log.info("각각 엔티티 변환 보자 : "+contractService.dtoToEntity(dto));
		}
		
		redirectAttributes.addFlashAttribute("registerList",registerList);
		
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
