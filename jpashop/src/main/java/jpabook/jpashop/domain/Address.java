package jpabook.jpashop.domain;

import jakarta.persistence.Embeddable;
import lombok.Getter;

@Embeddable
@Getter // 값 타입은 변경되면 안되기 때문에 Getter만 만듬
public class Address {

    private String city;
    private String street;
    private String zipcode;

    // 2. jpa 스펙상 생성자를 만들어줘야함. 단 protected로
    // (public으로 하면 누구나 호출할 수 있다는 문제도 있고 jpa 스펙에서는 리플랙션이나 프록시 설정을 위해 protected로 쓰는 것 추천)
    protected Address() {
    }

    // 1. 좋은 설계는 생성할 때만 값 세팅을 하고 setter를 아예 제공하지 않는 것
    public Address(String city, String street, String zipcode) {
        this.city = city;
        this.street = street;
        this.zipcode = zipcode;
    }
}
