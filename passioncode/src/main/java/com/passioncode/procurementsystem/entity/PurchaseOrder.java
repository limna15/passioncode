package com.passioncode.procurementsystem.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString(exclude = "detailPurchaseOrder")
public class PurchaseOrder {	//구매발주서
	
	//발주서번호
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(length = 8, columnDefinition = "INT(8)")
	private Integer no;

//	@OneToMany(fetch = FetchType.LAZY)
//	@JoinColumn(nullable = false)
//	private DetailPurchaseOrder detailPurchaseOrder;	//발주 상세
	//수진아 여기 이거 세부구매발주서에서 ManyToOne으로 외래키 잡아서 OneToMany 또 안해줘도 될거야.. 아마도 나는 그렇게 알고 있어! 
	//그리고 테이블 우리가 설정한거에도 구매발주서가 1 세부구매발주서가 n 으로 외래키도 세부구매발주서에 생기기도 하고! 이거 아마 지워도 될거 같은데! 
	//나영이랑 같이 한번 확인 해볼래??
	

}
