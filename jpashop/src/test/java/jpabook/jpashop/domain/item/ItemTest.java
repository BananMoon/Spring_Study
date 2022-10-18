package jpabook.jpashop.domain.item;

import jpabook.jpashop.exception.NotEnoughStockException;
import org.junit.Assert;
import org.junit.Test;

// 단위 테스트
public class ItemTest {
    @Test(expected = NotEnoughStockException.class)
    public void 재고수량초과() throws Exception {
        //given
        Book book = Book.createBook("김영한", "isbn", "시골 JPA", 10000, 10);
        //when
        book.reduceStock(13);

        //then
        Assert.fail("재고수량 부족 예외가 터져야한다.");
    }
}