package com.passioncode.procurementsystem.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.passioncode.procurementsystem.entity.MRP;
import com.passioncode.procurementsystem.entity.MaterialOut;

public interface MaterialOutRepository extends JpaRepository<MaterialOut, Integer> {

	/**
	 * mrp를 이용한 MaterialOut 출고 엔티티 찾기
	 * @param mrp
	 * @return
	 */
	public MaterialOut findByMrp(MRP mrp);
	
	/**
	 * MRP을 이용하여 자재출고상태(완료, 버튼) 체크하기 <br>
	 * 0= 버튼, 1= 완료
	 * @param mrp
	 * @return
	 */	
	public Boolean existsByMrp(MRP mrp);
	
	/**
	 * 입고수량, 단가 가져오기 <br>
	 * 입고 + 세부구매발주서 + 조달계획 + 계약서 + mrp 조인해서 -> 품목코드,날짜 를 이용해서 해당 입고수량,단가 가져오기
	 * @param materialCode
	 * @param date
	 * @return
	 */
	@Query(value="SELECT midpoppcon.dpoamount dpoamount,midpoppcon.unit_price unit_price "
			+ "FROM "
			+ "(SELECT  midpopp.code code,midpopp.date date,midpopp.`status` STATUS,midpopp.transaction_status transaction_status,midpopp.detail_purchase_order_code detail_purchase_order_code, "
			+ "midpopp.dpoamount dpoamount,midpopp.mrp_code mrp_code,midpopp.contract_no contract_no, "
			+ "con.unit_price unit_price "
			+ "FROM "
			+ "(SELECT midpo.code code,midpo.date date,midpo.`status` STATUS,midpo.transaction_status transaction_status,midpo.detail_purchase_order_code detail_purchase_order_code, "
			+ "midpo.dpoamount dpoamount,pp.mrp_code mrp_code,pp.contract_no contract_no "
			+ "FROM "
			+ "(SELECT mi.code code,mi.date date,mi.`status` STATUS,mi.transaction_status transaction_status,mi.detail_purchase_order_code detail_purchase_order_code,dpo.amount dpoamount "
			+ "FROM material_in mi JOIN detail_purchase_order dpo ON mi.detail_purchase_order_code=dpo.code) midpo "
			+ "JOIN procurement_plan pp ON midpo.detail_purchase_order_code=pp.detail_purchase_order_code) midpopp "
			+ "JOIN contract con ON midpopp.contract_no=con.no) midpoppcon "
			+ "JOIN mrp mrp ON midpoppcon.mrp_code=mrp.code "
			+ "WHERE midpoppcon.STATUS=1 AND date_format(midpoppcon.date, '%Y-%m-%d' ) = :date AND mrp.material_code = :materialCode", nativeQuery = true)
	public List<Object[]> getDPOAmountAndUnitPriceByMaterialCodeAndMIDate(@Param("materialCode") String materialCode,@Param("date") String date);
	
	
	/**
	 * 출고수량, 단가 가져오기 <br>
	 * 출고 + mrp + 조달계획 + 계약서 조인해서 -> 품목코드,날짜 를 이용해서 해당 출고수량,단가 가져오기
	 * @param materialCode
	 * @param date
	 * @return
	 */
	@Query(value="SELECT momrppp.mrpAmount mrpAmount, con.unit_price unit_price "
			+ "FROM "
			+ "(SELECT momrp.code CODE, momrp.date DATE, momrp.status STATUS, momrp.mrp_code mrp_code, "
			+ "momrp.mrpAmount mrpAmount, momrp.material_code material_code,pp.contract_no contract_no "
			+ "FROM "
			+ "(SELECT mo.code CODE, mo.date DATE, mo.status STATUS, mo.mrp_code mrp_code, "
			+ "mrp.amount mrpAmount, mrp.material_code material_code "
			+ "FROM material_out mo JOIN mrp mrp ON mo.mrp_code=mrp.code) momrp "
			+ "JOIN procurement_plan pp ON momrp.mrp_code=pp.mrp_code) momrppp "
			+ "JOIN contract con ON momrppp.contract_no=con.no "
			+ "WHERE momrppp.status = 1 AND date_format(momrppp.date, '%Y-%m-%d' )= :date AND momrppp.material_code= :materialCode", nativeQuery = true)
	public List<Object[]> getMRPAmountAndUnitPriceByMaterialCodeAndMIDate(@Param("materialCode") String materialCode,@Param("date") String date);
	
	
	
	
	
	
	
	
	
}
