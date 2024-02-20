package jpabook.jpashop.repository;

import jakarta.persistence.EntityManager;
import jpabook.jpashop.domain.item.Item;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ItemRepository {

    private final EntityManager em;

    public void save(Item item) {
        // item은 JPA에 저장하기 전까지 ID값이 없다(완전히 새로 생성하는 객체라는 뜻)
        if (item.getId() == null) {
            // 그런 경우 완전히 신규 등록(save 개념)
            em.persist(item);
        } else {
            // 이미 생성한 경우(이미 DB에 등록된걸 가져온거) update 개념으로 해주는게 merge
            em.merge(item);
        }
    }

    public Item findOne(Long id) {
        return em.find(Item.class, id);
    }

    public List<Item> findAll() {
        return em.createQuery("select i from Item i", Item.class)
                .getResultList();
    }
}
