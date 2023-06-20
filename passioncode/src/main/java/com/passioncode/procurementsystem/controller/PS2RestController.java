package com.passioncode.procurementsystem.controller;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.passioncode.procurementsystem.entity.ProgressCheck;
import com.passioncode.procurementsystem.service.DetailPurchaseOrderService;
import com.passioncode.procurementsystem.service.ProgressCheckService;
import com.passioncode.procurementsystem.service.PurchaseOrderService;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@RestController
@RequiredArgsConstructor
@RequestMapping("/procurement2")
@Log4j2
public class PS2RestController {
	

	private final PurchaseOrderService purchaseOrderService;
	private final DetailPurchaseOrderService detailPurchaseOrderService;
	private final ProgressCheckService progressCheckService;
	
	@PostMapping(value ="/progressCheck2")
	public void addDate(@RequestBody String[] dateAndCode, HttpServletResponse response) {
		log.info("이상하게 찍히는 >>"+dateAndCode);
		log.info("날짜 >>> " + dateAndCode[0]);
		log.info("발주 코드 >>> " + dateAndCode[1]);
		String mydate = dateAndCode[0]+"T00:10";//내가 가져오는 데이터는 DateTime이라 LocalDateTime 붙임
		//LocalDateTime localDateTime = LocalDateTime.parse(dateAndCode[0]);
		LocalDateTime localDateTime = LocalDateTime.parse(mydate);
		Integer mycode = Integer.parseInt(dateAndCode[1]);
		log.info("시간으로 변환 >>> " + localDateTime);
		//여기서 서비스 해서 저장하기 
		progressCheckService.nextCheckDate(localDateTime, mycode);
		//log.info("등록되는 검수일정:  ");
		String redirect_url="/procurement2/progressCheck";
		try {
			response.sendRedirect(redirect_url);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log.info("리다이렉트 실패");
		}
		
		
	}

}
