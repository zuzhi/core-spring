package com.zuzhi.corespring.book;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.util.Map;

import static org.springframework.web.bind.annotation.RequestMethod.*;

/**
 * Controller that handles requests for the books endpoint at /books
 *
 * @author zuzhi
 * @date 04/04/2018
 */
@RestController
@RequestMapping("/api/v1/books")
public class BookController {

    private BookService bookService;

    @Autowired
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @RequestMapping(value = "", method = GET,
            produces = "application/json")
    public Page<Book> findAll(@RequestParam(defaultValue = "1") int page,
                              @RequestParam(defaultValue = "10") int size) {
        Pageable pageRequest = PageRequest.of(page - 1, size);
        return bookService.findAll(pageRequest);
    }

    @RequestMapping(value = "/{bookId}", method = GET,
            produces = "application/json")
    public Book findById(@PathVariable Long bookId) {
        return bookService.findById(bookId);
    }

    @RequestMapping(value = "", method = POST,
            consumes = "application/json", produces = "application/json")
    public ResponseEntity<Book> saveBook(@Valid @RequestBody Book book,
                                         UriComponentsBuilder ucb) {
        bookService.save(book);

        UriComponents uriComponents = ucb.path("/books/{id}").buildAndExpand(book.getId());

        return ResponseEntity.created(uriComponents.toUri()).body(book);
    }

    @RequestMapping(value = "/{bookId}", method = PUT,
            consumes = "application/json", produces = "application/json")
    public Book update(@Validated(Book.Existing.class) @RequestBody Book book,
                       @PathVariable Long bookId) {
        return bookService.update(book, bookId);
    }

    @RequestMapping(value = "/{bookId}", method = PATCH,
            consumes = "application/json", produces = "application/json")
    public Book update(@RequestBody Map<String, String> updates,
                       @PathVariable Long bookId) {
        return bookService.update(updates, bookId);
    }

    @RequestMapping(value = "{bookId}", method = DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable Long bookId) {
        bookService.deleteById(bookId);
    }

}