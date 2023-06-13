package moonz.springtx.order;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    @Transactional
    public void order(Order order) throws NotEnoughMoneyException {
        order.setPayStatus("완료");   // 정상 if문에서 set하면 데이터에 반영되지 않음.
        orderRepository.save(order);

        if ("예외".equals(order.getUsername())) {
            log.error("시스템 예외 발생");
            throw new RuntimeException();
        }
        if ("잔고 부족".equals(order.getUsername())) {
            log.error("잔고 부족 비즈니스 예외 발생");
            order.setPayStatus("대기");   // 데이터 커밋은 되기를 기대함.
            throw new NotEnoughMoneyException("잔고가 부족합니다.");
        }
        if ("정상".equals(order.getUsername())) {
            log.info("정상 승인");
        }
        log.info("order의 paystatus:"+ order.getPayStatus());
        log.info("=====결제 승인 프로세스 완료=====");
    }

}
