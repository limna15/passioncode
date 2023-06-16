package com.passioncode.procurementsystem.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.passioncode.procurementsystem.dto.MaterialInDTO;
import com.passioncode.procurementsystem.service.MateriallInService;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@RestController
@RequiredArgsConstructor
@RequestMapping("/procurement3")
@Log4j2
public class PS3RestController {
	
	private final MateriallInService materialInService;
	
//	@PostMapping(value="materialInRegister", produces=MediaType.APPLICATION_JSON_VALUE)
//	public Integer materialInRegister(@RequestBody MaterialInDTO materialInDTO) {
//		log.info("잘 보내지나 >>> "+ materialInDTO);
//		materialInDTO.setStatus(true);
//		materialInDTO.setTransactionStatus(false);
//		
//		log.info("상태값 바꾸기 >>> "+ materialInDTO);
//		
//		return materialInService.register(materialInDTO);
//		//화면에 보내는 데이터? Boolean 타입이 필요한건가?
//	}

}
