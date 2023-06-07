package com.passioncode.procurementsystem.repository;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.passioncode.procurementsystem.dto.DetailPurchaseOrderDTO;
import com.passioncode.procurementsystem.entity.DetailPurchaseOrder;
import com.passioncode.procurementsystem.entity.MaterialIn;

import lombok.extern.log4j.Log4j2;

@SpringBootTest
@Log4j2
public class MaterialInRepositoryTests {

	@Autowired
	MaterialInRepository materialInRepository;
	
	@Autowired
	DetailPurchaseOrderRepository detailPurchaseOrderRepository;
	
	@Autowired
	ProcurementPlanRepository procurementPlanRepository;
	
	@Test
	public void getList() {
		Optional<MaterialIn> list= materialInRepository.findById(1);
		
		MaterialIn in= list.get();
		
		List<MaterialIn> list2= materialInRepository.findAll();
		
		log.info("이렇게 읽는게 맞는건가....."+in);
		log.info("리스트로 읽을수 있냐구"+list2);
	}
	
//	@Transactional
	@Test
	public void insertTest() {
		//세부구매발주서 ID로 읽기
		Optional<DetailPurchaseOrder> result= detailPurchaseOrderRepository.findById(3);
		//log.info("세부구매발주서 읽기 get" + result.get());
		//직접적인 외래키(지연로딩된것)를 읽으려면 어노테이션 @Transactional 필요
		//외래키 사용하면 get을 쓰지 않아도 해당 테이블을 가져올 수 있음
		log.info("세부구매발주서 읽기 " + result);
		
		DetailPurchaseOrder dpo= result.get();
		log.info("세부구매발주서 코드 읽기 " + dpo.getCode());
		
		
		//입고일 현재시간으로 읽기 
		log.info("입고일, localdatetime 현재시간 읽기 : "+LocalDateTime.now());
		
		//입고상태, 발행상태 기본값이 세팅되어있지만 null이 허용이 안되있어서 null을 넣으면 에러가 남
		MaterialIn materialIn= MaterialIn.builder().date(LocalDateTime.now()).status(1).transactionStatus(0).detailPurchaseOrder(dpo).build();
		
		materialInRepository.save(materialIn);
	}
	
	@Test
	public void readTest() {
		//읽어와야하는 내용
		//발주서번호, 발주코드(세부구매발주서테이블), 조달납기예정일(조달계획테이블), 품목코드, 품목명, 입고상태, 발행상태
		
		Optional<DetailPurchaseOrder> result= detailPurchaseOrderRepository.findById(3);
		DetailPurchaseOrder dpo= result.get();
		log.info("세부구매발주서의 구매발주서번호 읽기 " + dpo.getPurchaseOrder().getNo());
		
		List<DetailPurchaseOrder> list= detailPurchaseOrderRepository.findAll();
		log.info("세부구매발주서 리스트 " + list);
		
	}
}
