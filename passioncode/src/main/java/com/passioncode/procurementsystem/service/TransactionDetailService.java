package com.passioncode.procurementsystem.service;

import com.passioncode.procurementsystem.dto.TransactionDetailDTO;
import com.passioncode.procurementsystem.entity.DetailPurchaseOrder;

public interface TransactionDetailService {
	
	/**
	 * 세부구매발주서 엔티티를 이용해 transactionDetailDTO 만들기(transactionDetail -> transactionDetailDTO)
	 * @param detailPurchaseOrder
	 * @return
	 */
	TransactionDetailDTO transactionDetailDTO(DetailPurchaseOrder detailPurchaseOrder);


}
