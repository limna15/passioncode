package com.passioncode.procurementsystem.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.stereotype.Service;

import com.passioncode.procurementsystem.dto.ContractDTO;
import com.passioncode.procurementsystem.entity.Company;
import com.passioncode.procurementsystem.entity.Contract;
import com.passioncode.procurementsystem.entity.Material;
import com.passioncode.procurementsystem.repository.CompanyRepository;
import com.passioncode.procurementsystem.repository.ContractRepository;
import com.passioncode.procurementsystem.repository.MaterialRepository;

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
	public Contract get(Integer no) {
		return contractRepository.findById(no).get();
	}
	
	
	@Override
	public List<ContractDTO> materialEntityToDTO(Material material) {
		//계약서번호, 품목코드, 품목명, 협력회사, 담당자, 담당자연락처, 품목공급LT, 단가, 거래조건, 계약서, 계약 상태
		
		Collection<Contract> collectionContract = contractRepository.findByMaterial(material);
		List<Contract> contractList = (ArrayList<Contract>) collectionContract;
		
		List<ContractDTO> contractDTOList = new ArrayList<>();
		ContractDTO contractDTO = null;
		
		//contractList에 값이 존재할때 = 계약상태가 완료인 상태
		if(contractList.size()!=0) {
			for(int i=0;i<contractList.size();i++) {
				contractDTO = ContractDTO.builder().contractNo(contractList.get(i).getNo()).materialCode(contractList.get(i).getMaterial().getCode())
						.materialName(contractList.get(i).getMaterial().getName()).companyName(contractList.get(i).getCompany().getName())
						.manager(contractList.get(i).getCompany().getManager()).managerTel(contractList.get(i).getCompany().getManagerTel())
						.supplyLt(contractList.get(i).getSupplyLt()).unitPrice(contractList.get(i).getUnitPrice())
						.dealCondition(contractList.get(i).getDealCondition()).contractFile(contractList.get(i).getContractFile())
						.contractStatus(true).build();
				contractDTOList.add(contractDTO);
			}
		}else { //contractList에 비어있을때 = 계약상태가 미완료인 상태
			contractDTO = ContractDTO.builder().materialCode(material.getCode()).materialName(material.getName()).contractStatus(false).build();
			contractDTOList.add(contractDTO);
		}				
		
		log.info("품목을 통해 뽑은 contractDTOList : "+contractDTOList);
		return contractDTOList;
	}

	@Override
	public ContractDTO contractEntityToDTO(Contract contract) {
		ContractDTO contractDTO = ContractDTO.builder().contractNo(contract.getNo()).materialCode(contract.getMaterial().getCode()).materialName(contract.getMaterial().getName())
				.companyName(contract.getCompany().getName()).manager(contract.getCompany().getManager()).managerTel(contract.getCompany().getManagerTel())
				.supplyLt(contract.getSupplyLt()).unitPrice(contract.getUnitPrice()).dealCondition(contract.getDealCondition()).contractFile(contract.getContractFile())
				.contractStatus(true).build();
		return contractDTO;
	}

	@Override
	public Contract dtoToEntity(ContractDTO contractDTO) {		
		//계약서번호, 품목코드(품목), 사업자등록번호(협력회사), 품목공급LT, 단가, 거래조건, 계약서
		Collection<Company> collectionCompany = companyRepository.findByNameContaining(contractDTO.getCompanyName());
		List<Company> companyList = (List<Company>) collectionCompany;
		log.info("품목명으로 찾은 companyList 봐보자 : "+companyList);
		Company company = companyList.get(0);
		
		Contract contract = Contract.builder().no(contractDTO.getContractNo()).material(materialRepository.findById(contractDTO.getMaterialCode()).get())
							.company(company).supplyLt(contractDTO.getSupplyLt()).unitPrice(contractDTO.getUnitPrice()).dealCondition(contractDTO.getDealCondition())
							.contractFile(contractDTO.getContractFile()).build();
							
		return contract;
	}

	@Override
	public List<ContractDTO> getDTOList() {
		List<Material> materialList = materialRepository.findAll();
		
		List<ContractDTO> contractDTOList = new ArrayList<>();
		ContractDTO contractDTO = null;
		
		for(int j=0;j<materialList.size();j++) {
			Collection<Contract> collectionContract = contractRepository.findByMaterial(materialList.get(j));
			List<Contract> contractList = (ArrayList<Contract>) collectionContract;		
			
			//contractList에 값이 존재할때 = 계약상태가 완료인 상태
			if(contractList.size()!=0) {
				for(int i=0;i<contractList.size();i++) {
					contractDTO = ContractDTO.builder().contractNo(contractList.get(i).getNo()).materialCode(contractList.get(i).getMaterial().getCode())
							.materialName(contractList.get(i).getMaterial().getName()).companyName(contractList.get(i).getCompany().getName())
							.manager(contractList.get(i).getCompany().getManager()).managerTel(contractList.get(i).getCompany().getManagerTel())
							.supplyLt(contractList.get(i).getSupplyLt()).unitPrice(contractList.get(i).getUnitPrice())
							.dealCondition(contractList.get(i).getDealCondition()).contractFile(contractList.get(i).getContractFile())
							.contractStatus(true).build();
					contractDTOList.add(contractDTO);
				}
			}else { //contractList에 비어있을때 = 계약상태가 미완료인 상태
				contractDTO = ContractDTO.builder().materialCode(materialList.get(j).getCode()).materialName(materialList.get(j).getName()).contractStatus(false).build();
				contractDTOList.add(contractDTO);
			}				
		}		
		
		return contractDTOList;
	}



}
