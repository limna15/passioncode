package com.passioncode.procurementsystem.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import com.passioncode.procurementsystem.entity.DetailPurchaseOrder;
import com.passioncode.procurementsystem.entity.ProgressCheck;

public interface ProgressCheckRepository extends JpaRepository<ProgressCheck, Integer> {
	
	public ProgressCheck findByDetailPurchaseOrder(DetailPurchaseOrder detailPurchaseOrder);
	
	public ProgressCheck findByCode(Integer code);
	
	@Transactional
	@Query(value="SELECT pc.code AS pccode, pc.date AS pcdate, pc.etc AS pcectc, pc.rate AS pcrate, pc.detail_purchase_order_code as pcdetail FROM progress_check AS pc WHERE pc.detail_purchase_order_code=:detailNo", nativeQuery = true)
	public List<Object[]> findByDetailPurchaseOrderList(@Param(value = "detailNo")Integer detailNo);
	
}
