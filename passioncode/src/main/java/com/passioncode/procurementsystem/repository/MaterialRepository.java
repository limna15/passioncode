package com.passioncode.procurementsystem.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import com.passioncode.procurementsystem.entity.Material;

public interface MaterialRepository extends JpaRepository<Material, String> {
	
	/**
	 * 품목명을 포한한 name을 이용하여 품목 찾기 (대소문자 무시하고 찾음)
	 * @param name
	 * @return
	 */
	public List<Material> findByNameContainingIgnoreCase(String name);
	
	/**
	 * 전체 품목리스트 정렬해서 가져오기 <br>
	 * 계약서 테이블과 조인해서, 계약상태 미완료가 상단으로, 그리고 기본 품목코드 오름차순 정렬으로 세팅
	 * @return
	 */
	@Query(value="SELECT distinct CODE,NAME,share_status,size,quality,spec,drawing_no,drawing_file,middle_category_code FROM material "
			+ "left outer JOIN contract ON CODE=material_code ORDER BY material_code,code " , nativeQuery = true)
	public List<Material> getListWithSort();
	
	/**
	 * 계약 미완료인 품목리스트 정렬해서 가져오기 (품목코드 오름차순) <br>
	 * 계약서 테이블과 조인해서, 계약상태 미완료인것만 + 품목코드 오름차순 정렬
	 * @return
	 */
	@Query(value="SELECT CODE,NAME,share_status,size,quality,spec,drawing_no,drawing_file,middle_category_code FROM material "
			+ "left outer JOIN contract ON CODE=material_code WHERE material_code IS NULL ORDER BY NO,code ", nativeQuery = true)
	public List<Material> getListNoContract();
	
	/**
	 * 품목코드(앞에 두문자 CG)를 이용해서 품목 찾기 (대소문자 구별함)
	 * @param code
	 * @return
	 */
	List<Material> findByCodeContaining(String code);
	
}
