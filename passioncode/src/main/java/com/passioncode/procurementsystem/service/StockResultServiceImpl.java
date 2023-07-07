package com.passioncode.procurementsystem.service;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import com.passioncode.procurementsystem.dto.DrawingFileDTO;
import com.passioncode.procurementsystem.dto.MaterialDTO;
import com.passioncode.procurementsystem.dto.MiddleCategoryDTO;
import com.passioncode.procurementsystem.dto.StockResultDTO;
import com.passioncode.procurementsystem.entity.Material;
import com.passioncode.procurementsystem.entity.MiddleCategory;
import com.passioncode.procurementsystem.repository.LargeCategoryRepository;
import com.passioncode.procurementsystem.repository.MaterialOutRepository;
import com.passioncode.procurementsystem.repository.MaterialRepository;
import com.passioncode.procurementsystem.repository.MiddleCategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@RequiredArgsConstructor
@Log4j2
public class StockResultServiceImpl implements StockResultService {
	
	@Autowired
	MaterialRepository materialRepository;
	
	@Autowired
	MaterialOutRepository materialOutRepository;
	
	@Autowired
	LargeCategoryRepository largeCategoryRepository;
	
	@Autowired
	MiddleCategoryRepository middleCategoryRepository;
	
	public String shareStatusChangeToString(Integer shareStatus) {
		// 0 : 공용, 1 : 전용
		return shareStatus==0 ? "공용" : "전용";
	}
	
	public MiddleCategoryDTO MCentityToMCDTO(MiddleCategory middleCategory) {
		MiddleCategoryDTO middleCategoryDTO = MiddleCategoryDTO.builder().middleCode(middleCategory.getCode()).middleCategory(middleCategory.getCategory())
																		.largeCode(middleCategory.getLargeCategory().getCode())
																		.largeCategory(middleCategory.getLargeCategory().getCategory()).build();
		return middleCategoryDTO;
	}
	
	public MaterialDTO materialEntityToDTO(Material material) {
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
	
	
	@Override
	public List<StockResultDTO> getStockResultDTOList() {
		//재고산출 들어가자마자 보이는 재고산출DTO 리스트!  
		//오늘 날짜 기준으로, 그 해당 년도의 1월 1일 날짜부터 오늘까지의 날짜까지 재고산출 계산해서 재고산출DTO리스트 만들기
		
		//1. 오늘 날짜 기준으로, 그 해당년도 1월 1일 과 오늘날짜 구하기
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date today = new Date();
		String todayStr = simpleDateFormat.format(today);
//		log.info("오늘 날짜 스트링으로 보자 : "+todayStr);
		String todayYearStr = todayStr.substring(0,5);
		//log.info("오늘 날짜의 년도 스트링으로 보자 : "+todayYearStr);
		
		Date yearFirstdate = today;
		try {
			yearFirstdate = simpleDateFormat.parse(todayYearStr+"01-01");
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//log.info("바꾼 올해 1월1일 보자 : "+yearFirstdate);
		String yearFirstdateStr = simpleDateFormat.format(yearFirstdate);
//		log.info("올해 1월1일 문자 잘 만들어졌나 : "+yearFirstdateStr);
		
		//2. 재고산출목록 받아온거 재고산출DTO 리스트에 넣어주기
		List<Object[]> calculStockResult = materialOutRepository.getCalculStockResult(yearFirstdateStr, todayStr);
//		log.info("이건 잘 가져오는건가 : "+calculStockResult);
		
		List<StockResultDTO> stockResultDTOList = new ArrayList<>();
		
		
		for(Object[] result:calculStockResult) {
			StockResultDTO stockResultDTO = StockResultDTO.builder().largeCategoryCode(String.valueOf(result[0])).largeCategoryName(String.valueOf(result[1]))
																	.middleCategoryCode(String.valueOf(result[2])).middleCategoryName(String.valueOf(result[3]))
																	.materialCode(String.valueOf(result[4])).materialName(String.valueOf(result[5]))
																	.inAmount(Integer.parseInt(String.valueOf(result[6])))
																	.outAmount(Integer.parseInt(String.valueOf(result[7])))
																	.stockAmount(Integer.parseInt(String.valueOf(result[8])))
																	.stockUnitPrice(Integer.parseInt(String.valueOf(result[9]))).build();
			stockResultDTOList.add(stockResultDTO);
		}
//		log.info("만들어진 재고산출DTO 리스트 보자 : "+stockResultDTOList);
		
		return stockResultDTOList;
	}

	@Override
	public List<StockResultDTO> getStockResultDTOListByPeriod(String startDate,String endDate) {
		//화면에서 받아온 시작날짜, 끝날짜 받아서, 재고산출DTO 로 만들어주기
		List<Object[]> calculStockResult = materialOutRepository.getCalculStockResult(startDate, endDate);
		
		List<StockResultDTO> stockResultDTOList = new ArrayList<>();
		
		
		for(Object[] result:calculStockResult) {
			StockResultDTO stockResultDTO = StockResultDTO.builder().largeCategoryCode(String.valueOf(result[0])).largeCategoryName(String.valueOf(result[1]))
																	.middleCategoryCode(String.valueOf(result[2])).middleCategoryName(String.valueOf(result[3]))
																	.materialCode(String.valueOf(result[4])).materialName(String.valueOf(result[5]))
																	.inAmount(Integer.parseInt(String.valueOf(result[6])))
																	.outAmount(Integer.parseInt(String.valueOf(result[7])))
																	.stockAmount(Integer.parseInt(String.valueOf(result[8])))
																	.stockUnitPrice(Integer.parseInt(String.valueOf(result[9]))).build();
			stockResultDTOList.add(stockResultDTO);
		}
//		log.info("만들어진 재고산출DTO 리스트 보자 : "+stockResultDTOList);
		
		return stockResultDTOList;
	}

}
