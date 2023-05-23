package sperka.pl.bookcase.server.service;

import sperka.pl.bookcase.server.dto.BookDto;

import java.time.Instant;
import java.util.List;

public interface BookDatabaseSynchronizationService {
    List< BookDto > synchronize( List<BookDto> books, Instant lastSyncTime);
}
