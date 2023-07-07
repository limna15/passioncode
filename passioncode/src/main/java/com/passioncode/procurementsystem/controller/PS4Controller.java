package com.passioncode.procurementsystem.controller;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import com.passioncode.procurementsystem.dto.LargeCategoryDTO;
import com.passioncode.procurementsystem.dto.MaterialOutDTO;
import com.passioncode.procurementsystem.dto.MiddleCategoryDTO;
import com.passioncode.procurementsystem.dto.StockResultDTO;
import com.passioncode.procurementsystem.entity.LargeCategory;
import com.passioncode.procurementsystem.entity.MiddleCategory;
import com.passioncode.procurementsystem.entity.ProcurementPlan;
import com.passioncode.procurementsystem.repository.MaterialOutRepository;
import com.passioncode.procurementsystem.service.LargeCategoryService;
import com.passioncode.procurementsystem.service.MaterialOutService;
import com.passioncode.procurementsystem.service.MiddleCategoryService;
import com.passioncode.procurementsystem.service.ProcurementPlanService;
import com.passioncode.procurementsystem.service.StockReportService;
import com.passioncode.procurementsystem.service.StockResultService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Controller
@RequiredArgsConstructor
@RequestMapping("procurement4")
@Log4j2
public class PS4Controller {
	
	private final MaterialOutService materialOutService;
	private final ProcurementPlanService procurementPlanService;
	private final StockResultService stockResultService;
	private final LargeCategoryService largeCategoryService;
	private final MiddleCategoryService middleCategoryService;
	private final StockReportService stockReportService;
	
	@GetMapping("materialOut")
	public void materialOut(Model model, MaterialOutDTO materialOutDTO) {
		List<MaterialOutDTO> materialOutDTOList = materialOutService.getDTOList();

		log.info("materialOutDTOlist.............");

		model.addAttribute("DTOList", materialOutDTOList);
	}
	
	@PostMapping("materialOutRegister")
	public String materialOutRegister(MaterialOutDTO materialOutDTO, HttpServletRequest request) {
		log.info("materialOutDTO >>> " + materialOutDTO);
		
		Integer detailPurchaseOrderCodeStr= Integer.parseInt(request.getParameter("dpoCode"));
		String status= request.getParameter("status");
		
		log.info("js로 만들어 보낸 form 데이터 detailPurchaseOrderCode잘 받아오나 >>> " + detailPurchaseOrderCodeStr);
		log.info("js로 만들어 보낸 form 데이터 status 잘 받아오나 >>> " + status);
		
		ProcurementPlan pp= procurementPlanService.getPpByDetailPurchaseOrder(detailPurchaseOrderCodeStr);
		Integer mrpCode= pp.getMrp().getCode();
		
		materialOutDTO= MaterialOutDTO.builder().mrpCode(mrpCode).outStatus(status).build();
		materialOutService.register(materialOutDTO);
		
		return "redirect:/procurement4/materialOut";
	}
	
	@GetMapping("stockResult")
	public void stockResult(Model model) {
		List<StockResultDTO> stockResultDTOList = stockResultService.getStockResultDTOList();
		
		model.addAttribute("DTOList", stockResultDTOList);
	}
	
	
	@GetMapping("stockReport")
	public void stockReport(Model model) {
		
		//대분류 셀렉트 정보는 모든 정보 다 보내주기
		List<LargeCategoryDTO> LargeCategoryDTOList = largeCategoryService.getDTOList();
		model.addAttribute("LargeCategoryDTOList", LargeCategoryDTOList );
		
		//중분류 셀렉트 정보는 대분류 리스트에서 처음보여주게 되는거에 해당되는 중분류만 보여주기
		//나머지 중분류는 화면에서 바뀔때마다 불러오기 때문에!
		LargeCategory largeCategory = middleCategoryService.getLargeCategory(LargeCategoryDTOList.get(0).getCode());
		List<MiddleCategory> MiddleCategoryListByLC1 = middleCategoryService.getMiddleCategoryByLargeCategory(largeCategory);
		
		List<MiddleCategoryDTO> MiddleCategoryDTOListByLC1 = new ArrayList<>();
		for(MiddleCategory middleCategory:MiddleCategoryListByLC1) {
			MiddleCategoryDTOListByLC1.add(middleCategoryService.entityToDTO(middleCategory));
		}		
		model.addAttribute("MiddleCategoryDTOList", MiddleCategoryDTOListByLC1);
		
		
		List<Date> DateList = new ArrayList<>();
		List<String> DateStrList = new ArrayList<>();
		
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date today = new Date();
		
		Date startDate = today;
		try {
			startDate = simpleDateFormat.parse("2023-07-01");
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Date endDate = today;
		try {
			endDate = simpleDateFormat.parse("2023-07-07");
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Calendar cal1 = Calendar.getInstance();
		cal1.setTime(endDate);
		// 기준일로 설정. month의 경우 해당월수-1을 해줍니다.
//		cal1.set(2023,0,5);
//		cal1.setTime(test);
		
		Calendar cal2 = Calendar.getInstance();
		cal2.setTime(startDate);
		
		while(!cal2.after(cal1)) {
			Date caldate=cal2.getTime();
			cal2.add(Calendar.DATE, 1);
			DateList.add(caldate);
			DateStrList.add(simpleDateFormat.format(caldate));	
		}
		log.info("계산 된건가?? : "+DateList);
		log.info("계산 된건가?? : "+DateStrList);
		model.addAttribute("DateList",DateList);
		model.addAttribute("DateStrLisg",DateStrList);
		
		
		List<StockResultDTO> stockResultDTOList = stockReportService.getStockReportForLCList();
		model.addAttribute("stockResultDTOList",stockResultDTOList);
		
		
		String mylabels="[";
		for(String labels:DateStrList) {
			mylabels += "\""+labels+"\",";
		}
		mylabels=mylabels.substring(0, mylabels.length()-1)+"]";
		log.info("잘 만들어 졌나? mylabels : "+mylabels);
		
		model.addAttribute("mylabels",mylabels);

		
//		String pricedata="[";
//		for(StockResultDTO dto :stockResultDTOs) {
//			pricedata += dto.getStockTotalPrice()+",";
//		}
//		pricedata=pricedata.substring(0, pricedata.length()-1)+"]";
//		log.info("잘 만들어 졌나? pricedata : "+pricedata);
//		
//		model.addAttribute("pricedata",pricedata);
		
	}

}
