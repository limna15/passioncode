package com.passioncode.procurementsystem.service;
import com.passioncode.procurementsystem.dto.DetailPurchaseOrderDTO;
import com.passioncode.procurementsystem.entity.Contract;
import com.passioncode.procurementsystem.entity.DetailPurchaseOrder;
import com.passioncode.procurementsystem.entity.MaterialOut;
import com.passioncode.procurementsystem.entity.PurchaseOrder;

public interface DetailPurchaseOrderService {
	
	DetailPurchaseOrderDTO read(Integer no);

	/**
	 * 조달계획 엔티티를 이용해 PurchaseOrderDTO로 만들기(ProcurementPlan -> PurchaseOrderDTO)
	 * @param detailPurchaseOrder
	 * @return
	 */
	DetailPurchaseOrderDTO entityToDTO(DetailPurchaseOrder detailPurchaseOrder, MaterialOut materialOut);

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
}
