package jpabook.jpashop.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.*;
import org.aspectj.weaver.ast.Or;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.FetchType.*;

@Entity
@Table(name = "orders")
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order {

    @Id @GeneratedValue
    @Column(name = "order_id")
    private Long id;

    @ManyToOne(fetch = LAZY)  // member와 연관관계 적어줌(order입장에서는 다대일), 연관관계의 주인임!(매핑을 하는 쪽. 어디의 값이 수정되었을 때 어떤 값을 기준으로 jpa에서 바꿔줄건지 설정)
    @JoinColumn(name = "member_id") // 어떤 컬럼과 조인할지
    private Member member;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> orderItems = new ArrayList<>();

    @OneToOne(fetch = LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "delivery_id") // 1대1 관계에서는 어디에 fk를 설정해도 관계없음. 이런 경우 주로 시스템에서 먼저 호출하는(주되게 사용하는) 쪽으로 잡아주면 됨
    private Delivery delivery;

    private LocalDateTime orderDate; // 주문시간

    @Enumerated(EnumType.STRING)
    private OrderStatus status; // 주문상태 [ORDER, CANCEL]

    // ==연관관계 (편의) 메소드== //
    // 양방향 연관관계 설정...편하게 하는 법?
    // 양방향일 경우 양 쪽 객체를 모두 신경써야 한다. -> 하나의 메소드에서 양측의 관계를 설정하게 해주는 게 안전
    // 그래서 한쪽에서 양방향관계를 설정(set)하는 메소드를 적어준다
    // 연관관계 편의메소드는 양쪽이 있으면 컨트롤 하는 쪽(빈번하게 사용하는 쪽)이 양쪽에 대한 메소드를 정의하는 게 좋다(연관관계의 주인과 상관없음)

    // order-member : 일대다, 다대일 양방향
    public void setMember(Member member) {
        this.member = member;
        member.getOrders().add(this);
    }

    // order-orderItem : 일대다 양방향
    public void addOrderItem(OrderItem orderItem) {
        orderItems.add(orderItem);
        orderItem.setOrder(this);
    }

    // order-delivery : 일대일 양방향
    public void setDelivery(Delivery delivery) {
        this.delivery = delivery;
        delivery.setOrder(this);
    }

    // == 생성 메서드 == //
    // 이 order 도메인처럼 복잡한 경우 생성자가 별도로 있는 것이 좋음. static으로 선언해서..
    public static Order createOrder(Member member, Delivery delivery, OrderItem... orderItems) {
        Order order = new Order();
        order.setMember(member);
        order.setDelivery(delivery);
        for (OrderItem orderItem : orderItems) {
            order.addOrderItem(orderItem);
        }
        order.setStatus(OrderStatus.ORDER); // 첫 주문상태를 ORDER로 강제해놓음
        order.setOrderDate(LocalDateTime.now());    // 주문시간 현재로 세팅
        return order;
    }

    // == 비즈니스 로직 == //
    /**
     * 주문 취소
     */
    public void cancel() {
        if (delivery.getStatus() == DeliveryStatus.COMP) {  // 이미 배송상태가 완료이면
            throw new IllegalStateException("이미 배송완료된 상품은 취소가 불가능합니다.");
        }

        this.setStatus(OrderStatus.CANCEL);
        for (OrderItem orderItem : orderItems) {    // loop 돌면서 재고 원복(한번에 다건 주문가능하기 때문에~)
            orderItem.cancel();
        }
    }

    // == 조회 로직 == //
    /**
     * 전체 주문 가격 조회
     */
    public int getTotalPrice() {
        int totalPrice = 0;
        for (OrderItem orderItem : orderItems) {
            totalPrice += orderItem.getTotalPrice();    // orderItem에 있는 전체 가격을 가져온다. 왜? 주문 가격,수량이 있기 때문! 둘을 곱해서 계산해야하므로.
        }
        return totalPrice;
//        cf) java8의 stream 기능 활용하면 더 간단하게 바꿀 수 있음
//        return orderItems.stream()
//                .mapToInt(OrderItem::getTotalPrice)
//                .sum();
    }
}
