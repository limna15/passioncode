package com.passioncode.procurementsystem.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.passioncode.procurementsystem.dto.ContractDTO;
import com.passioncode.procurementsystem.dto.LargeCategoryDTO;
import com.passioncode.procurementsystem.dto.MaterialDTO;
import com.passioncode.procurementsystem.dto.MiddleCategoryDTO;
import com.passioncode.procurementsystem.dto.ProcurementPlanDTO;
import com.passioncode.procurementsystem.entity.Contract;
import com.passioncode.procurementsystem.entity.LargeCategory;
import com.passioncode.procurementsystem.entity.MRP;
import com.passioncode.procurementsystem.entity.Material;
import com.passioncode.procurementsystem.entity.MiddleCategory;
import com.passioncode.procurementsystem.entity.ProcurementPlan;
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
		List<MaterialDTO> DTOList = materialService.getDTOList();
//		log.info("품목정보 목록 화면 에서의 DTOList 봐보자~~~~~~~~~~~ : "+DTOList);
//		log.info("어디 그 이미지 dto 읽어보자 getDrawingUploadPath() : "+DTOList.get(0).getDrawingFileDTO().getDrawingUploadPath() );
//		log.info("어디 그 이미지 dto 읽어보자 getFileName() : "+DTOList.get(0).getDrawingFileDTO().getFileName() );
//		log.info("어디 그 이미지 dto 읽어보자 getUuid() : "+DTOList.get(0).getDrawingFileDTO().getUuid() );
//		log.info("어디 그 이미지 dto 읽어보자 getFolderPath() : "+DTOList.get(0).getDrawingFileDTO().getFolderPath() );
//		log.info("어디 그 이미지 dto 읽어보자 isImage() : "+DTOList.get(0).getDrawingFileDTO().isImage() );   
//		log.info("여기 까지 필드선언 기본값~~~~~~~~~~~~~~~~");
//		log.info("어디 그 이미지 dto 읽어보자 getDrawingFile() : "+DTOList.get(0).getDrawingFileDTO().getDrawingFile() );;
//		log.info("어디 그 이미지 dto 읽어보자 getImageURL() : "+DTOList.get(0).getDrawingFileDTO().getImageURL() );
//		log.info("어디 그 이미지 dto 읽어보자 getThumbnailURL() : "+DTOList.get(0).getDrawingFileDTO().getThumbnailURL() );
	}
	
	/**
	 * 품목정보 등록 화면 보기
	 * @param model
	 */
	@GetMapping("materialRegister")
	public void MaterialRegister(Model model) {
		log.info("품목정보 등록 화면 보기.....");
		
		//대분류 셀렉트 정보는 모든 정보 다 보내주기
		List<LargeCategoryDTO> LargeCategoryDTOList = largeCategoryService.getDTOList();
		model.addAttribute("LargeCategoryDTOList", LargeCategoryDTOList );
		
		//중분류 셀렉트 정보는 대분류 리스트에서 처음보여주게 되는거에 해당되는 중분류만 보여주기
		//나머지 중분류는 화면에서 바뀔때마다 불러오기 때문에!
		LargeCategory largeCategory = middleCategoryService.getLargeCategory(LargeCategoryDTOList.get(0).getCode());
		List<MiddleCategory> MiddleCategoryListByLC1 = middleCategoryService.getMiddleCategoryByLargeCategory(largeCategory);
		
		List<MiddleCategoryDTO> MiddleCategoryDTOListByLC1 = new ArrayList<>();
		for(MiddleCategory middleCategory:MiddleCategoryListByLC1) {
			MiddleCategoryDTOListByLC1.add(middleCategoryService.entityToDTO(middleCategory));
		}		
		model.addAttribute("MiddleCategoryDTOList", MiddleCategoryDTOListByLC1);
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
		//품목코드, 품목명, 대, 중, 규격, 재질, 제작사양, 도면번호, 도면Image, 공용여부 <br>
		//대분류코드, 중분류코드
		log.info("품목정보 등록 처리.....");
		log.info("품목 정보 등록 화면에서 보낸 MaterialDTO 가져오나 보자 : "+materialDTO);
		/*
		 * MaterialDTO(code=BS0003,CN0004, name=어디 수정 테스트2,도면 넣는 수정 테스트2, shareStatus=공용,공용, size=,, quality=,, spec=,, 
		 * drawingNo=,CN0004D01, drawingFile=,/PassionCode/upload/drawing/2023/06/25/df918a86-6b73-4dee-aa45-e80bf03d079b_나사 도면 샘플1.pdf, 
		 * drawingFileDTO=DrawingFileDTO(drawingUploadPath=/PassionCode/upload/drawing/, fileName=null, uuid=null, folderPath=null, image=false), 
		 * largeCategoryName=null, middleCategoryName=null, largeCategoryCode=BB0001,CC0001, middleCategoryCode=SS0001,NN0001, middleCategoryDTOList=null)
		 */
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
		log.info("품목 등록처리에서 만든 저장(등록)할 materialDTOList 보자 : "+materialDTOList);
		
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
	 * 품목 수정 화면 보기
	 * @param materialCodeList
	 * @param model
	 */
	@GetMapping("materialModify")
	public void materialModify(String[] materialCodeList, Model model) {
		log.info("품목 수정 화면 보기.....");
		log.info("품목 등록(목록) 화면에서 받아온 품목코드 리스트 보자 : "+materialCodeList);
		
		//클릭한 품목코드 리스트를 통해서, 수정화면에 보낼, MaterialDTO 리스트 만들기		
		List<MaterialDTO> materialDTOList = new ArrayList<>();
		//품목코드,품목명 넣어서 셋팅해주기 -> 계약상태는 아직 계약 등록할지 모르니까 null 넣어서 보내주자
		for(int i=0;i<materialCodeList.length;i++) {
			MaterialDTO materialDTO = materialService.entityToDTO(materialService.getMaterial(materialCodeList[i]));
			materialDTOList.add(materialDTO);
		}
		log.info("수정화면에 보내는 materialDTOList 봐보자 : "+materialDTOList);
		
		model.addAttribute("materialDTOList", materialDTOList);
		
		//대분류 셀렉트 정보는 모든 정보 다 보내주기
		List<LargeCategoryDTO> LargeCategoryDTOList = largeCategoryService.getDTOList();
		model.addAttribute("LargeCategoryDTOList", LargeCategoryDTOList );
		
		//중분류 셀렉트 정보는 읽어온 품목코드리스트에서 품목을 찾고, 그 품목의 중분류 코드를 이용해서,
		//그 중분류의 해당 대분류에서 모든 중분류들! 리스트로 만들어서 materialDTO에 다 보내짐
	}
	
	/**
	 * 품목정보 수정 처리 
	 * @param materialDTO
	 * @param redirectAttributes
	 * @param request
	 * @return
	 */
	@PostMapping("materialModify")
	public String materialModify2(MaterialDTO materialDTO,RedirectAttributes redirectAttributes,HttpServletRequest request) {
		//품목코드, 품목명, 대, 중, 규격, 재질, 제작사양, 도면번호, 도면Image, 공용여부 <br>
		//대분류코드, 중분류코드
		log.info("품목정보 수정 처리.....");
		log.info("품목 수정 화면에서 보낸 MaterialDTO 가져오나 보자 : "+materialDTO);
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
		log.info("품목 수정처리에서 만든 수정할 materialDTOList 보자 : "+materialDTOList);
		
		//받아온 DTO리스트 각각 DB에 수정하기
		for(MaterialDTO dto : materialDTOList) {
			materialService.modify(dto);
		}
		return "redirect:/procurement1/materialList";
	}
	
	@PostMapping("materialDelete")
	public String materialDelete(String[] materialCodeList,RedirectAttributes redirectAttributes) {
		log.info("품목 정보 삭제 처리.....");
		log.info("품목 목록(등록)에서 보낸 삭제하기 위한 materialCodeList"+materialCodeList);
		
		for(String code : materialCodeList) {
			Material material = materialService.getMaterial(code);
			MaterialDTO materialDTO = materialService.entityToDTO(material);
			materialService.delete(materialDTO);
		}
		
		//삭제된 품목코드 리스트
		redirectAttributes.addFlashAttribute("materialCodeList",materialCodeList);
		
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
		 //계약서번호, 품목코드, 품목명, 협력회사, 담당자, 담당자연락처, 품목공급LT, 단가, 거래조건, 계약서, 계약상태 <br>
		 //사업자등록번호
		log.info("계약 등록 화면 보기.....");
		
		//클릭한 품목코드 리스트를 통해서, 등록화면에 보낼, ContractDTO 리스트 만들기		
		List<ContractDTO> contractDTOList = new ArrayList<>();
		//품목코드,품목명 넣어서 셋팅해주기 -> 계약상태는 아직 계약 등록할지 모르니까 null 넣어서 보내주자
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
	public String ContractRegister2(ContractDTO contractDTO,RedirectAttributes redirectAttributes,HttpServletRequest request) {
		//계약서번호, 품목코드, 품목명, 협력회사, 담당자, 담당자연락처, 품목공급LT, 단가, 거래조건, 계약서, 계약상태 <br>
		//사업자등록번호
		
//		log.info("안보낼때 어떻게 읽나 보자 : "+request.getParameterValues("manager"));
//		if(request.getParameterValues("manager")==null) {
//			log.info("안보냈으면 이거 null 인건가????");
//		}
		// => 결론!!! 안보낼때 null 로 보내지는게 아니야!!!! 빈값으로 보내짐!
		log.info("계약 등록 처리.....");
		log.info("계약 등록 화면에서 보낸 ContractDTO 가져오나 보자 : "+contractDTO);
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
			contractDTO2.setContractFile(contractFile[i]);
//			contractDTO2.setContractFile("화면DB 테스트중");
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
		log.info("계약 등록 처리에서 만든 등록할 contractDTOList 보자 : "+contractDTOList);
	
		//등록된 계약서 번호 리스트
		List<Integer> registerList = new ArrayList<>();
		//받아온 DTO리스트 각각 DB에 저장하기
		for(ContractDTO dto : contractDTOList) {
			registerList.add(contractService.register(dto));
//			log.info("각각 엔티티 변환 보자 : "+contractService.dtoToEntity(dto));
		}
		redirectAttributes.addFlashAttribute("registerList",registerList);
		
		return "redirect:/procurement1/contractList";
	}
	
	/**
	 * 계약 수정 화면 보기
	 * @param contractNoList
	 * @param model
	 */
	@GetMapping("contractModify")
	public void contractModify(String[] contractNoList,Model model) {
		log.info("계약 수정 화면 보기.....");
		log.info("계약 등록(목록) 화면에서 받아온 계약서번호 리스트 보자 : "+contractNoList);
		
		//받아온 계약서번호 리스트를 통해서, 수정화면에 보낼, ContractDTO 리스트 만들기
		List<ContractDTO> contractDTOList = new ArrayList<>();
		for(int i=0; i<contractNoList.length; i++) {
			Contract contract = contractService.getContract(Integer.parseInt(contractNoList[i]));
			ContractDTO contractDTO = contractService.contractEntityToDTO(contract);
			contractDTOList.add(contractDTO);
		}
		log.info("계약 수정화면에 보내는 contractDTOList 봐보자 : "+contractDTOList);
		
		model.addAttribute("contractDTOList", contractDTOList);
	}
	
	/**
	 * 계약 수정 처리
	 * @param contractDTO
	 * @param redirectAttributes
	 * @param request
	 */
	@PostMapping("contractModify")
	public String contractModify2(ContractDTO contractDTO,RedirectAttributes redirectAttributes,HttpServletRequest request) {
		//계약서번호, 품목코드, 품목명, 협력회사, 담당자, 담당자연락처, 품목공급LT, 단가, 거래조건, 계약서, 계약상태 <br>
		//사업자등록번호
		log.info("계약 수정 처리.....");
		log.info("계약 수정 화면에서 보낸 ContractDTO 가져오나 보자 : "+contractDTO);
		//같은 이름으로 input 오는거 배열로 넘어옴 -> 각각을 배열로 받아서 하나씩 원하는대로 세팅해줘야함
		
		List<ContractDTO> contractDTOList = new ArrayList<>();
		
		//계약서 번호 배열
		String[] contractNo = request.getParameterValues("contractNo");	
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
		//계약상태 -> 수정하는거니까 완료로 해서 보냄
		
		//품목코드는 null일수 없으니까, 품목코드 배열 기준으로, 각각의 DTO에 넣어주고, 그값 DTO리스트에 넣기
		for(int i=0; i<materialCode.length; i++) {
			ContractDTO contractDTO2 = new ContractDTO();
			contractDTO2.setContractNo(Integer.parseInt(contractNo[i]));
			contractDTO2.setContractStatus("완료");			
			contractDTO2.setMaterialCode(materialCode[i]);
			contractDTO2.setMaterialName(materialName[i]);
			contractDTO2.setCompanyName(companyName[i]);;
			contractDTO2.setManager(manager[i]);
			contractDTO2.setManagerTel(managerTel[i]);
			contractDTO2.setSupplyLt(Integer.parseInt(supplyLt[i]));
			contractDTO2.setUnitPrice(Integer.parseInt(unitPrice[i]));
			contractDTO2.setContractFile(contractFile[i]);
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
		log.info("계약 수정 처리에서 만든 수정할 contractDTOList 보자 : "+contractDTOList);
		
		//받아온 DTO리스트 각각 DB에 수정하기
		for(ContractDTO dto : contractDTOList) {
			contractService.modify(dto);
		}
		return "redirect:/procurement1/contractList";
	}
	
	/**
	 * 조달계획 목록 화면보기
	 * @param model
	 */
	@GetMapping("procurementPlanList")
	public void ProcurementPlanList(Model model) {
		log.info("조달계획 목록 화면 보기.....");
		
		model.addAttribute("DTOList",procurementPlanService.getDTOList());
	}
	
	/**
	 * 조달계획 등록 화면보기
	 * @param mrpCodeList
	 * @param model
	 */
	@GetMapping("procurementPlanRegister")
	public void ProcurementPlanRegister(String[] mrpCodeList,Model model) {
		//품목코드, 품목명, 소요공정, 소요일, 소요량, 협력회사, 품목공급LT, 조달납기예정일, 최소발주일, 필요수량, 계약상태, 조달계획 등록상태, 조달계획 진행사항 <br>
		//조달계획코드, 자재소요계획코드, 사업자등록번호, 계약서번호 // 기본 여유기간
		
		log.info("조달계획 등록 화면 보기.....");
		
		//클릭한 mrp코드 리스트를 통해서, 등록화면에 보낼, ProcurementPlanDTO 리스트 만들기	
		List<ProcurementPlanDTO> procurementPlanDTOList = new ArrayList<>();
		
		//품목코드, 품목명, 소요공정, 소요일, 소요량, 자재소요계획코드 셋팅 가능
		//계약상태는 완료된것이라서 조달계획 등록 가능하기 때문에, 계약상태 = 완료 로 보내기
		//조달계획 등록상태, 조달계획 진행사항 은 아직 조달계획이 등록될지 모르니까 null 로 보내주자
		for(int i=0;i<mrpCodeList.length;i++) {
			MRP mrp = procurementPlanService.getMRP(Integer.parseInt(mrpCodeList[i]));
			ProcurementPlanDTO procurementPlanDTO = ProcurementPlanDTO.builder().materialCode(mrp.getMaterial().getCode()).materialName(mrp.getMaterial().getName())
																				.process(mrp.getProcess()).mrpDate(mrp.getDate()).mrpAmount(mrp.getAmount()).mrpCode(mrp.getCode())
																				.contractStatus("완료").build();
			procurementPlanDTOList.add(procurementPlanDTO);
		}
		log.info("(조달계획 목록에서 받은 mrpCodeList를 통해) 조달계획 등록화면에 보낼 procurementPlanDTOList 리스트 보자 : "+procurementPlanDTOList);
		
		model.addAttribute("procurementPlanDTOList", procurementPlanDTOList);		
	}
	
	/**
	 * 조달계획 등록 처리
	 * @param procurementPlanDTO
	 * @param redirectAttributes
	 * @param request
	 * @return
	 */
	@PostMapping("procurementPlanRegister")
	public String ProcurementPlanRegister2(ProcurementPlanDTO procurementPlanDTO,RedirectAttributes redirectAttributes,HttpServletRequest request) {
		//품목코드, 품목명, 소요공정, 소요일, 소요량, 협력회사, 품목공급LT, 조달납기예정일, 최소발주일, 필요수량, 계약상태, 조달계획 등록상태, 조달계획 진행사항 <br>
		//조달계획코드, 자재소요계획코드, 사업자등록번호, 계약서번호 // 기본 여유기간
				
		log.info("조달계획 등록 처리.....");
		
		log.info("조달계획 등록 화면에서 보낸 ProcurementPlanDTO 가져오나 보자 : "+procurementPlanDTO);		
		//같은 이름으로 input 오는거 배열로 넘어옴 -> 각각을 배열로 받아서 하나씩 원하는대로 세팅해줘야함
		//받아온 date(소요일, 조달납기예정일, 최소발주일) String으로 왔기 때문에 Date 형식으로 변환해줘야함
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		
		List<ProcurementPlanDTO> procurementPlanDTOList = new ArrayList<>();
		
		//조달계획코드 -> 등록하는 거라서 null 값, 애초에 보내지도 않음
		
		//품목코드 배열
		String[] materialCode = request.getParameterValues("materialCode");	
		//품목명 배열
		String[] materialName = request.getParameterValues("materialName");
		//소요공정 배열
		String[] process = request.getParameterValues("process");		
		//소요일 배열 -> Date 형식으로 변환
		String[] mrpDateStr = request.getParameterValues("mrpDate");		
//		log.info("지금 이 mrpDate를 못 읽어서 오는건가???? 봐보자!!!");
//		for(String test:mrpDateStr) {
//			log.info("받아온 mrpDate 하나씩 봐보자 : "+test);
//		}		
//		Date[] mrpDate = null; 	 => 이렇게 했더니, 밑에 try catch 에서 잡게 되므로 null이 존재할 가능성때문에, 자바 에러가 뜸
		List<Date> mrpDateList = new ArrayList<>();
		for(int i=0;i<mrpDateStr.length;i++) {
			try {
				mrpDateList.add(simpleDateFormat.parse(mrpDateStr[i]));
			} catch (ParseException e) {
				log.info("소요일 date 변환시 오류 발생함!!");
			}
			log.info("변경되서 넣어진 소요일 날짜 리스트 봐보자 : "+mrpDateList);
		}	
		//소요량 배열 -> Integer로 변경
		String[] mrpAmount = request.getParameterValues("mrpAmount");	
		//협력회사 배열
		String[] companyName = request.getParameterValues("companyName");				
		//품목공급LT 배열 -> Integer로 변경
		String[] supplyLt = request.getParameterValues("supplyLt");	
		//조달납기예정일 배열 -> Date 형식으로 변환
		String[] dueDateStr = request.getParameterValues("dueDate");		
		List<Date> dueDateList = new ArrayList<>();
		for(int i=0;i<dueDateStr.length;i++) {
			try {
				dueDateList.add(simpleDateFormat.parse(dueDateStr[i]));
			} catch (ParseException e) {
				log.info("조달납기예정일 date 변환시 오류 발생함!!");
			}
			log.info("변경되서 넣어진 조달납기예정일 날짜 리스트 봐보자 : "+dueDateList);
		}
		//최소발주일 배열 -> Date 형식으로 변환
		String[] minimumOrderDateStr = request.getParameterValues("minimumOrderDate");		
		List<Date> minimumOrderDateList = new ArrayList<>();
		for(int i=0;i<minimumOrderDateStr.length;i++) {
			try {
				minimumOrderDateList.add(simpleDateFormat.parse(minimumOrderDateStr[i]));
			} catch (ParseException e) {
				log.info("최소발주일일 date 변환시 오류 발생함!!");
			}
			log.info("변경되서 넣어진 최소발주일 날짜 리스트 봐보자 : "+minimumOrderDateList);
		}
		//필요수량 배열 -> Integer로 변경
		String[] ppAmount = request.getParameterValues("ppAmount");	
		//자재소요계획코드 배열 -> Integer로 변경
		String[] mrpCode = request.getParameterValues("mrpCode");	
		//사업자등록번호 배열 
		String[] companyNo = request.getParameterValues("companyNo");		
		//계약서번호 -> Integer로 변경
		String[] contractNo = request.getParameterValues("contractNo");	
		//기본여유기간 배열 -> Integer로 변경
		String[] freePeriod = request.getParameterValues("freePeriod");	
		//계약상태, 조달계획 등록상태 -> 완료
		//조달계획 진행사항 -> 발주 예정
		
		//품목코드는 null일수 없으니까, 품목코드 배열 기준으로, 각각의 DTO에 넣어주고, 그값 DTO리스트에 넣기
		for(int i=0; i<materialCode.length; i++) {
			ProcurementPlanDTO procurementPlanDTO2 = new ProcurementPlanDTO();
			//조달계획코드 null
			procurementPlanDTO2.setContractStatus("완료");		
			procurementPlanDTO2.setPpRegisterStatus("완료");
			procurementPlanDTO2.setPpProgress("발주 예정");
			
			procurementPlanDTO2.setMaterialCode(materialCode[i]);
			procurementPlanDTO2.setMaterialName(materialName[i]);
			procurementPlanDTO2.setProcess(process[i]);
			procurementPlanDTO2.setMrpDate(mrpDateList.get(i));
			procurementPlanDTO2.setMrpAmount(Integer.parseInt(mrpAmount[i]));
			procurementPlanDTO2.setCompanyName(companyName[i]);;
			procurementPlanDTO2.setSupplyLt(Integer.parseInt(supplyLt[i]));
			procurementPlanDTO2.setDueDate(dueDateList.get(i));
			procurementPlanDTO2.setMinimumOrderDate(minimumOrderDateList.get(i));
			procurementPlanDTO2.setPpAmount(Integer.parseInt(ppAmount[i]));
			procurementPlanDTO2.setMrpCode(Integer.parseInt(mrpCode[i]));
			procurementPlanDTO2.setCompanyNo(companyNo[i]);
			procurementPlanDTO2.setContractNo(Integer.parseInt(contractNo[i]));
			procurementPlanDTO2.setFreePeriod(Integer.parseInt(freePeriod[i]));
			//받아온 데이터에 null값으로 오는 항목이 없음! 그대로 리스트에 넣어주기			
			procurementPlanDTOList.add(procurementPlanDTO2);
		}
		log.info("조달계획 등록 처리에서 만든 등록할 procurementPlanDTOList 보자 : "+procurementPlanDTOList);
	
		//등록된 조달계획코드 리스트
		List<Integer> registerList = new ArrayList<>();
		//받아온 DTO리스트 각각 DB에 저장하기
		for(ProcurementPlanDTO dto : procurementPlanDTOList) {
			registerList.add(procurementPlanService.register(dto));
			log.info("각각 엔티티 변환 보자 : "+procurementPlanService.dtoToEntity(dto));
		}
		redirectAttributes.addFlashAttribute("registerList",registerList);
		
		return "redirect:/procurement1/procurementPlanList";
	}
	
	/**
	 * 조달계획 수정 화면 보기
	 * @param ppCodeList
	 * @param model
	 */
	@GetMapping("procurementPlanModify")
	public void procurementPlanModify(String[] ppCodeList,Model model) {
		log.info("조달계획 수정 화면 보기.....");
		log.info("조달계획 등록(목록) 화면에서 받아온 조달계획코드 리스트 보자 : "+ppCodeList);
		
		//받아온 조달계획코드 리스트를 통해서, 수정화면에 보낼, ProcurementPlanDTO 리스트 만들기
		List<ProcurementPlanDTO> procurementPlanDTOList = new ArrayList<>();
		for(int i=0; i<ppCodeList.length; i++) {
			ProcurementPlan procurementPlan = procurementPlanService.getProcurementPlan(Integer.parseInt(ppCodeList[i]));
			ProcurementPlanDTO procurementPlanDTO = procurementPlanService.ppEntityToDTO(procurementPlan);
			procurementPlanDTOList.add(procurementPlanDTO);
		}
		log.info("조달계획 수정화면에 보내는 procurementPlanDTOList 봐보자 : "+procurementPlanDTOList);
		
		model.addAttribute("procurementPlanDTOList", procurementPlanDTOList);
	}

	/**
	 * 조달계획 수정 처리
	 * @param procurementPlanDTO
	 * @param redirectAttributes
	 * @param request
	 * @return
	 */
	@PostMapping("procurementPlanModify")
	public String procurementPlanModify2(ProcurementPlanDTO procurementPlanDTO,RedirectAttributes redirectAttributes,HttpServletRequest request) {
		//품목코드, 품목명, 소요공정, 소요일, 소요량, 협력회사, 품목공급LT, 조달납기예정일, 최소발주일, 필요수량, 계약상태, 조달계획 등록상태, 조달계획 진행사항 <br>
		//조달계획코드, 자재소요계획코드, 사업자등록번호, 계약서번호 // 기본 여유기간
				
		log.info("조달계획 수정 처리.....");
		
		log.info("조달계획 수정 화면에서 보낸 ProcurementPlanDTO 가져오나 보자 : "+procurementPlanDTO);		
		//같은 이름으로 input 오는거 배열로 넘어옴 -> 각각을 배열로 받아서 하나씩 원하는대로 세팅해줘야함
		//받아온 date(소요일, 조달납기예정일, 최소발주일) String으로 왔기 때문에 Date 형식으로 변환해줘야함
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		
		List<ProcurementPlanDTO> procurementPlanDTOList = new ArrayList<>();
		
		//조달계획코드 배열 -> Integer로 변환
		String[] ppCode = request.getParameterValues("ppCode");	
		//품목코드 배열
		String[] materialCode = request.getParameterValues("materialCode");	
		//품목명 배열
		String[] materialName = request.getParameterValues("materialName");
		//소요공정 배열
		String[] process = request.getParameterValues("process");		
		//소요일 배열 -> Date 형식으로 변환
		String[] mrpDateStr = request.getParameterValues("mrpDate");		
//		log.info("지금 이 mrpDate를 못 읽어서 오는건가???? 봐보자!!!");
//		for(String test:mrpDateStr) {
//			log.info("받아온 mrpDate 하나씩 봐보자 : "+test);
//		}		
//		Date[] mrpDate = null; 	 => 이렇게 했더니, 밑에 try catch 에서 잡게 되므로 null이 존재할 가능성때문에, 자바 에러가 뜸
		List<Date> mrpDateList = new ArrayList<>();
		for(int i=0;i<mrpDateStr.length;i++) {
			try {
				mrpDateList.add(simpleDateFormat.parse(mrpDateStr[i]));
			} catch (ParseException e) {
				log.info("소요일 date 변환시 오류 발생함!!");
			}
			log.info("변경되서 넣어진 소요일 날짜 리스트 봐보자 : "+mrpDateList);
		}	
		//소요량 배열 -> Integer로 변경
		String[] mrpAmount = request.getParameterValues("mrpAmount");	
		//협력회사 배열
		String[] companyName = request.getParameterValues("companyName");				
		//품목공급LT 배열 -> Integer로 변경
		String[] supplyLt = request.getParameterValues("supplyLt");	
		//조달납기예정일 배열 -> Date 형식으로 변환
		String[] dueDateStr = request.getParameterValues("dueDate");		
		List<Date> dueDateList = new ArrayList<>();
		for(int i=0;i<dueDateStr.length;i++) {
			try {
				dueDateList.add(simpleDateFormat.parse(dueDateStr[i]));
			} catch (ParseException e) {
				e.printStackTrace();
				log.info("조달납기예정일 date 변환시 오류 발생함!!");
			}
			log.info("변경되서 넣어진 조달납기예정일 날짜 리스트 봐보자 : "+dueDateList);
		}
		//최소발주일 배열 -> Date 형식으로 변환
		String[] minimumOrderDateStr = request.getParameterValues("minimumOrderDate");		
		List<Date> minimumOrderDateList = new ArrayList<>();
		for(int i=0;i<minimumOrderDateStr.length;i++) {
			try {
				minimumOrderDateList.add(simpleDateFormat.parse(minimumOrderDateStr[i]));
			} catch (ParseException e) {
				e.printStackTrace();
				log.info("최소발주일일 date 변환시 오류 발생함!!");
			}
			log.info("변경되서 넣어진 최소발주일 날짜 리스트 봐보자 : "+minimumOrderDateList);
		}
		//필요수량 배열 -> Integer로 변경
		String[] ppAmount = request.getParameterValues("ppAmount");	
		//자재소요계획코드 배열 -> Integer로 변경
		String[] mrpCode = request.getParameterValues("mrpCode");	
		//사업자등록번호 배열 
		String[] companyNo = request.getParameterValues("companyNo");		
		//계약서번호 -> Integer로 변경
		String[] contractNo = request.getParameterValues("contractNo");	
		//기본여유기간 배열 -> Integer로 변경
		String[] freePeriod = request.getParameterValues("freePeriod");	
		//수정할 수 있는 상태가, 둘다 완료이고 발주예정 인것만 가능하니까!
		//계약상태, 조달계획 등록상태 -> 완료
		//조달계획 진행사항 -> 발주 예정
		
		//품목코드는 null일수 없으니까, 품목코드 배열 기준으로, 각각의 DTO에 넣어주고, 그값 DTO리스트에 넣기
		for(int i=0; i<materialCode.length; i++) {
			ProcurementPlanDTO procurementPlanDTO2 = new ProcurementPlanDTO();
			//조달계획코드 null
			procurementPlanDTO2.setContractStatus("완료");		
			procurementPlanDTO2.setPpRegisterStatus("완료");
			procurementPlanDTO2.setPpProgress("발주 예정");
			procurementPlanDTO2.setPpCode(Integer.parseInt(ppCode[i]));	
			procurementPlanDTO2.setMaterialCode(materialCode[i]);
			procurementPlanDTO2.setMaterialName(materialName[i]);
			procurementPlanDTO2.setProcess(process[i]);
			procurementPlanDTO2.setMrpDate(mrpDateList.get(i));
			procurementPlanDTO2.setMrpAmount(Integer.parseInt(mrpAmount[i]));
			procurementPlanDTO2.setCompanyName(companyName[i]);;
			procurementPlanDTO2.setSupplyLt(Integer.parseInt(supplyLt[i]));
			procurementPlanDTO2.setDueDate(dueDateList.get(i));
			procurementPlanDTO2.setMinimumOrderDate(minimumOrderDateList.get(i));
			procurementPlanDTO2.setPpAmount(Integer.parseInt(ppAmount[i]));
			procurementPlanDTO2.setMrpCode(Integer.parseInt(mrpCode[i]));
			procurementPlanDTO2.setCompanyNo(companyNo[i]);
			procurementPlanDTO2.setContractNo(Integer.parseInt(contractNo[i]));
			procurementPlanDTO2.setFreePeriod(Integer.parseInt(freePeriod[i]));
			//받아온 데이터에 null값으로 오는 항목이 없음! 그대로 리스트에 넣어주기			
			procurementPlanDTOList.add(procurementPlanDTO2);
		}
		log.info("조달계획 수정 처리에서 만든 수정할 procurementPlanDTOList 보자 : "+procurementPlanDTOList);
	
		//받아온 DTO리스트 각각 DB에 수정하기
		for(ProcurementPlanDTO dto : procurementPlanDTOList) {
			procurementPlanService.modify(dto);
		}
		return "redirect:/procurement1/procurementPlanList";
	}
	
	
	

}
