package com.passioncode.procurementsystem.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import org.springframework.stereotype.Service;
import com.passioncode.procurementsystem.dto.DetailPublishDTO;
import com.passioncode.procurementsystem.dto.DetailPurchaseOrderDTO;
import com.passioncode.procurementsystem.entity.DetailPurchaseOrder;
import com.passioncode.procurementsystem.entity.ProcurementPlan;
import com.passioncode.procurementsystem.entity.PurchaseOrder;
import com.passioncode.procurementsystem.repository.DetailPurchaseOrderRepository;
import com.passioncode.procurementsystem.repository.ProcurementPlanRepository;
import com.passioncode.procurementsystem.repository.PurchaseOrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@RequiredArgsConstructor
@Log4j2
public class DetailPurchaseOrderServiceImpl implements DetailPurchaseOrderService {

	private final PurchaseOrderRepository purchaseOrderRepository;
	private final DetailPurchaseOrderRepository detailPurchaseOrderRepository;
	private final ProcurementPlanRepository procurementPlanRepository;

	@Override
	public List<DetailPurchaseOrderDTO> print(Integer ppCode, Integer no) {//조달코드, 발주서 번호
		//프린트하기 위해 발주서 번호를 받아서 리스트를 받아와야 한다.
		//프린트하기 위해 발주서 번호를 받아서 리스트를 받아와야 한다.
				PurchaseOrder poNo = purchaseOrderRepository.findById(no).get();//발주서 번호 보내기
				List<DetailPurchaseOrder> dCode = detailPurchaseOrderRepository.findByPurchaseOrder(poNo);	
				List<DetailPurchaseOrderDTO> dList = new ArrayList<>();
				List<ProcurementPlan> ppList= new ArrayList<>();
				DetailPurchaseOrderDTO dto = null;
				//조달 번호도 보내주자~~
				ProcurementPlan pp = procurementPlanRepository.findById(ppCode).get();	//조달 번호 보내기
				//필요한 것: 발주서 번호, 협력회사, 발주일자, 납기예정일, 발주코드, 품목코드, 품목, 발주수량, 단가, 공급가격
				for(int i =0;i<dCode.size();i++) {
					ppList.add(procurementPlanRepository.findByDetailPurchaseOrder(dCode.get(i)));
					dto = DetailPurchaseOrderDTO.builder()
							.purchaseOrderNo2(addBlank2(poNo.getNo()))
							.purchaseOrderNo(poNo.getNo())
							.materialName(ppList.get(i).getMrp().getMaterial().getName())
							.companyName(ppList.get(i).getContract().getCompany().getName())
							.purchaseOrderDate(ppList.get(i).getDetailPurchaseOrder().getDate()).dueDate(ppList.get(i).getDueDate())
							.purchaseOrderCode(ppList.get(i).getDetailPurchaseOrder().getCode())
							.purchaseOrderCode2(addBlank(ppList.get(i).getDetailPurchaseOrder().getCode()))
							.materialCode(ppList.get(i).getMrp().getMaterial().getCode()).purchaseOrderAmount(ppList.get(i).getAmount())
							.unitPrice(ppList.get(i).getContract().getUnitPrice()).suppluPrice((ppList.get(i).getAmount())*(ppList.get(i).getContract().getUnitPrice()))
							.build();
					dList.add(dto);
					log.info(dList);
		}
		return dList;
	}
	
	@Override
	public List<DetailPublishDTO> detailToDTO(Integer no) {
		List<Object[]> result = detailPurchaseOrderRepository.myDetailList(no);

		List<DetailPublishDTO> detailList = new ArrayList<>();// 저장하는 곳
		ProcurementPlan pp3 =new ProcurementPlan();
		
		for (Object[] arr : result) {// 이 아래가 변환하는 것 이다.
			log.info("222>>" + Arrays.toString(arr));
			// 출력 모양[2, (주)경도전자, 2023-06-10, 200, CN0001, Bolt1, 100]
			
			DetailPublishDTO detailpDTO = new DetailPublishDTO();
			detailpDTO.setPpcode((Integer) arr[0]);
			detailpDTO.setCname((String) arr[1]);
			detailpDTO.setDue_date((Date) arr[2]);
			detailpDTO.setUnit_price((Integer) arr[3]);
			detailpDTO.setMcode((String) arr[4]);
			detailpDTO.setMname((String) arr[5]);
			detailpDTO.setPpamount(((Integer) arr[6]));
			detailpDTO.setPurchaseOrderDate(LocalDateTime.now());
			detailpDTO.setPono(detailPurchaseOrderRepository.findMaxOrderNo());
			detailpDTO.setPocode(detailPurchaseOrderRepository.findMaxCode());
			detailpDTO.setSupply_price((((Integer) arr[6])) * ((Integer) arr[3]));// 필요수량 * 단가
			detailpDTO.setShowPono(addBlank(detailPurchaseOrderRepository.findMaxOrderNo()));
			detailpDTO.setShowPocode(addBlank2(detailPurchaseOrderRepository.findMaxCode()));
			
			
			ProcurementPlan pp = procurementPlanRepository.findById((Integer)arr[0]).get();//조달계획에 저장
			
			procurementPlanRepository.save(pp);
			detailList.add(detailpDTO);
			
		}

		return detailList;

	}
	
	
	
