package jpabook.jpashop.service;

import jpabook.jpashop.controller.ItemForm;
import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.exception.ItemNotFoundException;
import jpabook.jpashop.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

// Repository에게 위임하는 단계
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BookService implements ItemService{

    private final BookRepository bookRepository;

    @Transactional
    public void save(ItemForm itemForm) {
        bookRepository.save((Book) itemForm.toEntity());
    }

    @Override
    public void saveItem(ItemForm itemForm) {
        bookRepository.save((Book)itemForm.toEntity());
    }

    public List<Book> findItems() {
        return bookRepository.findAll();
    }

    public Book findItem(Long itemId) {
        Book item = bookRepository.findById(itemId)
                .orElseThrow(() -> new ItemNotFoundException("Book Not Found Exception"));
        System.out.println("id= " + item.getId() +
                ", Name= " + item.getName() +
                ", Price= " + item.getPrice() +
                ", quantity= " + item.getStockQuantity()
        );
        return item;
    }

    @Transactional
    public void updateItem(Long itemId, Book param) {
        Book findItem = bookRepository.findOne(itemId)
                .orElseThrow(() -> new ItemNotFoundException("Book Not Found Exception"));
        findItem.update(param.getPrice(), param.getName(), param.getStockQuantity());
    }
}
