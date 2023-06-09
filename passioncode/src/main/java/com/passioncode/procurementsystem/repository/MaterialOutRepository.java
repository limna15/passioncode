package com.passioncode.procurementsystem.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.passioncode.procurementsystem.entity.MRP;
import com.passioncode.procurementsystem.entity.MaterialOut;

public interface MaterialOutRepository extends JpaRepository<MaterialOut, Integer> {

	/**
	 * mrp를 이용한 MaterialOut 출고 엔티티 찾기
	 * @param mrp
	 * @return
	 */
	List<MaterialOut> findByMrp(MRP mrp);
}
