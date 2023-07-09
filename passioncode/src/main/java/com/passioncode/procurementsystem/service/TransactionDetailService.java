package com.passioncode.procurementsystem.service;

import java.util.List;
import com.passioncode.procurementsystem.dto.TransactionDetailDTO;
import com.passioncode.procurementsystem.entity.DetailPurchaseOrder;
import com.passioncode.procurementsystem.entity.PurchaseOrder;
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
	
	/**
	 * 거래명세서 목록에 보여줄 거래명세서DTO 리스트(거래명세서 번호 문자ver으로 보여주기)
	 * @return
	 */
	List<TransactionDetailDTO> getTdDTOList();
	
	/**
	 * TransactionDetailDTO 리스트 가져오기
	 * @return
	 */
	List<TransactionDetailDTO> getTransactionDetailDTOLsit();
	
	/**
	 * 구매발주서 존재여부로 거래명세서 테이블에 존재하는지 확인하기
	 * @param purchaseOrder
	 * @return
	 */
	Boolean checkDone(PurchaseOrder purchaseOrder);
}
