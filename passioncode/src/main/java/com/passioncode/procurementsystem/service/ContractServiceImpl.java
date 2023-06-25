package com.passioncode.procurementsystem.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.passioncode.procurementsystem.dto.ContractDTO;
import com.passioncode.procurementsystem.dto.ContractFileDTO;
import com.passioncode.procurementsystem.entity.Company;
import com.passioncode.procurementsystem.entity.Contract;
import com.passioncode.procurementsystem.entity.Material;
import com.passioncode.procurementsystem.repository.CompanyRepository;
import com.passioncode.procurementsystem.repository.ContractRepository;
import com.passioncode.procurementsystem.repository.MaterialRepository;

import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@RequiredArgsConstructor
@Log4j2
public class ContractServiceImpl implements ContractService {
	
	private final ContractRepository contractRepository;	
	private final MaterialRepository materialRepository;
	private final CompanyRepository companyRepository;
	
	@Override
	public Contract getContract(Integer no) {
		return contractRepository.findById(no).get();
	}
	
	@Override
	public Material getMaterial(String code) {
		return materialRepository.findById(code).get();
	}

	@Override
	public Company getCompany(String no) {
		return companyRepository.findById(no).get();
	}
	
	@Override
	public List<Company> searchCompany(String name) {
		return companyRepository.findByNameContaining(name);
	}
	
	@Override
	public List<Company> searchCompanyWithDeal(String name) {
		return companyRepository.findByNameContainingWithDeal(name);
	}
	
	@Override
	public List<Contract> searchContractByCompanyNameAndMaterialCode(String companyName, String materialCode) {
		return contractRepository.findByCompanyNameAndMaterialCode(companyName, materialCode);
	}
	
	@Override
	public List<Contract> searchContractByMaterialCode(String materialCode) {
		return contractRepository.findByMaterialCode(materialCode);
	}
	
	/**
	 * 계약서번호 문자버전으로 바꾸기 <br>
	 * 계약서번호 1 -> C00001 로 바꿔주기
	 * @param contractNo
	 * @return
	 */
	public String makeContractNoStr(Integer contractNo) {
		//계약서번호 1 -> C00001 로 바꿔주기
		String contractNoStr = String.format("%08d",contractNo);
		contractNoStr = "C" + contractNoStr;
		log.info("바꾼 계약서 번호 좀 보자 : ",contractNoStr);
		return contractNoStr;
	}

		
	@Override
	public List<ContractDTO> materialEntityToDTO(Material material) {
		//계약서번호, 품목코드, 품목명, 협력회사, 담당자, 담당자연락처, 품목공급LT, 단가, 거래조건, 계약서, 계약 상태 / 사업자등록번호
		
		List<Contract> contractList = contractRepository.findByMaterial(material);
		
		List<ContractDTO> contractDTOList = new ArrayList<>();
		ContractDTO contractDTO = null;
		
		//contractList에 값이 존재할때 = 계약상태가 완료인 상태
		if(contractList.size()!=0) {
			for(int i=0;i<contractList.size();i++) {
				contractDTO = ContractDTO.builder().contractNo(contractList.get(i).getNo()).materialCode(contractList.get(i).getMaterial().getCode())
													.contractNoStr(makeContractNoStr(contractList.get(i).getNo()))
													.contractFileDTO(new ContractFileDTO(contractList.get(i).getContractFile()))
													.materialName(contractList.get(i).getMaterial().getName()).companyNo(contractList.get(i).getCompany().getNo())
													.companyName(contractList.get(i).getCompany().getName()).manager(contractList.get(i).getCompany().getManager())
													.managerTel(contractList.get(i).getCompany().getManagerTel()).supplyLt(contractList.get(i).getSupplyLt())
													.unitPrice(contractList.get(i).getUnitPrice()).dealCondition(contractList.get(i).getDealCondition())
													.contractFile(contractList.get(i).getContractFile()).contractStatus("완료").build();
				contractDTOList.add(contractDTO);
			}
		}else { //contractList에 비어있을때 = 계약상태가 미완료인 상태
			contractDTO = ContractDTO.builder().materialCode(material.getCode()).materialName(material.getName()).contractStatus("미완료").build();
			contractDTOList.add(contractDTO);
		}				
		
		//log.info("materialEntityToDTO(Material material) 함수를 통한, 품목을 통해 뽑은 contractDTOList : "+contractDTOList);
		return contractDTOList;
	}

	@Override
	public ContractDTO contractEntityToDTO(Contract contract) {
		ContractDTO contractDTO = ContractDTO.builder().contractNo(contract.getNo()).materialCode(contract.getMaterial().getCode()).materialName(contract.getMaterial().getName())
														.contractNoStr(makeContractNoStr(contract.getNo()))
														.contractFileDTO(new ContractFileDTO(contract.getContractFile()))
														.companyNo(contract.getCompany().getNo()).companyName(contract.getCompany().getName()).manager(contract.getCompany().getManager())
														.managerTel(contract.getCompany().getManagerTel()).supplyLt(contract.getSupplyLt()).unitPrice(contract.getUnitPrice())
														.dealCondition(contract.getDealCondition()).contractFile(contract.getContractFile()).contractStatus("완료").build();
		return contractDTO;
	}

