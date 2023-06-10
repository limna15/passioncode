package com.passioncode.procurementsystem.repository;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import com.passioncode.procurementsystem.dto.MaterialDTO;
import com.passioncode.procurementsystem.entity.Material;
import com.passioncode.procurementsystem.entity.MiddleCategory;

import lombok.extern.log4j.Log4j2;

@SpringBootTest
@Log4j2
public class MaterialRepositoryTests {
	
	@Autowired
	MaterialRepository materialRepository;
	
	@Autowired
	MiddleCategoryRepository middleCategoryRepository;
	
	@Autowired
	ContractRepository contractRepository;
	
	//트랜잭션 처리는 테스트에서 쓰면 자동 롤백된다! 그래서 지연로딩부분때문에 하는거 아니면 쓰면 안돼! 삽입 안돼!
//	@Transactional
//	@Commit
//	트랙젹션 어노테이션 쓰려면 쓰고, 커밋 어노테이션 추가해줘야지! 제대로 커밋됨!
	@Test
	public void InsertTest() {
//		Optional<MiddleCategory> result = middleCategoryRepository.findById("GG0001");		//철, 기어
		Optional<MiddleCategory> result = middleCategoryRepository.findById("SS0001");		//보드, 센서
		
		MiddleCategory middleCategory = result.get();
		
//		Material material = Material.builder().code("CGa0001").name("Gear").size("G1").quality("B").spec("12mm").shareStatus(0)
//							.drawingNo("B23").drawingFile("테스트중").middleCategory(middleCategory).build();
		Material material = Material.builder().code("BSa0001").name("Sensor").size("S1").quality("A").spec("알루미늄").shareStatus(1)
				.drawingNo("N34").drawingFile("테스트중").middleCategory(middleCategory).build();
		
		materialRepository.save(material);
		
	}
	
	@Transactional
	@Test
	public void readCategoryTest() {
		Optional<Material> result = materialRepository.findById("BPa0001");
		Material material = result.get();
		log.info("읽어온 material 한번 보자 : "+material);
		log.info("모야모야 바로 읽을 수 있는건가??? : "+materialRepository.findById("BPa0001").get());
		log.info("품목에서 외래키 중분류 보자 : "+material.getMiddleCategory());
		log.info("품목에서 외래키 중분류타고 대분류도 봐보자 : "+material.getMiddleCategory().getLargeCategory());
		
	}
	
	/**
	 * 공용여부 Integer -> 한글로 만들어주기 <br>
	 * 0 : 공용, 1 : 전용
	 * @param shareStatus
	 * @return
	 */
	public String shareStatusChangeToString(Integer shareStatus) {
		// 0 : 공용, 1 : 전용
		return shareStatus==0 ? "공용" : "전용";
	}
	
	/**
	 * 공용여부 한글 -> Integer 로 만들어주기 <br>
	 * 공용 : 0 , 전용 : 1
	 * @param shareStatus
	 * @return
	 */
	public Integer shareStatusChangeToInteger(String shareStatus) {
		// 공용 : 0 , 전용 : 1
		return shareStatus.equals("공용") ? 0 : 1;
	}
	
	/**
	 * 품목 엔티티를 이용하여 계약상태 체크하기 <br>
	 * 완료 : 계약상태 O, 미완료 : 계약상태 X
	 * @param material
	 * @return
	 */
	public String contractStatusCheck(Material material) {		
		//완료 : 계약상태 O, 미완료 : 계약상태 X
		return contractRepository.existsByMaterial(material) ? "완료" : "미완료";
	}
	
	@Transactional
	@Test
	public void materialDTOTest() {
//		Material material = materialRepository.findById("BPa0001").get();
		Material material = materialRepository.findById("CBa0001").get();
		MaterialDTO materialDTO =  MaterialDTO.builder().code(material.getCode()).name(material.getName()).size(material.getSize()).quality(material.getQuality())
														.spec(material.getSpec()).drawingNo(material.getDrawingNo()).drawingFile(material.getDrawingFile())
														.shareStatus(shareStatusChangeToString(material.getShareStatus())).stockAmount(material.getStockAmount())
														.largeCategoryName(material.getMiddleCategory().getLargeCategory().getCategory())
														.middleCategoryName(material.getMiddleCategory().getCategory())
														.contractStatus(contractStatusCheck(material))
														.largeCategoryCode(material.getMiddleCategory().getLargeCategory().getCode())
														.middleCategoryCode(material.getMiddleCategory().getCode()).build();
		log.info("DTO 테스트 결과 확인해보자 : "+materialDTO);
	}
	
