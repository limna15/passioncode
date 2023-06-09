package com.passioncode.procurementsystem.repository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;
import com.passioncode.procurementsystem.dto.ContractDTO;
import com.passioncode.procurementsystem.dto.ContractFileDTO;
import com.passioncode.procurementsystem.entity.Company;
import com.passioncode.procurementsystem.entity.Contract;
import com.passioncode.procurementsystem.entity.Material;
import lombok.extern.log4j.Log4j2;

@SpringBootTest
@Log4j2
public class ContractRepositoryTests {

	@Autowired
	ContractRepository contractRepository;
	
	@Autowired
	CompanyRepository companyRepository;
	
	@Autowired
	MaterialRepository materialRepository;
	
	@Autowired
	ProcurementPlanRepository procurementPlanRepository;
	
//	@Transactional
	@Test
	public void InsertTest() {
		Collection<Company> companyTest = companyRepository.findByNameContaining("길승산업");
		log.info("어디 test 보자 : "+companyTest);
//		companyTest.forEach(aa -> log.info("모야 나온거야?? : "+aa.getName()));
		ArrayList<Company> companyTest2 = (ArrayList<Company>) companyTest;
		log.info("모야 된거 아니야??? : "+companyTest2.get(0));
		
		Collection<Material> materialTest = materialRepository.findByNameContainingIgnoreCase("sensor");
		log.info("어디 대소문자 구별보자 : "+materialTest);
		ArrayList<Material> materialTest2=(ArrayList<Material>) materialTest;
		log.info("이렇게 보면 되나?? : "+materialTest2.get(0));
		
		
		Contract contract = Contract.builder().supplyLt(13).unitPrice(20000).contractFile("쿼리로 테스트중").material(materialTest2.get(0)).company(companyTest2.get(0)).build();
		
		contractRepository.save(contract);		
	}
	
	
	@Test
	public void contractStatusTest() {
		log.info("기존의 존재여부 체크 값 보자: "+contractRepository.existsById(2));
//		Optional<Material> result = materialRepository.findById("BPa0001");
		Optional<Material> result = materialRepository.findById("CGa0002");
		Material material = result.get();
		log.info("어디 만들어놓은 존재여부 체크 보자 : "+contractRepository.existsByMaterial(material));
	}
	
	@Transactional
	@Test
	public void contractDTOTest() {
		//계약서번호, 품목코드, 품목명, 협력회사, 담당자, 담당자연락처, 품목공급LT, 단가, 거래조건, 계약서, 계약상태
		
		//계약이 완료가 된 계약DTO 불로오기 (사실상 계약서테이블에 있는 값들 이용하면 계약완료된 DTO)
		Contract contract = contractRepository.findById(1).get();
		ContractDTO contractDTO = ContractDTO.builder().contractNo(contract.getNo()).materialCode(contract.getMaterial().getCode()).materialName(contract.getMaterial().getName())
														.companyNo(contract.getCompany().getNo()).companyName(contract.getCompany().getName()).manager(contract.getCompany().getManager())
														.managerTel(contract.getCompany().getManagerTel()).supplyLt(contract.getSupplyLt()).unitPrice(contract.getUnitPrice())
														.dealCondition(contract.getDealCondition()).contractFile(contract.getContractFile()).contractStatus("완료").build();
		log.info("계약 완료된 DTO(사실상 계약서테이블의 내용!) : "+contractDTO);
		
		//계약이 미완료인 계약DTO 만들기 (계약테이블에 없는건 다 null로 해서 만들기)
		//CGa0002 품목 계약서 없는 상태
		Material material = materialRepository.findById("CGa0002").get();
		ContractDTO contractDTO2 = ContractDTO.builder().materialCode(material.getCode()).materialName(material.getName()).contractStatus("미완료").build();
		log.info("계약 미완료된 DTO : "+contractDTO2);
		
		/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		
		//품목 기준으로 계약서를 찾아서 DTO 만들기
//		Material material2 = materialRepository.findById("CGa0002").get();  //계약 미완료인 품목
		Material material2 = materialRepository.findById("CGa0001").get();  //계약 완료된 품목
//		log.info("material2 봐보자! : "+material2);
//		Collection<Contract> collectionContract = contractRepository.findByMaterial(material2);
//		List<Contract> contractList = (ArrayList<Contract>) collectionContract;
		List<Contract> contractList = contractRepository.findByMaterial(material2);
//		log.info("contractList 봐보자 : "+contractList);
		
		List<ContractDTO> contractDTOList = new ArrayList<>();
		ContractDTO contractDTO3 = null;
		
		//contractList에 값이 존재할때 = 계약상태가 완료인 상태
		if(contractList.size()!=0) {
			for(int i=0;i<contractList.size();i++) {
				contractDTO3 = ContractDTO.builder().contractNo(contractList.get(i).getNo()).materialCode(contractList.get(i).getMaterial().getCode())
													.materialName(contractList.get(i).getMaterial().getName()).companyNo(contractList.get(i).getCompany().getNo())
													.companyName(contractList.get(i).getCompany().getName()).manager(contractList.get(i).getCompany().getManager())
													.managerTel(contractList.get(i).getCompany().getManagerTel()).supplyLt(contractList.get(i).getSupplyLt())
													.unitPrice(contractList.get(i).getUnitPrice()).dealCondition(contractList.get(i).getDealCondition())
													.contractFile(contractList.get(i).getContractFile()).contractStatus("완료").build();
				contractDTOList.add(contractDTO3);
			}
		}else { //contractList에 비어있을때 = 계약상태가 미완료인 상태
			contractDTO3 = ContractDTO.builder().materialCode(material.getCode()).materialName(material.getName()).contractStatus("미완료").build();
			contractDTOList.add(contractDTO3);
		}				
		
		log.info("제대로 리스트가 된건가...?? : "+contractDTOList);
		
		
	}
	
