package com.passioncode.procurementsystem.repository;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import com.passioncode.procurementsystem.dto.DrawingFileDTO;
import com.passioncode.procurementsystem.dto.MaterialDTO;
import com.passioncode.procurementsystem.dto.MaterialOutDTO;
import com.passioncode.procurementsystem.dto.MiddleCategoryDTO;
import com.passioncode.procurementsystem.dto.StockResultDTO;
import com.passioncode.procurementsystem.entity.MRP;
import com.passioncode.procurementsystem.entity.Material;
import com.passioncode.procurementsystem.entity.MaterialIn;
import com.passioncode.procurementsystem.entity.MaterialOut;
import com.passioncode.procurementsystem.entity.MiddleCategory;
import com.passioncode.procurementsystem.entity.ProcurementPlan;
import lombok.extern.log4j.Log4j2;

@Log4j2
@SpringBootTest
public class MaterialOutRepositoryTests {
	
	@Autowired
	MaterialOutRepository materialOutRepository;
	
	@Autowired
	ProcurementPlanRepository procurementPlanRepository;
	
	@Autowired
	MaterialInRepository materialInRepository;
	
	@Autowired
	MRPRepository mrpRepository;
	
	@Autowired
	MaterialRepository materialRepository;
	
	@Autowired
	MiddleCategoryRepository middleCategoryRepository;
	
	@Autowired
	LargeCategoryRepository largeCategoryRepository;
	
	@Transactional
	@Test
	public void findByMRPTest() {
		MRP mrp = mrpRepository.findById(1).get();
		log.info("출고 찍히는거 보자 : "+materialOutRepository.findByMrp(mrp));
	}
	
	@Test
	public void InsertTest() {
		//출고코드, 출고상태, 출고일, 자재소요계획코드(외래키)(MRP)
		MRP mrp = mrpRepository.findById(1).get();
		
		MaterialOut materialOut = MaterialOut.builder().status(1).mrp(mrp).build();
		
		materialOutRepository.save(materialOut);
		
	}
	
	//발주코드에 문자를 넣어서 보내기
	public String codeStr(Integer num1) {
		String pNum = String.format("%05d", num1);
		pNum = "DPO"+pNum;
		
		return pNum;	
	}
	
	@Transactional
	@Test
	public void materialOutToDTO() {
		MRP mrp = mrpRepository.findById(1).get();
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
		
		log.info(materialOutDTO);
	}
	
	@Transactional
	@Test
	public void DTOListTest() {
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
		log.info("moList 한번 보자 >>> " + moList + ", 사이즈는 >>> " + moList.size());
		
		for(int i=0; i<ppList.size(); i++) {
		log.info("ppList 완료일 한번 보자 >>> " + ppList.get(i).getCompletionDate());
			//세부구매발주서 등록 +  완료일(입고일) 등록 -> 출고 리스트(출고 상태 0(버튼))
			if(ppList.get(i).getDetailPurchaseOrder() != null && ppList.get(i).getCompletionDate() != null) {
				//출고 엔티티에 존재 O
				if(materialOutRepository.existsByMrp(ppList.get(i).getMrp())){
					moDTO= MaterialOutDTO.builder().dpoCode(ppList.get(i).getDetailPurchaseOrder().getCode())
							.mrpDate(ppList.get(i).getMrp().getDate()).materialCode(ppList.get(i).getMrp().getMaterial().getCode())
							.materialName(ppList.get(i).getMrp().getMaterial().getName())
							.process(ppList.get(i).getMrp().getProcess()).mrpAmount(ppList.get(i).getMrp().getAmount()).outStatus("1").build();			
					moDTOList.add(moDTO);		
				//출고 엔티티에 존재 X	
				}else {
					moDTO= MaterialOutDTO.builder().dpoCode(ppList.get(i).getDetailPurchaseOrder().getCode())
							.mrpDate(ppList.get(i).getMrp().getDate()).materialCode(ppList.get(i).getMrp().getMaterial().getCode())
							.materialName(ppList.get(i).getMrp().getMaterial().getName())
							.process(ppList.get(i).getMrp().getProcess()).mrpAmount(ppList.get(i).getMrp().getAmount()).outStatus("0").build();			
					moDTOList.add(moDTO);			
				}
				
			}
		}
		log.info("moDTOList >>> " + moDTOList + ", 사이즈는 >>> " + moDTOList.size());	
	}
	
