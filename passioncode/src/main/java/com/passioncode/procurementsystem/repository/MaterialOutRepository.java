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
	
	
	@Query(value="SELECT t1.tdate tdate, GREATEST(t1.입고금액-t2.출고금액,0) 재고금액 "
			+ "FROM  "
			+ "(SELECT t1.tdate tdate, nvl(t2.입고금액,0) 입고금액  "
			+ "FROM "
			+ "(WITH RECURSIVE CTE  AS ( "
			+ "        SELECT DATE_FORMAT(:startDate, '%Y-%m-%d') AS DT FROM DUAL "
			+ "        UNION ALL "
			+ "        SELECT DATE_ADD(DT, INTERVAL 1 DAY) FROM CTE "
			+ "        WHERE DT < DATE_FORMAT(:endDate, '%Y-%m-%d') "
			+ ") "
			+ "SELECT date_format(DT,'%Y-%m-%d') AS TDATE FROM CTE) t1  "
			+ "left outer JOIN "
			+ "(SELECT nvl(t2.입고일,t1.TDATE) 입고일, nvl(t2.입고량,0) 입고량, nvl(t2.입고금액,0) 입고금액, t2.대분류코드 "
			+ "FROM  "
			+ "(WITH RECURSIVE CTE  AS ( "
			+ "        SELECT DATE_FORMAT(:startDate, '%Y-%m-%d') AS DT FROM DUAL "
			+ "        UNION ALL "
			+ "        SELECT DATE_ADD(DT, INTERVAL 1 DAY) FROM CTE "
			+ "        WHERE DT < DATE_FORMAT(:endDate, '%Y-%m-%d') "
			+ ") "
			+ "SELECT date_format(DT,'%Y-%m-%d') AS TDATE FROM CTE) t1  "
			+ "left outer JOIN "
			+ "(SELECT t2.입고일, SUM(t2.입고량) 입고량,SUM(t2.입고금액) 입고금액, t2.대분류코드 "
			+ "FROM "
			+ "(SELECT t1.입고일, SUM(t1.입고량) 입고량,SUM(t1.입고금액) 입고금액, t1.중분류코드, t1.대분류코드 "
			+ "FROM "
			+ "(SELECT DATE_FORMAT(midpoppconmrpmc.date, '%Y-%m-%d') 입고일, "
			+ "sum(midpoppconmrpmc.dpoamount) 입고량, "
			+ "round(avg(midpoppconmrpmc.unit_price),-1) 평균입고단가, "
			+ "sum(midpoppconmrpmc.dpoamount) * round(avg(midpoppconmrpmc.unit_price),-1) 입고금액, "
			+ "midpoppconmrpmc.material_code 품목코드, "
			+ "midpoppconmrpmc.middle_category_code 중분류코드, "
			+ "mc.large_category_code 대분류코드 "
			+ "FROM "
			+ "(SELECT midpoppconmrp.code code,midpoppconmrp.date date,midpoppconmrp.`status` STATUS,midpoppconmrp.transaction_status transaction_status,midpoppconmrp.detail_purchase_order_code detail_purchase_order_code, "
			+ "midpoppconmrp.dpoamount dpoamount,midpoppconmrp.mrp_code mrp_code,midpoppconmrp.contract_no contract_no, "
			+ "midpoppconmrp.unit_price unit_price, "
			+ "midpoppconmrp.material_code material_code, "
			+ "m.middle_category_code  "
			+ "FROM "
			+ "(SELECT midpoppcon.code code,midpoppcon.date date,midpoppcon.`status` STATUS,midpoppcon.transaction_status transaction_status,midpoppcon.detail_purchase_order_code detail_purchase_order_code, "
			+ "midpoppcon.dpoamount dpoamount,midpoppcon.mrp_code mrp_code,midpoppcon.contract_no contract_no, "
			+ "midpoppcon.unit_price unit_price, "
			+ "mrp.material_code material_code "
			+ "FROM "
			+ "(SELECT  midpopp.code code,midpopp.date date,midpopp.`status` STATUS,midpopp.transaction_status transaction_status,midpopp.detail_purchase_order_code detail_purchase_order_code, "
			+ "midpopp.dpoamount dpoamount,midpopp.mrp_code mrp_code,midpopp.contract_no contract_no, "
			+ "con.unit_price unit_price "
			+ "FROM "
			+ "(SELECT midpo.code code,midpo.date date,midpo.`status` STATUS,midpo.transaction_status transaction_status,midpo.detail_purchase_order_code detail_purchase_order_code, "
			+ "midpo.dpoamount dpoamount,pp.mrp_code mrp_code,pp.contract_no contract_no "
			+ "FROM "
			+ "(SELECT mi.code code,mi.date date,mi.`status` STATUS,mi.transaction_status transaction_status,mi.detail_purchase_order_code detail_purchase_order_code,dpo.amount dpoamount "
			+ "FROM material_in mi JOIN detail_purchase_order dpo ON mi.detail_purchase_order_code=dpo.code where mi.status =1) midpo "
			+ "JOIN procurement_plan pp ON midpo.detail_purchase_order_code=pp.detail_purchase_order_code) midpopp "
			+ "JOIN contract con ON midpopp.contract_no=con.no) midpoppcon "
			+ "JOIN mrp mrp ON midpoppcon.mrp_code=mrp.code) midpoppconmrp "
			+ "JOIN material m ON midpoppconmrp.material_code=m.code) midpoppconmrpmc "
			+ "JOIN middle_category mc ON midpoppconmrpmc.middle_category_code=mc.code "
			+ "GROUP BY 품목코드,입고일) t1 "
			+ "GROUP BY t1.중분류코드, t1.입고일) t2 "
			+ "GROUP BY t2.대분류코드, t2.입고일) t2 "
			+ "ON t1.TDATE=t2.입고일 WHERE t2.대분류코드=:LargeCode) t2 "
			+ "ON t1.tdate=t2.입고일) t1 "
			+ "JOIN "
			+ "(SELECT t1.tdate tdate, nvl(t2.출고금액,0) 출고금액 "
			+ "FROM  "
			+ "(WITH RECURSIVE CTE  AS ( "
			+ "        SELECT DATE_FORMAT(:startDate, '%Y-%m-%d') AS DT FROM DUAL "
			+ "        UNION ALL "
			+ "        SELECT DATE_ADD(DT, INTERVAL 1 DAY) FROM CTE "
			+ "        WHERE DT < DATE_FORMAT(:endDate, '%Y-%m-%d') "
			+ ") "
			+ "SELECT date_format(DT,'%Y-%m-%d') AS TDATE FROM CTE) t1 "
			+ "left outer JOIN "
			+ "(SELECT nvl(t2.출고일,t1.TDATE) 출고일, nvl(t2.출고량,0) 출고량, nvl(t2.출고금액,0) 출고금액, t2.대분류코드 대분류코드 "
			+ "FROM  "
			+ "(WITH RECURSIVE CTE  AS ( "
			+ "        SELECT DATE_FORMAT(:startDate, '%Y-%m-%d') AS DT FROM DUAL "
			+ "        UNION ALL "
			+ "        SELECT DATE_ADD(DT, INTERVAL 1 DAY) FROM CTE "
			+ "        WHERE DT < DATE_FORMAT(:endDate, '%Y-%m-%d') "
			+ ") "
			+ "SELECT date_format(DT,'%Y-%m-%d') AS TDATE FROM CTE) t1  "
			+ "left outer JOIN "
			+ "(SELECT t2.출고일, SUM(t2.출고량) 출고량, SUM(t2.출고금액) 출고금액, t2.대분류코드 "
			+ "FROM "
			+ "(SELECT t1.출고일, SUM(t1.출고량) 출고량, SUM(t1.출고금액) 출고금액, t1.중분류코드, t1.대분류코드 "
			+ "FROM "
			+ "(SELECT DATE_FORMAT(momrpppconm.date, '%Y-%m-%d') 출고일, "
			+ "sum(momrpppconm.mrpAmount) 출고량, round(avg(momrpppconm.unit_price),-1) 평균출고단가, sum(momrpppconm.mrpAmount) *  round(avg(momrpppconm.unit_price),-1) 출고금액,        "
			+ "momrpppconm.material_code 품목코드, "
			+ "momrpppconm.middle_category_code 중분류코드, mc.large_category_code 대분류코드 "
			+ "FROM "
			+ "(SELECT  momrpppcon.date DATE, "
			+ "momrpppcon.mrpAmount mrpAmount, momrpppcon.material_code material_code,momrpppcon.unit_price unit_price, "
			+ "m.middle_category_code middle_category_code "
			+ "FROM "
			+ "(SELECT momrppp.date DATE, "
			+ "momrppp.mrpAmount mrpAmount, momrppp.material_code material_code,con.unit_price unit_price "
			+ "FROM "
			+ "(SELECT momrp.code CODE, momrp.date DATE, momrp.status STATUS, momrp.mrp_code mrp_code, "
			+ "momrp.mrpAmount mrpAmount, momrp.material_code material_code,pp.contract_no contract_no  "
			+ "FROM  "
			+ "(SELECT mo.code CODE, mo.date DATE, mo.status STATUS, mo.mrp_code mrp_code, "
			+ "mrp.amount mrpAmount, mrp.material_code material_code "
			+ "FROM material_out mo JOIN mrp mrp ON mo.mrp_code=mrp.code WHERE mo.status=1) momrp "
			+ "JOIN procurement_plan pp ON momrp.mrp_code=pp.mrp_code) momrppp "
			+ "JOIN contract con ON momrppp.contract_no=con.no) momrpppcon "
			+ "JOIN material m ON momrpppcon.material_code=m.code) momrpppconm "
			+ "JOIN middle_category mc ON momrpppconm.middle_category_code=mc.code "
			+ "GROUP BY 품목코드, DATE_FORMAT(momrpppconm.date, '%Y-%m-%d')) t1 "
			+ "GROUP BY t1.중분류코드, t1.출고일) t2 "
			+ "GROUP BY t2.대분류코드, t2.출고일) t2 "
			+ "ON t1.TDATE=t2.출고일 WHERE t2.대분류코드=:LargeCode) t2 "
			+ "ON t1.tdate=t2.출고일) t2 "
			+ "ON t1.tdate=t2.tdate", nativeQuery = true)
	public List<Object[]> getCalculStockTotalPriceForLC(@Param("startDate") String startDate,@Param("endDate") String endDate,@Param("LargeCode") String LargeCode);	
	
}
