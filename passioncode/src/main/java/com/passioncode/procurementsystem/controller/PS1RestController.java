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
		//log.info("값은 가져오나 보자 : "+companyName);
		
		List<ContractDTO> searchContractDTOs = new ArrayList<>();
		List<Company> companyList = contractService.searchCompanyWithDeal(companyName);
		//log.info("가져온걸로 잘 검색했나~~"+companyList);
		
		for(Company company:companyList) {
			ContractDTO contractDTO = ContractDTO.builder().companyNo(company.getNo()).companyName(company.getName())
															.manager(company.getManager()).managerTel(company.getManagerTel()).build();
			searchContractDTOs.add(contractDTO);
		}
		//log.info("만들어진 회사리스트 보자 : "+searchContractDTOs);
		
		return searchContractDTOs;
	}
	
	/**
	 * 조달계획 등록 화면에서, 품목코드를 통해 계약서 찾기 <br>
	 * 이때, 해당 품목코드를 읽어와서, 그 해당되는 계약서롤 찾기
	 * @param materialCode
	 * @return
	 */
	@PostMapping(value="contractSearch",produces=MediaType.APPLICATION_JSON_VALUE)
	public List<ContractDTO> contractSearch(@RequestBody String materialCode){
		//계약서번호, 품목코드, 품목명, 협력회사, 담당자, 담당자연락처, 품목공급LT, 단가, 거래조건, 계약서, 계약상태 <br>
		//사업자등록번호
		
		//받아온 품목코드 읽어보자
		//log.info("화면에서 보낸 품목코드 : "+materialCode); 
				
		List<ContractDTO> searchContractDTOs = new ArrayList<>();
		
		List<Contract> contractList = contractService.searchContractByMaterialCode(materialCode);
		//log.info("잘 검색해서 왔나 계약서 보자 : "+contractList);
		
		//거래조건은 null일경우 null 이라고 화면에 그대로 찍히기 때문에, 거래조건이 null일때 "" 빈값으로 셋팅 바꿔주자
		for(Contract contract:contractList) {
			ContractDTO contractDTO = contractService.contractEntityToDTO(contract);
			if(contractDTO.getDealCondition()==null) {
				contractDTO.setDealCondition("");
			}
			searchContractDTOs.add(contractDTO);
		}		
		//log.info("만들어진 계약서 리스트 보자 : "+searchContractDTOs);
		
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
		//log.info("소요일, 기본여유기간, 품목공급LT 객체로 보낸 json 타입 보자 : "+procurementPlanDTO); 		
		
		ProcurementPlanDTO result = procurementPlanService.getProcurementPlanCalculate(procurementPlanDTO);
		//log.info("계산된 값 확인해 보자 : "+result);
		
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
		//log.info("만들어진 중분류 DTO리스트 보자 : "+middleCategoryDTOList);
		
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
		//log.info("일단 제대로 받아오나 함 보자 : "+middleCategoryDTOList);
		
		//받은 대분류코드, 중분류코드 문자만 추출해서 만들고, 만든거 리스트에 넣어주기 
		// -> 생성된 품목코드의 문자부분의 리스트
		List<String> getLCWithMCList = new ArrayList<>();
		for(MiddleCategoryDTO middleCategoryDTO:middleCategoryDTOList) {
			String getLC = middleCategoryDTO.getLargeCode().substring(0,1);
			String getMC = middleCategoryDTO.getMiddleCode().substring(0,1);
			getLCWithMCList.add(getLC + getMC);
		}
		//log.info("대분류,중분류 코드 문자부분만 합쳐서 만든 문자 코드 리스트 보자 : ",getLCWithMCList);
		
		//생성된 품목코드의 숫자부분 리스트로 만들기
		List<String> maxOnlyNumByStringList = new ArrayList<>();
		
		//생성된 품목코드의 문자부분 리스트를 통해서, 모든 품목 찾아와서, 숫자부분만 추출후 최대값 가져오고, 최대값 +1 문자로 만들어서 리스트로 만들기
		for(int i=0; i<getLCWithMCList.size(); i++) {
			//대분류, 중분류 첫글자씩 합쳐온 글자를 검색해서 해당되는 모든 품목 찾아오기
			List<Material> materialList=materialService.getMaterialListByCodeContaining(getLCWithMCList.get(i));

			//검색해서 찾아온 값이 존재하면 ->거기서 +1 , 없다면! 신규!!! -> 0001 로!!
			if(materialList.size()>0) {
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
			}else { 	//검색해서 찾아온 품목이 없어!! 이건 완전 신규 품목이야!!! 
				//시작하는 숫자 
				Integer startNum = 1;
				
				//시작하는 숫자, 4자릿수 맞춰서 문자로 만들기 (ex> 1 -> 0001)
				String startNumByString = String.format("%04d",startNum);
				
				//만든 숫자리스트 -> 위에서 선언한, 생성된 품목코드의 숫자부분 리스트에 넣기
				maxOnlyNumByStringList.add(startNumByString);	
			}
		}
		//log.info("생성된 품목코드의 숫자부분 리스트 만든거 확인하기 : "+maxOnlyNumByStringList);
		
		List<MaterialDTO> finalGenerateMaterialCodeList = new ArrayList<>();
		
		for(int i=0;i<getLCWithMCList.size();i++) {
			MaterialDTO materialDTO = new MaterialDTO();
			materialDTO.setCode(getLCWithMCList.get(i)+maxOnlyNumByStringList.get(i));
			finalGenerateMaterialCodeList.add(materialDTO);
		}
		
		//중복되서 만들어진 품목코드!!!! 중복안되게!!! 다시 배정해주기!! (최종 위에서 만든 코드를 이용해서!!!)
		//다 만든 리스트에서, 같은 코드가 나왔을경우, 다시 한번 숫자 세팅해주기
		//(즉 같은 문자부분이 2개이상이면, 같은 맥스번호를 받아오기때문에, 다시한번 숫자부분 값 설정해주기) 
		for(int i=0;i<finalGenerateMaterialCodeList.size();i++) {
			//비교할 문자 뽑아내기
			String compare = finalGenerateMaterialCodeList.get(i).getCode();
//			log.info("현재 "+i+"번째 비교 중! 그 문자 : "+compare);
			for(int j=0;j<finalGenerateMaterialCodeList.size();j++) {
				if(i!=j) {	//같은 순서는 같을 수 밖에 없으니 같은 인덱스순서 제외하고, 같은문자일시에 +1해서 셋팅해서 코드넣어주기
					if(compare.equals(finalGenerateMaterialCodeList.get(j).getCode())){
						String presentStr = finalGenerateMaterialCodeList.get(j).getCode().substring(0,2);
						String presentNum = finalGenerateMaterialCodeList.get(j).getCode().substring(2);
						Integer plusNum = Integer.parseInt(presentNum)+1;
						String plusNumStr = String.format("%04d",plusNum);
						String plusCodeGenerate = presentStr+plusNumStr;
//						log.info("코드 +1해서 만들어진거 문자 보자 : "+plusCodeGenerate);
						//리스트는 제거를 해주고 넣어줘야! 값이 대체가 된다, 안그러면 그 해당인덱스에 add로 추가된다.
						finalGenerateMaterialCodeList.remove(j);
						finalGenerateMaterialCodeList.add(j, MaterialDTO.builder().code(plusCodeGenerate).build());
//						log.info("제대로 리스트에 그 해당 인덱스에 넣어진건가?? : "+finalGenerateMaterialCodeList.get(j));
					}
				}
			}
		}
		//log.info("만들어진 최종 품목코드 리스트(즉 MaterialDTO 리스트) 보기 : "+finalGenerateMaterialCodeList);
		
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
		//log.info("일단 제대로 받아오나 함 보자 middleCategoryDTOList : "+middleCategoryDTOList);
		
		//middleCategory에 받은 품목코드 materialDTO에 담기!
		List<MaterialDTO> materialDTOList = new ArrayList<>();
		
		for(MiddleCategoryDTO middleCategoryDTO:middleCategoryDTOList ) {
			Material material = materialService.getMaterial(middleCategoryDTO.getMiddleCategory());
			MaterialDTO materialDTO = materialService.entityToDTO(material);
			materialDTOList.add(materialDTO);			
		}
		//log.info("MaterialDTO 리스트로 품목코드 옮긴거 보자! : "+materialDTOList);
		
		//받아온 품목코드들은 변경되야하는 기존의 품목코드니까 삭제를 시키기 
		//----------------------------------------------------------- MRP 관련----------------------------------------------------------------------------------------------//
		// -----> 이때 같이!! 생성하면서 만들었던!! MRP 2개도 같이 지워주고 + 밑에서 다시 새로 만든 품목코드 만들었을 때! 다시 MRP 2개 만들어주자!!!
		// 아니아니!! 이 RestController 작업 바뀐 품목코드 생성까지!!! 그리고 생성코드 화면에 보내서, 거기서 폼으로 생성한 품목코드로 품목 저장하게 Controller로 처리 하니까!!
		// 여기서 그 바뀐 품목코드로 바로 mrp 2개 못 만들어!! ----> 이건 수정처리 Controller 에서!! 그 해당 mrp 만들어주자!!!!
		// 주의!!! mrp를 지울때!! mrp에 외래키로 품목코드가 잡혀있으니까!!!! 순서를 mrp를 먼저 지워주고!! 품목을 지워줘야 한다!!
		for(MaterialDTO materialDTO : materialDTOList) {
			materialService.mrpDeleteWithMaterialModify(materialDTO.getCode());
			materialService.delete(materialDTO);
		}
		//----------------------------------------------------------- MRP 관련 끝----------------------------------------------------------------------------------------------//
		
		//이제 중복되거나 할일 없으니까, 받아온 대분류, 중분류 를 통해 품목코드 만들기
		//받은 대분류코드, 중분류코드 문자만 추출해서 만들고, 만든거 리스트에 넣어주기 
		// -> 생성된 품목코드의 문자부분의 리스트
		List<String> getLCWithMCList = new ArrayList<>();
		for(MiddleCategoryDTO middleCategoryDTO:middleCategoryDTOList) {
			String getLC = middleCategoryDTO.getLargeCode().substring(0,1);
			String getMC = middleCategoryDTO.getMiddleCode().substring(0,1);
			getLCWithMCList.add(getLC + getMC);
		}
		//log.info("대분류,중분류 코드 문자부분만 합쳐서 만든 문자 코드 리스트 보자 : ",getLCWithMCList);
		
		//생성된 품목코드의 숫자부분 리스트로 만들기
		List<String> maxOnlyNumByStringList = new ArrayList<>();
		
		//생성된 품목코드의 문자부분 리스트를 통해서, 모든 품목 찾아와서, 숫자부분만 추출후 최대값 가져오고, 최대값 +1 문자로 만들어서 리스트로 만들기
		for(int i=0; i<getLCWithMCList.size(); i++) {			
			//대분류, 중분류 첫글자씩 합쳐온 글자를 검색해서 해당되는 모든 품목 찾아오기
			List<Material> materialList=materialService.getMaterialListByCodeContaining(getLCWithMCList.get(i));
			
			//검색해서 찾아온 값이 존재하면 ->거기서 +1 , 없다면! 신규!!! -> 0001 로!!
			if(materialList.size()>0) {
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
			}else { 	//검색해서 찾아온 품목이 없어!! 이건 완전 신규 품목이야!!! 
				//시작하는 숫자 
				Integer startNum = 1;
				
				//시작하는 숫자, 4자릿수 맞춰서 문자로 만들기 (ex> 1 -> 0001)
				String startNumByString = String.format("%04d",startNum);
				
				//만든 숫자리스트 -> 위에서 선언한, 생성된 품목코드의 숫자부분 리스트에 넣기
				maxOnlyNumByStringList.add(startNumByString);	
			}
		}
		//log.info("생성된 품목코드의 숫자부분 리스트 만든거 확인하기 : "+maxOnlyNumByStringList);
		
		List<MaterialDTO> finalGenerateMaterialCodeList = new ArrayList<>();
		
		for(int i=0;i<getLCWithMCList.size();i++) {
			MaterialDTO materialDTO = new MaterialDTO();
			materialDTO.setCode(getLCWithMCList.get(i)+maxOnlyNumByStringList.get(i));
			finalGenerateMaterialCodeList.add(materialDTO);
		}
		log.info("!!!!!!!!!!!!만들어진 최종 품목코드 리스트(즉 MaterialDTO 리스트) 보기 : "+finalGenerateMaterialCodeList);
		
		//----------------------------------------------------------- MRP 관련----------------------------------------------------------------------------------------------//
		//----------- 테스트 진행을 위한, 품목 생성하자마자, 그 해당하는 품목의 MRP 2개를 랜덤으로 세팅해서 만들어주었던거! 그 해당 MRP 2개 품목코드로 바꿔주기 --------------//
		//------------- 새로 만들어낸 품목코드를 통해 --> 이 해당하는 품목이 MRP 2개를 랜덤으로 세팅해서 만들어 주기!!! ------------------------------------------------------//
		// 아니아니!! 이 RestController 작업 바뀐 품목코드 생성까지!!! 그리고 생성코드 화면에 보내서, 거기서 폼으로 생성한 품목코드로 품목 저장하게 Controller로 처리 하니까!!
		// 여기서 그 바뀐 품목코드로 바로 mrp 2개 못 만들어!! ----> 이건 수정처리 Controller 에서!! 그 해당 mrp 만들어주자!!!!
		//----------------------------------------------------------- MRP 관련 끝----------------------------------------------------------------------------------------------//
		
		//중복되서 만들어진 품목코드!!!! 중복안되게!!! 다시 배정해주기!! (최종 위에서 만든 코드를 이용해서!!!)
		//다 만든 리스트에서, 같은 코드가 나왔을경우, 다시 한번 숫자 세팅해주기
		//(즉 같은 문자부분이 2개이상이면, 같은 맥스번호를 받아오기때문에, 다시한번 숫자부분 값 설정해주기) 
		for(int i=0;i<finalGenerateMaterialCodeList.size();i++) {
			//비교할 문자 뽑아내기
			String compare = finalGenerateMaterialCodeList.get(i).getCode();
//					log.info("현재 "+i+"번째 비교 중! 그 문자 : "+compare);
			for(int j=0;j<finalGenerateMaterialCodeList.size();j++) {
				if(i!=j) {	//같은 순서는 같을 수 밖에 없으니 같은 인덱스순서 제외하고, 같은문자일시에 +1해서 셋팅해서 코드넣어주기
					if(compare.equals(finalGenerateMaterialCodeList.get(j).getCode())){
						String presentStr = finalGenerateMaterialCodeList.get(j).getCode().substring(0,2);
						String presentNum = finalGenerateMaterialCodeList.get(j).getCode().substring(2);
						Integer plusNum = Integer.parseInt(presentNum)+1;
						String plusNumStr = String.format("%04d",plusNum);
						String plusCodeGenerate = presentStr+plusNumStr;
//								log.info("코드 +1해서 만들어진거 문자 보자 : "+plusCodeGenerate);
						//리스트는 제거를 해주고 넣어줘야! 값이 대체가 된다, 안그러면 그 해당인덱스에 add로 추가된다.
						finalGenerateMaterialCodeList.remove(j);
						finalGenerateMaterialCodeList.add(j, MaterialDTO.builder().code(plusCodeGenerate).build());
//								log.info("제대로 리스트에 그 해당 인덱스에 넣어진건가?? : "+finalGenerateMaterialCodeList.get(j));
					}
				}
			}
		}
		
		return finalGenerateMaterialCodeList;		
//		return null;
	}
	
	/**
	 * 품목 정보 수정화면 진입을 위한, 수정가능여부 체크해주기<br>
	 * 품목코드 리스트를 이용해서, 계약상태 여부 체크하기
	 * @param materialCodeList
	 * @return
	 */
	@PostMapping(value="contractStatusCheck", produces=MediaType.TEXT_PLAIN_VALUE)
	public String contractStatusCheck(@RequestBody List<String> materialCodeList) {
		//주의사항!! 반환타입은 boolean이 안된다! 그래서 String 문자열로 반환해주기!
		//화면에서 받아온 품목코드 리스트로 조회하기!!
		//품목코드로 계약서 조회하는데 
		// 1. 조회가 안된다 => 계약 미완료 -> 수정 가능
		// 2. 조회가 된다 => 계약 완료 -> 수정 불가능
		//log.info("받아온 품목코드 리스트 봐보자 : "+materialCodeList);
		
		boolean contractStatusCheck = false;
		
		for(String materialCode : materialCodeList) {
			Material material = materialService.getMaterial(materialCode);
			String contractStatus = materialService.contractStatusCheck(material);
			if(contractStatus.equals("완료")) {
				contractStatusCheck = false;
				break;
			}else {
				contractStatusCheck = true;
			}
		}
		//log.info("그래서 contractStatusCheck 최종 참, 거짓 확인해 보자 : "+contractStatusCheck);
		
		return contractStatusCheck+"";
	}
	