	@Transactional
	@Test
	public void sortDTOListTest() {		
		//출고리스트에는 보여주면 안되는 입고상태 취소된 miList
		List<MaterialIn> miList= materialInRepository.findAll();
		List<MaterialIn> cancleMiList= new ArrayList<>();
		for(int i=0; i<miList.size(); i++) {
			if(miList.get(i).getStatus() == false){
				cancleMiList.add(miList.get(i));
				log.info("입고상태가 취소인 품목들의 세부구매발주서 정보 >>> " + miList.get(i).getDetailPurchaseOrder());
			}
		}
		
		List<ProcurementPlan> ppList= procurementPlanRepository.findAll();
		
		log.info("ppList 한번 보자 >>> " + ppList + ", 사이즈는 >>> " + ppList.size());
				
		List<MaterialOutDTO> moDTOList= new ArrayList<>();
		List<MaterialOutDTO> notNullMoDTOList= new ArrayList<>();
		List<MaterialOutDTO> nullMoDTOList= new ArrayList<>();
		MaterialOutDTO moDTO= null;
		List<MaterialOut> moList= materialOutRepository.findAll();
		log.info("moList 한번 보자 >>> " + moList + ", 사이즈는 >>> " + moList.size());
		
		for(int i=0; i<ppList.size(); i++) {
		//log.info("ppList 완료일 한번 보자 >>> " + ppList.get(i).getCompletionDate());
			//세부구매발주서 등록 +  완료일(입고일) 등록 -> 출고 리스트(출고 상태 (버튼))
			if(ppList.get(i).getDetailPurchaseOrder() != null && ppList.get(i).getCompletionDate() != null) {
				//출고 엔티티에 존재 O
				if(materialOutRepository.existsByMrp(ppList.get(i).getMrp())){		
					if((ppList.get(i).getDetailPurchaseOrder().getCode()== miList.get(i).getDetailPurchaseOrder().getCode()) && miList.get(i).getStatus() == false) {
						moDTO=null;
						log.info("입고 상태가 취소되었어용 >>> " + moDTO);
						log.info("입고 상태 취소 + 상태보기 >>> " + i + "번째, " + miList.get(i).getStatus());
					}else {
						//추가해야될 내용 -> 입고상태가 완료(1)이면 출고DTO에 넣어주고, 취소(0)이면 리스트에는 안보여주고 DB에 출고상태 0, 출고일 null로 등록만함
						//입고상태 취소된 세부구매발주서와 자재입고에 등록된 세부구매발주서 번호가 같으면
						moDTO= MaterialOutDTO.builder().dpoCode(ppList.get(i).getDetailPurchaseOrder().getCode())
								.mrpDate(ppList.get(i).getMrp().getDate()).materialCode(ppList.get(i).getMrp().getMaterial().getCode())
								.materialName(ppList.get(i).getMrp().getMaterial().getName())
								.process(ppList.get(i).getMrp().getProcess()).mrpAmount(ppList.get(i).getMrp().getAmount()).outStatus("1").build();			
						notNullMoDTOList.add(moDTO);		
						log.info("입고 상태가 완료 DTO >>> " + moDTO);
						log.info("입고 상태 완료 + 상태보기 >>> " + i + "번째, " + miList.get(i).getStatus());
					}	
				//출고 엔티티에 존재 X
				}else {
					moDTO= MaterialOutDTO.builder().dpoCode(ppList.get(i).getDetailPurchaseOrder().getCode())
							.mrpDate(ppList.get(i).getMrp().getDate()).materialCode(ppList.get(i).getMrp().getMaterial().getCode())
							.materialName(ppList.get(i).getMrp().getMaterial().getName())
							.process(ppList.get(i).getMrp().getProcess()).mrpAmount(ppList.get(i).getMrp().getAmount()).outStatus("0").build();			
					nullMoDTOList.add(moDTO);
					log.info("입고가 아직 아예 안된 아이 >>> "  + i + "번째, " + moDTO);
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
		log.info("moDTOList >>> " + moDTOList + ", 사이즈는 >>> " + moDTOList.size());	
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
	
	//////////////BS0001 품목 재고산출 해보자 6/1 ~ 6/5일 까지로 해보자!
//	@Transactional
//	@Test
//	public void stockResultTest() {
//		//BS0001 품목 재고산출 해보자 6/1 ~ 6/5일 까지로 해보자!
//		
//		//1. 테스트를 위한 Date[] 배열 만들기 (6/1~6/5로 테스트)
//		Date[] dateList = new Date[5];
//		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
//		try {
//			Date date = simpleDateFormat.parse("2023-06-01");
//			dateList[0] = date;
//			date = simpleDateFormat.parse("2023-06-02");
//			dateList[1] = date;
//			date = simpleDateFormat.parse("2023-06-03");
//			dateList[2] = date;
//			date = simpleDateFormat.parse("2023-06-04");
//			dateList[3] = date;
//			date = simpleDateFormat.parse("2023-06-05");
//			dateList[4] = date;
//		} catch (ParseException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		for(Date date:dateList) {
//			log.info("어디 제대로 날짜 됬나보자 : "+date);
//		}
//		//2. 화면에서 Date[]로 받아온거 List<String> 로 변환해주기
//		List<String> dateStrList = new ArrayList<>();	
//		for(Date date:dateList) {
//			dateStrList.add(simpleDateFormat.format(date));
//		}
//		log.info("날짜 문자로 바뀐거 어디 보자 : "+dateStrList);
//		
//		//3. 일단 BS0001에 대한 입고 6/1~6/5일 거 계산해보자
//		//BS0001에 대해, 입고수량,단가,입고금액 -> 6/1~/6/5 계산을 위해서 재고산출DTO 리스트를 새로 만들어서 이용하쟈
//		//현재는 BS0001의 날짜별로 입고수량,단가,입고금액 구하기
//		List<StockResultDTO> byMaterialList = new ArrayList<>();
//		
//		for(String date : dateStrList) {
//			//첫번째 날짜 6/1
//			List<Object[]> miAmountAndunitPriceList = materialOutRepository.getDPOAmountAndUnitPriceByMaterialCodeAndMIDate("BS0001", date);
//			//입고수량, 단가
//			if(miAmountAndunitPriceList.size()>0) {
//				Integer inAmount = 0;
//				Integer unitPrice = 0;
//				for(Object[] result:miAmountAndunitPriceList) {
//					inAmount = inAmount + (int)(result[0]);
//					unitPrice = unitPrice + (int)(result[1]);
//				}
//				Integer finalUnitPrice = unitPrice/miAmountAndunitPriceList.size();
//				Integer inPrice = inAmount * finalUnitPrice;		
//				StockResultDTO stockResultDTO = StockResultDTO.builder().inAmount(inAmount).inPrice(inPrice).dateForCalculate(date).build();
//				byMaterialList.add(stockResultDTO);
//			}else {
//				StockResultDTO stockResultDTO = StockResultDTO.builder().inAmount(0).inPrice(0).dateForCalculate(date).build();
//				byMaterialList.add(stockResultDTO);
//			}
//		}
//		log.info("만들어진 BS0001 6/1~6/5 기간 동안 각각의 값들 보자 입고! : "+byMaterialList);
//		
//		//BS0001의 기간들 합쳐서! 최종 입고수량,단가,입고금액 만들어주기
//		Integer calculInAmount = 0;
//		Integer calaculInPrice = 0;
//		for(StockResultDTO material:byMaterialList) {
//			calculInAmount = calculInAmount + material.getInAmount();
//			calaculInPrice = calaculInPrice + material.getInPrice();
//		}
//		log.info("일단 기간 전체 합친 입고수량 : "+calculInAmount);
//		log.info("일단 기간 전체 합친 입고금액 : "+calaculInPrice);
//		
//		
//		//4. 일단 BS0001에 대한 출고 6/1~6/5일 거 계산해보자
//		//BS0001에 대해, 출고수량,단가,출고금액 -> 6/1~/6/5 계산을 위해서 재고산출DTO 리스트를 새로 만들어서 이용하쟈
//		//현재는 BS0001의 날짜별로 출고수량,단가,출고금액 구하기
//		List<StockResultDTO> byMaterialList2 = new ArrayList<>();
//		
//		for(String date : dateStrList) {
//			//첫번째 날짜 6/1
//			List<Object[]> miAmountAndunitPriceList = materialOutRepository.getMRPAmountAndUnitPriceByMaterialCodeAndMIDate("BS0001", date);
//			//출고수량, 단가
//			if(miAmountAndunitPriceList.size()>0) {
//				Integer outAmount = 0;
//				Integer unitPrice = 0;
//				for(Object[] result:miAmountAndunitPriceList) {
//					outAmount = outAmount + (int)(result[0]);
//					unitPrice = unitPrice + (int)(result[1]);
//				}
//				Integer finalUnitPrice = unitPrice/miAmountAndunitPriceList.size();
//				Integer outPrice = outAmount * finalUnitPrice;		
//				StockResultDTO stockResultDTO = StockResultDTO.builder().outAmount(outAmount).outPrice(outPrice).dateForCalculate(date).build();
//				byMaterialList2.add(stockResultDTO);
//			}else {
//				StockResultDTO stockResultDTO = StockResultDTO.builder().outAmount(0).outPrice(0).dateForCalculate(date).build();
//				byMaterialList2.add(stockResultDTO);
//			}
//		}
//		log.info("만들어진 BS0001 6/1~6/5 기간 동안 각각의 값들 보자 출고! : "+byMaterialList2);
//		
//		//BS0001의 기간들 합쳐서! 최종 출고수량,단가,출고금액 만들어주기
//		Integer calculOutAmount = 0;
//		Integer calaculOutPrice = 0;
//		for(StockResultDTO material:byMaterialList2) {
//			calculOutAmount = calculOutAmount + material.getOutAmount();
//			calaculOutPrice = calaculOutPrice + material.getOutPrice();
//		}
//		log.info("일단 기간 전체 합친 출고수량 : "+calculOutAmount);
//		log.info("일단 기간 전체 합친 출고금액 : "+calaculOutPrice);
//		
//		log.info("일단 기간 전체 합친 입고수량 : "+calculInAmount);
//		log.info("일단 기간 전체 합친 입고금액 : "+calaculInPrice);
//		//위에서 계산한 총 기간의 입고수량, 입고금액, 출고수량, 출고금액 ==> 재고수량, 재고금액, 재고단가 구하기
//		//재고수량 = 입고수량 - 출고수량
//		Integer stockAmount = calculInAmount - calculOutAmount;
//		log.info("일단 기간 전체의 재고수량 : "+stockAmount);
//		//재고금액 = 입고금액 - 출고금액 (재고수량이 0 이하 일경우 재고금액 0으로 만들기)
//		Integer stockTotalPrice = 0; 
//		if(stockAmount>0) {
//			stockTotalPrice = calaculInPrice - calaculOutPrice; 
//		}
//		log.info("일단 기간 전체의 재고금액 : "+stockTotalPrice);
//		//재고단가 = 재고금액/재고수량 
//		//재고수량이 0이면 재고단가 =0 으로 만들기
//		Integer stockUnitPrice =0;
//		if(stockAmount>0) {
//			stockUnitPrice = stockTotalPrice/stockAmount;
//		}
//		log.info("일단 기간 전체의 재고단가 : "+stockUnitPrice);
//		
//		//위에서 만든 모든 값들과 품목 정보 재고산출DTO에 넣고, 재고산출DTO 리스트에 넣어주기
//		List<StockResultDTO> stockResultDTOList = new ArrayList<>();
//		Material material = materialRepository.findById("BS0001").get();
//		MaterialDTO materialDTO = materialEntityToDTO(material);
//		StockResultDTO stockResultDTO = StockResultDTO.builder().materialCode("BS0001").materialName(materialDTO.getCode()).materialName(materialDTO.getName())
//																.largeCategoryCode(materialDTO.getLargeCategoryCode()).largeCategoryName(materialDTO.getLargeCategoryName())
//																.middleCategoryCode(materialDTO.getMiddleCategoryCode()).middleCategoryName(materialDTO.getMiddleCategoryName())
//																.inAmount(calculInAmount).inPrice(calaculInPrice).outAmount(calculOutAmount).outPrice(calaculOutPrice)
//																.stockAmount(stockAmount).stockUnitPrice(stockUnitPrice)
//																.stockTotalPrice(stockTotalPrice).build();
//		stockResultDTOList.add(stockResultDTO);		
//		
//		log.info("어디 재고산출DTO 리스트 보자 : "+stockResultDTOList);		
//				
//		
////		List<Object[]> test= materialOutRepository.getDPOAmountByMaterialCodeAndMIDate("BS0001", "2023-06-03");
////		List<Object> test= materialOutRepository.getDPOAmountByMaterialCodeAndMIDate("CG0001", "2023-06-28");
//		
////		log.info("테스트 어디 보자 : "+test);
////		Integer MIAmount = 0;
////		for(Object[] result:test) {
////			MIAmount = MIAmount+(int)(result[0]);
////		}
////		log.info("최종 입고량 보자 : "+MIAmount);
//	}
	
	@Test
	public void calculate() {
		Integer num1 = 0;
		Integer num2 = 0;
		Integer num3 = num2/num1;
		log.info("나누기 테스트 : "+num3);
	}
	
	@Test
	public void todayDateTest() {
		//오늘 날짜 기준으로, 그 해당 년도의 1월1일부터 오늘까지 Date[] 만들기
		//1. 일단 List로 Date 만들기
		List<Date> DateList = new ArrayList<>();
		List<String> DateStrLisg = new ArrayList<>();
		
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date today = new Date();
		String todayStr = simpleDateFormat.format(today);
		log.info("오늘 날짜 스트링으로 보자 : "+todayStr);
		String todayYearStr = todayStr.substring(0,4);
		log.info("오늘 날짜의 년도 스트링으로 보자 : "+todayYearStr);
		
		Date yearFirstdate = today;
		try {
			yearFirstdate = simpleDateFormat.parse(todayYearStr+"-01-01");
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		log.info("바꾼 올해 1월1일 보자 : "+yearFirstdate);
		
		
		Calendar cal1 = Calendar.getInstance();
		cal1.setTime(today);
		// 기준일로 설정. month의 경우 해당월수-1을 해줍니다.
//		cal1.set(2023,0,5);
//		cal1.setTime(test);
		
		Calendar cal2 = Calendar.getInstance();
		cal2.setTime(yearFirstdate);
		
		while(!cal2.after(cal1)) {
			Date caldate=cal2.getTime();
			cal2.add(Calendar.DATE, 1);
			DateList.add(caldate);
			DateStrLisg.add(simpleDateFormat.format(caldate));	
		}
		log.info("계산 된건가?? : "+DateList);
		log.info("계산 된건가?? : "+DateStrLisg);
	}
	
	
	@Test
	public void dateCalculeate() {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date today = new Date();
		String todayStr = simpleDateFormat.format(today);
		log.info("오늘 날짜 스트링으로 보자 : "+todayStr);
		String todayYearStr = todayStr.substring(0,7);
		log.info("오늘 날짜의 년도 스트링으로 보자 : "+todayYearStr);
	}
	
	@Test
	public void reportListTest() {
		//1. 오늘 날짜 기준으로, 그 해당년도와 해달 월의 1일 과 오늘날짜 구하기
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("yyyy-MM-dd");
		Date today = new Date();
//		String todayStr = simpleDateFormat2.format(today)+" 00:00:00";
		String todayStr = simpleDateFormat.format(today);
		log.info("오늘 날짜 스트링으로 보자1 : "+todayStr);
//		log.info("오늘 날짜 스트링으로 보자2 : "+todayStr+" 00:00:00");
		String todayYearMonthStr = todayStr.substring(0,7);
		log.info("오늘 날짜의 년도+월 스트링으로 보자 : "+todayYearMonthStr);
		
		Date yearMonthFirstdate = today;
		try {
			yearMonthFirstdate = simpleDateFormat.parse(todayYearMonthStr+"-01 00:00:00");
			log.info("오늘 날짜 년도와 월 날짜 변환 됐나 보자 : "+yearMonthFirstdate);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		log.info("바꾼 올해 해당월 1일 보자 : "+yearFirstdate);
		String yearMonthFirstdateStr = simpleDateFormat.format(yearMonthFirstdate);
		log.info("올해 1월1일 문자 잘 만들어졌나 : "+yearMonthFirstdateStr);
		
		//2. 재고금액목록 받아온거 재고산출DTO 리스트에 넣어주기
		List<Object[]> calculStockReport = materialOutRepository.getCalculStockReportForLC(yearMonthFirstdateStr, todayStr);
		log.info("이건 잘 가져오는건가 : "+calculStockReport);
		
		List<StockResultDTO> stockResultDTOList = new ArrayList<>();
		
		for(Object[] result:calculStockReport) {
			StockResultDTO stockResultDTO = StockResultDTO.builder().dateForCalculate(String.valueOf(result[0])+" 00:00:00")
																	.largeCategoryCode(String.valueOf(result[1])).largeCategoryName(String.valueOf(result[2]))
																	.stockTotalPrice(Integer.parseInt(String.valueOf(result[3]))).build();
			stockResultDTOList.add(stockResultDTO);
		}
		log.info("만든 DTO리스트 보자 : "+stockResultDTOList);
	}
	
	
	
	
	
	
	
	
}
