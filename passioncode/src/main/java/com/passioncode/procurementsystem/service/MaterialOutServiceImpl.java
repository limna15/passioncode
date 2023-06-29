package com.passioncode.procurementsystem.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.passioncode.procurementsystem.dto.MaterialOutDTO;
import com.passioncode.procurementsystem.entity.MRP;
import com.passioncode.procurementsystem.entity.MaterialIn;
import com.passioncode.procurementsystem.entity.MaterialOut;
import com.passioncode.procurementsystem.entity.ProcurementPlan;
import com.passioncode.procurementsystem.repository.MRPRepository;
import com.passioncode.procurementsystem.repository.MaterialInRepository;
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
	
	@Autowired
	MaterialInRepository materialInRepository;


	@Override
	public MaterialOutDTO materialOutToDTO(MRP mrp) {
		ProcurementPlan pp= procurementPlanRepository.findByMrp(mrp);
		MaterialOut mo= materialOutRepository.findByMrp(mrp);
		MaterialOutDTO materialOutDTO= null;
		
		//출고 테이블에 존재하는 mrp
		if(materialOutRepository.existsByMrp(mrp)){
			materialOutDTO= MaterialOutDTO.builder().dpoCodeStr(codeStr(pp.getDetailPurchaseOrder().getCode()))
					.mrpDate(mrp.getDate()).materialCode(mrp.getMaterial().getCode()).materialName(mrp.getMaterial().getName())
					.process(mrp.getProcess()).mrpAmount(mrp.getAmount()).outStatus("1").build();			
		//출고 테이블에 존재X
		}else {
			materialOutDTO= MaterialOutDTO.builder().dpoCodeStr(codeStr(pp.getDetailPurchaseOrder().getCode()))
					.mrpDate(mrp.getDate()).materialCode(mrp.getMaterial().getCode()).materialName(mrp.getMaterial().getName())
					.process(mrp.getProcess()).mrpAmount(mrp.getAmount()).outStatus("0").build();	
		}	
		return materialOutDTO;
	}
	
	@Override
	public MaterialOut DTOtoEntity(MaterialOutDTO materialOutDTO) {
		MaterialOut materialOut= MaterialOut.builder().status(1).mrp(mrpRepository.findById(materialOutDTO.getMrpCode()).get()).build();		
		return materialOut;
	}
	
	@Override
	public List<MaterialOutDTO> getDTOList() {
		//출고리스트에는 보여주면 안되는 입고상태 취소된 miList
		List<MaterialIn> miList= materialInRepository.findAll();
		List<MaterialIn> cancleMiList= new ArrayList<>();
		for(int i=0; i<miList.size(); i++) {
			if(miList.get(i).getStatus() == false){
				cancleMiList.add(miList.get(i));
				//log.info("입고상태가 취소인 품목들의 세부구매발주서 정보 >>> " + miList.get(i).getDetailPurchaseOrder());
			}
		}
		
		List<ProcurementPlan> ppList= procurementPlanRepository.findAll();
		
		//log.info("ppList 한번 보자 >>> " + ppList + ", 사이즈는 >>> " + ppList.size());
				
		List<MaterialOutDTO> moDTOList= new ArrayList<>();
		List<MaterialOutDTO> notNullMoDTOList= new ArrayList<>();
		List<MaterialOutDTO> nullMoDTOList= new ArrayList<>();
		MaterialOutDTO moDTO= null;
		List<MaterialOut> moList= materialOutRepository.findAll();
		//log.info("moList 한번 보자 >>> " + moList + ", 사이즈는 >>> " + moList.size());
		
		MaterialOut materialOut= null;
		
		for(int i=0; i<=ppList.size(); i++) {
		//log.info("ppList 완료일 한번 보자 >>> " + ppList.get(i).getCompletionDate());
			//세부구매발주서 등록 +  완료일(입고일) 등록 -> 출고 리스트(출고 상태 (버튼))
			if(ppList.get(i).getDetailPurchaseOrder() != null && ppList.get(i).getCompletionDate() != null) {
				//출고 엔티티에 존재 O
				if(materialOutRepository.existsByMrp(ppList.get(i).getMrp())){		
					if((ppList.get(i).getDetailPurchaseOrder().getCode()== miList.get(i).getDetailPurchaseOrder().getCode()) && miList.get(i).getStatus() == false) {
						moDTO= MaterialOutDTO.builder().outStatus("0").mrpCode(ppList.get(i).getMrp().getCode()).build();
						//log.info("입고 상태가 취소되었어용 >>> " + moDTO);
						//log.info("입고 상태 취소 + 상태보기 >>> " + i + "번째, " + miList.get(i).getStatus());
					}else {
						//추가해야될 내용 -> 입고상태가 완료(1)이면 출고DTO에 넣어주고, 취소(0)이면 리스트에는 안보여주고 DB에 출고상태 0, 출고일 null로 등록만함
						//입고상태 취소된 세부구매발주서와 자재입고에 등록된 세부구매발주서 번호가 같으면
						moDTO= MaterialOutDTO.builder().dpoCodeStr(codeStr(ppList.get(i).getDetailPurchaseOrder().getCode()))
								.mrpDate(ppList.get(i).getMrp().getDate()).materialCode(ppList.get(i).getMrp().getMaterial().getCode())
								.materialName(ppList.get(i).getMrp().getMaterial().getName())
								.process(ppList.get(i).getMrp().getProcess()).mrpAmount(ppList.get(i).getMrp().getAmount()).outStatus("1").build();			
						notNullMoDTOList.add(moDTO);		
						//log.info("입고 상태가 완료 DTO >>> " + moDTO);
						//log.info("입고 상태 완료 + 상태보기 >>> " + i + "번째, " + miList.get(i).getStatus());
					}	
				//출고 엔티티에 존재 X
				}else {
					moDTO= MaterialOutDTO.builder().dpoCodeStr(codeStr(ppList.get(i).getDetailPurchaseOrder().getCode()))
							.mrpDate(ppList.get(i).getMrp().getDate()).materialCode(ppList.get(i).getMrp().getMaterial().getCode())
							.materialName(ppList.get(i).getMrp().getMaterial().getName())
							.process(ppList.get(i).getMrp().getProcess()).mrpAmount(ppList.get(i).getMrp().getAmount()).outStatus("0").build();			
					nullMoDTOList.add(moDTO);
					//log.info("입고가 아직 아예 안된 아이 >>> "  + i + "번째, " + moDTO);
				}
			}//if문 끝(세부구매발주서 등록 + 입고일 등록)
			else {
				break;
			}	
		}//for문 끝
		
		//엔티티에 존재 X(null) -> 존재 O(출고 완료된 상태) 순으로 넣기
		for(MaterialOutDTO dto: nullMoDTOList) {
			moDTOList.add(dto);
		}
		for(MaterialOutDTO dto: notNullMoDTOList) {
			moDTOList.add(dto);
		}
		return moDTOList;
	}
	
	//발주코드에 문자를 넣어서 보내기
	public String codeStr(Integer num1) {
		String pNum = String.format("%05d", num1);
		pNum = "DPO"+pNum;
		
		return pNum;	
	}

	@Override
	public Integer register(MaterialOutDTO materialOutDTO) {
		MaterialOut materialOut= DTOtoEntity(materialOutDTO);
		materialOutRepository.save(materialOut);
		
		return materialOut.getCode();
	}
}
