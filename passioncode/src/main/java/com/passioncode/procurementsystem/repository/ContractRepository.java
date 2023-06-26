package com.passioncode.procurementsystem.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.passioncode.procurementsystem.entity.Contract;
import com.passioncode.procurementsystem.entity.Material;

public interface ContractRepository extends JpaRepository<Contract, Integer> {
	
	/**
	 * 품목을 이용하여 계약서 찾기
	 * @param material
	 * @return
	 */
	public List<Contract> findByMaterial(Material material);
	
	/**
	 * 품목을 이용하여 계약상태(미완료,완료) 체크하기 <br>
	 * 존재여부=계약상태, False=미완료, True=완료
	 * @param material
	 * @return
	 */
	public Boolean existsByMaterial(Material material);
	
	/**
	 * 회사이름과 품목코드를 이용해서 계약서 찾기
	 * @param companyName
	 * @param materialCode
	 * @return
	 */
	@Query(value="SELECT con.`no`,con.material_code,con.company_no,con.supply_lt,con.unit_price,con.deal_condition,con.contract_file FROM contract con "
			+ "JOIN company com ON con.company_no=com.no WHERE com.name LIKE %:companyName% AND con.material_code = :materialCode ", nativeQuery = true)
	public List<Contract> findByCompanyNameAndMaterialCode(@Param("companyName") String companyName, @Param("materialCode") String materialCode);
		
	/**
	 * 품목코드를 이용해서 계약서 찾기
	 * @param companyName
	 * @param materialCode
	 * @return
	 */
	@Query(value="SELECT con.`no`,con.material_code,con.company_no,con.supply_lt,con.unit_price,con.deal_condition,con.contract_file FROM contract con "
			+ "JOIN company com ON con.company_no=com.no WHERE con.material_code = :materialCode ", nativeQuery = true)
	public List<Contract> findByMaterialCode(@Param("materialCode") String materialCode);
	
	
}
