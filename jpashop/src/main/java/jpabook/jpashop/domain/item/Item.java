package jpabook.jpashop.domain.item;

import jakarta.persistence.*;
import jpabook.jpashop.domain.Category;
import jpabook.jpashop.exception.NotEnoughStockExcepion;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "dtype")
@Getter @Setter
public abstract class Item {
    // Item 클래스는 추상 클래스로 만들어줌. 하위에 상속할 구현체를 가질 것.
    // 상속관계 매핑 중요! 전략은 부모 클래스에 잡아줘야함(전략종류: single table, table per class, joined)

    @Id @GeneratedValue
    @Column(name = "item_id")
    private Long id;

    private String name;
    private int price;
    private int stockQuantity;

    @ManyToMany(mappedBy = "items")
    private List<Category> categories = new ArrayList<>();

    // == 비즈니스 로직 == //
    // 데이터를 가지고 있는 쪽에 비즈니스 메서드가 있는 것이 가장 좋다. 응집력이 높아짐
    /**
    *  stock 증가
    */
    public void addStock(int quantity) {
        this.stockQuantity += quantity;
    }

    /**
     *  stock 감소
     */
    public void removeStock(int quantity) {
         int restStock = this.stockQuantity - quantity;
         if (restStock < 0) {
             throw new NotEnoughStockExcepion("need more stock");
         }
         this.stockQuantity = restStock;
    }
}
