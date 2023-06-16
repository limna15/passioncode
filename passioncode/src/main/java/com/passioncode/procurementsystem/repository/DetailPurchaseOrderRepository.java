package com.passioncode.procurementsystem.repository;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.passioncode.procurementsystem.dto.ContractDTO;
import com.passioncode.procurementsystem.dto.DetailPublishDTO;
import com.passioncode.procurementsystem.dto.DetailPurchaseOrderDTO;
import com.passioncode.procurementsystem.entity.DetailPurchaseOrder;
import com.passioncode.procurementsystem.entity.Material;
import com.passioncode.procurementsystem.entity.ProcurementPlan;
import com.passioncode.procurementsystem.entity.PurchaseOrder;

public interface DetailPurchaseOrderRepository extends JpaRepository<DetailPurchaseOrder, Integer>{
	
	@Query(value="SELECT MAX(CODE)+1 FROM detail_purchase_order",nativeQuery = true)
	public Integer findMaxCode();
	
	@Query(value="SELECT MAX(purchase_order_no)+1 FROM detail_purchase_order",nativeQuery = true)
	public Integer findMaxOrderNo();
	
	@Modifying(clearAutomatically = true,flushAutomatically = true)//select 문이 아님을 나타낸다 
	@Transactional
	@Query(value="UPDATE procurement_plan myp SET myp.detail_purchase_order_code=:detailcode WHERE myp.code=:pcode",nativeQuery = true)
	public void myUpdate(@Param("detailcode")Integer detailcode,@Param("pcode")Integer pcode);
	
	@Transactional
	@Query(value="UPDATE procurement_plan AS pp SET pp.detail_purchase_order_code=:detailcode WHERE pp.code=:pcode",nativeQuery = true)
	public void myUpdate2(@Param("detailcode")Integer detailcode,@Param("pcode")Integer pcode);
	
	//문자용 실험
//	@Query(value="SELECT distinct pp.code, com.name, pp.due_date, cont.unit_price, mat.code, mat.name FROM company AS com JOIN contract AS cont JOIN procurement_plan AS pp JOIN mrp AS m"
//			+ "JOIN material AS mat"
//			+ "WHERE pp.code=:mycode AND cont.no=pp.contract_no AND cont.company_no=com.no AND pp.mrp_code=m.code AND mat.code=m.material_code;",nativeQuery = true)
//	
	@Query(value="SELECT distinct pp.code, com.name, pp.due_date, cont.unit_price, mat.code, mat.name FROM company AS com JOIN contract AS cont JOIN procurement_plan AS pp JOIN mrp AS m JOIN material AS mat WHERE pp.code=:mycode AND cont.no=pp.contract_no AND cont.company_no=com.no AND pp.mrp_code=m.code AND mat.code=m.material_code",nativeQuery = true)
	public void dd(@Param("mycode")Integer myPublish);
	
	//\r\n
	//List 실험 이 아래가 되는 코드
	@Query(value="SELECT distinct pp.code AS ppcode , com.name AS cname, pp.due_date, cont.unit_price, mat.code AS mcode, mat.name AS mname, pp.amount AS ppamount FROM company AS com JOIN contract AS cont JOIN procurement_plan AS pp JOIN mrp AS m JOIN material AS mat WHERE pp.code=:mycode AND cont.no=pp.contract_no AND cont.company_no=com.no AND pp.mrp_code=m.code AND mat.code=m.material_code",nativeQuery = true)
	public List<Object[]> myDetailList(@Param("mycode")Integer mycode);
	
	//@Query(value="SELECT * FROM contract",nativeQuery = true)
	//public List<Object[]> myDetailList();
	/**
	 * 선택한 것의 정보를 갖고 온다
	 * @param myOrder
	 */
	@Query(value="SELECT * FROM procurement_plan myp WHERE myp.code=:mycode",nativeQuery = true)
	public void myPublish(@Param("mycode")Integer myOrder);
	
	/**
	 * 선택한 것의 정보를 갖고 온다
	 * @param myOrder
	 */
	@Query(value="SELECT * FROM procurement_plan myp WHERE myp.code=:mycode",nativeQuery = true)
	public void myPublishList(@Param("mycode")List<PurchaseOrder> myOrder);
	
	@Query(value="SELECT * FROM procurement_plan myp WHERE myp.code=:mycode",nativeQuery = true)
	public static String my() {
		return null;
	}
}
