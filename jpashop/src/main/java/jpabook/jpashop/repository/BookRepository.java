package jpabook.jpashop.repository;

import jpabook.jpashop.domain.item.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long>, ItemRepository<Book> {
}
