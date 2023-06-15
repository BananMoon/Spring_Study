package moonz.springtx.order;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * checked, unchecked 예외를 상황에 맞게 다르게 동작한다는 것을 알수 있다.
 * 비즈니스 예외에서도 롤백시키길 원한다면, rollbackFor 옵션 이용한다.
 * - 메모리 DB 사용 시, JPA에서 자동으로 테이블 생성해줌.
 */
@Slf4j
@SpringBootTest
class OrderServiceTest {
    // TODO @Autowired, 객체가 주입되어야 실행되지!!
    @Autowired OrderService orderService;
    @Autowired OrderRepository orderRepository;

    @Test
    void complete() throws NotEnoughMoneyException {
        // given
        Order order = new Order();
        order.setId(1L);
        order.setUsername("정상");
        // when
        orderService.order(order);

        // then
        Order findOrder = orderRepository.findById(order.getId()).get();
        Assertions.assertEquals(order.getId(), findOrder.getId());
        Assertions.assertEquals("완료", findOrder.getPayStatus());
    }

    @Test
    void runtimeException() {
        // given
        Order order = new Order();
//        order.setId(2L);  // 세팅안 할 경우 자동 1
        order.setUsername("예외");
        // when
        assertThatThrownBy(() -> orderService.order(order))
                .isInstanceOf(RuntimeException.class);

        // then: RuntimeException 발생 시 데이터가 롤백되어야 한다.
        Optional<Order> findOrder = orderRepository.findById(order.getId());
        assertThat(findOrder.isEmpty()).isTrue();

        log.info("order의 id : {}", order.getId());
    }
    @Test
    void bizException() {
        // given
        Order order = new Order();
        order.setUsername("잔고 부족");
        // when - 테스트 및 값 확인을 위해 try-catch 문으로 감싼다. (그렇지 않으면 notEnough~ 에러 뜸)
        try {
            orderService.order(order);
        } catch (NotEnoughMoneyException e) {
            log.error("고객에게 잔고 부족을 알리고 별도의 계좌로 입금 요청 드린다.");
        }

        // then : 예외 발생했지만 db에 저장되어있는 상태여야함
        Order findOrder = orderRepository.findById(order.getId()).get();
        Assertions.assertEquals(order.getId(), findOrder.getId());
        Assertions.assertEquals("대기", findOrder.getPayStatus());
    }
}