package com.passioncode.procurementsystem.repository;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.passioncode.procurementsystem.entity.MaterialIn;

import lombok.extern.log4j.Log4j2;

@SpringBootTest
@Log4j2
public class MaterialInRepositoryTests {

	@Autowired
	MaterialInRepository materialInRepository;
	
	@Test
	public void getList() {
		Optional<MaterialIn> list= materialInRepository.findById(1);
		
		MaterialIn in= list.get();
		
		List<MaterialIn> list2= materialInRepository.findAll();
		
		log.info("이렇게 읽는게 맞는건가....."+in);
		log.info("리스트로 읽을수 있냐구"+list2);
	}
}
