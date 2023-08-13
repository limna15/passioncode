package com.passioncode.procurementsystem.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {
	
//	@GetMapping("layout/basic")
//	public void layoutTest() {
//		
//	}
	@GetMapping("layout/basic")
	public String layoutTest() {
		return "layout/layoutTest";
	}
	
	//메인 도메인 에서 추가로 passioncode/procurementSystem 입력해야지, 메인화면이 떴었지만, 도메인주소만으로 메인화면 나오게 바꿈!
	@GetMapping(value="/")
	public String mainTest() {
		return "layout/procurementSystem";
	}
	
}
