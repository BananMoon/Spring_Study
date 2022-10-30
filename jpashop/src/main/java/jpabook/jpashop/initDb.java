package jpabook.jpashop;

import jpabook.jpashop.domain.*;
import jpabook.jpashop.domain.item.Book;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;

/**
 * userA
 *   JPA1
 *   JPA2
 * userB
 *   SPRING1
 *   SPRING2
 */
@Component
@RequiredArgsConstructor
public class initDb {

    private final InitService initService;

    @PostConstruct  // Spring이 모두 로드되고 나면 호출된다.
    public void init() {
        initService.dbInit1();  // 여기에 트랜잭션 로직이 들어가면 실행 안됨.
        initService.dbInit2();
    }

    @Component
    @RequiredArgsConstructor
    @Transactional
    static class InitService{
        private final EntityManager em;
        public void dbInit1() {
            Member member = createMember("문윤지", "서울", "1", "1111");
            em.persist(member);

            Book book1 = createBook("홍길동", "isbn", "JPA1 BOOK", 10000, 100);
            em.persist(book1);

            Book book2 = createBook("김영한", "isbn2", "JPA2 BOOK", 20000, 100);
            em.persist(book2);

            OrderItem orderItem1 = OrderItem.createOrderItem(book1, 10000, 1);
            OrderItem orderItem2 = OrderItem.createOrderItem(book2, 20000, 2);

            Delivery delivery = new Delivery(member.getAddress(), DeliveryStatus.READY);
            Order order = Order.createOrder(member, delivery, orderItem1, orderItem2);
            em.persist(order);
        }

        public void dbInit2() {
            Member member = createMember("문지", "진주", "2", "2222");
            em.persist(member);

            Book book1 = createBook("홍길은", "isbn", "SPRING1 BOOK", 20000, 200);
            em.persist(book1);

            Book book2 = createBook("김영한", "isbn2", "SPRING2 BOOK", 40000, 300);
            em.persist(book2);

            OrderItem orderItem1 = OrderItem.createOrderItem(book1, 10000, 1);
            OrderItem orderItem2 = OrderItem.createOrderItem(book2, 20000, 2);

            Delivery delivery = new Delivery(member.getAddress(), DeliveryStatus.READY);
            Order order = Order.createOrder(member, delivery, orderItem1, orderItem2);
            em.persist(order);
        }

        private Member createMember(String name, String city, String street, String zipcode) {
            return Member.builder()
                    .name(name)
                    .address(new Address(city, street, zipcode))
                    .build();
        }

        private Book createBook(String author, String isbn, String name, int price, int stockQuantity) {
            return Book.createBook(author, isbn, name, price, stockQuantity);
        }
    }


}

