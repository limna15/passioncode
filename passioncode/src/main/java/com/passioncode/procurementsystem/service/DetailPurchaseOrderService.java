package com.passioncode.procurementsystem.service;
import java.util.List;

import com.passioncode.procurementsystem.dto.DetailPublishDTO;
import com.passioncode.procurementsystem.dto.DetailPurchaseOrderDTO;
import com.passioncode.procurementsystem.entity.DetailPurchaseOrder;
import com.passioncode.procurementsystem.entity.ProcurementPlan;
import com.passioncode.procurementsystem.entity.PurchaseOrder;

public interface DetailPurchaseOrderService {
	/**
	 * 발주서 발행 전 갖고 오기 위해서 
	 * @return
	 */
	List<DetailPublishDTO> detailToDTO(Integer no);
	
	/**
	 * 조달계획 엔티티를 이용해 PurchaseOrderDTO로 만들기(ProcurementPlan -> DetailPurchaseOrderDTO)
	 * @param procurementPlan
	 * @return
	 */
	DetailPurchaseOrderDTO entityToDTO(ProcurementPlan procurementPlan);

	/**
	 * MaterialDTO를 이용해서 품목 엔티티 만들기(MaterialDTO -> Material)
	 * @param materialDTO
	 * @return
	 */
	//Material dtoToEntity(MaterialDTO materialDTO);
	
	/**
	 * 발주코드를 이용해서 세부구매발주서 엔티티 가져오기
	 * @param no
	 * @return
	 */
	DetailPurchaseOrder get(Integer code);

	/**
	 * 
	 * @return
	 */
	List<DetailPurchaseOrderDTO> getDTOList();

	/**
	 * 세부구매발주 DTO를 엔티티로 
	 * @param detailPurchaseOrderDTO
	 * @return
	 */
	DetailPurchaseOrder dtoToEntity(DetailPurchaseOrderDTO detailPurchaseOrderDTO);
	
	/**
	 * 구매발주서 번호로 세부구매발주서 가져오기
	 * @param purchaseOrder
	 * @return
	 */
	List<DetailPurchaseOrder> getDetailByPurchaseNo(PurchaseOrder purchaseOrder);

	/**
	 * 구매발주서에 보여줄 발주서세부 리스트
	 * @return
	 */
	List<DetailPurchaseOrder> getDetailList();
	

	void updataePp(Integer num1);
}
