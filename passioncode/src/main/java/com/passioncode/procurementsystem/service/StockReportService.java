package com.passioncode.procurementsystem.service;

import java.util.Date;
import java.util.List;
import com.passioncode.procurementsystem.dto.StockReportDTO;

public interface StockReportService {
	
	/**
	 * 재고금액 리포트 화면에서, 처음에 보여줄 대분류별 현황 <br>
	 * 기간은 오늘날짜의 해당하는 년도와 해당월 1일부터 오늘날짜까지!
	 * @return
	 */
	List<StockReportDTO> getStockReportForLCList();
	
	/**
	 * 화면에서 받은 첫날짜, 끝날짜로, 재고금액 계산한 재고산출DTO 대분류 리스트
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	List<StockReportDTO> getStockReportForLCListByPeriod(String startDate,String endDate);
	
	/**
	 * 재고금액 리포트 화면에서, 처음에 보여줄 중분류별 현황 <br>
	 * 기간은 오늘날짜의 해당하는 년도와 해당월 1일부터 오늘날짜까지!
	 * @return
	 */
	List<StockReportDTO> getStockReportForMCList();
	
	/**
	 * 화면에서 받은 첫날짜, 끝날짜로, 재고금액 계산한 재고산출DTO 중분류 리스트
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	List<StockReportDTO> getStockReportForMCListByPeriod(String startDate,String endDate);
	
	/**
	 * 날짜문자 버전을 시작날짜,끝날짜를 이용해서 문자리스트로 만들기
	 * @param startDateStr
	 * @param endDateStr
	 * @return
	 */
	List<String> getDateStrList(String startDateStr,String endDateStr);
	
	/**
	 * 날짜를 시작날짜,끝날짜를 이용해서 날짜리스트로 만들기
	 * @param startDateStr
	 * @param endDateStr
	 * @return
	 */
	List<Date> getDateList(String startDateStr,String endDateStr);
}
