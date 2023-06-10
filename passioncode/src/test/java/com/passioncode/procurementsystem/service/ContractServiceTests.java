package com.passioncode.procurementsystem.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import com.passioncode.procurementsystem.dto.ContractDTO;
import com.passioncode.procurementsystem.entity.Company;
import com.passioncode.procurementsystem.entity.Contract;
import com.passioncode.procurementsystem.entity.Material;

import lombok.extern.log4j.Log4j2;

@Log4j2
@SpringBootTest
public class ContractServiceTests {
	
	@Autowired
	ContractService contractService;
	
	@Autowired
	MaterialService materialService;
	
	@Transactional
	@Test
	public void materialEntityToDTOTest() {
		//CGa0002 계약 미완료 품목
		//CGa0001 계약 완료 품목
		log.info("품목엔티티 이용해서 contractDTO 가져오나 보자(리스트) :"+contractService.materialEntityToDTO(materialService.getMaterial("CGa0002")));
		log.info("품목엔티티 이용해서 contractDTO 가져오나 보자(리스트) :"+contractService.materialEntityToDTO(materialService.getMaterial("CGa0001")));		
	}
	
	@Transactional
	@Test
	public void contractEntityToDTOTest() {
		log.info("계약서 엔티티 이용해서 contractDTO 가져오기 : "+contractService.contractEntityToDTO(contractService.getContract(1)));
	}
	
	@Transactional
	@Test
	public void dtoToEntityTest() {
		ContractDTO contractDTO = contractService.contractEntityToDTO(contractService.getContract(1));
		log.info("contractDTO -> 계약서 엔티티로 바꾸기 : "+contractService.dtoToEntity(contractDTO));
	}
	
	@Transactional
	@Test
	public void getDTOListTest() {
		log.info("모든 contractDTO 리스트 보기 : "+contractService.getDTOList());
		log.info("리스트 크기 봐보자 : "+contractService.getDTOList().size());
	}
	
	@Test
	public void registerTest() {
		//계약서번호, 품목코드, 품목명, 협력회사, 담당자, 담당자연락처, 품목공급LT, 단가, 거래조건, 계약서, 계약 상태 / 사업자등록번호
		// 화면에서 사업자등록번호 숨겨서 가지고 있자!
		
		//계약서등록 시, 협력회사를 찾아 등록할때, 거래가능여부가 1인 회사중에서만 찾음!! 만약 바꾸고싶으면 계약 등록전에 협력회사 관리창에서 바꿔줘야함!
		Material material = contractService.getMaterial("CNa0001");
		Company company = contractService.getCompany("104-84-55123");
		
		//계약상태는, 계약을 등록하는 상황이기때문에 무조건 true -> 화면에서 계약상태 숨겨서 값을 완료로 보내주자!
		//입력하는 상황이라서 계약서번호도 숨겨서 null값으로 보내주자!!!
		ContractDTO contractDTO = ContractDTO.builder().materialCode(material.getCode()).materialName(material.getName())
														.companyNo(company.getNo()).companyName(company.getName()).manager(company.getManager())
														.managerTel(company.getManagerTel()).supplyLt(20).unitPrice(500).contractFile("DTO넣는거로 테스트중")
														.contractStatus("완료").build();
		log.info("contractDTO 봐보자 : "+contractDTO);
		
		//계약서번호, 품목공급LT, 단가, 거래조건, 계약서, 품목코드(외래키)(품목), 사업자등록번호(외래키)(협력회사)
//		Contract contract = Contract.builder().supplyLt(contractDTO.getSupplyLt()).unitPrice(contractDTO.getUnitPrice()).contractFile(contractDTO.getContractFile())
//												.material(contractService.getMaterial(contractDTO.getMaterialCode()))
//												.company(contractService.getCompany(contractDTO.getCompanyNo())).build();
//		log.info("DTO -> contract 봐보자 : "+contract);
		
		log.info("어디 서비스로 등록 제대로 되었나? 번호 보자 : "+contractService.register(contractDTO));		
	}
	
	@Test
	public void modifyTest() {
		//계약서번호, 품목코드, 품목명, 협력회사, 담당자, 담당자연락처, 품목공급LT, 단가, 거래조건, 계약서, 계약 상태 / 사업자등록번호
		// 화면에서 사업자등록번호 숨겨서 가지고 있자!
		
		//계약서등록 시, 협력회사를 찾아 등록할때, 거래가능여부가 1인 회사중에서만 찾음!! 만약 바꾸고싶으면 계약 등록전에 협력회사 관리창에서 바꿔줘야함!
		Material material = contractService.getMaterial("CNa0001");
		Company company = contractService.getCompany("104-84-55123");
		
		//계약상태는, 계약을 수정하는 상황이기때문에 무조건 true -> 화면에서 계약상태 숨겨서 값을 완료 로 보내주자!
		//수정하는 거기때문에, 계약서 번호 추가해서 테스트!
		ContractDTO contractDTO = ContractDTO.builder().materialCode(material.getCode()).materialName(material.getName()).contractNo(9)
														.companyNo(company.getNo()).companyName(company.getName()).manager(company.getManager())
														.managerTel(company.getManagerTel()).supplyLt(20).unitPrice(500).contractFile("DTO넣는거로 테스트중 수정2")
														.contractStatus("완료").build();
		log.info("contractDTO 봐보자 : "+contractDTO);
		contractService.modify(contractDTO);		
	}
	
	@Transactional
	@Commit
	@Test
	public void deleteTest() {
		Contract contract = contractService.getContract(10);
		ContractDTO contractDTO = contractService.contractEntityToDTO(contract);	//이때 DTO로 만들면서 외래키 지연로딩 쓰기 때문에 @Transactional 필요
		contractService.delete(contractDTO);										//하지만 테스트환경에서는 @Transactional 쓰면 자동롤백 되기때문에, 삭제 테스틀르 위해 @Commit 필요
	}
	

}
