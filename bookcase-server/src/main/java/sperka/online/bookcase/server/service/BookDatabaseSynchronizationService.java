package sperka.online.bookcase.server.service;

import sperka.online.bookcase.server.dto.BookDto;
import sperka.online.bookcase.server.entity.Book;

import java.time.Instant;
import java.util.List;

public interface BookDatabaseSynchronizationService {
    List<BookDto> synchronize(List<BookDto> books, Instant lastSyncTime);
}
