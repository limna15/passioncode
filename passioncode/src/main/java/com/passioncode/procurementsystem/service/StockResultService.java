package com.passioncode.procurementsystem.service;

import java.util.Date;
import java.util.List;

import com.passioncode.procurementsystem.dto.StockResultDTO;

public interface StockResultService {
	
	/**
	 * 재고산출 화면에서, 처음 진입화면에서 보여주는 재고산출DTO 리스트 <br>
	 * 기간은 오늘날짜의 해당하는 년도의 1월1일부터 오늘날짜까지!
	 * @return
	 */
	List<StockResultDTO> getStockResultDTOList();
	
	/**
	 * 화면에서 받은 기간으로, 재고산출계산한 재고산출DTO 리스트
	 * @param dateStrList
	 * @return
	 */
	List<StockResultDTO> getStockResultDTOListByPeriod(Date[] dateList);
	

}
