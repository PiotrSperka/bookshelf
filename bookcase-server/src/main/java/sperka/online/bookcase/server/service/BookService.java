package sperka.online.bookcase.server.service;

import sperka.online.bookcase.server.dto.BookDto;

import javax.validation.Valid;

public interface BookService {
    BookDto save(@Valid BookDto dto);
    void delete(Long id);
}
