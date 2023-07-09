package com.passioncode.procurementsystem.controller;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import com.passioncode.procurementsystem.dto.LargeCategoryDTO;
import com.passioncode.procurementsystem.dto.MaterialOutDTO;
import com.passioncode.procurementsystem.dto.MiddleCategoryDTO;
import com.passioncode.procurementsystem.dto.StockResultDTO;
import com.passioncode.procurementsystem.entity.LargeCategory;
import com.passioncode.procurementsystem.entity.MiddleCategory;
import com.passioncode.procurementsystem.entity.ProcurementPlan;
import com.passioncode.procurementsystem.service.LargeCategoryService;
import com.passioncode.procurementsystem.service.MaterialOutService;
import com.passioncode.procurementsystem.service.MiddleCategoryService;
import com.passioncode.procurementsystem.service.ProcurementPlanService;
import com.passioncode.procurementsystem.service.StockReportService;
import com.passioncode.procurementsystem.service.StockResultService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Controller
@RequiredArgsConstructor
@RequestMapping("procurement4")
@Log4j2
public class PS4Controller {
	
	private final MaterialOutService materialOutService;
	private final ProcurementPlanService procurementPlanService;
	private final StockResultService stockResultService;
	private final LargeCategoryService largeCategoryService;
	private final MiddleCategoryService middleCategoryService;
	private final StockReportService stockReportService;
	
	@GetMapping("materialOut")
	public void materialOut(Model model, MaterialOutDTO materialOutDTO) {
		List<MaterialOutDTO> materialOutDTOList = materialOutService.getDTOList();

		log.info("materialOutDTOlist.............");

		model.addAttribute("DTOList", materialOutDTOList);
	}
	
	@PostMapping("materialOutRegister")
	public String materialOutRegister(MaterialOutDTO materialOutDTO, HttpServletRequest request) {
		log.info("materialOutDTO >>> " + materialOutDTO);
		
		Integer detailPurchaseOrderCodeStr= Integer.parseInt(request.getParameter("dpoCode"));
		String status= request.getParameter("status");
		
		log.info("js로 만들어 보낸 form 데이터 detailPurchaseOrderCode잘 받아오나 >>> " + detailPurchaseOrderCodeStr);
		log.info("js로 만들어 보낸 form 데이터 status 잘 받아오나 >>> " + status);
		
		ProcurementPlan pp= procurementPlanService.getPpByDetailPurchaseOrder(detailPurchaseOrderCodeStr);
		Integer mrpCode= pp.getMrp().getCode();
		
		materialOutDTO= MaterialOutDTO.builder().mrpCode(mrpCode).outStatus(status).build();
		materialOutService.register(materialOutDTO);
		
		return "redirect:/procurement4/materialOut";
	}
	
