package moonz.core.order;

public interface OrderService {
    // 1. 주문 생성
    Order createOrder(Long memberId, String itemName, int itemPrice);
}
