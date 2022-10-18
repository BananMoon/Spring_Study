package jpabook.jpashop.repository;

import jpabook.jpashop.domain.item.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ItemRepository<T extends Item> extends JpaRepository<T, Long> {
    Optional<T> findOne(Long itemId);
    /*final EntityManager em;

    public void save(Item item) {
        if (item.getId() == null) em.persist(item);  // db에 신규 등록
        else em.merge(item);  // db에 이미 있는 데이터를 업데이트. item은 영속성 상태로 바뀌지 않음.
    }

    public Item findOne (Long itemId) {
        return em.find(Item.class, itemId);
    }

    public List<Item> findAll() {
        return em.createQuery("select i from Item i", Item.class)
                .getResultList();
    };*/
}