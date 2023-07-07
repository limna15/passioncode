package com.passioncode.procurementsystem.service;
import java.util.List;
import com.passioncode.procurementsystem.dto.StockResultDTO;

public interface StockReportService {
	
	/**
	 * 재고금액 리포트 화면에서, 처음에 보여줄 대분류별 현황 <br>
	 * 기간은 오늘날짜의 해당하는 년도와 해당월 1일부터 오늘날짜까지!
	 * @return
	 */
	List<StockResultDTO> getStockReportForLCList();
	
	/**
	 * 화면에서 받은 첫날짜, 끝날짜로, 재고금액 계산한 재고산출DTO 리스트
	 * @param dateStrList
	 * @return
	 */
	List<StockResultDTO> getStockReportForLCListByPeriod(String startDate,String endDate);
	

}
