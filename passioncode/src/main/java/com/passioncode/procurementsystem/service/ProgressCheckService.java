package com.passioncode.procurementsystem.service;

import java.time.LocalDateTime;
import java.util.List;

import com.passioncode.procurementsystem.dto.DetailProgressCheckListDTO;
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
	
	//진척검수 계획 등록
	void nextCheckDate(LocalDateTime date, Integer code);
	
	//진척 검수 평가(납기 진도율, 기타, 발주 코드)
	void addAvg(Integer num1, String etc, Integer num2);

	//미니 기록 리스트
	List<DetailProgressCheckListDTO> getminiDTOList(Integer num1);

	//고유키를 이용한 진척 검수 평가
	void addAvg2(Integer num1, String etc, Integer num2);
	
	

}
