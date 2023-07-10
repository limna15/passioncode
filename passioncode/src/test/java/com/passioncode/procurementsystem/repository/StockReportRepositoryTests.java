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
import com.passioncode.procurementsystem.dto.StockReportDTO;
import lombok.extern.log4j.Log4j2;

@Log4j2
@SpringBootTest
public class StockReportRepositoryTests {
	
	@Autowired
	StockReportRepository stockReportRepository;

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
		List<Object[]> calculStockReport = stockReportRepository.getCalculStockReportForLC(yearMonthFirstdateStr, todayStr);
		log.info("이건 잘 가져오는건가 : "+calculStockReport);
		
		List<StockReportDTO> stockReportDTOList = new ArrayList<>();
		
		for(Object[] result:calculStockReport) {
			StockReportDTO stockReportDTO = StockReportDTO.builder().eachDateStr(String.valueOf(result[0])+" 00:00:00")
																	.largeCategoryCode(String.valueOf(result[1])).largeCategoryName(String.valueOf(result[2]))
																	.stockTotalPrice(Integer.parseInt(String.valueOf(result[3]))).build();
			stockReportDTOList.add(stockReportDTO);
		}
		log.info("만든 DTO리스트 보자 : "+stockReportDTOList);
	}
	
}
