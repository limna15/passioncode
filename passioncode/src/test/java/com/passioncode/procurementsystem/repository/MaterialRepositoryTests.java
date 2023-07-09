package com.passioncode.procurementsystem.repository;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;
import com.passioncode.procurementsystem.dto.MaterialDTO;
import com.passioncode.procurementsystem.entity.Material;
import com.passioncode.procurementsystem.entity.MiddleCategory;
import com.passioncode.procurementsystem.entity.ProcurementPlan;
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
	
	@Autowired
	ProcurementPlanRepository procurementPlanRepository;
	
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
														.shareStatus(shareStatusChangeToString(material.getShareStatus()))
														.largeCategoryName(material.getMiddleCategory().getLargeCategory().getCategory())
														.middleCategoryName(material.getMiddleCategory().getCategory())
														.largeCategoryCode(material.getMiddleCategory().getLargeCategory().getCode())
														.middleCategoryCode(material.getMiddleCategory().getCode()).build();
		log.info("DTO 테스트 결과 확인해보자 : "+materialDTO);
	}
	
	@Test
	public void readMateralBycode() {		
		log.info("이게 안되는건가,,? : "+materialRepository.findById("PCa0001").orElse(null));
	}
	
	
	@Test
	public void getDTOListTest() {
		List<Material> materialList = materialRepository.findAll(Sort.by(Sort.Direction.ASC, "code"));
		log.info("리스트 정렬된거 봐보자 : "+materialList);		
	}
	
	@Test
	public void getListBySortTest() {
		List<Material> materialList = materialRepository.getListWithSort();
		log.info("정렬된 리스트 잘되나 어디 한번 보자 : "+materialList);
	}
	
	
	
