package com.passioncode.procurementsystem.repository;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
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
	

}
