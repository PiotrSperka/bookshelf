package sperka.online.bookcase.desktop.service;

import sperka.online.bookcase.desktop.entity.Book;

import java.util.List;

public interface BookService {
    List<Book> getAll();
    boolean exists(Book book);
    void save(Book book);
}
