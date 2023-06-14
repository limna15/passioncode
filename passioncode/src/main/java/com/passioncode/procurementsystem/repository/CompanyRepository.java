package com.passioncode.procurementsystem.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.passioncode.procurementsystem.entity.Company;

public interface CompanyRepository extends JpaRepository<Company, String> {
	
	/**
	 * 협력회사명을 포함한 name을 이용하여 협력회사 찾기
	 * @param name
	 * @return
	 */
	public List<Company> findByNameContaining(String name);
	
	/**
	 * 협력회사명을 포함한 name을 이용하여 거래가능한 협력회사 찾기
	 * @param keyword
	 * @return
	 */
	@Query(value="SELECT * FROM company WHERE NAME LIKE %:keyword% AND deal_status=1 ", nativeQuery = true)
	public List<Company> findByNameContainingWithDeal(@Param("keyword") String keyword);

//	@Query(value="SELECT * FROM company c WHERE c.NAME LIKE %:keyword% AND c.deal_status=1;")
//	public List<Company> getCompnayByNameWithDeal2(@Param("keyword") String keyword);
	
	
	
	
	
}

