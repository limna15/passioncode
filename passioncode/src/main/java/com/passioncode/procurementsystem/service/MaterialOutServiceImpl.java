package com.passioncode.procurementsystem.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.passioncode.procurementsystem.dto.MaterialOutDTO;
import com.passioncode.procurementsystem.entity.MRP;
import com.passioncode.procurementsystem.entity.MaterialOut;
import com.passioncode.procurementsystem.entity.ProcurementPlan;
import com.passioncode.procurementsystem.repository.MRPRepository;
import com.passioncode.procurementsystem.repository.MaterialOutRepository;
import com.passioncode.procurementsystem.repository.ProcurementPlanRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@RequiredArgsConstructor
@Log4j2
public class MaterialOutServiceImpl implements MaterialOutService {
	
	@Autowired
	MRPRepository mrpRepository;
	
	@Autowired
	ProcurementPlanRepository procurementPlanRepository;
	
	@Autowired
	MaterialOutRepository materialOutRepository;

	@Override
	public List<MaterialOutDTO> getDTOList() {
		List<MRP> mrpList= mrpRepository.findAll();
		List<ProcurementPlan> ppList= new ArrayList<>();
				
		for(int i=0; i<mrpList.size(); i++) {
			if(procurementPlanRepository.existsByMrp(mrpList.get(i))) {
				ppList.add(procurementPlanRepository.findByMrp(mrpList.get(i)));				
			}
		}
		
		//log.info("ppList 한번 보자 >>> " + ppList + ", 사이즈는 >>> " + ppList.size());
		
		List<MaterialOutDTO> moDTOList= new ArrayList<>();
		MaterialOutDTO moDTO= null;
		List<MaterialOut> moList= materialOutRepository.findAll();
		
		for(int i=0; i<ppList.size(); i++) {
			//세부구매발주서 등록 +  완료일(입고일) 등록 -> 출고 리스트(출고 상태 0(버튼))
			if(ppList.get(i).getDetailPurchaseOrder() != null && ppList.get(i).getCompletionDate() != null) {
				//출고 엔티티에 존재 O
				if(materialOutRepository.existsByMrp(ppList.get(i).getMrp())){
					moDTO= MaterialOutDTO.builder().dpoCodeStr(codeStr(ppList.get(i).getDetailPurchaseOrder().getCode()))
							.mrpDate(ppList.get(i).getMrp().getDate()).materialCode(ppList.get(i).getMrp().getMaterial().getCode())
							.materialName(ppList.get(i).getMrp().getMaterial().getName())
							.process(ppList.get(i).getMrp().getProcess()).mrpAmount(ppList.get(i).getMrp().getAmount()).outStatus("1").build();			
					moDTOList.add(moDTO);		
				//출고 엔티티에 존재 X	
				}else {
					moDTO= MaterialOutDTO.builder().dpoCodeStr(codeStr(ppList.get(i).getDetailPurchaseOrder().getCode()))
							.mrpDate(ppList.get(i).getMrp().getDate()).materialCode(ppList.get(i).getMrp().getMaterial().getCode())
							.materialName(ppList.get(i).getMrp().getMaterial().getName())
							.process(ppList.get(i).getMrp().getProcess()).mrpAmount(ppList.get(i).getMrp().getAmount()).outStatus("0").build();			
					moDTOList.add(moDTO);			
				}
				
			}
		}
		return moDTOList;
	}
	
	//발주코드에 문자를 넣어서 보내기
	public String codeStr(Integer num1) {
		String pNum = String.format("%05d", num1);
		pNum = "DPO"+pNum;
		
		return pNum;	
	}
}
