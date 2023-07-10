package com.passioncode.procurementsystem.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.passioncode.procurementsystem.dto.StockReportDTO;
import com.passioncode.procurementsystem.repository.StockReportRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@RequiredArgsConstructor
@Log4j2
public class StockReportServiceImpl implements StockReportService {
	
	@Autowired
	StockReportRepository stockReportRepository;

	@Override
	public List<StockReportDTO> getStockReportForLCList() {
		//대분류별 현황
		//재고금액 리포트 들어가자마자 보이는 재고산출DTO 리스트!  
		//오늘 날짜 기준으로, 그 해당 년도와 해당월 1일 날짜부터 오늘까지의 날짜까지 재고금액 계산해서 재고산출DTO리스트 만들기
		
		//1. 오늘 날짜 기준으로, 그 해당년도와 해달 월의 1일 과 오늘날짜 구하기
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
		
		//2. 재고금액목록 받아온거 재고산출DTO 리스트에 넣어주기
		List<Object[]> calculStockReport = stockReportRepository.getCalculStockReportForLC(yearMonthFirstdateStr, todayStr);
//		log.info("이건 잘 가져오는건가 : "+calculStockReport);
		
		List<StockReportDTO> stockReportDTOList = new ArrayList<>();
		
		for(Object[] result:calculStockReport) {
			StockReportDTO stockReportDTO = StockReportDTO.builder().eachDateStr(String.valueOf(result[0])+" 00:00:00")
																	.largeCategoryCode(String.valueOf(result[1])).largeCategoryName(String.valueOf(result[2]))
																	.stockTotalPrice(Integer.parseInt(String.valueOf(result[3]))).build();
			stockReportDTOList.add(stockReportDTO);
		}
		return stockReportDTOList;
	}

	@Override
	public List<StockReportDTO> getStockReportForLCListByPeriod(String startDate, String endDate) {
		//대분류별 현황
		//화면에서 받아온 시작날짜, 끝날짜 받아서, 재고산출DTO 로 만들어주기
		List<Object[]> calculStockReport = stockReportRepository.getCalculStockReportForLC(startDate, endDate);
//		log.info("이건 잘 가져오는건가 : "+calculStockReport);
		
		List<StockReportDTO> stockReportDTOList = new ArrayList<>();
		
		for(Object[] result:calculStockReport) {
			StockReportDTO stockReportDTO = StockReportDTO.builder().eachDateStr(String.valueOf(result[0])+" 00:00:00")
																	.largeCategoryCode(String.valueOf(result[1])).largeCategoryName(String.valueOf(result[2]))
																	.stockTotalPrice(Integer.parseInt(String.valueOf(result[3]))).build();
			stockReportDTOList.add(stockReportDTO);
		}
		return stockReportDTOList;
	}

	@Override
	public List<String> getDateStrList(String startDateStr, String endDateStr) {
		List<Date> DateList = new ArrayList<>();
		List<String> DateStrList = new ArrayList<>();
		
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date today = new Date();
		
		Date startDate = today;
		try {
			startDate = simpleDateFormat.parse(startDateStr+" 00:00:00");
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Date endDate = today;
		try {
			endDate = simpleDateFormat.parse(endDateStr+" 00:00:00");
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Calendar cal1 = Calendar.getInstance();
		cal1.setTime(endDate);
		// 기준일로 설정. month의 경우 해당월수-1을 해줍니다.
//		cal1.set(2023,0,5);
//		cal1.setTime(test);
		
		Calendar cal2 = Calendar.getInstance();
		cal2.setTime(startDate);
		
		while(!cal2.after(cal1)) {
			Date caldate=cal2.getTime();
			cal2.add(Calendar.DATE, 1);
			DateList.add(caldate);
			DateStrList.add(simpleDateFormat.format(caldate));	
		}
		//log.info("계산 된건가?? : "+DateList);
		//log.info("계산 된건가?? : "+DateStrList);
		return DateStrList;
	}

	@Override
	public List<Date> getDateList(String startDateStr, String endDateStr) {
		List<Date> DateList = new ArrayList<>();
		List<String> DateStrList = new ArrayList<>();
		
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date today = new Date();
		
		Date startDate = today;
		try {
			startDate = simpleDateFormat.parse(startDateStr+" 00:00:00");
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Date endDate = today;
		try {
			endDate = simpleDateFormat.parse(endDateStr+" 00:00:00");
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Calendar cal1 = Calendar.getInstance();
		cal1.setTime(endDate);
		// 기준일로 설정. month의 경우 해당월수-1을 해줍니다.
//		cal1.set(2023,0,5);
//		cal1.setTime(test);
		
		Calendar cal2 = Calendar.getInstance();
		cal2.setTime(startDate);
		
		while(!cal2.after(cal1)) {
			Date caldate=cal2.getTime();
			cal2.add(Calendar.DATE, 1);
			DateList.add(caldate);
			DateStrList.add(simpleDateFormat.format(caldate));	
		}
		//log.info("계산 된건가?? : "+DateList);
		//log.info("계산 된건가?? : "+DateStrList);
		return DateList;
	}

	@Override
	public List<StockReportDTO> getStockReportForMCList() {
		//중분류별 현황
		//재고금액 리포트 들어가자마자 보이는 재고산출DTO 리스트!  
		//오늘 날짜 기준으로, 그 해당 년도와 해당월 1일 날짜부터 오늘까지의 날짜까지 재고금액 계산해서 재고산출DTO리스트 만들기
		
		//1. 오늘 날짜 기준으로, 그 해당년도와 해달 월의 1일 과 오늘날짜 구하기
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
		
		//2. 재고금액목록 받아온거 재고산출DTO 리스트에 넣어주기
		List<Object[]> calculStockReport = stockReportRepository.getCalculStockReportForMC(yearMonthFirstdateStr, todayStr);
//		log.info("이건 잘 가져오는 건가 : "+calculStockReport);
		
		List<StockReportDTO> stockReportDTOList = new ArrayList<>();
		
		for(Object[] result:calculStockReport) {
			StockReportDTO stockReportDTO = StockReportDTO.builder().eachDateStr(String.valueOf(result[0])+" 00:00:00")
																	.middleCategoryCode(String.valueOf(result[1])).middleCategoryName(String.valueOf(result[2]))
																	.stockTotalPrice(Integer.parseInt(String.valueOf(result[3]))).build();
			stockReportDTOList.add(stockReportDTO);
		}
		return stockReportDTOList;
	}

	@Override
	public List<StockReportDTO> getStockReportForMCListByPeriod(String startDate, String endDate) {
		//대분류별 현황
		//화면에서 받아온 시작날짜, 끝날짜 받아서, 재고산출DTO 로 만들어주기
		List<Object[]> calculStockReport = stockReportRepository.getCalculStockReportForMC(startDate, endDate);
		
		List<StockReportDTO> stockReportDTOList = new ArrayList<>();
		
		for(Object[] result:calculStockReport) {
			StockReportDTO stockReportDTO = StockReportDTO.builder().eachDateStr(String.valueOf(result[0])+" 00:00:00")
																	.middleCategoryCode(String.valueOf(result[1])).middleCategoryName(String.valueOf(result[2]))
																	.stockTotalPrice(Integer.parseInt(String.valueOf(result[3]))).build();
			stockReportDTOList.add(stockReportDTO);
		}
		return stockReportDTOList;
	}

}
