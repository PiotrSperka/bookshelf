package sperka.online.bookcase.server.service;

import sperka.online.bookcase.commons.dto.BookDto;
import sperka.online.bookcase.server.entity.Book;

import java.util.List;

public interface BookDatabaseSynchronizationService {
    List<Book> synchronize(List<BookDto> books);
}
