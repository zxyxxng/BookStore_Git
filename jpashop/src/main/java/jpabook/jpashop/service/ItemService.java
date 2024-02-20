package jpabook.jpashop.service;

import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemService {  // ItemService 클래스는 itemRepository에 단순히 위임만 하는 클래스. 경우에 따라서는 service를 만들어야되나? 고민할 필요 있음.
    // 컨트롤러에서 리파지토리에 바로 접근해서 하는 것도 문제는 없다. 다만 여러 도메인의 로직을 활용하는 비즈니스 로직이 있는 경우 service가 필요할수도

    private final ItemRepository itemRepository;

    @Transactional
    public void saveItem(Item item) {
        itemRepository.save(item);
    }

    /**
     * 영속성 컨텍스트가 자동 변경
     * - 변경감지로 명확하게 수정해주는 것이 실무에서 훨씬 나음
     */
    @Transactional
    public void updateItem(Long itemId, String name, int price, int stockQuantity) {    // 일단 이거 세개만 변경한다고 가정~
        Item findItem = itemRepository.findOne(itemId);
        // + 실무에서 이렇게 set을 남발하면 안되고 의미있는 메소드를 만들어서 관리하는 것이 좋음. ex) findItem.change(price, name, stockQuantity);
        // 그렇게 해야 변경지점이 entity로 다 가기 때문. 변경지점 역추적하기도 쉬움. 항상 추적가능하게 설계하는 것이 중요. 유지보수 쉽다.
        findItem.setName(name);
        findItem.setPrice(price);
        findItem.setStockQuantity(stockQuantity);
    }

    public List<Item> findItems() {
        return itemRepository.findAll();
    }

    public Item findOne(Long itemId) {
        return itemRepository.findOne(itemId);
    }
}
