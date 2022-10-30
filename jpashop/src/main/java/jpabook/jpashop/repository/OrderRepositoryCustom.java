package jpabook.jpashop.repository;

import jpabook.jpashop.domain.Order;

import java.util.List;
import java.util.Optional;

public interface OrderRepositoryCustom {
    Optional<List<Order>> findAllByQueryDsl(OrderSearch orderSearch);
}