	@Override
	public DetailPurchaseOrder dtoToEntity(DetailPurchaseOrderDTO detailPurchaseOrderDTO) {
		// 구매발주서번호, 발주 코드는 자동으로 만들어져서 여기서는 만들지 않는다.
		// 발주코드 발주수량, 발주일자, 발주서 번호(외래키) (총 4개)
		// Integer code, PurchaseOrder purchaseOrder
		//List<Object[]> result = detailPurchaseOrderRepository.myDetailList(no);
		
		PurchaseOrder po = PurchaseOrder.builder().build();//같은 회사라면 이거는 한 번만
		
		//이 아래가 같은 회사라면 여러번, 다른 회사여도 여러번
		DetailPurchaseOrder detailPurchaseOrder = DetailPurchaseOrder.builder()
				.amount(detailPurchaseOrderDTO.getPurchaseOrderAmount()).date(LocalDateTime.now())
				.purchaseOrder(po).build();
		
		return detailPurchaseOrder;

	}
	
	
	@Override
	public List<DetailPurchaseOrderDTO> getDTOList() {
		List<ProcurementPlan> procurmentPlanList = procurementPlanRepository.findAll();

		List<DetailPurchaseOrderDTO> detailDTOList = new ArrayList<>();

		for (int i = 0; i < procurmentPlanList.size(); i++) {
			detailDTOList.add(entityToDTO(procurmentPlanList.get(i)));
		}
		System.out.println("보여줘요 디테일>>>>>>>" + detailDTOList);
		return detailDTOList;

	}

	@Override
	public DetailPurchaseOrderDTO entityToDTO(ProcurementPlan procurementPlan) {
		// 세부 구매 발주서 발행 화면
		// 발주서 번호, 협력 회사, 발주 일자, 납기 예정일
		// 발주 코드, 품목 코드, 발주 수량, 단가, 공급 가격, 조달계획코드(총 10개)
		DetailPurchaseOrderDTO detailPurchaseOrderDTO = DetailPurchaseOrderDTO.builder()
				.purchaseOrderNo(detailPurchaseOrderRepository.findMaxOrderNo())
				.companyName(procurementPlan.getContract().getCompany().getName())
				.materialName(procurementPlan.getMrp().getMaterial().getName()).purchaseOrderDate(LocalDateTime.now())
				.dueDate(procurementPlan.getDueDate()).purchaseOrderCode(detailPurchaseOrderRepository.findMaxCode())
				.materialCode(procurementPlan.getMrp().getMaterial().getCode())
				.purchaseOrderAmount(
						(procurementPlan.getAmount()))
				.unitPrice(procurementPlan.getContract().getUnitPrice())
				.suppluPrice((procurementPlan.getAmount()) * (procurementPlan.getContract().getUnitPrice()))
				.procurementPlan(procurementPlan.getCode()).build();

		log.info("세부 구매 발주서 발행DTO" + detailPurchaseOrderDTO);
		return detailPurchaseOrderDTO;
	}

	@Override
	public DetailPurchaseOrder get(Integer code) {
		return detailPurchaseOrderRepository.findById(code).get();
	}
	
	@Override
	public List<DetailPurchaseOrder> getDetailByPurchaseNo(PurchaseOrder purchaseOrder) {
		
		return detailPurchaseOrderRepository.findByPurchaseOrder(purchaseOrder);
	}
	
	@Override
	public List<DetailPurchaseOrder> getDetailList(){
		List<DetailPurchaseOrder> detailList = detailPurchaseOrderRepository.findAll();
		return detailList;
		
	}



	@Override
	public void updataePp(Integer num1) {
		ProcurementPlan pp = procurementPlanRepository.findById(num1).get();
		Integer amount = procurementPlanRepository.findById(num1).get().getAmount();
		PurchaseOrder po = PurchaseOrder.builder().build();
		purchaseOrderRepository.save(po);//발주서 번호 생성 후 저장
		DetailPurchaseOrder detailPurchaseOrder = DetailPurchaseOrder.builder()
				.amount(amount).date(LocalDateTime.now())
				.purchaseOrder(po).build();
		//세부구매발주서 저장
		
		detailPurchaseOrderRepository.save(detailPurchaseOrder);
		
		//procurementPlanRepository.save(pp);필요 없는 것 같아 지움 없어도 잘 됨
		//총 9개
		ProcurementPlan pp2 = ProcurementPlan.builder()
				.amount(pp.getAmount())
				.code(pp.getCode())
				.completionDate(pp.getCompletionDate())
				.contract(pp.getContract())
				.detailPurchaseOrder(detailPurchaseOrder)
				.dueDate(pp.getDueDate())
				.minimumOrderDate(pp.getMinimumOrderDate())
				.mrp(pp.getMrp())
				.registerDate(pp.getRegisterDate()).build();
				
		procurementPlanRepository.save(pp2);
		
		log.info("저장하는 조달계획번호  ~~~>>" + pp2);
		log.info("22288888888>>" + detailPurchaseOrder);
		
	}
	
	//발주코드에 문자를 넣어서 보내기
	public String addBlank(Integer num1) {
		String pNum = String.format("%05d", num1);
		pNum = "DPO"+pNum;
		log.info("발주코드에 찍어 보낼 문자"+pNum);
		
		return pNum;
		
	}
	
	//발주서번호에 문자를 넣어서 보내기
	public String addBlank2(Integer num1) {
		String pNum = String.format("%08d", num1);
		pNum = "PO"+pNum;
		log.info("발주서번호에 찍어 보낼 문자"+pNum);
		
		return pNum;
		
	}

}
