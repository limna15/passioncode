package com.passioncode.procurementsystem.mapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import lombok.extern.log4j.Log4j2;

@SpringBootTest
@Log4j2
public class MaterialInMapperTests {

	@Autowired
	MaterialInMapper materialInMapper;
	
	//mapperTest: 시간 가져오기
	@Test
	public void test() {
		log.info(materialInMapper);
		log.info("시간 불러오기! "+materialInMapper.getTime());
	}
	
}
