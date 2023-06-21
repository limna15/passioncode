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
import com.passioncode.procurementsystem.entity.ProcurementPlan;
import com.passioncode.procurementsystem.entity.PurchaseOrder;
import com.passioncode.procurementsystem.service.DetailPurchaseOrderService;
import com.passioncode.procurementsystem.service.MateriallInService;
import com.passioncode.procurementsystem.service.ProcurementPlanService;
import com.passioncode.procurementsystem.service.PurchaseOrderService;
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
	
	private final MateriallInService materialInService;
	private final TransactionDetailService transactionDetailService;	
	private final PurchaseReportService purchaseReportService;
	private final DetailPurchaseOrderService detailPurchaseOrderService;
	private final PurchaseOrderService purchaseOrderService;
	private final ProcurementPlanService procurementPlanService;
	
	@GetMapping("/materialIn")
	public void materialIn(Model model, HttpServletRequest request, MaterialInDTO materialInDTO, String purchaseCode) {
		List<MaterialInDTO> materialInDTOList = materialInService.getMaterialInDTOLsit();

		log.info("materialInDTOlist............." + materialInDTOList);

		model.addAttribute("DTOList", materialInDTOList);
	}
	
	@PostMapping(value="materialInRegister")
	public String materialInRegister(MaterialInDTO materialInDTO, RedirectAttributes redirectAttributes, HttpServletRequest request) {
		log.info("잘 보내지나 >>> "+ materialInDTO);
		
		Integer purchaseOrderNo= Integer.parseInt(request.getParameter("no"));
		Integer detailPurchaseOrderCode= Integer.parseInt(request.getParameter("code"));
		String materialCode= request.getParameter("materialCode");
		String materialName= request.getParameter("materialName");
		Integer amount= Integer.parseInt(request.getParameter("amount"));
		String status= request.getParameter("status");
		
//		log.info("js로 만들어 보낸 form 데이터 purchaseOrderNo 잘 받아오나 >>> " + purchaseOrderNo);
//		log.info("js로 만들어 보낸 form 데이터 detailPurchaseOrderCode잘 받아오나 >>> " + detailPurchaseOrderCode);
//		log.info("js로 만들어 보낸 form 데이터 materialCode 잘 받아오나 >>> " + materialCode);
//		log.info("js로 만들어 보낸 form 데이터 materialName 잘 받아오나 >>> " + materialName);
//		log.info("js로 만들어 보낸 form 데이터 amount 잘 받아오나 >>> " + amount);
//		log.info("js로 만들어 보낸 form 데이터 status 잘 받아오나 >>> " + status);
		
		if(status.equals("1")) { //입고 상태가 완료일 때
			materialInDTO.setCode(purchaseOrderNo);
			materialInDTO.setCode(detailPurchaseOrderCode);
			materialInDTO.setMaterialCode(materialCode);
			materialInDTO.setMaterialName(materialName);
			materialInDTO.setAmount(amount);
			materialInDTO.setStatus(true);
			materialInDTO.setTransactionStatus("발행 예정");
			log.info("입고 상태가 완료일 경우, materialDTO 잘 세팅이 되나 >>> " + materialInDTO);
			materialInService.register(materialInDTO);
		}else { //입고 상태가 취소일 때
			materialInDTO.setCode(purchaseOrderNo);
			materialInDTO.setCode(detailPurchaseOrderCode);
			materialInDTO.setMaterialCode(materialCode);
			materialInDTO.setMaterialName(materialName);
			materialInDTO.setAmount(amount);
			materialInDTO.setStatus(false);
			materialInDTO.setTransactionStatus("발행 불가");
			log.info("입고 상태가 취소일 경우, materialDTO 잘 세팅이 되나 >>> " + materialInDTO);
			materialInService.register(materialInDTO);
		}
		
		materialInService.updatePPCompletionDate(materialInDTO.getCode());

		return "redirect:/procurement3/materialIn";
	}
	
	//거래명세서 리스트 화면
	@GetMapping("/transactionList")
	public void transactionList(Model model, TransactionDetailDTO transactionDetailDTO) {
		log.info("안보내지는건가 >>> " + transactionDetailService.getTdDTOList());
		model.addAttribute("list", transactionDetailService.getTdDTOList());
	}
	
	//거래명세서 번호를 눌렀을 때 해당 거래명세서의 내용 보여주는 화면
	@GetMapping("/transactionDetail")
	public void transactionDetail(@Param(value= "purchaseNo") String purchaseNo, Model model) {
		//log.info("파라미터로 보낸값 읽어지나 >>> " + purchaseNo);
		
		PurchaseOrder po= purchaseOrderService.getPurchaseOrder(Integer.parseInt(purchaseNo));
		List<DetailPurchaseOrder> dpoList= detailPurchaseOrderService.getDetailByPurchaseNo(po);
		log.info("발주서 번호로 세부발주서리스트 가져오기 >>> " + dpoList);
		
		List<ProcurementPlan> ppList= new ArrayList<>();
		List<MaterialIn> miList= new ArrayList<>();
		for(int i=0; i<dpoList.size(); i++) {
			ppList.add(procurementPlanService.getPpByDetailPurchaseOrder(dpoList.get(i).getCode()));
			miList.add(materialInService.getMeterialInByDetailPurchaseOrder(dpoList.get(i).getCode()));
		}
		log.info("ppList는 어떻게 찍히지 >>> " + ppList);
		log.info("miList는 어떻게 찍히지 >>> " + miList);
		
		List<TransactionDetailDTO> transactionDetailDTOList= new ArrayList<>();
		TransactionDetailDTO transactionDetailDTO= null;
		
		for(int i=0; i<miList.size(); i++) {
			if(miList.get(i).getStatus()) { //입고상태가 true(1=완료)일때만
				transactionDetailDTO= TransactionDetailDTO.builder().company("패션코드")
						.purchaseOrderNo(Integer.parseInt(purchaseNo)).detailPurchaseOrderCode(dpoList.get(i).getCode()).date(miList.get(i).getDate())
						.companyNo(ppList.get(i).getContract().getCompany().getNo()).companyName(ppList.get(i).getContract().getCompany().getName())
						.CEO(ppList.get(i).getContract().getCompany().getCeo()).companyAddress(ppList.get(i).getContract().getCompany().getAddress())
						.manager(ppList.get(i).getContract().getCompany().getManager()).managerTel(ppList.get(i).getContract().getCompany().getManagerTel())
						.materialCode(ppList.get(i).getContract().getMaterial().getCode()).materialName(ppList.get(i).getContract().getMaterial().getName())
						.amount(dpoList.get(i).getAmount()).unitPrice(ppList.get(i).getContract().getUnitPrice()).build();
				transactionDetailDTOList.add(transactionDetailDTO);
			}
		}
		
		//구매발주서 번호가 존재하지 않으면(=false) 거래명세서 등록하기
		if(!transactionDetailService.checkDone(po)) {
			transactionDetailService.register(transactionDetailDTO);
		}
		model.addAttribute("tdList", transactionDetailDTOList);
	}
	
	@GetMapping("/purchaseReport")
	public void purchaseReport(Model model, PurchaseReportDTO purchaseReportDTO) {
		log.info("발주진행 현황관리");		
		model.addAttribute("list", purchaseReportService.getCountPurchaseReportDTO());
	}
}
