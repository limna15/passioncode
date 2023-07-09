package com.passioncode.procurementsystem.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.passioncode.procurementsystem.dto.PurchaseReportDTO;
import com.passioncode.procurementsystem.service.PurchaseReportService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@RestController
@RequiredArgsConstructor
@RequestMapping("/procurement3")
@Log4j2
public class PS3RestController {

	private final PurchaseReportService purchaseReportService;
	
	
	@PostMapping(value="reportDate", produces=MediaType.APPLICATION_JSON_VALUE)
	public List<PurchaseReportDTO> getDate(@RequestBody Date[] result){
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		List<String> dates= new ArrayList<>();
		
		for(Date date: result) {
			dates.add(simpleDateFormat.format(date));
		}
		
		int datesList= dates.size();
		String getDates[]= dates.toArray(new String[datesList]);
		
		log.info("result 포맷형식 바꿔서 받기 >>> " + dates);	
		log.info("dates 리스트에서 배열로 바꿔서 받기 >>> " + getDates);
		
		List<PurchaseReportDTO> prDTOList= purchaseReportService.getPurchaseReportDTOList(getDates);
		log.info("발주현황DTOList 받아오기 >>> " + prDTOList);
		
		return prDTOList;
	}
}
