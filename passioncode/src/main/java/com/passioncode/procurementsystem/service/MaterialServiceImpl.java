package com.passioncode.procurementsystem.service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.passioncode.procurementsystem.dto.DrawingFileDTO;
import com.passioncode.procurementsystem.dto.MaterialDTO;
import com.passioncode.procurementsystem.dto.MiddleCategoryDTO;
import com.passioncode.procurementsystem.entity.MRP;
import com.passioncode.procurementsystem.entity.Material;
import com.passioncode.procurementsystem.entity.MiddleCategory;
import com.passioncode.procurementsystem.repository.ContractRepository;
import com.passioncode.procurementsystem.repository.LargeCategoryRepository;
import com.passioncode.procurementsystem.repository.MRPRepository;
import com.passioncode.procurementsystem.repository.MaterialRepository;
import com.passioncode.procurementsystem.repository.MiddleCategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@RequiredArgsConstructor
@Log4j2
public class MaterialServiceImpl implements MaterialService {
	
	private final ContractRepository contractRepository;	
	private final MaterialRepository materialRepository;
	private final MiddleCategoryRepository middleCategoryRepository;
	private final LargeCategoryRepository largeCategoryRepository;
	private final MRPRepository mrpRepository;
	
	
	@Override
	public Material getMaterial(String code) {		
		return materialRepository.findById(code).orElse(null);
	}
	
	@Override
	public List<Material> getMaterialListByCodeContaining(String code) {
		return materialRepository.findByCodeContaining(code);
	}
	
	/**
	 * 공용여부 Integer -> 한글로 만들어주기 <br>
	 * 0 : 공용, 1 : 전용
	 * @param shareStatus
	 * @return
	 */
	public String shareStatusChangeToString(Integer shareStatus) {
		// 0 : 공용, 1 : 전용
		return shareStatus==0 ? "공용" : "전용";
	}
	
	/**
	 * 공용여부 한글 -> Integer 로 만들어주기 <br>
	 * 공용 : 0 , 전용 : 1
	 * @param shareStatus
	 * @return
	 */
	public Integer shareStatusChangeToInteger(String shareStatus) {
		// 공용 : 0 , 전용 : 1
		return shareStatus.equals("공용") ? 0 : 1;
	}
	
	public MiddleCategoryDTO MCentityToMCDTO(MiddleCategory middleCategory) {
		MiddleCategoryDTO middleCategoryDTO = MiddleCategoryDTO.builder().middleCode(middleCategory.getCode()).middleCategory(middleCategory.getCategory())
																		.largeCode(middleCategory.getLargeCategory().getCode())
																		.largeCategory(middleCategory.getLargeCategory().getCategory()).build();
		return middleCategoryDTO;
	}

	@Override
	public MaterialDTO entityToDTO(Material material) {
		//품목코드, 품목명, 대, 중, 규격, 재질, 제작사양, 도면번호, 도면Image, 공용여부
		//도면파일 업로드가 되어있는것, 안되어있는것 나누어서 DTO 변환 해주자!!
		MaterialDTO materialDTO = new MaterialDTO();
		
		if(material.getDrawingFile() !=null) {	//도면파일이 존재 O -> drawingFileDTO 값을 세팅해주기
			materialDTO =  MaterialDTO.builder().code(material.getCode()).name(material.getName()).size(material.getSize()).quality(material.getQuality())
												.spec(material.getSpec()).drawingNo(material.getDrawingNo()).drawingFile(material.getDrawingFile())
												.drawingFileDTO(new DrawingFileDTO(material.getDrawingFile()))
												.shareStatus(shareStatusChangeToString(material.getShareStatus()))
												.largeCategoryName(material.getMiddleCategory().getLargeCategory().getCategory())
												.middleCategoryName(material.getMiddleCategory().getCategory())
												.largeCategoryCode(material.getMiddleCategory().getLargeCategory().getCode())
												.middleCategoryCode(material.getMiddleCategory().getCode()).build();
		}else {									//도면파일이 존재 X -> drawingFileDTO null로 세팅해주기
			materialDTO =  MaterialDTO.builder().code(material.getCode()).name(material.getName()).size(material.getSize()).quality(material.getQuality())
												.spec(material.getSpec()).drawingNo(material.getDrawingNo()).drawingFile(material.getDrawingFile())
												.drawingFileDTO(null)
												.shareStatus(shareStatusChangeToString(material.getShareStatus()))
												.largeCategoryName(material.getMiddleCategory().getLargeCategory().getCategory())
												.middleCategoryName(material.getMiddleCategory().getCategory())
												.largeCategoryCode(material.getMiddleCategory().getLargeCategory().getCode())
												.middleCategoryCode(material.getMiddleCategory().getCode()).build();
		}
		
		//만든 materialDTO 에 List<MiddleCategory> middleCategoryList 추가하기
		List<MiddleCategory> middleCategoryList = middleCategoryRepository.findByLargeCategory(largeCategoryRepository.findById(materialDTO.getLargeCategoryCode()).get());
		List<MiddleCategoryDTO> middleCategoryDTOList = new ArrayList<>();
		for(MiddleCategory middleCategory:middleCategoryList) {
			middleCategoryDTOList.add(MCentityToMCDTO(middleCategory));
		}	
		
		materialDTO = materialDTO.toBuilder().middleCategoryDTOList(middleCategoryDTOList).build();
		
		return materialDTO;
	}
	