//	트랜잭션 처리는 테스트에서 쓰면 자동 롤백된다! 그래서 지연로딩부분때문에 하는거 아니면 쓰면 안돼! 삽입 안돼!
//	@Transactional
//	@Commit
//	트랙젹션 어노테이션 쓰려면 쓰고, 커밋 어노테이션 추가해줘야지! 제대로 커밋됨!
	@Test
	public void InsertByDTOTest() {
		MaterialDTO materialDTO =  MaterialDTO.builder().code("PCa0001").name("PCB보드").size("PC001").quality("ABC")
														.spec("10*10cm").drawingNo("PC3333").drawingFile("서비스로 입력 테스트중").shareStatus("공용")
														.largeCategoryName("플라스틱").middleCategoryName("케이스")
														.largeCategoryCode("PP0001").middleCategoryCode("CC0001").build();
		log.info("만들어진 materialDTO 보자 : "+materialDTO);
		
		Material material = Material.builder().code(materialDTO.getCode()).name(materialDTO.getName()).shareStatus(shareStatusChangeToInteger(materialDTO.getShareStatus()))
												.size(materialDTO.getSize()).quality(materialDTO.getQuality()).spec(materialDTO.getSpec())
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
									.largeCategoryName("플라스틱").middleCategoryName("케이스")
									.largeCategoryCode("PP0001")
									.middleCategoryCode("CC0001").build();
		log.info("만들어진 materialDTO 보자 : "+materialDTO);
		
		Material material = Material.builder().code(materialDTO.getCode()).name(materialDTO.getName()).shareStatus(shareStatusChangeToInteger(materialDTO.getShareStatus()))
											.size(materialDTO.getSize()).quality(materialDTO.getQuality()).spec(materialDTO.getSpec())
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
	
	@Test
	public void makeMaterialCodeTEst() {
		//대분류 CC0001 철, 중분류 GG0001 기어 -> 현재 품목 CG0002 까지 있는 상태
		String largeCategoryCode = "CC0001";
		String middleCategoryCode = "GG0001";
		String getLC = largeCategoryCode.substring(0,1);
		String getMC = middleCategoryCode.substring(0,1);
		String getLCWithMC = getLC + getMC;
		
		//대분류, 중분류 첫글자씩 합쳐온 글자를 검색해서 해당되는 모든 품목 찾아오기
		List<Material> materialList=materialRepository.findByCodeContaining(getLCWithMC);
		
		//찾아온 품목코드에서 앞에 두글자 빼고 숫자만 리스트로 담기
		List<Integer> onlyNumList = new ArrayList<>();
		for(Material m:materialList) {
			onlyNumList.add(Integer.parseInt(m.getCode().substring(2)));
		}
		
		//뽑아낸 숫자 리스트 중에서 최고값 (ex> 2)
		Integer maxOnlyNumByInt = Collections.max(onlyNumList);
		
		//최고 숫자에 +1 하고, 4자릿수로 맞춰서 문자로 만들기 (ex> 2+1 =3 -> 0003)
        String maxOnlyNumByString = String.format("%04d",maxOnlyNumByInt+1);

		//대분류+중분류 합친 문자 + 위에서 만든 4자릿수 합치기
        String finalGenerateMaterialCode = getLCWithMC+maxOnlyNumByString;
		
		log.info("최종 생성된 품목코드 : "+	finalGenerateMaterialCode);
//		String test = largeCategoryCode+middleCategoryCode;
//		log.info("문자합치기.. 이게 되나?? : "+test);
		
	}
	
	@Test
	public void fileTest() {
//		String drawingFile = "\\PassionCode\\upload\\2023\\06\\21\\97743ec3-da5b-44a3-9e79-c98e4faf90b3_HappyDay!!!!.jpg";
		String drawingFile = "\\PassionCode\\upload\\drawing\\2023\\06\\21\\65334b3a-f3c2-4d06-92b4-7850e2ede958_HappyLunch~.jpg";
//		String drawingFile = "\\PassionCode\\upload\\drawing\\2023\\06\\21\\thumb_65334b3a-f3c2-4d06-92b4-7850e2ede958_HappyLunch~.jpg";
//		String drawingFile = "\\PassionCode\\upload\\2023\\06\\21\\thumb_97743ec3-da5b-44a3-9e79-c98e4faf90b3_HappyDay!!!!.jpg";
		String uploadPath = drawingFile.substring(0,28);
		log.info("어디어디 보자~~~~~~~ : "+uploadPath);
		// \PassionCode\ upload\drawing\
		String folderPath = drawingFile.substring(28,39);
		log.info("어디어디 보자2~~~~~~~ : "+folderPath);
		// 2023\06\21\
		String uuid = drawingFile.substring(39, 75);
		log.info("uuid 보자!! : "+ uuid);
		// 65334b3a-f3c2-4d06-92b4-7850e2ede958
		// 언더바 _ 제외하고 fileName 보자!!
		String fileName = drawingFile.substring(76);
		log.info("어디 이제 오리지날 파일이름 보자!!! : "+fileName);
//		String uuidAndFileName = drawingFile.substring(39);
//		log.info("어디어디 보자3~~~~~~~ : "+uuidAndFileName);
		// 97743ec3-da5b-44a3-9e79-c98e4faf90b3_HappyDay!!!!.jpg
		// thumb_97743ec3-da5b-44a3-9e79-c98e4faf90b3_HappyDay!!!!.jpg
	}
	
	@Test
	public void decodeTest() throws UnsupportedEncodingException {
		String test = URLDecoder.decode("2023%2F06%2F21%2F132d1fea-7240-4cf5-89c0-cffc758f00d4_a+a.jpg","UTF-8");
		log.info("test 결과 : "+test);
	}
	
	@Test
	public void expectedPurchaseOrderCheckTest() {
		//품목코드로 조달계획 조회하는데 
		// 1. 조회가 안된다 => mrp조차 등록안되서, 혹은 조달계획조차 등록이 안된거니까, 등록한지 얼마 안된 품목! 수정 가능!
		// 2. 조회가 되는데, 리스트에서 조달완료일,세부구매발주서 둘다 전부 null => 한번도 발주한적이 없는거니까, 얼마안된 등록이라 수정 가능!!
		
		boolean expectedPurchaseOrderCheck = false;
		
		List<String> materialCodeList = new ArrayList<>();
		materialCodeList.add("CN0007");
		materialCodeList.add("CG0002");
		log.info("품목리스트 봐보자 : "+materialCodeList);
		
		for(String materialCode : materialCodeList) {
			List<ProcurementPlan> ppList = procurementPlanRepository.getPPJoinMRPByMaterialCode(materialCode);
			
			if(ppList.size()==0) {
				expectedPurchaseOrderCheck = true;
			}else {
				for(int i=0; i<ppList.size(); i++) {
					if(ppList.get(i).getCompletionDate()!=null) {
						expectedPurchaseOrderCheck = false;
						break;
					}else {
						if(ppList.get(i).getDetailPurchaseOrder()!=null) {
							expectedPurchaseOrderCheck = false;
							break;
						}else {
							expectedPurchaseOrderCheck = true;
						}
					}
				}
			}
			if(!expectedPurchaseOrderCheck) {
				break;
			}
		}
		log.info("그래서 최종 참, 거짓 확인해 보자 : "+expectedPurchaseOrderCheck);
		
		
//		boolean expectedPurchaseOrderCheck = false;
//		
//		List<ProcurementPlan> ppList = procurementPlanRepository.getPPJoinMRPByMaterialCode("CN0001");
//		
//		if(ppList.size()==0) {
//			expectedPurchaseOrderCheck = true;
//		}else {
//			for(int i=0; i<ppList.size(); i++) {
//				if(ppList.get(i).getCompletionDate()!=null) {
//					expectedPurchaseOrderCheck = false;
//					break;
//				}else {
//					if(ppList.get(i).getDetailPurchaseOrder()!=null) {
//						expectedPurchaseOrderCheck = false;
//						break;
//					}else {
//						expectedPurchaseOrderCheck = true;
//					}
//				}
//			}
//		}
//		log.info("그래서 최종 참, 거짓 확인해 보자 : "+expectedPurchaseOrderCheck);
		
	}
	
	
	@Test
	public void numTest() {
		
		//시작하는 숫자 
		Integer startNum = 1;
		
		//시작하는 숫자, 4자릿수 맞춰서 문자로 만들기 (ex> 1 -> 0001)
		String startNumByString = String.format("%04d",startNum);
		log.info("일단 문자 보자 : "+ startNumByString);
		log.info("문자 숫자로 바꾼거 보자 : "+ Integer.parseInt(startNumByString));
		
	}
	
	
	@Test
	public void codeGenerateTest() {
		List<String> codeList = new ArrayList<>();
		
		codeList.add("AB0012");
		codeList.add("BC0002");
		codeList.add("CD0002");
		codeList.add("CD0002");
		codeList.add("BC0002");
		codeList.add("CD0002");
		codeList.add("BC0002");
		codeList.add("BC0002");
		log.info("어디 만들어진 처음 코드 리스트 봐보자!!!! : "+codeList);
//		codeList.remove(2);
//		codeList.add(2, "테스트");
		
		for(int i=0;i<codeList.size();i++) {
			String compare = codeList.get(i);
			log.info("현재 "+i+"번째 비교 중! 그 문자 : "+compare);
			for(int j=0;j<codeList.size();j++) {
				if(i!=j) {
					if(compare.equals(codeList.get(j))){
						String presentStr = codeList.get(j).substring(0,2);
						String presentNum = codeList.get(j).substring(2);
						Integer plusNum = Integer.parseInt(presentNum)+1;
						String plusNumStr = String.format("%04d",plusNum);
						String plusCodeGenerate = presentStr+plusNumStr;
						log.info("코드 +1해서 만들어진거 문자 보자 : "+plusCodeGenerate);
						codeList.remove(j);
						codeList.add(j, plusCodeGenerate);
						log.info("제대로 리스트에 그 해당 인덱스에 넣어진건가?? : "+codeList.get(j));
					}
				}
			}
		}
		
		log.info("어디 만들어진 코드 리스트 봐보자!!!! : "+codeList);
		
		
		
	}
	
	
	
	
	
	
	

}
