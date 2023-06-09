package com.passioncode.procurementsystem.service;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import com.passioncode.procurementsystem.dto.MaterialDTO;
import com.passioncode.procurementsystem.entity.Material;
import com.passioncode.procurementsystem.repository.ContractRepository;
import com.passioncode.procurementsystem.repository.MaterialRepository;

import lombok.extern.log4j.Log4j2;

@Log4j2
@SpringBootTest
public class MaterialSeviceTests {
	
	@Autowired
	MaterialService materialService;
	
	@Test
	public void materialFindByIdTest() {
		log.info("material 값 보자~ : "+materialService.getMaterial("BPa0001"));
	}	
	
	@Transactional
	@Test
	public void entityToDTOTest() {
		log.info("어디 materialDTO 잘가져오는지 결과좀 봐보자 : "+materialService.entityToDTO(materialService.getMaterial("BPa0001")));
		log.info("어디 materialDTO 잘가져오는지 결과좀 봐보자 : "+materialService.entityToDTO(materialService.getMaterial("CGa0002")));
	}
	
	@Transactional
	@Test
	public void dtoToEntityTest() {
		MaterialDTO materialDTO = materialService.entityToDTO(materialService.getMaterial("BPa0001"));
		log.info("엔티티로 잘 바꾸나 봐보자 : "+materialService.dtoToEntity(materialDTO));
	}
	
	@Transactional
	@Test
	public void materialDTOListTest() {
		log.info("어디 DTO 리스트 봐보자 : "+materialService.getDTOList());
		log.info("리스트 크기 봐보자 : "+materialService.getDTOList().size());
	}
	
	@Test
	public void contractStatusCheckTest() {
		log.info("이거 계약상태 존재 여부 체크좀 해보쟈 : "+materialService.contractStatusCheck(materialService.getMaterial("PCa0001")));
	}
	
	
//	트랜잭션 처리는 테스트에서 쓰면 자동 롤백된다! 그래서 지연로딩부분때문에 하는거 아니면 쓰면 안돼! 삽입 안돼!
//	@Transactional
//	@Commit
//	트랙젹션 어노테이션 쓰려면 쓰고, 커밋 어노테이션 추가해줘야지! 제대로 커밋됨!
	@Test
	public void materialRegisterTest() {
		//현재 등록화면에서 계약상태는 없음, 애초에 처음 등록하는 화면이라 당연히 계약상태는 미완료라서
		MaterialDTO materialDTO =  MaterialDTO.builder().code("PCa0001").name("PCB보드").size("PC001").quality("ABC")
									.spec("10*10cm").drawingNo("PC3333").drawingFile("서비스로 입력 테스트중").shareStatus("공용")
									.largeCategoryName("플라스틱").middleCategoryName("케이스")
									.contractStatus(materialService.contractStatusCheck(materialService.getMaterial("PCa0001")))
									.largeCategoryCode("PP0001")
									.middleCategoryCode("CC0001").build();
		log.info("만들어진 materialDTO 보자 : "+materialDTO);
//		Material material = materialService.dtoToEntity(materialDTO);
//		log.info("dto에서 엔티티로 바뀐거 보자 : "+material);
		log.info("등록이 되었는지 보자(등록된 품목코드) : "+materialService.register(materialDTO));
	}
	

}