	/**
	 * 도면이미지 첨부한기 전에 했던 것
	 * @param material
	 * @return
	 */
	public MaterialDTO entityToDTO2(Material material) {
		//품목코드, 품목명, 대, 중, 규격, 재질, 제작사양, 도면번호, 도면Image, 공용여부
		MaterialDTO materialDTO =  MaterialDTO.builder().code(material.getCode()).name(material.getName()).size(material.getSize()).quality(material.getQuality())
														.spec(material.getSpec()).drawingNo(material.getDrawingNo()).drawingFile(material.getDrawingFile())
														.shareStatus(shareStatusChangeToString(material.getShareStatus()))
														.largeCategoryName(material.getMiddleCategory().getLargeCategory().getCategory())
														.middleCategoryName(material.getMiddleCategory().getCategory())
														.largeCategoryCode(material.getMiddleCategory().getLargeCategory().getCode())
														.middleCategoryCode(material.getMiddleCategory().getCode()).build();						
		return materialDTO;
	}
		

	@Override
	public Material dtoToEntity(MaterialDTO materialDTO) {		
		//품목코드, 품목명, 공용여부, 규격, 재질, 제작사양, 도면번호, 도면, 중분류
		Material material = Material.builder().code(materialDTO.getCode()).name(materialDTO.getName()).shareStatus(shareStatusChangeToInteger(materialDTO.getShareStatus()))
												.size(materialDTO.getSize()).quality(materialDTO.getQuality())
												.spec(materialDTO.getSpec()).drawingNo(materialDTO.getDrawingNo()).drawingFile(materialDTO.getDrawingFile())
												.middleCategory(middleCategoryRepository.findById(materialDTO.getMiddleCategoryCode()).get()).build();
		return material;
	}

	@Override
	public List<MaterialDTO> getDTOList() {
		List<Material> materialList = materialRepository.findAll(Sort.by(Sort.Direction.ASC, "code"));
//		List<Material> materialList = materialRepository.getListWithSort();
		List<MaterialDTO> materialDTOList = new ArrayList<>();
		for(int i=0;i<materialList.size();i++) {
			materialDTOList.add(entityToDTO(materialList.get(i)));
		}
		//log.info("getDTOList()를 통해서 만든 materialDTO 리스트 : "+materialDTOList);
		return materialDTOList;
	}

	@Override
	public String contractStatusCheck(Material material) {		
		//완료 : 계약상태 O, 미완료 : 계약상태 X
		return contractRepository.existsByMaterial(material) ? "완료" : "미완료";
	}
	
	
	@Override
	public String register(MaterialDTO materialDTO) {
		Material material = dtoToEntity(materialDTO);
		materialRepository.save(material);
		log.info("저장된 품목(material) 정보 : "+material);
		
		return material.getCode();
	}
	
