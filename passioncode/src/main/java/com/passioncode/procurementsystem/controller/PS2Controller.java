package com.passioncode.procurementsystem.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.passioncode.procurementsystem.dto.DetailPurchaseOrderDTO;
import com.passioncode.procurementsystem.dto.PurchaseOrderDTO;
import com.passioncode.procurementsystem.entity.DetailPurchaseOrder;
import com.passioncode.procurementsystem.repository.DetailPurchaseOrderRepository;
import com.passioncode.procurementsystem.service.DetailPurchaseOrderService;
import com.passioncode.procurementsystem.service.ProgressCheckService;
import com.passioncode.procurementsystem.service.PurchaseOrderService;

import org.springframework.ui.Model;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Controller
@RequiredArgsConstructor
@RequestMapping("procurement2")
@Log4j2
public class PS2Controller {
	
	private final PurchaseOrderService purchaseOrderService;
	private final DetailPurchaseOrderService detailPurchaseOrderService;
	private final ProgressCheckService progressCheckService;
	
	
	@GetMapping("/purchaseOrder")	//발주서 발행
	public void PS2Test(Integer[] lengthList, Model model ,PurchaseOrderDTO purchaseOrderDTO, DetailPurchaseOrderDTO detailDTO, Integer checkBox, Integer checkBox3) {
		if(lengthList!=null) { //lengthList가 존재하는 경우에만 
		//log.info("발주서 번호들이 잘 가나..>>"+lengthList[0]);//리스트로 잘 간다. 번호? 자리수로 해줘야 함, for문 이용하기 
			for(int i=0;i<lengthList.length;i++) {
				log.info("발주서 번호들이 잘 가나..>>"+lengthList[i]);//잘 나온다
			}
		}
		
		//model.addAttribute("DetailPurchaseOrderList", detailPurchaseOrderService.getDTOList());
		//model.addAttribute("list",repository.findAll());
		model.addAttribute("purchaseOrderList", purchaseOrderService.getDTOList());
		
		//여서기 서비스 호출 뒤에 리턴 값을 보낸다
		model.addAttribute("myListData",checkBox);
		model.addAttribute("myPublishDTO",detailPurchaseOrderService.detailToDTO(checkBox));
		//log.info("제가 보고 싶은 값 2222>>>>>>==="+detailPurchaseOrderService.detailToDTO(checkBox));
		//log.info("제가 보고 싶은 값 입니다>>>>>>==="+checkBox);
		
	}
	
	
	@GetMapping("/publishDetail")//발주서 발행 버튼 누르면
	public void DetailList(Model model, DetailPurchaseOrderDTO deailDTO, RedirectAttributes redirectAttributes) {
		model.addAttribute("list",detailPurchaseOrderService.getDetailList());//미리보기 화면 부분
		
		//발행 버튼을 누르면 발행하도록 하기
		//log.info("잘 저장되는지 보기>>"+detailPurchaseOrderService.detailToDTO(null));
		model.addAttribute("de",detailPurchaseOrderService.dtoToEntity(deailDTO));
		List<DetailPurchaseOrderDTO> detail = new ArrayList<>();
		detail.add(deailDTO);//저장해 주기
		//return "redirect:/procurement2/purchaseOrder";
	}
	
	
	@GetMapping("/progressCheck")
	public void PS2Test2(Model model ,PurchaseOrderDTO purchaseOrderDTO, Integer checkBox) {
		log.info("progressCheck>>>>>>>"+purchaseOrderDTO);
		log.info("progressCheck일정등록 위해>>==="+checkBox);
		
		model.addAttribute("pCheckList", progressCheckService.getProgressCheckDTOList());
		
		
		
	}
	
	@GetMapping("/detailPurchaseOrder")
	public void PS2Test22(Model model) {
		model.addAttribute("DetailPurchaseOrderList", detailPurchaseOrderService.getDTOList());
		model.addAttribute("purchaseOrderList", purchaseOrderService.getDTOList());
		log.info("detailPurchaseOrder>>>>>>>"+purchaseOrderService.getDTOList());
		
		
		
	}
	
	@GetMapping("/purchaseOrderPublish")
	public void PS24Test22(Model model, String checkBox) {
		//model.addAttribute("purchaseOrderList", purchaseOrderService.getDTOList());
		//model.addAttribute("DetailPurchaseOrderList", detailPurchaseOrderService.getDTOList());
		//model.addAttribute("myListData",checkBox);
		log.info("purchaseOrderPublish>>>>>>>");
		
		
		
	}
	
	@GetMapping("/print")
	public void PS2TestPrint(DetailPurchaseOrderDTO detailDTO, Model model,PurchaseOrderDTO purchaseOrderDTO, Integer checkBox) {
		model.addAttribute("myPublishDTO",detailPurchaseOrderService.detailToDTO(checkBox));
		log.info("인쇄합니다>>>>>>>");
		
	}
	
	
	@GetMapping("/purchaseNo")
	public String purchaseNo(Integer[] lengthList, DetailPurchaseOrderDTO detailDTO, Model model,PurchaseOrderDTO purchaseOrderDTO, Integer checkBox2) {
		log.info("내가 원하는 발주서 번호>>33333>>"+checkBox2);
		model.addAttribute("myPublishData",detailPurchaseOrderService.dtoToEntity(detailDTO));
		
		
		detailPurchaseOrderService.updataePp(checkBox2);
		log.info("발행 합니다~~>>>>>>>"+checkBox2);
		
		//여기에 반복문을 넣어서 하기 
		//만약 더이상 발주할 것이 없다면
		String replay = "redirect:/procurement2/purchaseOrder";//마지막 발주 
		String replay2 = "redirect:/procurement2/purchaseNo?checkBox="+1;//이런 식으로 뒤에 더 붙이기
		
		return replay;
		
		
	}

}
