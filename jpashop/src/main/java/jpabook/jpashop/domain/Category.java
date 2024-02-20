package jpabook.jpashop.domain;

import jpabook.jpashop.domain.item.Item;
import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.FetchType.*;

@Entity
@Getter @Setter
public class Category {

    @Id @GeneratedValue
    @Column(name = "category_id")
    private Long id;

    private String name;

    @ManyToMany
    @JoinTable(name = "category_item",
            joinColumns = @JoinColumn(name = "category_id"),
            inverseJoinColumns = @JoinColumn(name = "item_id"))
    private List<Item> items = new ArrayList<>();

    // 이하 코드는 셀프로 양방향 연관관계를 건 것임
    // 실무에서 자주 사용되진 않지만 모든 케이스를 구현해보기 위해 테스트..
    @ManyToOne(fetch = LAZY)    // ManyToOne은 기본 로딩이 Eager(즉시로딩)이므로 반드시 LAZY(지연로딩)로 변경해줘야함
    @JoinColumn(name = "parent_id")
    private Category parent;    // 부모가 내 타입이니까 parent 설정

    @OneToMany(mappedBy = "parent")
    private List<Category> child = new ArrayList<>();   // 1부모에 자식은 n일 수 있으므로 list

    // == 연관관계 편의 메서드 == //
    public void addChildCategory(Category child) {
        this.child.add(child);
        child.setParent(this);
    }
}
