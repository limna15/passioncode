package com.passioncode.procurementsystem.service;
import java.util.List;

import com.passioncode.procurementsystem.dto.DetailPurchaseOrderDTO;
import com.passioncode.procurementsystem.entity.Contract;
import com.passioncode.procurementsystem.entity.DetailPurchaseOrder;
import com.passioncode.procurementsystem.entity.MaterialOut;
import com.passioncode.procurementsystem.entity.ProcurementPlan;
import com.passioncode.procurementsystem.entity.PurchaseOrder;

public interface DetailPurchaseOrderService {
	
	DetailPurchaseOrderDTO read(Integer no);

	/**
	 * detailPurchaseOrderDTO를 이용해서 세부구매발주서 발행
	 * @param detailPurchaseOrderDTO
	 * @return
	 */
	Integer register(DetailPurchaseOrderDTO detailPurchaseOrderDTO);
	
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

}
