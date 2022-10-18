package jpabook.jpashop.service;

import jpabook.jpashop.controller.ItemForm;
import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Transactional
public interface ItemService {
    void saveItem(ItemForm itemForm);
    List<Book> findItems();
    Item findItem(Long itemId);
}