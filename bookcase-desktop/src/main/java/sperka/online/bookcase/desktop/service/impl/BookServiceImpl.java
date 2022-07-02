package sperka.online.bookcase.desktop.service.impl;

import org.springframework.stereotype.Service;
import sperka.online.bookcase.desktop.entity.Book;
import sperka.online.bookcase.desktop.repository.BookRepository;
import sperka.online.bookcase.desktop.service.BookService;

import java.util.List;

@Service
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;

    public BookServiceImpl(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public List<Book> getAll() {
        return bookRepository.findAll();
    }

    @Override
    public boolean exists(Book book) {
        List<Book> books = bookRepository.findBySignature(book.getSignature());
        return !books.isEmpty();
    }

    @Override
    public void save(Book book) {
        bookRepository.save(book);
    }
}