	@GetMapping("stockResult")
	public void stockResult(Model model) {
		List<StockResultDTO> stockResultDTOList = stockResultService.getStockResultDTOList();
		
		model.addAttribute("DTOList", stockResultDTOList);
	}
	
	
	@GetMapping("stockReport")
	public void stockReport(Model model) {
		
		//대분류 셀렉트 정보는 모든 정보 다 보내주기
		List<LargeCategoryDTO> LargeCategoryDTOList = largeCategoryService.getDTOList();
		model.addAttribute("LargeCategoryDTOList", LargeCategoryDTOList );
		
		//중분류 셀렉트 정보는 대분류 리스트에서 처음보여주게 되는거에 해당되는 중분류만 보여주기
		//나머지 중분류는 화면에서 바뀔때마다 불러오기 때문에!
//		LargeCategory largeCategory = middleCategoryService.getLargeCategory(LargeCategoryDTOList.get(0).getCode());
//		List<MiddleCategory> MiddleCategoryListByLC1 = middleCategoryService.getMiddleCategoryByLargeCategory(largeCategory);
//		
//		List<MiddleCategoryDTO> MiddleCategoryDTOListByLC1 = new ArrayList<>();
//		for(MiddleCategory middleCategory:MiddleCategoryListByLC1) {
//			MiddleCategoryDTOListByLC1.add(middleCategoryService.entityToDTO(middleCategory));
//		}		
//		model.addAttribute("MiddleCategoryDTOList", MiddleCategoryDTOListByLC1);
		
		List<MiddleCategoryDTO> middleCategoryDTOList = middleCategoryService.getDTOList();
		model.addAttribute("middleCategoryDTOList",middleCategoryDTOList);
		
		
		//오늘 날짜 기준으로, 그 해당년도와 해달 월의 1일 과 오늘날짜 구하기
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date today = new Date();
		String todayStr = simpleDateFormat.format(today);
//		log.info("오늘 날짜 스트링으로 보자 : "+todayStr);
		String todayYearMonthStr = todayStr.substring(0,7);
		//log.info("오늘 날짜의 년도+월 스트링으로 보자 : "+todayYearMonthStr);
		
		Date yearMonthFirstdate = today;
		try {
			yearMonthFirstdate = simpleDateFormat.parse(todayYearMonthStr+"-01 00:00:00");
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//log.info("바꾼 올해+해당월 1일 보자 : "+yearMonthFirstdate);
		String yearMonthFirstdateStr = simpleDateFormat.format(yearMonthFirstdate);
//		log.info("올해+해당월 1일 문자 잘 만들어졌나 : "+yearMonthFirstdateStr);
		
//		List<Date> DateList = stockReportService.getDateList("2023-06-01","2023-06-15");
//		List<String> DateStrList = stockReportService.getDateStrList("2023-06-01","2023-06-15");
		List<Date> DateList = stockReportService.getDateList(yearMonthFirstdateStr,todayStr);
		List<String> DateStrList = stockReportService.getDateStrList(yearMonthFirstdateStr,todayStr);
		
		model.addAttribute("DateList",DateList);
		model.addAttribute("DateStrList",DateStrList);
		
		//전체로 받아온 대분류 전체 리스트를 대분류 각각의 리스트에 담아서 각각 이름의 리스트로 보내기
//		List<StockResultDTO> stockResultDTOList = stockReportService.getStockReportForLCListByPeriod("2023-06-01","2023-06-15");
		List<StockResultDTO> stockResultDTOLCList = stockReportService.getStockReportForLCList();
		
		model.addAttribute("stockResultDTOLCList",stockResultDTOLCList);
//		log.info("만들어진 재고금액리스트 보자 (대분류) : "+stockResultDTOLCList);
//		log.info("만들어진 재고금액리스트 사이즈 길이 보자 (대분류) : "+stockResultDTOLCList.size());
		
		//재고금액의 최대값 구하기 (대분류)
		//재고금액을 List<Integer>로 만들어서 받아서, 여기서 최대값을 뽑기
		List<Integer> stockTotalPriceLCList = new ArrayList<>();
		for(StockResultDTO stockResultDTO:stockResultDTOLCList) {
			stockTotalPriceLCList.add(stockResultDTO.getStockTotalPrice());
		}
		Integer maxStockTotalPriceLC = Collections.max(stockTotalPriceLCList);
//		log.info("재고금액 최대값 확인해보자 : "+maxStockTotalPriceLC);;
		model.addAttribute("maxStockTotalPriceLC",maxStockTotalPriceLC);
		
		//중분류 버전!!!!
		List<StockResultDTO> stockResultDTOMCList = stockReportService.getStockReportForMCList();
		model.addAttribute("stockResultDTOMCList",stockResultDTOMCList);
		log.info("만들어진 재고금액리스트 보자 (중분류) : "+stockResultDTOMCList);
		log.info("만들어진 재고금액리스트 사이즈 길이 보자 (중분류) : "+stockResultDTOMCList.size());
		
		//재고금액의 최대값 구하기 (중분류)
		//재고금액을 List<Integer>로 만들어서 받아서, 여기서 최대값을 뽑기
		List<Integer> stockTotalPriceMCList = new ArrayList<>();
		for(StockResultDTO stockResultDTO:stockResultDTOMCList) {
			stockTotalPriceMCList.add(stockResultDTO.getStockTotalPrice());
		}
		Integer maxStockTotalPriceMC = Collections.max(stockTotalPriceMCList);
		log.info("재고금액 최대값 확인해보자 : "+maxStockTotalPriceMC);
		model.addAttribute("maxStockTotalPriceMC",maxStockTotalPriceMC);
		
		
	}

}
