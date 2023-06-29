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
		//1. 오늘 날짜 기준으로, 그 해당 년도의 1월1일부터 오늘까지 List<String> 만들기
		//말고!! 해당 날짜의 해당 월!!!! 그 해당 월로!! 너무 올래걸려서 수정!!!!
		List<String> DateStrList = new ArrayList<>();
		
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date today = new Date();
		String todayStr = simpleDateFormat.format(today);
		//log.info("오늘 날짜 스트링으로 보자 : "+todayStr);
		String todayYearStr = todayStr.substring(0,7);
		//log.info("오늘 날짜의 년도 스트링으로 보자 : "+todayYearStr);
		
		Date yearFirstdate = today;
		try {
			yearFirstdate = simpleDateFormat.parse(todayYearStr+"-01");
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//log.info("바꾼 올해 1월1일 보자 : "+yearFirstdate);
		
		Calendar cal1 = Calendar.getInstance();
		cal1.setTime(today);
		// 기준일로 설정. month의 경우 해당월수-1을 해줍니다.
//				cal1.set(2023,0,5);
//				cal1.setTime(test);
		
		Calendar cal2 = Calendar.getInstance();
		cal2.setTime(yearFirstdate);
		
		while(!cal2.after(cal1)) {
			Date caldate=cal2.getTime();
			cal2.add(Calendar.DATE, 1);
			DateStrList.add(simpleDateFormat.format(caldate));	
		}
		//log.info("계산 된건가?? : "+DateStrLisg);

		//2. 화면에 뿌릴 재고산출DTO 리스트 만들고, 전체 품목읽어와서, 전체품목DTO 리스트 만들기
		List<StockResultDTO> stockResultDTOList = new ArrayList<>();
		List<Material> materialList = materialRepository.findAll(Sort.by(Sort.Direction.ASC, "code"));
		List<MaterialDTO> materialDTOList = new ArrayList<>();
		for(Material material:materialList) {
			MaterialDTO materialDTO = materialEntityToDTO(material);
			materialDTOList.add(materialDTO);		
		}
		
		//3. 가져온 품목 리스트에서 각각 품목별로, 기간별 입고수량, 출고수량, 입고금액, 출고금액, 재고수량, 재고금액 계산해서 넣어주기
		for(MaterialDTO materialDTO:materialDTOList) {
			//품목별 계산을 위해, 재고산출DTO를 이용해서 리스트 이용한다.
			//품목별 기간별 각각의 입고수량,단가 가져와서 세팅한다.
			//log.info("현재 하는 품목코드!! : "+materialDTO.getCode());
			
			//3-1. 일단 BS0001에 대한 입고 6/1~6/5일 거 계산해보자
			//BS0001에 대해, 입고수량,단가,입고금액 -> 6/1~/6/5 계산을 위해서 재고산출DTO 리스트를 새로 만들어서 이용하쟈
			//현재는 BS0001의 날짜별로 입고수량,단가,입고금액 구하기
			List<StockResultDTO> byMaterialList = new ArrayList<>();
			
			for(String date : DateStrList) {
				//첫번째 날짜 6/1
				List<Object[]> miAmountAndunitPriceList = materialOutRepository.getDPOAmountAndUnitPriceByMaterialCodeAndMIDate(materialDTO.getCode(), date);
				//입고수량, 단가
				if(miAmountAndunitPriceList.size()>0) {
					Integer inAmount = 0;
					Integer unitPrice = 0;
					for(Object[] result:miAmountAndunitPriceList) {
						inAmount = inAmount + (int)(result[0]);
						unitPrice = unitPrice + (int)(result[1]);
					}
					Integer finalUnitPrice = unitPrice/miAmountAndunitPriceList.size();
					Integer inPrice = inAmount * finalUnitPrice;		
					StockResultDTO stockResultDTO = StockResultDTO.builder().inAmount(inAmount).inPrice(inPrice).dateForCalculate(date).build();
					byMaterialList.add(stockResultDTO);
				}else {
					StockResultDTO stockResultDTO = StockResultDTO.builder().inAmount(0).inPrice(0).dateForCalculate(date).build();
					byMaterialList.add(stockResultDTO);
				}
			}
			//log.info("만들어진 특정 품목 6/1~6/5 기간 동안 각각의 값들 보자 입고! : "+byMaterialList);
			
			//품목의 기간들 합쳐서! 최종 입고수량,단가,입고금액 만들어주기
			Integer calculInAmount = 0;
			Integer calaculInPrice = 0;
			for(StockResultDTO material:byMaterialList) {
				calculInAmount = calculInAmount + material.getInAmount();
				calaculInPrice = calaculInPrice + material.getInPrice();
			}
			//log.info("일단 기간 전체 합친 입고수량 : "+calculInAmount);
			//log.info("일단 기간 전체 합친 입고금액 : "+calaculInPrice);
			
			
			//3-2. 일단 BS0001에 대한 출고 6/1~6/5일 거 계산해보자
			//BS0001에 대해, 출고수량,단가,출고금액 -> 6/1~/6/5 계산을 위해서 재고산출DTO 리스트를 새로 만들어서 이용하쟈
			//현재는 BS0001의 날짜별로 출고수량,단가,출고금액 구하기
			List<StockResultDTO> byMaterialList2 = new ArrayList<>();
			
			for(String date : DateStrList) {
				//첫번째 날짜 6/1
				List<Object[]> miAmountAndunitPriceList = materialOutRepository.getMRPAmountAndUnitPriceByMaterialCodeAndMIDate(materialDTO.getCode(), date);
				//출고수량, 단가
				if(miAmountAndunitPriceList.size()>0) {
					Integer outAmount = 0;
					Integer unitPrice = 0;
					for(Object[] result:miAmountAndunitPriceList) {
						outAmount = outAmount + (int)(result[0]);
						unitPrice = unitPrice + (int)(result[1]);
					}
					Integer finalUnitPrice = unitPrice/miAmountAndunitPriceList.size();
					Integer outPrice = outAmount * finalUnitPrice;		
					StockResultDTO stockResultDTO = StockResultDTO.builder().outAmount(outAmount).outPrice(outPrice).dateForCalculate(date).build();
					byMaterialList2.add(stockResultDTO);
				}else {
					StockResultDTO stockResultDTO = StockResultDTO.builder().outAmount(0).outPrice(0).dateForCalculate(date).build();
					byMaterialList2.add(stockResultDTO);
				}
			}
			//log.info("만들어진 BS0001 6/1~6/5 기간 동안 각각의 값들 보자 출고! : "+byMaterialList2);
			
			//BS0001의 기간들 합쳐서! 최종 출고수량,단가,출고금액 만들어주기
			Integer calculOutAmount = 0;
			Integer calaculOutPrice = 0;
			for(StockResultDTO material:byMaterialList2) {
				calculOutAmount = calculOutAmount + material.getOutAmount();
				calaculOutPrice = calaculOutPrice + material.getOutPrice();
			}
			//log.info("일단 기간 전체 합친 출고수량 : "+calculOutAmount);
			//log.info("일단 기간 전체 합친 출고금액 : "+calaculOutPrice);
			
			//log.info("일단 기간 전체 합친 입고수량 : "+calculInAmount);
			//log.info("일단 기간 전체 합친 입고금액 : "+calaculInPrice);
			//위에서 계산한 총 기간의 입고수량, 입고금액, 출고수량, 출고금액 ==> 재고수량, 재고금액, 재고단가 구하기
			//재고수량 = 입고수량 - 출고수량
			Integer stockAmount = calculInAmount - calculOutAmount;
			if(calculInAmount - calculOutAmount<0) {
				stockAmount = 0;
			}
			//log.info("일단 기간 전체의 재고수량 : "+stockAmount);
			//재고금액 = 입고금액 - 출고금액 (재고수량이 0 이하 일경우 재고금액 0으로 만들기)
			Integer stockTotalPrice = 0; 
			if(stockAmount>0) {
				stockTotalPrice = calaculInPrice - calaculOutPrice; 
			}
			//log.info("일단 기간 전체의 재고금액 : "+stockTotalPrice);
			//재고단가 = 재고금액/재고수량 
			//재고수량이 0이면 재고단가 =0 으로 만들기
			Integer stockUnitPrice =0;
			if(stockAmount>0) {
				stockUnitPrice = stockTotalPrice/stockAmount;
			}
			//log.info("일단 기간 전체의 재고단가 : "+stockUnitPrice);
			
			//위에서 만든 모든 값들과 품목 정보 재고산출DTO에 넣고, 재고산출DTO 리스트에 넣어주기
			
			StockResultDTO stockResultDTO = StockResultDTO.builder().materialCode(materialDTO.getCode()).materialName(materialDTO.getName())
																	.largeCategoryCode(materialDTO.getLargeCategoryCode()).largeCategoryName(materialDTO.getLargeCategoryName())
																	.middleCategoryCode(materialDTO.getMiddleCategoryCode()).middleCategoryName(materialDTO.getMiddleCategoryName())
																	.inAmount(calculInAmount).inPrice(calaculInPrice).outAmount(calculOutAmount).outPrice(calaculOutPrice)
																	.stockAmount(stockAmount).stockUnitPrice(stockUnitPrice)
																	.stockTotalPrice(stockTotalPrice).build();
			//log.info("현재 "+materialDTO.getCode()+"로 만든 재고산출 DTO 보자 : "+stockResultDTO);
			stockResultDTOList.add(stockResultDTO);		
		}
		//log.info("최종 재고산출DTO 리스트 보자 : "+stockResultDTOList);		
		
		return stockResultDTOList;
	}

	@Override
	public List<StockResultDTO> getStockResultDTOListByPeriod(Date[] dateList) {
		//1. 화면에서 Date[]로 받아온거 List<String> 로 변환해주기
		List<String> dateStrList = new ArrayList<>();	
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		for(Date date:dateList) {
			dateStrList.add(simpleDateFormat.format(date));
		}
		//log.info("날짜 문자로 바뀐거 어디 보자 : "+dateStrList);
		
		//2. 화면에 뿌릴 재고산출DTO 리스트 만들고, 전체 품목읽어와서, 전체품목DTO 리스트 만들기
		List<StockResultDTO> stockResultDTOList = new ArrayList<>();
		List<Material> materialList = materialRepository.findAll(Sort.by(Sort.Direction.ASC, "code"));
		List<MaterialDTO> materialDTOList = new ArrayList<>();
		for(Material material:materialList) {
			MaterialDTO materialDTO = materialEntityToDTO(material);
			materialDTOList.add(materialDTO);		
		}
		
		//3. 가져온 품목 리스트에서 각각 품목별로, 기간별 입고수량, 출고수량, 입고금액, 출고금액, 재고수량, 재고금액 계산해서 넣어주기
		for(MaterialDTO materialDTO:materialDTOList) {
			//품목별 계산을 위해, 재고산출DTO를 이용해서 리스트 이용한다.
			//품목별 기간별 각각의 입고수량,단가 가져와서 세팅한다.
			//log.info("현재 하는 품목코드!! : "+materialDTO.getCode());
			
			//3-1. 일단 BS0001에 대한 입고 6/1~6/5일 거 계산해보자
			//BS0001에 대해, 입고수량,단가,입고금액 -> 6/1~/6/5 계산을 위해서 재고산출DTO 리스트를 새로 만들어서 이용하쟈
			//현재는 BS0001의 날짜별로 입고수량,단가,입고금액 구하기
			List<StockResultDTO> byMaterialList = new ArrayList<>();
			
			for(String date : dateStrList) {
				//첫번째 날짜 6/1
				List<Object[]> miAmountAndunitPriceList = materialOutRepository.getDPOAmountAndUnitPriceByMaterialCodeAndMIDate(materialDTO.getCode(), date);
				//입고수량, 단가
				if(miAmountAndunitPriceList.size()>0) {
					Integer inAmount = 0;
					Integer unitPrice = 0;
					for(Object[] result:miAmountAndunitPriceList) {
						inAmount = inAmount + (int)(result[0]);
						unitPrice = unitPrice + (int)(result[1]);
					}
					Integer finalUnitPrice = unitPrice/miAmountAndunitPriceList.size();
					Integer inPrice = inAmount * finalUnitPrice;		
					StockResultDTO stockResultDTO = StockResultDTO.builder().inAmount(inAmount).inPrice(inPrice).dateForCalculate(date).build();
					byMaterialList.add(stockResultDTO);
				}else {
					StockResultDTO stockResultDTO = StockResultDTO.builder().inAmount(0).inPrice(0).dateForCalculate(date).build();
					byMaterialList.add(stockResultDTO);
				}
			}
			//log.info("만들어진 특정 품목 6/1~6/5 기간 동안 각각의 값들 보자 입고! : "+byMaterialList);
			
			//품목의 기간들 합쳐서! 최종 입고수량,단가,입고금액 만들어주기
			Integer calculInAmount = 0;
			Integer calaculInPrice = 0;
			for(StockResultDTO material:byMaterialList) {
				calculInAmount = calculInAmount + material.getInAmount();
				calaculInPrice = calaculInPrice + material.getInPrice();
			}
			//log.info("일단 기간 전체 합친 입고수량 : "+calculInAmount);
			//log.info("일단 기간 전체 합친 입고금액 : "+calaculInPrice);
			
			
			//3-2. 일단 BS0001에 대한 출고 6/1~6/5일 거 계산해보자
			//BS0001에 대해, 출고수량,단가,출고금액 -> 6/1~/6/5 계산을 위해서 재고산출DTO 리스트를 새로 만들어서 이용하쟈
			//현재는 BS0001의 날짜별로 출고수량,단가,출고금액 구하기
			List<StockResultDTO> byMaterialList2 = new ArrayList<>();
			
			for(String date : dateStrList) {
				//첫번째 날짜 6/1
				List<Object[]> miAmountAndunitPriceList = materialOutRepository.getMRPAmountAndUnitPriceByMaterialCodeAndMIDate(materialDTO.getCode(), date);
				//출고수량, 단가
				if(miAmountAndunitPriceList.size()>0) {
					Integer outAmount = 0;
					Integer unitPrice = 0;
					for(Object[] result:miAmountAndunitPriceList) {
						outAmount = outAmount + (int)(result[0]);
						unitPrice = unitPrice + (int)(result[1]);
					}
					Integer finalUnitPrice = unitPrice/miAmountAndunitPriceList.size();
					Integer outPrice = outAmount * finalUnitPrice;		
					StockResultDTO stockResultDTO = StockResultDTO.builder().outAmount(outAmount).outPrice(outPrice).dateForCalculate(date).build();
					byMaterialList2.add(stockResultDTO);
				}else {
					StockResultDTO stockResultDTO = StockResultDTO.builder().outAmount(0).outPrice(0).dateForCalculate(date).build();
					byMaterialList2.add(stockResultDTO);
				}
			}
			//log.info("만들어진 BS0001 6/1~6/5 기간 동안 각각의 값들 보자 출고! : "+byMaterialList2);
			
			//BS0001의 기간들 합쳐서! 최종 출고수량,단가,출고금액 만들어주기
			Integer calculOutAmount = 0;
			Integer calaculOutPrice = 0;
			for(StockResultDTO material:byMaterialList2) {
				calculOutAmount = calculOutAmount + material.getOutAmount();
				calaculOutPrice = calaculOutPrice + material.getOutPrice();
			}
			//log.info("일단 기간 전체 합친 출고수량 : "+calculOutAmount);
			//log.info("일단 기간 전체 합친 출고금액 : "+calaculOutPrice);
			
			//log.info("일단 기간 전체 합친 입고수량 : "+calculInAmount);
			//log.info("일단 기간 전체 합친 입고금액 : "+calaculInPrice);
			//위에서 계산한 총 기간의 입고수량, 입고금액, 출고수량, 출고금액 ==> 재고수량, 재고금액, 재고단가 구하기
			//재고수량 = 입고수량 - 출고수량
			Integer stockAmount = calculInAmount - calculOutAmount;
			if(calculInAmount - calculOutAmount<0) {
				stockAmount = 0;
			}
			//log.info("일단 기간 전체의 재고수량 : "+stockAmount);
			//재고금액 = 입고금액 - 출고금액 (재고수량이 0 이하 일경우 재고금액 0으로 만들기)
			Integer stockTotalPrice = 0; 
			if(stockAmount>0) {
				stockTotalPrice = calaculInPrice - calaculOutPrice; 
			}
			//log.info("일단 기간 전체의 재고금액 : "+stockTotalPrice);
			//재고단가 = 재고금액/재고수량 
			//재고수량이 0이면 재고단가 =0 으로 만들기
			Integer stockUnitPrice =0;
			if(stockAmount>0) {
				stockUnitPrice = stockTotalPrice/stockAmount;
			}
			//log.info("일단 기간 전체의 재고단가 : "+stockUnitPrice);
			
			//위에서 만든 모든 값들과 품목 정보 재고산출DTO에 넣고, 재고산출DTO 리스트에 넣어주기
			
			StockResultDTO stockResultDTO = StockResultDTO.builder().materialCode(materialDTO.getCode()).materialName(materialDTO.getName())
																	.largeCategoryCode(materialDTO.getLargeCategoryCode()).largeCategoryName(materialDTO.getLargeCategoryName())
																	.middleCategoryCode(materialDTO.getMiddleCategoryCode()).middleCategoryName(materialDTO.getMiddleCategoryName())
																	.inAmount(calculInAmount).inPrice(calaculInPrice).outAmount(calculOutAmount).outPrice(calaculOutPrice)
																	.stockAmount(stockAmount).stockUnitPrice(stockUnitPrice)
																	.stockTotalPrice(stockTotalPrice).build();
			//log.info("현재 "+materialDTO.getCode()+"로 만든 재고산출 DTO 보자 : "+stockResultDTO);
			stockResultDTOList.add(stockResultDTO);		
		}
		//log.info("최종 재고산출DTO 리스트 보자 : "+stockResultDTOList);		
		
		return stockResultDTOList;
	}

}
