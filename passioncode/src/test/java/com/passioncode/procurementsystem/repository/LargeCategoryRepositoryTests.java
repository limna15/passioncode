package com.passioncode.procurementsystem.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.passioncode.procurementsystem.entity.LargeCategory;

@SpringBootTest
public class LargeCategoryRepositoryTests {
	
	@Autowired
	LargeCategoryRepository largeCategoryRepository;
	
	@Test
	public void InsertTest() {
//		LargeCategory largeCategory=new LargeCategory("CC0001", "철");
//		LargeCategory largeCategory=new LargeCategory("BB0001", "보드");
		LargeCategory largeCategory=new LargeCategory("PP0001", "플라스틱");
		largeCategoryRepository.save(largeCategory);
	}

}