	@Transactional
	@Test
	public void contractDTOTest2() {
		Material material2 = materialRepository.findById("CGa0001").get();  //계약 완료된 품목
//		log.info("material2 봐보자! : "+material2);
		Collection<Contract> collectionContract = contractRepository.findByMaterial(material2);
		log.info(">>>>>>>>>>>"+collectionContract);;
	}
	
	
	@Test
	public void InsertByDTOTest() {
		//계약서번호, 품목코드, 품목명, 협력회사, 담당자, 담당자연락처, 품목공급LT, 단가, 거래조건, 계약서, 계약 상태 / 사업자등록번호
		
		//계약서등록 시, 협력회사를 찾아 등록할때, 거래가능여부가 1인 회사중에서만 찾음!! 만약 바꾸고싶으면 계약 등록전에 협력회사 관리창에서 바꿔줘야함!
		Material material = materialRepository.findById("CNa0001").get();
		Company company = companyRepository.findById("104-84-55123").get();
		
		//계약상태는, 계약을 등록하는 상황이기때문에 무조건 true
		//입력하는 상항이라서 계약서 번호는 몰라!
		ContractDTO contractDTO = ContractDTO.builder().materialCode(material.getCode()).materialName(material.getName())
														.companyNo(company.getNo()).companyName(company.getName()).manager(company.getManager())
														.managerTel(company.getManagerTel()).supplyLt(20).unitPrice(500).contractFile("DTO넣는거로 테스트중")
														.contractStatus("완료").build();
		log.info("contractDTO 봐보자 : "+contractDTO);
		
		//계약서번호, 품목공급LT, 단가, 거래조건, 계약서, 품목코드(외래키)(품목), 사업자등록번호(외래키)(협력회사)
		Contract contract = Contract.builder().supplyLt(contractDTO.getSupplyLt()).unitPrice(contractDTO.getUnitPrice()).contractFile(contractDTO.getContractFile())
												.material(materialRepository.findById(contractDTO.getMaterialCode()).get())
												.company(companyRepository.findById(contractDTO.getCompanyNo()).get()).build();
		log.info("DTO -> contract 봐보자 : "+contract);
		
		contractRepository.save(contract);		
		
	}
	
	@Test	
	public void ModifyByDTOTest() {
		Material material = materialRepository.findById("CNa0001").get();
		Company company = companyRepository.findById("104-84-55123").get();
		
		//수정하는 거기때문에, 계약서 번호 추가해서 테스트!
		ContractDTO contractDTO = ContractDTO.builder().materialCode(material.getCode()).materialName(material.getName()).contractNo(9)
														.companyNo(company.getNo()).companyName(company.getName()).manager(company.getManager())
														.managerTel(company.getManagerTel()).supplyLt(11).unitPrice(2500).contractFile("DTO넣는거로 테스트중 수정")
														.contractStatus("완료").build();
		log.info("contractDTO 봐보자 : "+contractDTO);
		
		Contract contract = Contract.builder().supplyLt(contractDTO.getSupplyLt()).unitPrice(contractDTO.getUnitPrice()).contractFile(contractDTO.getContractFile())
												.material(materialRepository.findById(contractDTO.getMaterialCode()).get()).no(contractDTO.getContractNo())
												.company(companyRepository.findById(contractDTO.getCompanyNo()).get()).build();
		log.info("DTO -> contract 봐보자 : "+contract);
		
		contractRepository.save(contract);				
	}
	
	
	@Test
	public void DeleteByDTOTest() {
		ContractDTO contractDTO = ContractDTO.builder().contractNo(9).build();
		
		contractRepository.deleteById(contractDTO.getContractNo());
		
	}
	
	
	@Test
	public void findByCompanyNameAndMaterialCodeTest() {
//		log.info("어디 리스트 잘가져오나 보자 : "+contractRepository.findByCompanyNameAndMaterialCode("길승", "CGa0001"));
//		log.info("어디 리스트 잘가져오나 보자 : "+contractRepository.findByCompanyNameAndMaterialCode("", "CGa0001"));
		log.info("어디 리스트 잘가져오나 보자 : "+contractRepository.findByCompanyNameAndMaterialCode(null, "CGa0001"));
	}
	
	
	public String makeContractNoStr(Integer contractNo) {
		//계약서번호 1 -> C00001 로 바꿔주기
		String contractNoStr = String.format("%05d",contractNo);
		contractNoStr = "C" + contractNoStr;
		log.info("바꾼 계약서 번호 좀 보자 : ",contractNoStr);
		return contractNoStr;
	}
	
