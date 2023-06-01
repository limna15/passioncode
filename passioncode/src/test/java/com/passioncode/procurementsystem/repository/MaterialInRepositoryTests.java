package com.passioncode.procurementsystem.repository;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.passioncode.procurementsystem.entity.MaterialIn;

@SpringBootTest
public class MaterialInRepositoryTests {

	@Autowired
	MaterialInRepository materialInRepository;
	
	@Test
	public void getList() {
		Optional<MaterialIn> list= materialInRepository.findById(1);
		
		MaterialIn in= list.get();
		
		List<MaterialIn> list2= materialInRepository.findAll();
		
		System.out.println("이렇게 읽는게 맞는건가....."+in);
		System.out.println("리스트로 읽을수 있냐구"+list2);
	}
}
