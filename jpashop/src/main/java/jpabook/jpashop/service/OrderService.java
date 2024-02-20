package jpabook.jpashop.service;

import jpabook.jpashop.domain.Delivery;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderItem;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.repository.ItemRepository;
import jpabook.jpashop.repository.MemberRepository;
import jpabook.jpashop.repository.OrderRepository;
import jpabook.jpashop.repository.OrderSearch;
import lombok.RequiredArgsConstructor;
import org.aspectj.weaver.ast.Or;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;

    /**
     * 주문
     */
    @Transactional
    public Long order(Long memberId, Long itemId, int count) {

        // 엔티티 조회
        Member member = memberRepository.findOne(memberId);
        Item item = itemRepository.findOne(itemId);

        // 배송정보 생성
        Delivery delivery = new Delivery();
        delivery.setAddress(member.getAddress());

        // 주문상품 생성
        OrderItem orderItem = OrderItem.createOrderItem(item, item.getPrice(), count);  // 단순화를 위해 일단 주문상품은 하나만 넘기도록 함

        // 주문 생성
        Order order = Order.createOrder(member, delivery, orderItem);

        // 주문 저장
        orderRepository.save(order);
        // 왜 order만 저장해주는지? 이미 앞서 order 클래스에서 cascade 옵션 지정해줬기 때문! 따라서 order만 저장해줘도 나머지 orderItem과 delivery 엔티티도 자동으로 persist 해줌.
        // cascade 범위는 어떻게 설정? 참조하는 주인이 딱 private(다른 것이 참조할 수 없는) owner인 경우에만 사용해야!
        // 즉, 현재 delivery와 orderItem은 order 말고 아무도 안씀. order만 참조해서 사용. 라이프 사이클에 대해서 동일하게 관리를 할 때~ 등
        // 만약 다른 데서 delivery나 orderItem을 참조해서 쓴다면? 절대 cascade를 order에 사용하면 암됨. order 지우면 나머지 다 지워지니까...
        // 이런 경우 별도의 리파지토리 생성해서 각각 persist 하는 것이 나음.

        return order.getId();   // order의 식별자 값만 받는다
    }

    /**
     * 취소
     */
    @Transactional
    public void cancelOrder(Long orderId) {
        // 주문 엔티티 조회
        Order order = orderRepository.findOne(orderId);
        // 주문 취소
        order.cancel();
    }

    // 검색
    public List<Order> findOrders(OrderSearch orderSearch) {
        return orderRepository.findAllByString(orderSearch);
    }
}
