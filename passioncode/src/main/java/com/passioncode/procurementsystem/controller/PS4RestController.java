package com.passioncode.procurementsystem.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.passioncode.procurementsystem.dto.StockReportDTO;
import com.passioncode.procurementsystem.dto.StockResultDTO;
import com.passioncode.procurementsystem.service.StockReportService;
import com.passioncode.procurementsystem.service.StockResultService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@RestController
@RequiredArgsConstructor
@RequestMapping("procurement4")
@Log4j2
public class PS4RestController {
	
	private final StockResultService stockResultService;
	private final StockReportService stockReportService;
	
	/**
	 * 재고산출 화면에서, 기간검색에서 받은 기간리스트를 이용해서, 재고산출해주기
	 * @param dateList
	 * @return
	 */
	@PostMapping(value="stockResult", produces=MediaType.APPLICATION_JSON_VALUE)
	public List<StockResultDTO> stockResult(@RequestBody Date[] dateList) {
		//보낸 날짜 리스트 기간에서! 첫날짜와 끝날짜만 만든 메소드에 넣어주자
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String startDateStr = simpleDateFormat.format(dateList[0]);
		String endDateStr = simpleDateFormat.format(dateList[dateList.length-1]);
		
		List<StockResultDTO> stockResultDTOList = stockResultService.getStockResultDTOListByPeriod(startDateStr,endDateStr);
		
		return stockResultDTOList;
	}
	
	/**
	 * 재고금액 화면에서, 기간검색에서 받은 기간리스트를 이용해서 대분류 재고금액 계산해주기
	 * @param dateList
	 * @return
	 */
	@PostMapping(value="stockReportForLC", produces=MediaType.APPLICATION_JSON_VALUE)
	public List<StockReportDTO> stockReportForLC(@RequestBody Date[] dateList) {
		//보낸 날짜 리스트 기간에서! 첫날짜와 끝날짜만 만든 메소드에 넣어주자
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String startDateStr = simpleDateFormat.format(dateList[0]);
		String endDateStr = simpleDateFormat.format(dateList[dateList.length-1]);
		log.info("대분류 시작 날짜 : "+startDateStr);
		log.info("대분류 끝 날짜 : "+endDateStr);
		
		List<StockReportDTO> stockReportDTOList = stockReportService.getStockReportForLCListByPeriod(startDateStr, endDateStr);
		
		return stockReportDTOList;
	}
	
	/**
	 * 재고금액 화면에서, 기간검색에서 받은 기간리스트를 이용해서 중분류 재고금액 계산해주기
	 * @param dateList
	 * @return
	 */
	@PostMapping(value="stockReportForMC", produces=MediaType.APPLICATION_JSON_VALUE)
	public List<StockReportDTO> stockReportForMC(@RequestBody Date[] dateList) {
		//보낸 날짜 리스트 기간에서! 첫날짜와 끝날짜만 만든 메소드에 넣어주자
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String startDateStr = simpleDateFormat.format(dateList[0]);
		String endDateStr = simpleDateFormat.format(dateList[dateList.length-1]);
		log.info("중분류 시작 날짜 : "+startDateStr);
		log.info("중분류 끝 날짜 : "+endDateStr);
		
		List<StockReportDTO> stockReportDTOList = stockReportService.getStockReportForMCListByPeriod(startDateStr, endDateStr);
		
		return stockReportDTOList;
	}
	
	
}
