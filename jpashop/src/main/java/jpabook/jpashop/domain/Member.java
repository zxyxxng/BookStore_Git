package jpabook.jpashop.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Member {

    @Id @GeneratedValue
    @Column(name = "member_id") // fk명 지정
    private Long id;

    private String name;

    @Embedded
    private Address address;

    @OneToMany(mappedBy = "member")  // order와 연관관계 적어줌(orderT의 member필드에 의해 매핑된 것을 의미. 즉 종속됨)
    private List<Order> orders = new ArrayList<>();
}
