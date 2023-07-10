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
import lombok.extern.log4j.Log4j2;

@Log4j2
@SpringBootTest
public class StockResultRepositoryTests {
	
	@Autowired
	StockResultRepository stockResultRepository;

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
}
