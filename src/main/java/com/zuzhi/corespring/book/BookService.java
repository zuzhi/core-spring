package com.zuzhi.corespring.book;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

/**
 * @author zuzhi
 * @date 04/04/2018
 */
@Service
public class BookService {

    private static final Log logger = LogFactory.getLog(BookService.class);

    private BookRepository bookRepository;

    @Autowired
    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    Page<Book> findAll(Pageable pageRequest) {
        return bookRepository.findAll(pageRequest);
    }

    Book findById(Long bookId) {
        return bookRepository.findById(bookId)
                .orElseThrow(() -> new BookNotFoundException("Book [" + bookId + "] not found"));
    }

    void save(Book book) {
        bookRepository.save(book);
    }

    Book update(Book book, Long bookId) {
        /// book.getId() validation is handled by bean validation in entity class
        // checkNotNull(book.getId(), "Book id is required in request body");
        checkState(book.getId().equals(bookId),
                "Book id in request body [%s] does not match the one in path [%s]",
                bookId, book.getId());
        checkNotNull(bookRepository.findById(bookId));
        return bookRepository.save(book);
    }

    Book update(Map<String, String> updates, Long bookId) {
        final Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new BookNotFoundException("Book [" + bookId + "] not found"));

        updates.keySet()
                .forEach(key -> {
                    switch (key) {
                        case "author":
                            book.setAuthor(updates.get(key));
                            break;
                        case "title":
                            book.setTitle(updates.get(key));
                            break;
                        default:
                            break;
                    }
                });
        return bookRepository.save(book);
    }

    void deleteById(Long bookId) {
        checkNotNull(findById(bookId));
        bookRepository.deleteById(bookId);
    }

    List<Book> findByAuthor(String author) {
        return bookRepository.findByAuthor(author);
    }
}
