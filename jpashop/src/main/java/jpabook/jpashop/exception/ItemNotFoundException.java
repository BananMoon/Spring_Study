package jpabook.jpashop.exception;

import java.util.function.Supplier;

public class ItemNotFoundException extends RuntimeException {
    public ItemNotFoundException(String message) {
        super(message);
    }

}
