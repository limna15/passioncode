package com.passioncode.procurementsystem.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.passioncode.procurementsystem.dto.StockResultDTO;
import com.passioncode.procurementsystem.service.ContractService;
import com.passioncode.procurementsystem.service.LargeCategoryService;
import com.passioncode.procurementsystem.service.MaterialInService;
import com.passioncode.procurementsystem.service.MaterialOutService;
import com.passioncode.procurementsystem.service.MaterialService;
import com.passioncode.procurementsystem.service.MiddleCategoryService;
import com.passioncode.procurementsystem.service.ProcurementPlanService;
import com.passioncode.procurementsystem.service.StockResultService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@RestController
@RequiredArgsConstructor
@RequestMapping("procurement4")
@Log4j2
public class PS4RestController {
	
	private final MaterialService materialService;
	private final LargeCategoryService largeCategoryService;
	private final MiddleCategoryService middleCategoryService;
	private final ContractService contractService;
	private final ProcurementPlanService procurementPlanService; 
	private final MaterialInService materialInService;
	private final MaterialOutService materialOutService;
	private final StockResultService stockResultService;
	
	@PostMapping(value="stockResult", produces=MediaType.APPLICATION_JSON_VALUE)
	public List<StockResultDTO> stockResult2(@RequestBody Date[] dateList) {
		//보낸 날짜 리스트 기간에서! 첫날짜와 끝날짜만 만든 메소드에 넣어주자
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String startDateStr = simpleDateFormat.format(dateList[0]);
		String endDateStr = simpleDateFormat.format(dateList[dateList.length-1]);
		
		List<StockResultDTO> stockResultDTOList = stockResultService.getStockResultDTOListByPeriod(startDateStr,endDateStr);
		
		return stockResultDTOList;
	}
}