	@Test
	public void readMateralBycode() {
		
		log.info("이게 안되는건가,,? : "+materialRepository.findById("PCa0001").orElse(null));
	}
	
	
//	트랜잭션 처리는 테스트에서 쓰면 자동 롤백된다! 그래서 지연로딩부분때문에 하는거 아니면 쓰면 안돼! 삽입 안돼!
//	@Transactional
//	@Commit
//	트랙젹션 어노테이션 쓰려면 쓰고, 커밋 어노테이션 추가해줘야지! 제대로 커밋됨!
	@Test
	public void InsertByDTOTest() {
		MaterialDTO materialDTO =  MaterialDTO.builder().code("PCa0001").name("PCB보드").size("PC001").quality("ABC")
														.spec("10*10cm").drawingNo("PC3333").drawingFile("서비스로 입력 테스트중").shareStatus("공용")
														.largeCategoryName("플라스틱").middleCategoryName("케이스").stockAmount(0)
														.contractStatus(contractStatusCheck((materialRepository.findById("PCa0001").orElse(null))))
														.largeCategoryCode("PP0001").middleCategoryCode("CC0001").build();
		log.info("만들어진 materialDTO 보자 : "+materialDTO);
		
		Material material = Material.builder().code(materialDTO.getCode()).name(materialDTO.getName()).shareStatus(shareStatusChangeToInteger(materialDTO.getShareStatus()))
												.stockAmount(materialDTO.getStockAmount()).size(materialDTO.getSize()).quality(materialDTO.getQuality()).spec(materialDTO.getSpec())
												.drawingNo(materialDTO.getDrawingNo()).drawingFile(materialDTO.getDrawingFile())
												.middleCategory(middleCategoryRepository.findById(materialDTO.getMiddleCategoryCode()).get()).build();
		log.info("dto에서 엔티티로 바뀐거 보자 : "+material);
		materialRepository.save(material);		
	}
	
	@Transactional
	@Commit	
	@Test
	public void ModifyByDTOTest() {
		MaterialDTO materialDTO =  MaterialDTO.builder().code("PCa0001").name("PCB보드수정중").size("PC001수정").quality("ABC수정")
									.spec("10*10cm수정").drawingNo("PC3333수정").drawingFile("서비스로 입력 테스트중 수정").shareStatus("공용")
									.largeCategoryName("플라스틱").middleCategoryName("케이스").stockAmount(0)
									.contractStatus(contractStatusCheck((materialRepository.findById("PCa0001").orElse(null))))
									.largeCategoryCode("PP0001")
									.middleCategoryCode("CC0001").build();
		log.info("만들어진 materialDTO 보자 : "+materialDTO);
		
		Material material = Material.builder().code(materialDTO.getCode()).name(materialDTO.getName()).shareStatus(shareStatusChangeToInteger(materialDTO.getShareStatus()))
											.stockAmount(materialDTO.getStockAmount()).size(materialDTO.getSize()).quality(materialDTO.getQuality()).spec(materialDTO.getSpec())
											.drawingNo(materialDTO.getDrawingNo()).drawingFile(materialDTO.getDrawingFile())
											.middleCategory(middleCategoryRepository.findById(materialDTO.getMiddleCategoryCode()).get()).build();

		log.info("dto에서 엔티티로 바뀐거 보자 : "+material);
		
		materialRepository.save(material);	
		
	}
	
	@Test
	public void DeleteByDTOTest() {
		MaterialDTO materialDTO = MaterialDTO.builder().code("PCa0001").build();
				
		materialRepository.deleteById(materialDTO.getCode());
		
		
		
	}
	
	
	
	

}
