package com.passioncode.procurementsystem.service;

import java.time.LocalDateTime;
import java.util.List;

import com.passioncode.procurementsystem.dto.ProgressCheckDTO;
import com.passioncode.procurementsystem.entity.DetailPurchaseOrder;

public interface ProgressCheckService {

	/**
	 * 
	 * @return detailPurchaseOrder
	 */
	ProgressCheckDTO entityToDTO(DetailPurchaseOrder detailPurchaseOrder);
	
	//발주서 갖고오기
	List<ProgressCheckDTO> getProgressCheckDTOList();
	
	//발주 계획 등록
	void nextCheckDate(LocalDateTime date, Integer code);
	
	

}
