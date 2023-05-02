package sperka.online.bookcase.server.repository;

import sperka.online.bookcase.server.dto.BookDto;
import sperka.online.bookcase.server.dto.BookFilterDto;
import sperka.online.bookcase.server.entity.Book;

import java.time.Instant;
import java.util.List;

public interface BookRepository extends BasicRepository< Book > {
    List< Book > findSimilar( BookDto book );

    List< Book > getCreatedAfter( Instant date );

    List< Book > getModifiedAfter( Instant date );

    Book getLastModified();

    List< Book > getPaginated( int page, int perPage, BookFilterDto filters );

    Long getCountFiltered( BookFilterDto filters );

    Long getCountNotDeleted();
}