	@Override
	public Contract dtoToEntity(ContractDTO contractDTO) {		
		//계약서번호, 품목코드(품목), 사업자등록번호(협력회사), 품목공급LT, 단가, 거래조건, 계약서		
		Contract contract = Contract.builder().no(contractDTO.getContractNo()).material(materialRepository.findById(contractDTO.getMaterialCode()).get())
												.company(companyRepository.findById(contractDTO.getCompanyNo()).get()).supplyLt(contractDTO.getSupplyLt())
												.unitPrice(contractDTO.getUnitPrice()).dealCondition(contractDTO.getDealCondition())
												.contractFile(contractDTO.getContractFile()).build();							
		return contract;
	}

	@Override
	public List<ContractDTO> getDTOList() {
						
		List<ContractDTO> contractDTOList = new ArrayList<>();
		
		//계약이 미완료 인것만! 품목 가져오기 -> 계약 미완료인 상태
		List<Material> materialList = materialRepository.getListNoContract();
		
		//계약 미완료인것 먼저 DTO리스트에 넣기 -> contractFileDTO 은 Builder.Default 로 만들어주기때문에, 지금은 없기 때문에 null로 셋팅해서 넣어주기
		for(int i=0;i<materialList.size();i++) {
			ContractDTO contractDTO = ContractDTO.builder().materialCode(materialList.get(i).getCode()).materialName(materialList.get(i).getName())
															.contractStatus("미완료").contractFileDTO(null).build();
			contractDTOList.add(contractDTO);			
		}
		
		//계약서에서 계약서번호 오름차순으로 계약서 전부 가져오기 -> 계약상태 완료인 상태
		List<Contract> contractList = contractRepository.findAll(Sort.by(Sort.Direction.ASC, "no"));
		
		//계약 완료인것 DTO리스트에 넣기
		for(int i=0;i<contractList.size();i++) {
			contractDTOList.add(contractEntityToDTO(contractList.get(i)));
		}
		
		return contractDTOList;
	}
	
	/**
	 * 정렬생각 안하고 처음에 짠 ContractDTO리스트 불러오기
	 * @return
	 */	
	public List<ContractDTO> getDTOList2() {
		List<Material> materialList = materialRepository.findAll();
		
		List<ContractDTO> contractDTOList = new ArrayList<>();
		ContractDTO contractDTO = null;
		
		for(int j=0;j<materialList.size();j++) {
			List<Contract> contractList = contractRepository.findByMaterial(materialList.get(j));	
			
			//contractList에 값이 존재할때 = 계약상태가 완료인 상태
			if(contractList.size()!=0) {
				for(int i=0;i<contractList.size();i++) {
					contractDTO = ContractDTO.builder().contractNo(contractList.get(i).getNo()).materialCode(contractList.get(i).getMaterial().getCode())
														.materialName(contractList.get(i).getMaterial().getName()).companyName(contractList.get(i).getCompany().getName())
														.manager(contractList.get(i).getCompany().getManager()).managerTel(contractList.get(i).getCompany().getManagerTel())
														.supplyLt(contractList.get(i).getSupplyLt()).unitPrice(contractList.get(i).getUnitPrice())
														.dealCondition(contractList.get(i).getDealCondition()).contractFile(contractList.get(i).getContractFile())
														.contractStatus("완료").build();
					contractDTOList.add(contractDTO);
				}
			}else { //contractList에 비어있을때 = 계약상태가 미완료인 상태
				contractDTO = ContractDTO.builder().materialCode(materialList.get(j).getCode()).materialName(materialList.get(j).getName()).contractStatus("미완료").build();
				contractDTOList.add(contractDTO);
			}				
		}		
		
		return contractDTOList;
	} 


	@Override
	public Integer register(ContractDTO contractDTO) {
		//계약서번호, 품목코드, 품목명, 협력회사, 담당자, 담당자연락처, 품목공급LT, 단가, 거래조건, 계약서, 계약 상태 / 사업자등록번호
		// 화면에서 사업자등록번호 숨겨서 가지고 있자!
		
		//계약서등록 시, 협력회사를 찾아 등록할때, 거래가능여부가 1인 회사중에서만 찾음!! 만약 바꾸고싶으면 계약 등록전에 협력회사 관리창에서 바꿔줘야함!
		
		//계약상태는, 계약을 등록하는 상황이기때문에 무조건 true -> 화면에서 계약상태 숨겨서 값을 true로 보내주자!
		//입력하는 상황이라서 계약서번호도 숨겨서 null값으로 보내주자!!!
		
		//계약서번호, 품목공급LT, 단가, 거래조건, 계약서, 품목코드(외래키)(품목), 사업자등록번호(외래키)(협력회사)
		Contract contract = dtoToEntity(contractDTO);
		log.info("저장된 계약서(contract) 정보 : "+contract);
		
		contractRepository.save(contract);		
		
		return contract.getNo();
	}
	
	@Transactional
	@Override
	public void modify(ContractDTO contractDTO) {
		Contract contract = dtoToEntity(contractDTO);
		log.info("수정된 계약서(contract) 정보 : "+contract);		
		contractRepository.save(contract);		
	}

	@Override
	public void delete(ContractDTO contractDTO) {
		log.info("삭제된 계약서(contract) 정보 : "+dtoToEntity(contractDTO));
		contractRepository.deleteById(contractDTO.getContractNo());
	}


	






}
