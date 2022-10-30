package jpabook.jpashop.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jpabook.jpashop.domain.Order;
import static jpabook.jpashop.domain.QOrder.*;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;
@RequiredArgsConstructor
public class OrderRepositoryCustomImpl implements OrderRepositoryCustom{
    private final JPAQueryFactory query;

    /**
     * QueryDsl로 작성한 주문 검색 기능
     * - 검색 조건에 동적으로 쿼리 생성하여 주문 엔티티 조회
     * @return
     */
    @Override
    public Optional<List<Order>> findAllByQueryDsl(OrderSearch orderSearch) {
        return Optional.ofNullable(
            query.selectFrom(order)
                .fetch()
        );
    }
}
