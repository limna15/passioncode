package com.passioncode.procurementsystem.service;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;
import com.passioncode.procurementsystem.dto.MaterialDTO;
import com.passioncode.procurementsystem.entity.Material;
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
		log.info("어디 materialDTO 잘가져오는지 결과좀 봐보자 : "+materialService.entityToDTO(materialService.getMaterial("BP0001")));
		log.info("어디 materialDTO 잘가져오는지 결과좀 봐보자 : "+materialService.entityToDTO(materialService.getMaterial("CG0002")));
	}
	
	@Transactional
	@Test
	public void dtoToEntityTest() {
		MaterialDTO materialDTO = materialService.entityToDTO(materialService.getMaterial("BP0001"));
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
		log.info("이거 계약상태 존재 여부 체크좀 해보쟈 : "+materialService.contractStatusCheck(materialService.getMaterial("PC0001")));
	}
	
	
//	트랜잭션 처리는 테스트에서 쓰면 자동 롤백된다! 그래서 지연로딩부분때문에 하는거 아니면 쓰면 안돼! 삽입 안돼!
//	@Transactional
//	@Commit
//	트랙젹션 어노테이션 쓰려면 쓰고, 커밋 어노테이션 추가해줘야지! 제대로 커밋됨!
	@Test
	public void registerTest() {
		//현재 등록화면에서 계약상태는 없음, 애초에 처음 등록하는 화면이라 당연히 계약상태는 미완료라서
		//기존재고 수량 화면에는 없지만 숨겨서!! 0으로 기본값 세팅해주자
		MaterialDTO materialDTO =  MaterialDTO.builder().code("PC0001").name("PCB보드").size("PC001").quality("ABC")
														.spec("10*10cm").drawingNo("PC3333").drawingFile("서비스로 입력 테스트중").shareStatus("공용")
														.largeCategoryName("플라스틱").middleCategoryName("케이스")
														.largeCategoryCode("PP0001")
														.middleCategoryCode("CC0001").build();
		log.info("만들어진 materialDTO 보자 : "+materialDTO);
//		Material material = materialService.dtoToEntity(materialDTO);
//		log.info("dto에서 엔티티로 바뀐거 보자 : "+material);
		log.info("등록이 되었는지 보자(등록된 품목코드) : "+materialService.register(materialDTO));
	}
	
	@Test
	public void modifyTest() {
		MaterialDTO materialDTO =  MaterialDTO.builder().code("PC0001").name("PCB보드수정").size("PC001수정").quality("ABC수정")
														.spec("10*10cm수정").drawingNo("PC3333수정").drawingFile("서비스로 입력 테스트중 수정3").shareStatus("공용")
														.largeCategoryName("플라스틱").middleCategoryName("케이스")
														.largeCategoryCode("PP0001")
														.middleCategoryCode("CC0001").build();
		log.info("만들어진 materialDTO 보자 : "+materialDTO);
		materialService.modify(materialDTO);
	}
	
	@Transactional
	@Commit
	@Test
	public void deleteTest() {
		Material material = materialService.getMaterial("PCa0001");
		MaterialDTO materialDTO = materialService.entityToDTO(material);	//이때 DTO로 만들면서 외래키 지연로딩 쓰기 때문에 @Transactional 필요
		materialService.delete(materialDTO);								//하지만 테스트환경에서는 @Transactional 쓰면 자동롤백 되기때문에, 삭제 테스틀르 위해 @Commit 필요
		
	}
	
	@Transactional
	@Test
	public void drawingFileExistTest() {
		Material material = materialService.getMaterial("PC0002");
		log.info("도면 없는 품목 자체를 읽어보자 : "+material);
		MaterialDTO materialDTO = materialService.entityToDTO(material);
		log.info("어디 도면 없는 dto 찍어보자 : "+materialDTO);
	}
	
	@Test
	public void  getMaterialListByCodeContainingTest() {
		List<Material> materialList = materialService.getMaterialListByCodeContaining("TM");
		log.info("여기가 에러일거 같은데?? : "+materialList);
		
		if(materialList==null) {
			log.info("null 값인가???");
		}
		if(materialList.size()==0) {
			log.info("size가 0인건가??");
		}
	}
	
	

}
