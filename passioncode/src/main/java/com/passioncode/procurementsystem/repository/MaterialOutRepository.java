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
	public MaterialOut findByMrp(MRP mrp);
	
	/**
	 * MRP을 이용하여 자재출고상태(완료, 버튼) 체크하기 <br>
	 * 0= 버튼, 1= 완료
	 * @param mrp
	 * @return
	 */	
	public Boolean existsByMrp(MRP mrp);
}
