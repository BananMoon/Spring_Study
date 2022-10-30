package jpabook.jpashop.repository;

import jpabook.jpashop.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface temp_OrderRepository extends JpaRepository<Order, Long>, OrderRepositoryCustom {

}