package com.passioncode.procurementsystem.repository;

import java.util.ArrayList;


import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.passioncode.procurementsystem.dto.ContractDTO;
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
									.dealCondition(contract.getDealCondition()).contractFile(contract.getContractFile()).contractStatus(true).build();
		log.info("계약 완료된 DTO(사실상 계약서테이블의 내용!) : "+contractDTO);
		
		//계약이 미완료인 계약DTO 만들기 (계약테이블에 없는건 다 null로 해서 만들기)
		//CGa0002 품목 계약서 없는 상태
		Material material = materialRepository.findById("CGa0002").get();
		ContractDTO contractDTO2 = ContractDTO.builder().materialCode(material.getCode()).materialName(material.getName()).contractStatus(false).build();
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
								.contractFile(contractList.get(i).getContractFile()).contractStatus(true).build();
				contractDTOList.add(contractDTO3);
			}
		}else { //contractList에 비어있을때 = 계약상태가 미완료인 상태
			contractDTO3 = ContractDTO.builder().materialCode(material.getCode()).materialName(material.getName()).contractStatus(false).build();
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
	
	
	
	
}
