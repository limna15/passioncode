package com.passioncode.procurementsystem.controller;

import java.time.LocalDateTime;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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
	
	@PostMapping(value ="/detailNo")
	public void detailNo(@RequestBody Integer num1, HttpServletResponse response) {
		//log.info("num1 >>"+num1);
		
		
	}
	
	@PostMapping(value ="/publish")
	public void publish(@RequestBody Integer[] publishCode, HttpServletResponse response) {
		//log.info("이상하게 찍히는 >>"+publishCode);
		//log.info("첫 번째 값 >>> " + publishCode[0]);
		purchaseOrderService.makePoCode(publishCode);
		//log.info("발주서 발행:  "+publishCode);
		
	}
	
	@PostMapping(value ="/progressCheck2")
	public void addDate(@RequestBody String[] dateAndCode, HttpServletResponse response) {
		//log.info("이상하게 찍히는 >>"+dateAndCode);
		//log.info("날짜 >>> " + dateAndCode[0]);
		//log.info("발주 코드 >>> " + dateAndCode[1]);
		String mydate = dateAndCode[0]+"T00:10";//내가 가져오는 데이터는 DateTime이라 LocalDateTime 붙임
		//LocalDateTime localDateTime = LocalDateTime.parse(dateAndCode[0]);
		LocalDateTime localDateTime = LocalDateTime.parse(mydate);
		Integer mycode = Integer.parseInt(dateAndCode[1]);
		//log.info("시간으로 변환 >>> " + localDateTime);
		//여기서 서비스 해서 저장하기 
		progressCheckService.nextCheckDate(localDateTime, mycode);
		
	}
		
		@PostMapping(value ="/progressCheck3")
		public void addAvg(@RequestBody String[] percentAndEct, HttpServletResponse response) {
			//4가지를 보낼 예정: 퍼센트, 기타(안쓰면 없음이라고 내가 넣기), 진척검수코드 업데이트, 발주코드 ?
			//log.info("이상하게 찍히는 >>"+percentAndEct);
			//log.info("퍼센트 >>> " + percentAndEct[0]);
			//log.info("기타사항 >>> " + percentAndEct[1]);
			//log.info("발주 코드 >>> " + percentAndEct[2]);
			Integer mypercent = Integer.parseInt(percentAndEct[0]);
			String myetc = percentAndEct[1]+"";
			Integer mycode = Integer.parseInt(percentAndEct[2]);
			//여기서 서비스 해서 저장하기
			progressCheckService.addAvg2(mypercent, myetc, mycode);
		
	}

}
