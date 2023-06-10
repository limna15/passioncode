package com.passioncode.procurementsystem.service;

import com.passioncode.procurementsystem.dto.TransactionDetailDTO;
import com.passioncode.procurementsystem.entity.DetailPurchaseOrder;
import com.passioncode.procurementsystem.entity.TransactionDetail;

public interface TransactionDetailService {
	
	/**
	 * 세부구매발주서 엔티티를 이용해 transactionDetailDTO 만들기(detailPurchaseOrder -> transactionDetailDTO)
	 * @param detailPurchaseOrder
	 * @return
	 */
	TransactionDetailDTO transactionDetailToDTO(DetailPurchaseOrder detailPurchaseOrder);


	/**
	 * transactionDetailDTO를 이용해 거래명세서 엔티티 만들기(transactionDetailDTO -> transactionDetail)
	 * @param transactionDetailDTO
	 * @return
	 */
	TransactionDetail DTOToEntity(TransactionDetailDTO transactionDetailDTO);
	
	/**
	 * transactionDetailDTO를 이용해서 거래명세서 등록
	 * @param transactionDetailDTO
	 * @return
	 */
	Integer register(TransactionDetailDTO transactionDetailDTO);
}
