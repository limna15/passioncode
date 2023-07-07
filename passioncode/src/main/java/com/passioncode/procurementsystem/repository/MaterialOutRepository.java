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
	 * 재고산출 화면!! 재고산출하는 쿼리!!! <br>
	 * 첫날짜, 끝날짜를 넣으면 그 기간에 모든 품목의 재고산출이 계산됨
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	@Query(value="SELECT mc.large_category_code 대분류코드,lc.category 대분류이름, m.middle_category_code 중분류코드,mc.category 중분류이름, t1.품목코드 품목코드, m.name 품목명, "
			+ "t1.입고량 입고량, t1.출고량 출고량, t1.재고량 재고량,t1.재고단가 재고단가 "
			+ "FROM "
			+ "(select  품목코드,SUM(입고량) 입고량, SUM( 입고금액) 입고금액, SUM(출고량) 출고량, SUM(출고금액) 출고금액,SUM( 재고량) 재고량 , SUM( 재고금액) 재고금액, SUM( if( 재고량 =0, 0, 재고단가)) 재고단가 "
			+ "FROM "
			+ "(SELECT 날짜, 품목코드, 입고량, 입고금액, 출고량, 출고금액, if(입고량-출고량 <=0, 0, 입고량-출고량) 재고량,if(입고금액-출고금액 <=0,0, 입고금액-출고금액) 재고금액, round(nvl((입고금액-출고금액)/(입고량-출고량),0),-1)  재고단가 "
			+ "FROM "
			+ "(SELECT t1.TDATE 날짜, t1.품목코드 품목코드, ifnull(t1.입고량,0) 입고량,nvl(t1.입고금액,0) 입고금액, ifnull(t2.출고량,0) 출고량, nvl(t2.출고금액,0) 출고금액 "
			+ "FROM "
			+ "(SELECT t1.TDATE tdate, t1.품목코드 품목코드, t2.입고량 입고량, t2.평균입고단가 평균입고단가, t2.입고금액 입고금액 "
			+ "FROM "
			+ "(SELECT * "
			+ "FROM "
			+ "(WITH RECURSIVE CTE  AS ( "
			+ "        SELECT DATE_FORMAT(:startDate, '%Y-%m-%d') AS DT FROM DUAL "
			+ "        UNION ALL "
			+ "        SELECT DATE_ADD(DT, INTERVAL 1 DAY) FROM CTE "
			+ "        WHERE DT < DATE_FORMAT(:endDate, '%Y-%m-%d') "
			+ ") "
			+ "SELECT date_format(DT,'%Y-%m-%d') AS TDATE FROM CTE) t1, "
			+ "(SELECT distinct(mrp.material_code) 품목코드 "
			+ "FROM "
			+ "(SELECT  midpopp.code code,midpopp.date date,midpopp.`status` STATUS,midpopp.transaction_status transaction_status,midpopp.detail_purchase_order_code detail_purchase_order_code, "
			+ "midpopp.dpoamount dpoamount,midpopp.mrp_code mrp_code,midpopp.contract_no contract_no, "
			+ "con.unit_price unit_price "
			+ "FROM "
			+ "(SELECT midpo.code code,midpo.date date,midpo.`status` STATUS,midpo.transaction_status transaction_status,midpo.detail_purchase_order_code detail_purchase_order_code, "
			+ "midpo.dpoamount dpoamount,pp.mrp_code mrp_code,pp.contract_no contract_no "
			+ "FROM "
			+ "(SELECT mi.code code,mi.date date,mi.`status` STATUS,mi.transaction_status transaction_status,mi.detail_purchase_order_code detail_purchase_order_code,dpo.amount dpoamount "
			+ "FROM material_in mi JOIN detail_purchase_order dpo ON mi.detail_purchase_order_code=dpo.code WHERE mi.`status`=1) midpo "
			+ "JOIN procurement_plan pp ON midpo.detail_purchase_order_code=pp.detail_purchase_order_code) midpopp "
			+ "JOIN contract con ON midpopp.contract_no=con.no) midpoppcon "
			+ "JOIN mrp mrp ON midpoppcon.mrp_code=mrp.code "
			+ "GROUP BY material_code,DATE_FORMAT(midpoppcon.date, '%Y-%m-%d') "
			+ "UNION "
			+ "SELECT DISTINCT( momrpppconm.material_code) 품목코드 "
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
			+ "GROUP BY 품목코드, DATE_FORMAT(momrpppconm.date, '%Y-%m-%d')) t2) t1 "
			+ "LEFT JOIN "
			+ "(SELECT DATE_FORMAT(midpoppcon.date, '%Y-%m-%d') 입고일, "
			+ "sum(midpoppcon.dpoamount) 입고량, "
			+ "round(avg(midpoppcon.unit_price),-1) 평균입고단가, "
			+ "sum(midpoppcon.dpoamount)*round(avg(midpoppcon.unit_price),-1) 입고금액, "
			+ "mrp.material_code 품목코드 "
			+ "FROM "
			+ "(SELECT  midpopp.code code,midpopp.date date,midpopp.`status` STATUS,midpopp.transaction_status transaction_status,midpopp.detail_purchase_order_code detail_purchase_order_code, "
			+ "midpopp.dpoamount dpoamount,midpopp.mrp_code mrp_code,midpopp.contract_no contract_no, "
			+ "con.unit_price unit_price "
			+ "FROM "
			+ "(SELECT midpo.code code,midpo.date date,midpo.`status` STATUS,midpo.transaction_status transaction_status,midpo.detail_purchase_order_code detail_purchase_order_code, "
			+ "midpo.dpoamount dpoamount,pp.mrp_code mrp_code,pp.contract_no contract_no "
			+ "FROM "
			+ "(SELECT mi.code code,mi.date date,mi.`status` STATUS,mi.transaction_status transaction_status,mi.detail_purchase_order_code detail_purchase_order_code,dpo.amount dpoamount "
			+ "FROM material_in mi JOIN detail_purchase_order dpo ON mi.detail_purchase_order_code=dpo.code WHERE mi.`status`=1) midpo "
			+ "JOIN procurement_plan pp ON midpo.detail_purchase_order_code=pp.detail_purchase_order_code) midpopp "
			+ "JOIN contract con ON midpopp.contract_no=con.no) midpoppcon "
			+ "JOIN mrp mrp ON midpoppcon.mrp_code=mrp.code "
			+ "GROUP BY material_code,DATE_FORMAT(midpoppcon.date, '%Y-%m-%d')) t2 "
			+ "ON t1.TDATE=t2.입고일  AND t1.품목코드=t2.품목코드) t1 "
			+ "LEFT JOIN "
			+ "(SELECT DATE_FORMAT(momrpppconm.date, '%Y-%m-%d') 출고일, "
			+ "sum(momrpppconm.mrpAmount) 출고량, sum(momrpppconm.mrpAmount) *  round(avg(momrpppconm.unit_price),-1) 출고금액,        "
			+ "momrpppconm.material_code 품목코드 "
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
			+ "GROUP BY 품목코드, DATE_FORMAT(momrpppconm.date, '%Y-%m-%d')) t2 "
			+ "ON t1.tdate=t2.출고일 AND t1.품목코드=t2.품목코드) t1) t1 "
			+ "GROUP BY 품목코드) t1 "
			+ "JOIN material m "
			+ "ON m.code=t1.품목코드 "
			+ "JOIN middle_category mc "
			+ "ON mc.code=m.middle_category_code "
			+ "JOIN large_category lc "
			+ "ON mc.large_category_code=lc.code", nativeQuery = true)
	public List<Object[]> getCalculStockResult(@Param("startDate") String startDate,@Param("endDate") String endDate);	
	
	
	
	
	/**
	 * 재고금액 리포트(대분류) <br>
	 * 첫날짜, 끝날짜를 넣으면, 모든 기간별로 대분류 재고금액을 계산해줌
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	@Query(value="SELECT  t1.날짜 날짜,mc.large_category_code 대분류코드,lc.category 대분류이름, sum(t1.재고금액) 재고금액 "
			+ "FROM "
			+ "(select 날짜, 품목코드, 입고량, 입고금액, 출고량, 출고금액, 재고량,재고금액,if( 재고량 =0, 0, 재고단가) 재고단가 "
			+ "FROM "
			+ "(SELECT 날짜, 품목코드, 입고량, 입고금액, 출고량, 출고금액, if(입고량-출고량 <=0, 0, 입고량-출고량) 재고량,if(입고금액-출고금액 <=0,0, 입고금액-출고금액) 재고금액, round(nvl((입고금액-출고금액)/(입고량-출고량),0),-1)  재고단가 "
			+ "FROM "
			+ "(SELECT t1.TDATE 날짜, t1.품목코드 품목코드, ifnull(t1.입고량,0) 입고량,nvl(t1.입고금액,0) 입고금액, ifnull(t2.출고량,0) 출고량, nvl(t2.출고금액,0) 출고금액 "
			+ "FROM "
			+ "(SELECT t1.TDATE tdate, t1.품목코드 품목코드, t2.입고량 입고량, t2.평균입고단가 평균입고단가, t2.입고금액 입고금액 "
			+ "FROM "
			+ "(SELECT * "
			+ "FROM "
			+ "(WITH RECURSIVE CTE  AS ( "
			+ "        SELECT DATE_FORMAT(:startDate, '%Y-%m-%d') AS DT FROM DUAL "
			+ "        UNION ALL "
			+ "        SELECT DATE_ADD(DT, INTERVAL 1 DAY) FROM CTE "
			+ "        WHERE DT < DATE_FORMAT(:endDate, '%Y-%m-%d') "
			+ ") "
			+ "SELECT date_format(DT,'%Y-%m-%d') AS TDATE FROM CTE) t1, "
			+ "(SELECT distinct(mrp.material_code) 품목코드 "
			+ "FROM "
			+ "(SELECT  midpopp.code code,midpopp.date date,midpopp.`status` STATUS,midpopp.transaction_status transaction_status,midpopp.detail_purchase_order_code detail_purchase_order_code, "
			+ "midpopp.dpoamount dpoamount,midpopp.mrp_code mrp_code,midpopp.contract_no contract_no, "
			+ "con.unit_price unit_price "
			+ "FROM "
			+ "(SELECT midpo.code code,midpo.date date,midpo.`status` STATUS,midpo.transaction_status transaction_status,midpo.detail_purchase_order_code detail_purchase_order_code, "
			+ "midpo.dpoamount dpoamount,pp.mrp_code mrp_code,pp.contract_no contract_no "
			+ "FROM "
			+ "(SELECT mi.code code,mi.date date,mi.`status` STATUS,mi.transaction_status transaction_status,mi.detail_purchase_order_code detail_purchase_order_code,dpo.amount dpoamount "
			+ "FROM material_in mi JOIN detail_purchase_order dpo ON mi.detail_purchase_order_code=dpo.code WHERE mi.`status`=1) midpo "
			+ "JOIN procurement_plan pp ON midpo.detail_purchase_order_code=pp.detail_purchase_order_code) midpopp "
			+ "JOIN contract con ON midpopp.contract_no=con.no) midpoppcon "
			+ "JOIN mrp mrp ON midpoppcon.mrp_code=mrp.code "
			+ "GROUP BY material_code,DATE_FORMAT(midpoppcon.date, '%Y-%m-%d') "
			+ "UNION "
			+ "SELECT DISTINCT( momrpppconm.material_code) 품목코드 "
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
			+ "GROUP BY 품목코드, DATE_FORMAT(momrpppconm.date, '%Y-%m-%d')) t2) t1 "
			+ "LEFT JOIN "
			+ "(SELECT DATE_FORMAT(midpoppcon.date, '%Y-%m-%d') 입고일, "
			+ "sum(midpoppcon.dpoamount) 입고량, "
			+ "round(avg(midpoppcon.unit_price),-1) 평균입고단가, "
			+ "sum(midpoppcon.dpoamount)*round(avg(midpoppcon.unit_price),-1) 입고금액, "
			+ "mrp.material_code 품목코드 "
			+ "FROM "
			+ "(SELECT  midpopp.code code,midpopp.date date,midpopp.`status` STATUS,midpopp.transaction_status transaction_status,midpopp.detail_purchase_order_code detail_purchase_order_code, "
			+ "midpopp.dpoamount dpoamount,midpopp.mrp_code mrp_code,midpopp.contract_no contract_no, "
			+ "con.unit_price unit_price "
			+ "FROM "
			+ "(SELECT midpo.code code,midpo.date date,midpo.`status` STATUS,midpo.transaction_status transaction_status,midpo.detail_purchase_order_code detail_purchase_order_code, "
			+ "midpo.dpoamount dpoamount,pp.mrp_code mrp_code,pp.contract_no contract_no "
			+ "FROM "
			+ "(SELECT mi.code code,mi.date date,mi.`status` STATUS,mi.transaction_status transaction_status,mi.detail_purchase_order_code detail_purchase_order_code,dpo.amount dpoamount "
			+ "FROM material_in mi JOIN detail_purchase_order dpo ON mi.detail_purchase_order_code=dpo.code WHERE mi.`status`=1) midpo "
			+ "JOIN procurement_plan pp ON midpo.detail_purchase_order_code=pp.detail_purchase_order_code) midpopp "
			+ "JOIN contract con ON midpopp.contract_no=con.no) midpoppcon "
			+ "JOIN mrp mrp ON midpoppcon.mrp_code=mrp.code "
			+ "GROUP BY material_code,DATE_FORMAT(midpoppcon.date, '%Y-%m-%d')) t2 "
			+ "ON t1.TDATE=t2.입고일  AND t1.품목코드=t2.품목코드) t1 "
			+ "LEFT JOIN "
			+ "(SELECT DATE_FORMAT(momrpppconm.date, '%Y-%m-%d') 출고일, "
			+ "sum(momrpppconm.mrpAmount) 출고량, sum(momrpppconm.mrpAmount) *  round(avg(momrpppconm.unit_price),-1) 출고금액,        "
			+ "momrpppconm.material_code 품목코드 "
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
			+ "GROUP BY 품목코드, DATE_FORMAT(momrpppconm.date, '%Y-%m-%d')) t2 "
			+ "ON t1.tdate=t2.출고일 AND t1.품목코드=t2.품목코드) t1) t1) t1 "
			+ "JOIN material m ON m.code=t1.품목코드 "
			+ "JOIN middle_category mc ON m.middle_category_code=mc.code "
			+ "JOIN large_category lc ON lc.code=mc.large_category_code "
			+ "GROUP BY mc.large_category_code, t1.날짜", nativeQuery = true)
	public List<Object[]> getCalculStockReportForLC(@Param("startDate") String startDate,@Param("endDate") String endDate);	
	
	
}
