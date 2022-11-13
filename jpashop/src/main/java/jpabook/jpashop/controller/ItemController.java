package jpabook.jpashop.controller;

import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.service.BookService;
import jpabook.jpashop.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.util.Collections;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class ItemController {
//    private final ItemService itemService;
    private final BookService bookService;

    @GetMapping("/items/new")    //url 자체에 "items/1/new"처럼 요청하도록 해서 itmes/{item}/new 의 item을 숫자에 따라 book=1, movie=2, album=3처럼 구분해서
    public String createForm(Model model) {
        model.addAttribute("form", new ItemForm()); // 해당 객체를 넘겨주기 때문에 createItemForm.html에서 필드들을 인식하는 것!
        // 만약 item 중에서 고를 수 있도록 한다면 url에 book,item,
        return "items/createItemForm";
    }
    @PostMapping("/items/new")
    public String createItem (@Valid ItemForm itemForm, BindingResult result) {
        if (result.hasErrors()) {
            return "items/createItemForm";
        }
        // 서비스 호출
//        itemService.saveItem(itemForm);
        bookService.saveItem(itemForm);
        return "redirect:/items";   // 책목록 페이지로 리다이렉트
    }

    @GetMapping("/items")
    public String list(Model model) {
//        List<Item> items = itemService.findItems();
        List<Book> items = bookService.findItems();
        model.addAttribute("items", items);
        return "items/itemList";
    }
    @GetMapping("/items/{itemId}/edit")
    public String updateItemForm(@PathVariable("itemId") Long itemId, Model model) {    // View에 Data 넘겨야함
//        Book item = (Book)itemService.findItem(itemId);    // casting하는 것은 좋지 않지만 예제 단순화를 위해.
        Book item = bookService.findItem(itemId);    // casting하는 것은 좋지 않지만 예제 단순화를 위해.
        ItemForm form = ItemForm.builder()
                .id(itemId)
                .name(item.getName())
                .stockQuantity(item.getStockQuantity())
                .price(item.getPrice())
                .author(item.getAuthor())
                .isbn(item.getIsbn())
                .build();
        model.addAttribute("form", form);
        return "items/updateItemForm";
    }

    @PostMapping("/items/{itemId}/edit")
    public String updateItem(@ModelAttribute("form") ItemForm form) {
        // itemId를 수정하기 전에 회원이 itemId에 대한 권한이 있는지 체크해야함
//        Book item = Book.createBook(form.getAuthor(), form.getIsbn(), form.getName(), form.getPrice(), form.getStockQuantity());
        bookService.saveItem(form);
        return "redirect:/items";

    }
}
