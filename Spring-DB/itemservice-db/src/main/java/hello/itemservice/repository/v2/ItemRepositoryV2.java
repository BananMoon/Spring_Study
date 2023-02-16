package hello.itemservice.repository.v2;

import hello.itemservice.domain.Item;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 스프링데이터JPA 사용 레파지토리
 */
public interface ItemRepositoryV2 extends JpaRepository<Item, Long> {
}
