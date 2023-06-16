package com.passioncode.procurementsystem.controller;

import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.passioncode.procurementsystem.dto.MaterialInDTO;
import com.passioncode.procurementsystem.dto.PurchaseReportDTO;
import com.passioncode.procurementsystem.dto.TransactionDetailDTO;
import com.passioncode.procurementsystem.entity.DetailPurchaseOrder;
import com.passioncode.procurementsystem.entity.MaterialIn;
import com.passioncode.procurementsystem.service.DetailPurchaseOrderService;
import com.passioncode.procurementsystem.service.MateriallInService;
import com.passioncode.procurementsystem.service.PurchaseReportService;
import com.passioncode.procurementsystem.service.TransactionDetailService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Controller
@RequiredArgsConstructor
@RequestMapping("procurement3")
@Log4j2
public class PS3Controller {
	
	private final MateriallInService materiallInService;
	private final TransactionDetailService transactionDetailService;	
	private final PurchaseReportService purchaseReportService;
	private final DetailPurchaseOrderService detailPurchaseOrderService;
	
	@GetMapping("/materialIn")
	public void materialIn(Model model, HttpServletRequest request, MaterialInDTO materialInDTO, String purchaseCode) {
		List<MaterialInDTO> materialInDTOList = materiallInService.getMaterialInDTOLsit();
		//List<MaterialIn> materialInList= materiallInService.getMaterialInLsit();
		//log.info("materialInDTOlist............." + materialInDTOList);
		//log.info("purchaseCode >>> " + purchaseCode);
		//log.info("materialInDTOList >>> " + materialInDTOList);
		
		List<String> dpoCodeList= new ArrayList<>();
		if(purchaseCode!=null) {
			String[] splitdpoCode= purchaseCode.split(" ");
			for(int i=0; i<splitdpoCode.length; i++) {
				dpoCodeList.add(splitdpoCode[i].trim());
			}
		}
		//log.info("dpoCodeList >>> " + dpoCodeList);
		
		//String으로 넘어온 코드 Integer로 변환
		List<Integer> dpoCode= new ArrayList<>();
		for(int i=0; i<dpoCodeList.size(); i++) {
			dpoCode.add(Integer.parseInt(dpoCodeList.get(i)));
		}
		//log.info("dpoCode >>> " + dpoCode);
		
		DetailPurchaseOrder detailPurchaseOrder= null;
		TransactionDetailDTO transactionDetailDTO= null;
		List<TransactionDetailDTO> transactionDetailDTOList= new ArrayList<>();
		
		for(int i=0; i<dpoCode.size(); i++) {
			detailPurchaseOrder = detailPurchaseOrderService.get(dpoCode.get(i));
			//log.info("detailPurchaseOrder >>> " + detailPurchaseOrder);
			transactionDetailDTO= transactionDetailService.transactionDetailToDTO(detailPurchaseOrder);
			transactionDetailDTOList.add(transactionDetailDTO);
		}
		//log.info("transactionDetailDTOList >>> " + transactionDetailDTOList);
		
		//하나의 발주서 번호에 여러개의 발주 코드가 있을 경우: 품목정보를 제외하고는 한번씩만 표시되면 되기때문에 따로 객체 설정해줌
		TransactionDetailDTO tdDTOInfo = null;
		for(int i=0; i<transactionDetailDTOList.size(); i++) {
			for(int j=0; j<transactionDetailDTOList.size(); j++) {
				if(transactionDetailDTOList.get(i).getCompanyName().equals(transactionDetailDTOList.get(j).getCompanyName())){
					tdDTOInfo= TransactionDetailDTO.builder().company(transactionDetailDTOList.get(0).getCompany())
							.purchaseOrderNo(transactionDetailDTOList.get(0).getPurchaseOrderNo()).date(transactionDetailDTOList.get(0).getDate())
							.companyNo(transactionDetailDTOList.get(0).getCompanyNo()).companyName(transactionDetailDTOList.get(0).getCompanyName())
							.CEO(transactionDetailDTOList.get(0).getCEO()).companyAddress(transactionDetailDTOList.get(0).getCompanyAddress())
							.manager(transactionDetailDTOList.get(0).getManager()).managerTel(transactionDetailDTOList.get(0).getManagerTel()).build();
				};

			}
		}
		//log.info("tdDTOInfo >>> " + tdDTOInfo);
		
		//하나의 발주서 번호에 여러개의 발주 코드가 있을 경우: 수량/공급가격 정보
		int amount= 0;
		int price= 0;
		
		for(int i=0; i<transactionDetailDTOList.size(); i++) {
			amount += transactionDetailDTOList.get(i).getAmount();
			price += transactionDetailDTOList.get(i).getUnitPrice()*transactionDetailDTOList.get(i).getAmount();;
		}
		//log.info("amount >>> " + amount);
		//log.info("price >>> " + price);
		
		request.setAttribute("purchaseCode", purchaseCode);
		model.addAttribute("tdDTOList", transactionDetailDTOList);
		model.addAttribute("tdDTOInfo", tdDTOInfo);
		model.addAttribute("amount", amount);
		model.addAttribute("price", price);
		model.addAttribute("DTOList", materialInDTOList);
		//model.addAttribute("purchaseCode", purchaseCode);
	}
	
//	@PostMapping("/materialIn")
//	public String tdInfo(@Param("purchaseCode") String purchaseCode) {
//		log.info("안읽히나 본데 " + purchaseCode);
//	
//		return "redirect:/procurement3/materialIn?purchaseCode="+purchaseCode;
//	}
	
