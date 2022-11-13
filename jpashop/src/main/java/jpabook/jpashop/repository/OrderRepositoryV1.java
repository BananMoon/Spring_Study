package jpabook.jpashop.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.dto.SimpleOrderQueryDto;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

import static jpabook.jpashop.domain.QMember.member;
import static jpabook.jpashop.domain.QOrder.order;
import static org.springframework.util.StringUtils.hasText;

@Repository
public class OrderRepositoryV1 {
    private final EntityManager em;
    private final JPAQueryFactory query;

    public OrderRepositoryV1(EntityManager em) {
        this.em = em;
        query = new JPAQueryFactory(em);
    }

    public void save(Order order) {
        em.persist(order);
    }

    public Order findOne(Long orderId) {
        return em.find(Order.class, orderId);
    }

    // 동적 쿼리!
    // 조건이 있다면 status, name으로 조건처리가 된 쿼리를 실행하고
    // 조건이 없다면, 모든 데이터를 다 가져오도록 쿼리를 실행해야한다.
    // JPQL: Order와 연관된 Member join!
    public List<Order> findAllByJPQL(OrderSearch orderSearch) {
        String jpql = "select o from Order o join o.member m";
        boolean isFirstCondition = true;
        // 주문 상태 검색
        if (orderSearch.getOrderStatus() != null) {
            if (isFirstCondition) {
                jpql += " where";
                isFirstCondition = false;
            } else {
                jpql += " and";
            }
            jpql += "o.status = :status";
        }
        // 회원명 검색
        if (hasText(orderSearch.getMemberName())) {
            if (isFirstCondition) {
                jpql += " where";
                isFirstCondition = false;
            } else {
                jpql += " and";
            }
            jpql += "m.name like :name";
        }

        TypedQuery<Order> query = em.createQuery(jpql, Order.class)
                .setMaxResults(1000);// 최대 1000건

        if (orderSearch.getOrderStatus() != null) {
            query = query.setParameter("status", orderSearch.getOrderStatus());
        }
        if (hasText(orderSearch.getMemberName())) {
            query = query.setParameter("name", orderSearch.getMemberName());
        }
        return query.getResultList();
    }

    /*
     JPA가 제공하는 표준 Criteria
     동적 쿼리 만들 때 매우 메리트. 단점: 유지보수가 0
     실무에서 사용 안함.
     */
    public List<Order> findAllByCriteria(OrderSearch orderSearch) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Order> cq = cb.createQuery(Order.class);
        Root<Order> o = cq.from(Order.class);   // root 테이블
        Join<Object, Object> m = o.join("member", JoinType.INNER);  // join 테이블

        // 동적 쿼리 조합을(조건을) 만들 수 있음
        List<Predicate> criteria = new ArrayList<>();
        // 주문 상태 검색
        if (orderSearch.getOrderStatus() != null) {
            Predicate status = cb.equal(o.get("status"), orderSearch.getOrderStatus());
            criteria.add(status);
        }

        if (orderSearch.getMemberName() != null) {
            Predicate name = cb.like(m.<String>get("name"), "%" + orderSearch.getMemberName() + "%");
            criteria.add(name);
        }
        cq.where(cb.and(criteria.toArray(new Predicate[criteria.size()])));
        TypedQuery<Order> query = em.createQuery(cq).setMaxResults(1000);
        return query.getResultList();
    }

    /* QueryDsl 사용
    * - SQL(JPQL)과 모양이 유사하면서 자바 코드로 동적 쿼리를 편하게 쓸 수 있는 오픈 소스.
    * - JPQL을 코드로 만드는 빌더 역할
    *
    * 장점
    * - 컴파일 타임에 에러 체크 가능
    * - JPQL의 net 명령어와 달리 깔금한 DTO 조회 지원
    *   - V4에서 진행했던 findOrderItemMap 메서드 : dto로 조회하기 위해 코드 안에서 생성자를 호출
    *   - why? QueryDsl이 자동으로 setter/생성자/필드를 이용해서 생성해주기 때문에 DTO를 사용하기 편리.
    * - condition 등 재사용성이 높다.
     * -
     */
    public List<Order> findAll(OrderSearch orderSearch) {
        // JPAQueryFactory query = new JPAQueryFactory(em);
        return query.select(order)
                .from(order)
                .join(order.member, member)
                .where(statusEq(orderSearch.getOrderStatus()), /* null인 경우 해당 조건을 사용하지 않음. */
                        nameLike(orderSearch.getMemberName()))
                .limit(1000)
                .fetch();
    }

    private BooleanExpression nameLike(String memberName) {
        if (!StringUtils.hasText(memberName)) {
            return null;
        }
        return order.member.name.like(memberName);
    }

    private BooleanExpression statusEq(OrderStatus statusCond) {
        if (statusCond == null) {
            return null;
        }
        return order.status.eq(statusCond);
    }
    public List<Order> findAllWithMemberDelivery() {
        return em.createQuery("select o from Order o" +
                " join fetch o.member m" +
                " join fetch o.delivery d", Order.class
        ).getResultList();
    }

    /*
    - new 명령어를 사용해서 JPQL의 결과를 DTO로 즉시 변환
    - 애플리케이션 네트워크 용량 최적화 (a little bit)é
    - 재사용성 저하
    - API 스펙에 맞춘 코드가 레파지토리에 들어간 문제
    - DTO를 조회할 때는 fetch join을 사용할 수 없다. 오로지 엔티티에만 사용 가능.
     */
    public List<SimpleOrderQueryDto> findOrderDtos() {
        return em.createQuery(
                "select new jpabook.jpashop.dto.SimpleOrderQueryDto(o.id, m.name, o.orderDate, o.status, d.address)" +
                        " from Order o" +
                        " join fetch o.member m" +
                        " join fetch o.delivery d", SimpleOrderQueryDto.class
        ).getResultList();
    }

    /**
     * JPA에서의 distinct 기능 : SQL 문에 distinct 추가 + 조회된 객체값 일치하면 중복 제거
     * 1. SQL 문의 distinct 기능(모든 칼럼이 같은 데이터의 중복 제거)
     * 2. 애플리케이션 단에 와서 JPA가 객체의 참조값이 같으면 중복을 제거하도록 동작한다.
     */
    public List<Order> findAllWithItem() {
        return em.createQuery(
                "select distinct o from Order o" +
                        " join fetch o.member m" +
                        " join fetch o.delivery d" +
                        " join fetch o.orderItems oi" +
                        " join fetch oi.item", Order.class)
                .getResultList();
    }

    /* 사실 XToOne 관계의 객체들은 따로 패치조인하지 않으면, 마찬가지로 두개(member, delivery)의 쿼리가 더 나가지만 in 쿼리로 조회된다.
       하지만 네트워크를 더 타게 되는 문제가 있으므로, XToOne에 대해서는 패치 조인을 해주는 것을 권장.
     */
    public List<Order> findAllWithMemberDelivery(int offset, int limit) {
        return em.createQuery("select o from Order o" +
                " join fetch o.member m" +
                " join fetch o.delivery d", Order.class)
                .setFirstResult(offset)
                .setMaxResults(limit)
                .getResultList();
    }
}