//	/**
//	 * 품목 정보 수정화면 진입을 위한, 수정가능여부 체크해주기<br>
//	 * 품목코드 리스트를 이용해서, 수정이 가능한 품목코드 확인해주기
//	 * @param materialCodeList
//	 * @return
//	 */
//	@PostMapping(value="expectedPurchaseOrderCheck", produces=MediaType.TEXT_PLAIN_VALUE)
//	public String expectedPurchaseOrderCheck(@RequestBody List<String> materialCodeList) {
//		//주의사항!! 반환타입은 boolean이 안된다! 그래서 String 문자열로 반환해주기!
//		//화면에서 받아온 품목코드 리스트로 조회하기!!
//		//품목코드로 조달계획 조회하는데 
//		// 1. 조회가 안된다 => mrp조차 등록안되서, 혹은 조달계획조차 등록이 안된거니까, 등록한지 얼마 안된 품목! 수정 가능!
//		// 2. 조회가 되는데, 리스트에서 조달완료일,세부구매발주서 둘다 전부 null => 한번도 발주한적이 없는거니까, 얼마안된 등록이라 수정 가능!!
//		
//		log.info("받아온 품목코드 리스트 봐보자 : "+materialCodeList);
//		
//		boolean expectedPurchaseOrderCheck = false;
//		
//		for(String materialCode : materialCodeList) {
//			List<ProcurementPlan> ppList = procurementPlanService.getPPJoinMRPByMaterialCode(materialCode);
//			
//			if(ppList.size()==0) {
//				expectedPurchaseOrderCheck = true;
//			}else {
//				for(int i=0; i<ppList.size(); i++) {
//					if(ppList.get(i).getCompletionDate()!=null) {
//						expectedPurchaseOrderCheck = false;
//						break;
//					}else {
//						if(ppList.get(i).getDetailPurchaseOrder()!=null) {
//							expectedPurchaseOrderCheck = false;
//							break;
//						}else {
//							expectedPurchaseOrderCheck = true;
//						}
//					}
//				}
//			}
//			
//			if(!expectedPurchaseOrderCheck) {
//				break;
//			}
//		}
//		log.info("그래서 expectedPurchaseOrderCheck 최종 참, 거짓 확인해 보자 : "+expectedPurchaseOrderCheck);
//		
//		return expectedPurchaseOrderCheck+"";
//	}
	
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
		//log.info("계약서 목록화면에서 받아온 계약서 리스트 봐보자 : "+contractNoList);
		
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
		
		//log.info("그래서 contractInPPCheck 최종 참, 거짓 확인해 보자 : "+contractInPPCheck);
		
		return contractInPPCheck+"";
	}
	
	
	
	

}