	@Transactional
	@Override
	public void modify(MaterialDTO materialDTO) {
		Material material = dtoToEntity(materialDTO);
		materialRepository.save(material);
		log.info("수정된 품목(material) 정보 : "+material);
	}
	
	@Override
	public void delete(MaterialDTO materialDTO) {
		log.info("삭제된 품목(material)정보 : "+dtoToEntity(materialDTO));
		materialRepository.deleteById(materialDTO.getCode());		
	}

	/**
	 * 랜덤으로 소요일 계산해주기 <br>
	 * 소요일 = 오늘(입력하는)날짜 + (20~40)
	 * @return
	 */
	public Date makeRandomMrpDate() {
		DateFormat simpleDateFormat = new SimpleDateFormat("yyyy년 MM월 dd일"); 		
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		Date todayDate=cal.getTime();
		//log.info("캘린더 셋팅된 오늘날짜 : "+simpleDateFormat.format(todayDate));
		
		//오늘날짜 + (20~40) => 랜덤 소요일 만들기
		int mrpDateRandomVar = (int)(Math.random()*(40-20+1)+20);
		//log.info("소요일을 위한 랜덤변수 값 : "+mrpDateRandomVar);
		cal.add(Calendar.DATE, +mrpDateRandomVar);
		
		Date randomMrpDate=cal.getTime();
		//log.info("소요일 랜덤변수를 더해 만든 랜덤소요일 : "+simpleDateFormat.format(randomMrpDate));
		
		return randomMrpDate;
	}
	
	@Override
	public void mrpRegisterWithMaterialRegister(String materialCode) {
		//소요공정 -> 10개 중 (0~9)랜덤으로 세팅
		String[] inputProcess = {"LIB #1각형 J/R INSERT", "LIB #2원형 세정", "LIB #3각형 X-RAY", "LIB #4원형 TUBING", "LIB #5각형 L/P WELDING",
									"LIB #6각형 CAN-CAP WELDING", "LIB #7각형 E/L FILLING #2", "LIB #8각형 TAB WELDING", "LIB #9각형 BALL WELDING", "LIB #10원형 TUBING"};
		for(int i=0;i<2;i++) {
			//(int)(Math.random()*(최대값-최소값+1)+최소값)
			int processRandomVar = (int)(Math.random()*(9-0+1)+0);
			//log.info("소요공정을 위한 랜덤변수 값 : "+processRandomVar);
			String randomProcess = inputProcess[processRandomVar];
			//log.info("소요공정 랜덤변수 값을 이용한 소요공정 테스트 : "+randomProcess);
			//소요량 -> 100*랜덤(1~15) 계산해서 랜덤으로 세팅
			int mrpAmountRandomVar = (int)(Math.random()*(15-1+1)+1);
			//log.info("소요량을 위한 랜덤변수 값 : "+mrpAmountRandomVar);
			Integer randomMrpAmount = 100*mrpAmountRandomVar;
			//log.info("소요량 랜덤변수 값을 이용한 소요량 테스트 : "+randomMrpAmount);
			//소요일 -> 오늘(입력하는)날짜 + (20~40) 계산해서 랜덤으로 세팅
			//log.info("날짜 오늘꺼 테스트 : "+new Date());
			Date randomMrpDate = makeRandomMrpDate();
			//log.info("랜덤으로 만든 소요일 테스트 : "+randomMrpDate);
			
			MRP mrp=MRP.builder().process(randomProcess).amount(randomMrpAmount).date(randomMrpDate).material(materialRepository.findById(materialCode).get()).build();
			mrpRepository.save(mrp);
		}
		
	}

	@Override
	public void mrpDeleteWithMaterialModify(String materialCode) {
		//품목코드를 이용해서 mrp들 찾아와서 그 해당 mrp 지워주기
		List<MRP> mrpList = mrpRepository.findBymaterialCode(materialCode);
		//log.info("찾은 mrp 리스트 보자 : "+mrpList);
		
		for(MRP mrp : mrpList) {
			mrpRepository.delete(mrp);
		}
	}

	
	
	



}
