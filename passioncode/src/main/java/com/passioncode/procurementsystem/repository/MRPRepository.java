package com.passioncode.procurementsystem.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import com.passioncode.procurementsystem.entity.MRP;

public interface MRPRepository extends JpaRepository<MRP, Integer> {
	
	/**
	 * 품목코드를 이용하여 MRP를 찾기 (정확한 품목코드 이용)
	 * @param materialCode
	 * @return
	 */
	public List<MRP> findBymaterialCode(String materialCode);
	
	/**
	 * 조달계획과 조인하여 조달계획이 미완료인 MRP 리스트 가져오기 <br>
	 * 소요일 오름차순으로 정렬해서 가져오기 <br>
	 * 하지만 계약완료 여부 체크는 저것을 받아서 계산해줘야해
	 * @return
	 */
	@Query(value="SELECT * FROM "
				+ "(SELECT  m.code,m.material_code,m.process,m.amount,m.date "
				+ "FROM procurement_plan pp RIGHT outer JOIN mrp m ON pp.mrp_code=m.code WHERE pp.code IS NULL) ppm ORDER BY ppm.date", nativeQuery = true)
	public List<MRP> getMRPJoinPPWithNotCompletePP();
	
	/**
	 * MRP 총개수 세기 <br>
	 * count 조회 시 Long으로 리턴 -> 쓰려면 리턴 데이터타입이 Long이어야함
	 */
	public Long countBy();
	

}