	@PostMapping(value="materialInRegister")
	public String materialInRegister(MaterialInDTO materialInDTO, RedirectAttributes redirectAttributes, HttpServletRequest request) {
		log.info("잘 보내지나 >>> "+ materialInDTO);
		
		Integer purchaseOrderNo= Integer.parseInt(request.getParameter("no"));
		Integer detailPurchaseOrderCode= Integer.parseInt(request.getParameter("code"));
		String materialCode= request.getParameter("materialCode");
		String materialName= request.getParameter("materialName");
		Integer amount= Integer.parseInt(request.getParameter("amount"));
		
		log.info("js로 만들어 보낸 form 데이터 purchaseOrderNo 잘 받아오나 >>> " + purchaseOrderNo);
		log.info("js로 만들어 보낸 form 데이터 detailPurchaseOrderCode잘 받아오나 >>> " + detailPurchaseOrderCode);
		log.info("js로 만들어 보낸 form 데이터 materialCode 잘 받아오나 >>> " + materialCode);
		log.info("js로 만들어 보낸 form 데이터 materialName 잘 받아오나 >>> " + materialName);
		log.info("js로 만들어 보낸 form 데이터 amount 잘 받아오나 >>> " + amount);
		
		materialInDTO.setCode(purchaseOrderNo);
		materialInDTO.setCode(detailPurchaseOrderCode);
		materialInDTO.setMaterialCode(materialCode);
		materialInDTO.setMaterialName(materialName);
		materialInDTO.setAmount(amount);
		materialInDTO.setStatus(true);
		materialInDTO.setTransactionStatus(false);
		log.info("materialDTO 잘 세팅이 되나 >>> " + materialInDTO);
		materiallInService.register(materialInDTO);

		return "redirect:/procurement3/materialIn";
		//return "/procurement3/materialInRegister";
	}
	
	//form으로 묶인 input이 전부 보내짐, 각각 배열로 오기 때문에 code 배열 찍어본 것
//		String[] code = request.getParameterValues("code");
//		
//		for(String test : code) {
//		log.info("code 읽기 >>>> " + test);
//		}
	
	@GetMapping("/transactionList")
	public void transactionList(Model model, TransactionDetailDTO transactionDetailDTO) {
		model.addAttribute("list", transactionDetailService.getTransactionDetailList());
	}
	
	@GetMapping("/transactionPrint")
	public void printTest() {
		log.info("거래명세서 인쇄");		
	}
	
	@GetMapping("/purchaseReport")
	public void purchaseReport(Model model, PurchaseReportDTO purchaseReportDTO) {
		log.info("발주진행 현황관리");
		
		model.addAttribute("list", purchaseReportService.getCountPurchaseReportDTO());
	}
}
