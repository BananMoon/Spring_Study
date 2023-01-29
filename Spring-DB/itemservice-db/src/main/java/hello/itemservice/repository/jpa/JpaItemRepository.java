package hello.itemservice.repository.jpa;

import hello.itemservice.domain.Item;
import hello.itemservice.repository.ItemRepository;
import hello.itemservice.repository.ItemSearchCond;
import hello.itemservice.repository.ItemUpdateDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

@Slf4j
@Repository
@Transactional(readOnly = true) // 일반적으로는 서비스 계층에서 트랜잭션을 건다.
public class JpaItemRepository implements ItemRepository {
    // JPA는 EntityManager 의존관계 주입이 필요하다.
    // Spring이 EntityManagerFactory로 데이터 베이스 세팅하는 등을 자동 진해주고, 우리는 세팅된 EntityManager를 주입받아서 사용하기만 하면 된다.
    private final EntityManager em;

    public JpaItemRepository(EntityManager em) {
        this.em = em;
    }

    @Override
    @Transactional
    public Item save(Item item) {
        em.persist(item);   /*persist : 영구히 보존한다. */
        return item;
    }

    @Override
    @Transactional
    public void update(Long itemId, ItemUpdateDto updateParam) {
        Item findItem = em.find(Item.class, itemId);
        findItem.setItemName(updateParam.getItemName());
        findItem.setQuantity(updateParam.getQuantity());
        findItem.setPrice(updateParam.getPrice());
        // 트랜잭션 커밋 시점에 업데이트 쿼리 생성하여 날린다.
    }

    @Override
    public Optional<Item> findById(Long id) {
        Item item = em.find(Item.class, id);
        return Optional.ofNullable(item);
    }

    /**
     * JPA는 쿼리문이 복잡해질 경우, JPQL을 제공한다.
     * but JPQL은 동적 쿼리에 약하다..
     */
    @Override
    public List<Item> findAll(ItemSearchCond cond) {
        String jpql = "select i from Item i";   // Item 엔티티 자체인 i 반환

        // 동적쿼리
        Integer maxPrice = cond.getMaxPrice();
        String itemName = cond.getItemName();

        if (StringUtils.hasText(itemName) || maxPrice != null) {
            jpql += " where";
        }
        boolean andFlag = false;
        if (StringUtils.hasText(itemName)) {
            jpql += " i.itemName like concat('%',:itemName,'%')";
            andFlag = true;
        }
        if (maxPrice != null) {
            if (andFlag) {
                jpql += " and";
            }
            jpql += " i.price <= :maxPrice";
        }
        log.info("jpql={}", jpql);

        // 파라미터 설정하여 이름 기반 파라미터 바인딩
        TypedQuery<Item> query = em.createQuery(jpql, Item.class);
        if (StringUtils.hasText(itemName)) {
            query.setParameter("itemName", itemName);
        }
        if (maxPrice != null) {
            query.setParameter("maxPrice", maxPrice);
        }
        return query.getResultList();
    }
}
