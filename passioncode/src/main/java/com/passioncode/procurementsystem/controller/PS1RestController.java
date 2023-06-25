package com.passioncode.procurementsystem.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.passioncode.procurementsystem.dto.ContractDTO;
import com.passioncode.procurementsystem.dto.MaterialDTO;
import com.passioncode.procurementsystem.dto.MiddleCategoryDTO;
import com.passioncode.procurementsystem.dto.ProcurementPlanDTO;
import com.passioncode.procurementsystem.entity.Company;
import com.passioncode.procurementsystem.entity.Contract;
import com.passioncode.procurementsystem.entity.Material;
import com.passioncode.procurementsystem.entity.MiddleCategory;
import com.passioncode.procurementsystem.entity.ProcurementPlan;
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
	@PostMapping(value="companySearch",produces=MediaType.APPLICATION_JSON_VALUE)
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
	 * 조달계획 등록 화면에서, 품목코드를 통해 계약서 찾기 <br>
	 * 이때, 해당 품목코드를 읽어와서, 그 해당되는 계약서롤 찾기
	 * @param companyName
	 * @return
	 */
	@PostMapping(value="contractSearch",produces=MediaType.APPLICATION_JSON_VALUE)
	public List<ContractDTO> contractSearch(@RequestBody String materialCode){
		//계약서번호, 품목코드, 품목명, 협력회사, 담당자, 담당자연락처, 품목공급LT, 단가, 거래조건, 계약서, 계약상태 <br>
		//사업자등록번호
		
		//받아온 품목코드 읽어보자
		log.info("화면에서 보낸 품목코드 : "+materialCode); 
				
		List<ContractDTO> searchContractDTOs = new ArrayList<>();
		
		List<Contract> contractList = contractService.searchContractByMaterialCode(materialCode);
		log.info("잘 검색해서 왔나 계약서 보자 : "+contractList);
		
		//거래조건은 null일경우 null 이라고 화면에 그대로 찍히기 때문에, 거래조건이 null일때 "" 빈값으로 셋팅 바꿔주자
		for(Contract contract:contractList) {
			ContractDTO contractDTO = contractService.contractEntityToDTO(contract);
			if(contractDTO.getDealCondition()==null) {
				contractDTO.setDealCondition("");
			}
			searchContractDTOs.add(contractDTO);
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
	
	/**
	 * 품목정보 등록 화면에서, 대분류코드에 따른 중분류코드 바꿔주기 <br>
	 * @param Code
	 * @return
	 */
	@PostMapping(value="getMiddleCategoryByLargeCategory",produces=MediaType.APPLICATION_JSON_VALUE)
	public List<MiddleCategoryDTO> getMiddleCategoryByLargeCategory(@RequestBody String Code){
		//화면에서 대분류코드를 받아서, 그에 해당하는 중분류 리스트를 찾아서 돌려주기
		List<MiddleCategory> middleCategoryList = middleCategoryService.getMiddleCategoryByLargeCategory(middleCategoryService.getLargeCategory(Code));
		
		List<MiddleCategoryDTO> middleCategoryDTOList = new ArrayList<>();
		for(MiddleCategory middleCategory:middleCategoryList) {
			middleCategoryDTOList.add(middleCategoryService.entityToDTO(middleCategory));
		}
		log.info("만들어진 중분류 DTO리스트 보자 : "+middleCategoryDTOList);
		
		return	middleCategoryDTOList;
	}
	
	/**
	 * 품목정보 등록 화면에서, 품목코드 생성해주기 <br>
	 * 보낸 대분류코드, 중분류코드를 받아서, 품목코드 만들어서 보내주기
	 * @param middleCategoryDTOList
	 * @return
	 */
	@PostMapping(value="generateMaterialCode",produces=MediaType.APPLICATION_JSON_VALUE)
	public List<MaterialDTO> generateMaterialCode(@RequestBody List<MiddleCategoryDTO> middleCategoryDTOList){
		//화면에서 대분류코드, 중분류코드를 받아서, 품목코드를 만들어서 보내주기
		//코드생성 버튼 누를때마다, 모든 대분류코드, 중분류코드 받아서, 각각 품목코드 만들어주기
		log.info("일단 제대로 받아오나 함 보자 : "+middleCategoryDTOList);
		
		//받은 대분류코드, 중분류코드 문자만 추출해서 만들고, 만든거 리스트에 넣어주기 
		// -> 생성된 품목코드의 문자부분의 리스트
		List<String> getLCWithMCList = new ArrayList<>();
		for(MiddleCategoryDTO middleCategoryDTO:middleCategoryDTOList) {
			String getLC = middleCategoryDTO.getLargeCode().substring(0,1);
			String getMC = middleCategoryDTO.getMiddleCode().substring(0,1);
			getLCWithMCList.add(getLC + getMC);
		}
		log.info("대분류,중분류 코드 문자부분만 합쳐서 만든 문자 코드 리스트 보자 : ",getLCWithMCList);
		
		//생성된 품목코드의 숫자부분 리스트로 만들기
		List<String> maxOnlyNumByStringList = new ArrayList<>();
		
		//생성된 품목코드의 문자부분 리스트를 통해서, 모든 품목 찾아와서, 숫자부분만 추출후 최대값 가져오고, 최대값 +1 문자로 만들어서 리스트로 만들기
		for(String getLCWithMC:getLCWithMCList) {			
			//대분류, 중분류 첫글자씩 합쳐온 글자를 검색해서 해당되는 모든 품목 찾아오기
			List<Material> materialList=materialService.getMaterialListByCodeContaining(getLCWithMC);
			
			//찾아온 품목코드에서 앞에 두글자 빼고 숫자만 리스트로 담기
			List<Integer> onlyNumList = new ArrayList<>();
			for(Material m:materialList) {
				onlyNumList.add(Integer.parseInt(m.getCode().substring(2)));
			}
			
			//뽑아낸 숫자 리스트 중에서 최고값 (ex> 2)
			Integer maxOnlyNumByInt = Collections.max(onlyNumList);
			
			//최고 숫자에 +1 하고, 4자릿수로 맞춰서 문자로 만들기 (ex> 2+1 =3 -> 0003)
	        String maxOnlyNumByString = String.format("%04d",maxOnlyNumByInt+1);
			
			//만든 숫자리스트 -> 위에서 선언한, 생성된 품목코드의 숫자부분 리스트에 넣기
	        maxOnlyNumByStringList.add(maxOnlyNumByString);			
		}
		log.info("생성된 품목코드의 숫자부분 리스트 만든거 확인하기 : "+maxOnlyNumByStringList);
		
		List<MaterialDTO> finalGenerateMaterialCodeList = new ArrayList<>();
		
		for(int i=0;i<getLCWithMCList.size();i++) {
			MaterialDTO materialDTO = new MaterialDTO();
			materialDTO.setCode(getLCWithMCList.get(i)+maxOnlyNumByStringList.get(i));
			finalGenerateMaterialCodeList.add(materialDTO);
		}
		log.info("만들어진 최종 품목코드 리스트(즉 MaterialDTO 리스트) 보기 : "+finalGenerateMaterialCodeList);
		
		return finalGenerateMaterialCodeList;		
	}
	
	/**
	 * 품목정보 수정 화면에서, 품목코드 수정해주기 <br>
	 * 보낸 대분류코드, 중분류코드를 받아서, 품목코드 만들어서 보내주기
	 * @param middleCategoryDTOList
	 * @return
	 */
	@PostMapping(value="modifyMaterialCode",produces=MediaType.APPLICATION_JSON_VALUE)
	public List<MaterialDTO> modifyMaterialCode(@RequestBody List<MiddleCategoryDTO> middleCategoryDTOList){
		//화면에서 대분류코드, 중분류코드를 받아서, 품목코드를 만들어서 보내주기
		//그리고 기존의 품목코드를 받아서 그 품목코드 삭제를 시키고, 품목코드 생성하기! 그래야 중복이나 바뀔시 생기는 문제를 없앨수 있음!
		// 하지만 이때 @RequestBody List<MiddleCategoryDTO> middleCategoryDTOList, @RequestBody List<String> materialCodeList  이렇게 
		// @RequestBody 2개 안돼!! 그러니까 품목코드를 MiddleCategoryDTO의 middleCategory 속성에 넣어서 하나로 리스트 만들어서 보내주자!
		//같은 String이니까! 담아서 보낼 수 있을 듯! 하지만 middleCategory -> materialCode 가 되는거니까 헷갈릴테니 주의하자!
		
		//코드생성 버튼 누를때마다, 모든 대분류코드, 중분류코드 받아서, 각각 품목코드 만들어주기
		log.info("일단 제대로 받아오나 함 보자 middleCategoryDTOList : "+middleCategoryDTOList);
		
		//middleCategory에 받은 품목코드 materialDTO에 담기!
		List<MaterialDTO> materialDTOList = new ArrayList<>();
		
		for(MiddleCategoryDTO middleCategoryDTO:middleCategoryDTOList ) {
			MaterialDTO materialDTO = MaterialDTO.builder().code(middleCategoryDTO.getMiddleCategory()).build();
			materialDTOList.add(materialDTO);			
		}
		log.info("MaterialDTO 리스트로 품목코드 옮긴거 보자! : "+materialDTOList);
		
		//받아온 품목코드들은 변경되야하는 기존의 품목코드니까 삭제를 시키기
		for(MaterialDTO materialDTO : materialDTOList) {
			materialService.delete(materialDTO);
		}
		
		//이제 중복되거나 할일 없으니까, 받아온 대분류, 중분류 를 통해 품목코드 만들기
		//받은 대분류코드, 중분류코드 문자만 추출해서 만들고, 만든거 리스트에 넣어주기 
		// -> 생성된 품목코드의 문자부분의 리스트
		List<String> getLCWithMCList = new ArrayList<>();
		for(MiddleCategoryDTO middleCategoryDTO:middleCategoryDTOList) {
			String getLC = middleCategoryDTO.getLargeCode().substring(0,1);
			String getMC = middleCategoryDTO.getMiddleCode().substring(0,1);
			getLCWithMCList.add(getLC + getMC);
		}
		log.info("대분류,중분류 코드 문자부분만 합쳐서 만든 문자 코드 리스트 보자 : ",getLCWithMCList);
		
		//생성된 품목코드의 숫자부분 리스트로 만들기
		List<String> maxOnlyNumByStringList = new ArrayList<>();
		
		//생성된 품목코드의 문자부분 리스트를 통해서, 모든 품목 찾아와서, 숫자부분만 추출후 최대값 가져오고, 최대값 +1 문자로 만들어서 리스트로 만들기
		for(String getLCWithMC:getLCWithMCList) {			
			//대분류, 중분류 첫글자씩 합쳐온 글자를 검색해서 해당되는 모든 품목 찾아오기
			List<Material> materialList=materialService.getMaterialListByCodeContaining(getLCWithMC);
			
			//찾아온 품목코드에서 앞에 두글자 빼고 숫자만 리스트로 담기
			List<Integer> onlyNumList = new ArrayList<>();
			for(Material m:materialList) {
				onlyNumList.add(Integer.parseInt(m.getCode().substring(2)));
			}
			
			//뽑아낸 숫자 리스트 중에서 최고값 (ex> 2)
			Integer maxOnlyNumByInt = Collections.max(onlyNumList);
			
			//최고 숫자에 +1 하고, 4자릿수로 맞춰서 문자로 만들기 (ex> 2+1 =3 -> 0003)
	        String maxOnlyNumByString = String.format("%04d",maxOnlyNumByInt+1);
			
			//만든 숫자리스트 -> 위에서 선언한, 생성된 품목코드의 숫자부분 리스트에 넣기
	        maxOnlyNumByStringList.add(maxOnlyNumByString);			
		}
		log.info("생성된 품목코드의 숫자부분 리스트 만든거 확인하기 : "+maxOnlyNumByStringList);
		
		List<MaterialDTO> finalGenerateMaterialCodeList = new ArrayList<>();
		
		for(int i=0;i<getLCWithMCList.size();i++) {
			MaterialDTO materialDTO = new MaterialDTO();
			materialDTO.setCode(getLCWithMCList.get(i)+maxOnlyNumByStringList.get(i));
			finalGenerateMaterialCodeList.add(materialDTO);
		}
		log.info("만들어진 최종 품목코드 리스트(즉 MaterialDTO 리스트) 보기 : "+finalGenerateMaterialCodeList);
		
		return finalGenerateMaterialCodeList;		
//		return null;
	}
	
	/**
	 * 품목 정보 수정화면 진입을 위한, 수정가능여부 체크해주기<br>
	 * 품목코드 리스트를 이용해서, 수정이 가능한 품목코드 확인해주기
	 * @param materialCodeList
	 * @return
	 */
	@PostMapping(value="expectedPurchaseOrderCheck", produces=MediaType.TEXT_PLAIN_VALUE)
	public String expectedPurchaseOrderCheck(@RequestBody List<String> materialCodeList) {
		//주의사항!! 반환타입은 boolean이 안된다! 그래서 String 문자열로 반환해주기!
		//화면에서 받아온 품목코드 리스트로 조회하기!!
		//품목코드로 조달계획 조회하는데 
		// 1. 조회가 안된다 => mrp조차 등록안되서, 혹은 조달계획조차 등록이 안된거니까, 등록한지 얼마 안된 품목! 수정 가능!
		// 2. 조회가 되는데, 리스트에서 조달완료일,세부구매발주서 둘다 전부 null => 한번도 발주한적이 없는거니까, 얼마안된 등록이라 수정 가능!!
		
		log.info("받아온 품목코드 리스트 봐보자 : "+materialCodeList);
		
		boolean expectedPurchaseOrderCheck = false;
		
		for(String materialCode : materialCodeList) {
			List<ProcurementPlan> ppList = procurementPlanService.getPPJoinMRPByMaterialCode(materialCode);
			
			if(ppList.size()==0) {
				expectedPurchaseOrderCheck = true;
			}else {
				for(int i=0; i<ppList.size(); i++) {
					if(ppList.get(i).getCompletionDate()!=null) {
						expectedPurchaseOrderCheck = false;
						break;
					}else {
						if(ppList.get(i).getDetailPurchaseOrder()!=null) {
							expectedPurchaseOrderCheck = false;
							break;
						}else {
							expectedPurchaseOrderCheck = true;
						}
					}
				}
			}
			
			if(!expectedPurchaseOrderCheck) {
				break;
			}
		}
		log.info("그래서 expectedPurchaseOrderCheck 최종 참, 거짓 확인해 보자 : "+expectedPurchaseOrderCheck);
		
		return expectedPurchaseOrderCheck+"";
	}
	
	
	/**
	 * 품목 정보 수정화면 진입을 위한, 수정가능여부 체크해주기<br>
	 * 품목코드 리스트를 이용해서, 수정이 가능한 품목코드 확인해주기
	 * @param contractNoList
	 * @return
	 */
	@PostMapping(value="contractInPPCheck", produces=MediaType.TEXT_PLAIN_VALUE)
	public String contractInPPCheck(@RequestBody List<String> contractNoList) {
		//주의사항!! 반환타입은 boolean이 안된다! 그래서 String 문자열로 반환해주기!
		//화면에서 받아온 계약서번호 리스트로 조회하기!!
		//계약서번호로 조달계획 조회하는데
		// 1. 조회가 안된다 => 아직 조달계획에 등록이 안된거라서, 계약서 수정 가능
		// 2. 조회가 된다 => 조달계획에 등록이 된거라서, 계약서 수정 불가능
		log.info("계약서 목록화면에서 받아온 계약서 리스트 봐보자 : "+contractNoList);
		
		boolean contractInPPCheck = false;
		
		for(String contractNo : contractNoList) {
			//result ->  그 계약서가 조달계획에 있는지 여부! fasle -> 수정가능, true -> 수정 불가능!!
			boolean result = procurementPlanService.ppExistsByContract(Integer.parseInt(contractNo));
			if(result == true) {
				contractInPPCheck = false;
				break;
			}else {
				contractInPPCheck = true;
			}
		}
		
		log.info("그래서 contractInPPCheck 최종 참, 거짓 확인해 보자 : "+contractInPPCheck);
		
		return contractInPPCheck+"";
	}
	
	
	
	

}