	@Test
	public void makeContractNoStrTest() {
		Contract contract = contractRepository.findById(1).get();
		log.info("어디 보자 : "+makeContractNoStr(contract.getNo()));
	}
	
	
	@Test
	public void fileTest() {
		String contractFile = "/PassionCode/upload/contract/2023/06/20/1a2c5f54-51be-4b09-a656-01577901388f_거래계약서 샘플6.doc";
		String uploadPath = contractFile.substring(0,29);
		log.info("어디어디 보자~~~~~~~ : "+uploadPath);
		// /PassionCode/upload/contract/
		String folderPath = contractFile.substring(29,40);
		log.info("어디어디 보자2~~~~~~~ : "+folderPath);
		// 2023/06/20/
		String uuid = contractFile.substring(40, 76);
		log.info("uuid 보자!! : "+ uuid);
		// 1a2c5f54-51be-4b09-a656-01577901388f
		// 언더바 _ 제외하고 fileName 보자!!
		String fileName = contractFile.substring(77);
		log.info("어디 이제 오리지날 파일이름 보자!!! : "+fileName);
		// 거래계약서 샘플6.doc
	}
	
	public ContractDTO contractEntityToDTO(Contract contract) {
		ContractDTO contractDTO = ContractDTO.builder().contractNo(contract.getNo()).materialCode(contract.getMaterial().getCode()).materialName(contract.getMaterial().getName())
														.contractNoStr(makeContractNoStr(contract.getNo()))
														.contractFileDTO(new ContractFileDTO(contract.getContractFile()))
														.companyNo(contract.getCompany().getNo()).companyName(contract.getCompany().getName()).manager(contract.getCompany().getManager())
														.managerTel(contract.getCompany().getManagerTel()).supplyLt(contract.getSupplyLt()).unitPrice(contract.getUnitPrice())
														.dealCondition(contract.getDealCondition()).contractFile(contract.getContractFile()).contractStatus("완료").build();
		return contractDTO;
	}
	
	@Transactional
	@Test
	public void getDTOListTest() {
		List<ContractDTO> contractDTOList = new ArrayList<>();
		
		//계약이 미완료 인것만! 품목 가져오기 -> 계약 미완료인 상태
		List<Material> materialList = materialRepository.getListNoContract();
		
		//계약 미완료인것 먼저 DTO리스트에 넣기 -> contractFileDTO 은 Builder.Default 로 만들어주기때문에, 지금은 없기 때문에 null로 셋팅해서 넣어주기
		for(int i=0;i<materialList.size();i++) {
			ContractDTO contractDTO = ContractDTO.builder().materialCode(materialList.get(i).getCode()).materialName(materialList.get(i).getName())
															.contractStatus("미완료").contractFileDTO(null).build();
			contractDTOList.add(contractDTO);			
		}
		log.info("만든 중간 dto 리스트 보자 : "+contractDTOList);
		
		//계약서에서 계약서번호 오름차순으로 계약서 전부 가져오기 -> 계약상태 완료인 상태
		List<Contract> contractList = contractRepository.findAll(Sort.by(Sort.Direction.ASC, "no"));
		log.info("정렬해서가져온 contract 리스트 보자 : "+contractList);
		
//		ContractDTO contractDTO2 = contractEntityToDTO(contractList.get(0));
		
		//계약 완료인것 DTO리스트에 넣기
		for(int i=0;i<contractList.size();i++) {
			contractDTOList.add(contractEntityToDTO(contractList.get(i)));
		}
		
		log.info("만든 최종 dto 리스트 보자 : "+contractDTOList);
	}
	
	@Test
	public void contractInPPCheckTest() {
		//화면에서 받아온 계약서번호 리스트로 조회하기!!
		//계약서번호로 조달계획 조회하는데
		// 1. 조회가 안된다 => 아직 조달계획에 등록이 안된거라서, 계약서 수정 가능
		// 2. 조회가 된다 => 조달계획에 등록이 된거라서, 계약서 수정 불가능
//		boolean result = procurementPlanRepository.existsByContract(contractRepository.findById(8).get());
//		log.info("결과 보자 : "+result);

		List<String> contractNoList = new ArrayList<>();
		contractNoList.add("1");
		contractNoList.add("2");
		contractNoList.add("8");
//		contractNoList.add("12");
		
		boolean contractInPPCheck = false;
		
		for(String contractNo : contractNoList) {
			//result -> 그 계약서가 조달계획에 있는지 여부! fasle -> 수정가능, true -> 수정 불가능!!
			boolean result = procurementPlanRepository.existsByContract(contractRepository.findById(Integer.parseInt(contractNo)).get());
			if(result == true) {
				contractInPPCheck = false;
				break;
			}else {
				contractInPPCheck = true;
			}
		}
		
		log.info("그래서 최종 참, 거짓 확인해 보자 : "+contractInPPCheck);
		
	}
	
	
	
	
	
	
}
