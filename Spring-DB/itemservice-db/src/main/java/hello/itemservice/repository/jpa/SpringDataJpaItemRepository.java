package hello.itemservice.repository.jpa;

import hello.itemservice.domain.Item;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

// 아래 3가지 메서드는 동적 쿼리를 이용해서 간단히 해결할 수 있다. 동적쿼리는 QueryDsl을 사용하자!
public interface SpringDataJpaItemRepository extends JpaRepository<Item, Long> {    // Item 엔티티의 저장소로 사용

    List<Item> findByItemNameLike(String itemName);
    List<Item> findByPriceLessThanEqual(Integer price);

    // 쿼리 메서드 (메서드명의 길이가 사용 시 불편하므로, 아래 메서드와 같은 기능 수행)
    List<Item> findByItemNameLikeAndPriceLessThanEqual(String itemName, Integer price);

    // 쿼리 직접 실행 : 명시적으로 파라미터 바인딩 해줘야 한다. (이 방식이 더 낫겠지?)
    @Query("select i from Item i where i.itemName like :itemName and i.price <= :price")
    List<Item> findItems(@Param("itemName") String itemName, @Param("price") Integer price);
}
