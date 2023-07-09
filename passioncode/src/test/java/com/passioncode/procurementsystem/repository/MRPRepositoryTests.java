package com.passioncode.procurementsystem.repository;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import com.passioncode.procurementsystem.entity.MRP;
import com.passioncode.procurementsystem.entity.Material;
import lombok.extern.log4j.Log4j2;

@SpringBootTest
@Log4j2
public class MRPRepositoryTests {
	
	@Autowired
	MRPRepository mrpRepository;
	
	@Autowired
	MaterialRepository materialRepository;
	
	@Test
	public void InsertTest() {
		Collection<Material> materialTest = materialRepository.findByNameContainingIgnoreCase("gear");
		log.info("어디 대소문자 구별보자 : "+materialTest);
		ArrayList<Material> materialTest2=(ArrayList<Material>) materialTest;
		log.info("이렇게 보면 되나?? : "+materialTest2.get(0));
		
		Date date=new Date();
		log.info("데이트 어디 날짜 한번 어떻게 찍히나 보자!! : "+date);
		
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy년 MM월 dd일"); 
		try {
			date=simpleDateFormat.parse("2023년 06월 20일");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		log.info(simpleDateFormat.format(date));
		
		MRP mrp=MRP.builder().process("1각형 B/P WELDING").amount(150).material(materialTest2.get(0)).date(date).build();
		mrpRepository.save(mrp);
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
		log.info("캘린더 셋팅된 오늘날짜 : "+simpleDateFormat.format(todayDate));
		
		//오늘날짜 + (20~40) => 랜덤 소요일 만들기
		int mrpDateRandomVar = (int)(Math.random()*(40-20+1)+20);
		log.info("소요일을 위한 랜덤변수 값 : "+mrpDateRandomVar);
		cal.add(Calendar.DATE, +mrpDateRandomVar);
		
		Date randomMrpDate=cal.getTime();
		log.info("소요일 랜덤변수를 더해 만든 랜덤소요일 : "+simpleDateFormat.format(randomMrpDate));
		
		return randomMrpDate;
	}
	
	@Test
	public void InsertTest2() {
		//원활한 테스트를 위해, 새로운 품목을 등록할때마다, 그에 해당하는 mrp를 2개씩 생성해서 만들어주자
		//소요공정 -> 10개 중 (0~9)랜덤으로 세팅
		String[] inputProcess = {"LIB #1각형 J/R INSERT", "LIB #2원형 세정", "LIB #3각형 X-RAY", "LIB #4원형 TUBING", "LIB #5각형 L/P WELDING",
									"LIB #6각형 CAN-CAP WELDING", "LIB #7각형 E/L FILLING #2", "LIB #8각형 TAB WELDING", "LIB #9각형 BALL WELDING", "LIB #10원형 TUBING"};
		for(int i=0;i<2;i++) {
			//(int)(Math.random()*(최대값-최소값+1)+최소값)
			int processRandomVar = (int)(Math.random()*(9-0+1)+0);
			log.info("소요공정을 위한 랜덤변수 값 : "+processRandomVar);
			String randomProcess = inputProcess[processRandomVar];
			log.info("소요공정 랜덤변수 값을 이용한 소요공정 테스트 : "+randomProcess);
			//소요량 -> 100*랜덤(1~15) 계산해서 랜덤으로 세팅
			int mrpAmountRandomVar = (int)(Math.random()*(15-1+1)+1);
			log.info("소요량을 위한 랜덤변수 값 : "+mrpAmountRandomVar);
			Integer randomMrpAmount = 100*mrpAmountRandomVar;
			log.info("소요량 랜덤변수 값을 이용한 소요량 테스트 : "+randomMrpAmount);
			//소요일 -> 오늘(입력하는)날짜 + (20~40) 계산해서 랜덤으로 세팅
			log.info("날짜 오늘꺼 테스트 : "+new Date());
			Date randomMrpDate = makeRandomMrpDate();
			log.info("랜덤으로 만든 소요일 테스트 : "+randomMrpDate);
			
			MRP mrp=MRP.builder().process(randomProcess).amount(randomMrpAmount).date(randomMrpDate).material(materialRepository.findById("BP0002").get()).build();
			mrpRepository.save(mrp);
		}
	}
	
	@Test
	public void DeleteTest() {
		//품목코드를 이용해서 mrp들 찾아와서 그 해당 mrp 지워주기
		List<MRP> mrpList = mrpRepository.findBymaterialCode("CB0004");
		log.info("찾은 mrp 리스트 보자 : "+mrpList);
		
		for(MRP mrp : mrpList) {
			mrpRepository.delete(mrp);
		}
	}

}
