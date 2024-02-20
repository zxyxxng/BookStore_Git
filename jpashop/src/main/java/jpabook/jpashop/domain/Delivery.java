package jpabook.jpashop.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import static jakarta.persistence.FetchType.*;

@Entity
@Getter @Setter
public class Delivery {

    @Id @GeneratedValue
    @Column(name = "delivery_id")
    private Long id;

    @OneToOne(mappedBy = "delivery", fetch = LAZY)
    private Order order;

    @Embedded
    private  Address address;

    @Enumerated(EnumType.STRING)    // enum 타입은 반드시 Enumerated 해줘야 하는데 ordinal 방식으로 하면 숫자로 됨. 문제는 enum이 중간에 추가되면 순서 밀려서 망하는거. 반드시 string 방식으로 사용하기
    private DeliveryStatus status; // READY, COMP
}
